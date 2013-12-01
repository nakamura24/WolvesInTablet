package jp.game.wolvesintablet;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;

public class GameRule {
	private static GameRule instance = new GameRule();
	private int mDays;
	private ArrayList<Player> mKillPlayers = new ArrayList<Player>();
	private ArrayList<Player> mOwlPlayers = new ArrayList<Player>();
	private ArrayList<Long> mVotedUIDs = new  ArrayList<Long>();
	private HashMap<Long, Integer> mVotes = new HashMap<Long, Integer>();

	public static GameRule getInstance() {
		return instance;
	}
	
	public GameRule(){
		this.mDays = 1;
	}
	
	public int getDays(){
		return this.mDays;
	}

	// 朝のメッセージ
	public String getMoningMassage(Context context) {
		try {
			String message = "";
			Resources resource = context.getResources();
			String textView_died_message = resource
					.getString(R.string.moning_died_message_textView);
			String textView_no_died = resource
					.getString(R.string.moning_no_died_textView);
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
						.getString(R.string.moning_doubt_message_textView);
				message += String.format(textView_doubt_message, doutePlayer);
			}
			return message;
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return null;
	}
	
	public void setVotedUIDs(ArrayList<Player> players){
		mVotedUIDs.clear();
		mVotes.clear();
		for (Player player : players) {
			mVotedUIDs.add(player.getUID());
		}
	}
	
	public ArrayList<Player> getVotedPlayers() {
		ArrayList<Player> votedPlayers = new ArrayList<Player>();
		Players players = Players.getInstance();
		for (long UID : mVotedUIDs) {
			votedPlayers.add(players.getPlayer(UID));
		}
		return votedPlayers;
	}
	
	public void votePlayer(long UID) {
		int votes = mVotes.get(UID);
		votes++;
		mVotes.put(UID, votes);
	}
}
