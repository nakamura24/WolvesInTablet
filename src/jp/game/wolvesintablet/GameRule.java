package jp.game.wolvesintablet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jp.game.wolvesintablet.Player.STATUS;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class GameRule {
	private static final String TAG = "GameRule";
	private static GameRule instance = new GameRule();
	private int mDays;
	private ArrayList<Player> mKillPlayers = new ArrayList<Player>();
	// private ArrayList<Player> mOwlPlayers = new ArrayList<Player>();
	private long LynchedPlayerUID = 0;
	private ArrayList<Long> mVotedUIDs = new ArrayList<Long>();
	private HashMap<Long, Integer> mVotes = new HashMap<Long, Integer>();
	private String mVoteResult = null;

	public static GameRule getInstance() {
		return instance;
	}

	public GameRule() {
		this.mDays = 1;
	}

	public int getDays() {
		return this.mDays;
	}

	public int incrementDays() {
		return this.mDays++;
	}

	public long getLynchedPlayer() {
		return LynchedPlayerUID;
	}

	public String getVoteResult() {
		return mVoteResult;
	}

	public ArrayList<Long> getVotedUIDs() {
		return mVotedUIDs;
	}

	public void setVotedUIDs(ArrayList<Player> players) {
		mVotedUIDs.clear();
		mVotes.clear();
		for (Player player : players) {
			mVotedUIDs.add(player.getUID());
		}
	}

	public void votePlayer(long UID) {
		int votes = 0;
		if (mVotes.containsKey(UID)) {
			votes = mVotes.get(UID);
		}
		votes++;
		mVotes.put(UID, votes);
	}

	// 朝のメッセージ
	public String getMoningMassage(Context context) {
		Log.i(TAG, "getMoningMassage");
		try {
			String message = "";
			Resources resource = context.getResources();
			String textView_died_message = resource
					.getString(R.string.moning_died_message_text);
			String textView_no_died = resource
					.getString(R.string.moning_no_died_text);
			if (mKillPlayers.size() > 0) {
				String DiedPlayers = "";
				for (Player player : mKillPlayers) {
					DiedPlayers += player.getName() + " ";
				}
				message = String.format(textView_died_message, DiedPlayers);
			} else {
				message = textView_no_died;
			}
			if (mDays > 2) {
				String doutePlayer = "";
				String textView_doubt_message = resource
						.getString(R.string.moning_doubt_message_text);
				message += String.format(textView_doubt_message, doutePlayer);
			}
			return message;
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return null;
	}

	public String getJudgementMassage(Context context, Players players) {
		Log.i(TAG, "getJudgementMassage");
		String massage = "";
		try {
			Resources resource = context.getResources();
			String revote_message_text = resource
					.getString(R.string.judgement_revote_message_text);
			String died_message_text = resource
					.getString(R.string.judgement_died_message_text);
			// String no_died_text = resource
			// .getString(R.string.judgement_no_died_text);
			String result_text = resource
					.getString(R.string.judgement_result_text);

			ArrayList<Map.Entry<Long, Integer>> entries = new ArrayList<Map.Entry<Long, Integer>>(
					mVotes.entrySet());
			Collections.sort(entries,
					new Comparator<Map.Entry<Long, Integer>>() {
						@Override
						public int compare(Entry<Long, Integer> entry1,
								Entry<Long, Integer> entry2) {
							return ((Integer) entry2.getValue())
									.compareTo((Integer) entry1.getValue());
						}
					});
			mVoteResult = "";
			for (Entry<Long, Integer> sortedentry : entries) {
				mVoteResult += players.getPlayer(sortedentry.getKey())
						.getName()
						+ String.format(result_text, sortedentry.getValue());
			}
			// 過半数を超えている
			if (entries.get(0).getValue() * 2 > players.getAlivePlayers()
					.size()) {
				LynchedPlayerUID = entries.get(0).getKey();
				players.getPlayer(entries.get(0).getKey()).setStatus(
						STATUS.Lynched);
				massage = String.format(died_message_text,
						players.getPlayer(entries.get(0).getKey()).getName());
				setVotedUIDs(new ArrayList<Player>());
			} else {
				ArrayList<Player> revotePlayers = new ArrayList<Player>();
				revotePlayers.add(players.getPlayer(entries.get(0).getKey()));
				String voteable = players.getPlayer(entries.get(0).getKey())
						.getName() + " ";
				int second = entries.get(1).getValue();
				for (Entry<Long, Integer> sortedentry : entries) {
					if (sortedentry.getValue() == second) {
						revotePlayers.add(players.getPlayer(sortedentry
								.getKey()));
						voteable += players.getPlayer(sortedentry.getKey())
								.getName() + " ";
					}
				}
				massage = String.format(revote_message_text, voteable);
				setVotedUIDs(revotePlayers);
			}
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return massage;
	}
}
