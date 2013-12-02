package jp.game.wolvesintablet;

import jp.game.wolvesintablet.Player.STATUS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class GameRule {
	private static final String TAG = "GameRule";
	private static GameRule instance = new GameRule();
	private int mDays;
	private ArrayList<Player> mKillPlayers = new ArrayList<Player>();
	private ArrayList<Player> mGuardPlayers = new ArrayList<Player>();
	private ArrayList<Player> mOwlPlayers = new ArrayList<Player>();
	private Player mDoutePlayer;
	private long LynchedPlayerUID = 0;
	private ArrayList<Player> mVotedPlayers = new ArrayList<Player>();
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

	public ArrayList<Player> getVotedPlayers() {
		return mVotedPlayers;
	}

	public void setVotedPlayers(ArrayList<Player> players) {
		mVotedPlayers.clear();
		mVotes.clear();
		for (Player player : players) {
			mVotedPlayers.add(player);
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
			Resources resource = context.getResources();
			String died_message_text = resource
					.getString(R.string.moning_died_message_text);
			String no_died_text = resource
					.getString(R.string.moning_no_died_text);
			String message = "";
			if (mDays > 1) {
				if (mKillPlayers.size() > 0) {
					message = " ";
					for (Player player : mKillPlayers) {
						message += player.getName() + " ";
					}
					message = String.format(died_message_text, message) + "\n";
				} else {
					message = no_died_text + "\n";
				}
				String doubt_message_text = resource
						.getString(R.string.moning_doubt_message_text);
				message += String.format(doubt_message_text, mDoutePlayer.getName());
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
			ArrayList<Map.Entry<Long, Integer>> sortedVotes = sortVotes();
			mVoteResult = "";
			for (Entry<Long, Integer> sortedentry : sortedVotes) {
				mVoteResult += players.getPlayer(sortedentry.getKey())
						.getName()
						+ String.format(result_text, sortedentry.getValue());
			}
			// 過半数を超えている
			if (sortedVotes.get(0).getValue() * 2 > players.getAlivePlayers()
					.size()) {
				LynchedPlayerUID = sortedVotes.get(0).getKey();
				players.getPlayer(sortedVotes.get(0).getKey()).setStatus(
						STATUS.Lynched);
				massage = String.format(died_message_text,
						players.getPlayer(sortedVotes.get(0).getKey()).getName());
				setVotedPlayers(new ArrayList<Player>());
			} else {
				ArrayList<Player> revotePlayers = new ArrayList<Player>();
				revotePlayers.add(players.getPlayer(sortedVotes.get(0).getKey()));
				String voteable = players.getPlayer(sortedVotes.get(0).getKey())
						.getName() + " ";
				int second = sortedVotes.get(1).getValue();
				for (Entry<Long, Integer> sortedentry : sortedVotes) {
					if (sortedentry.getValue() == second) {
						revotePlayers.add(players.getPlayer(sortedentry
								.getKey()));
						voteable += players.getPlayer(sortedentry.getKey())
								.getName() + " ";
					}
				}
				massage = String.format(revote_message_text, voteable);
				setVotedPlayers(revotePlayers);
			}
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return massage;
	}

	public void actionResult(Context context, Players players) {
		Log.i(TAG, "actionResult");
		try {
			mVotes.clear();
			for (Player player : players.getAlivePlayers()) {
				switch (player.getRole()) {
				case Werewolf:
					mKillPlayers.add(players.getPlayer(player
							.getSelectedPlayerUID()));
					break;
				case Bodyguard:
					mGuardPlayers.add(players.getPlayer(player
							.getSelectedPlayerUID()));
					break;
				case Owlman:
					mOwlPlayers.add(players.getPlayer(player
							.getSelectedPlayerUID()));
					break;
				default:
					votePlayer(player.getSelectedPlayerUID());
					break;
				}
			}
			Random rnd = new Random(System.currentTimeMillis());
			long killedPlayerUID = mKillPlayers.get(
					rnd.nextInt(mKillPlayers.size())).getUID();
			mKillPlayers.clear();
			mKillPlayers.add(players.getPlayer(killedPlayerUID));
			for (Player player : mGuardPlayers) {
				if (player.getSelectedPlayerUID() == killedPlayerUID) {
					mKillPlayers.clear();
				}
			}
			for (Player player : mKillPlayers) {
				player.setStatus(STATUS.Killed);
			}
			ArrayList<Map.Entry<Long, Integer>> sortedVotes = sortVotes();
			mDoutePlayer = players.getPlayer(sortedVotes.get(0).getKey());
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
	}

	private ArrayList<Map.Entry<Long, Integer>> sortVotes() {
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
		return entries;
	}
}
