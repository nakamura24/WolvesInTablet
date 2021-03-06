/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.wolvesintablet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import jp.game.wolvesintablet.Player.STATUS;
import jp.game.wolvesintablet.Role.ROLE;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class GameRule {
	private static final String TAG = "GameRule";
	private static GameRule instance = new GameRule();
	private int mDays = 1;
	private ArrayList<Player> mKillPlayers = new ArrayList<Player>();
	private ArrayList<Player> mOwlPlayers = new ArrayList<Player>();
	private Player mDoutePlayer = new Player();
	private long mLynchedPlayerUID = 0;
	private ArrayList<Player> mVotedPlayers = new ArrayList<Player>();
	private HashMap<Long, Integer> mVotes = new HashMap<Long, Integer>();
	private String mVoteResult = null;

	public static GameRule getInstance() {
		return instance;
	}

	public void initialize() {
		this.mDays = 1;
		this.mKillPlayers = new ArrayList<Player>();
		this.mOwlPlayers = new ArrayList<Player>();
		this.mDoutePlayer = new Player();
		this.mLynchedPlayerUID = 0;
		this.mVotedPlayers = new ArrayList<Player>();
		this.mVotes = new HashMap<Long, Integer>();
		this.mVoteResult = null;
	}

	public int getDays() {
		return this.mDays;
	}

	public int incrementDays() {
		return this.mDays++;
	}

	public long getLynchedPlayerUID() {
		return mLynchedPlayerUID;
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

	// オプションの行動
	public Player optionAction(Context context, Players players, long UID) {
		Log.i(TAG, "optionAction");
		try {
			Player player = players.getPlayer(UID);
			Player selectedPlayer = new Player();
			switch (player.getRole()) {
			case Seer:
				selectedPlayer = new Player(players.getPlayer(player
						.getSelectedPlayerUID()));
				if (selectedPlayer.getRole() != ROLE.Werewolf) {
					selectedPlayer.setRole(ROLE.Villager);
				}
				return selectedPlayer;
			case Medium:
				selectedPlayer = new Player(players.getPlayer(mLynchedPlayerUID));
				if (selectedPlayer.getRole() != ROLE.Werewolf) {
					selectedPlayer.setRole(ROLE.Villager);
				}
				return selectedPlayer;
			case Mythomaniac:
				selectedPlayer = new Player(players.getPlayer(player
						.getSelectedPlayerUID()));
				if (selectedPlayer.getRole() == ROLE.Werewolf) {
					player.setRole(ROLE.Werewolf);
				} else if (selectedPlayer.getRole() == ROLE.Seer) {
					player.setRole(ROLE.Seer);
				} else {
					player.setRole(ROLE.Villager);
				}
				return player;
			default:
				break;
			}
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return null;
	}

	// 夜の行動を処理
	public void actionResult(Context context, Players players) {
		Log.i(TAG, "actionResult");
		try {
			ArrayList<Player> attackPlayers = new ArrayList<Player>();
			ArrayList<Player> guardPlayers = new ArrayList<Player>();
			mVotes.clear();
			for (Player player : players.getAlivePlayers()) {
				switch (player.getRole()) {
				case Werewolf:
					attackPlayers.add(players.getPlayer(player
							.getSelectedPlayerUID()));
					break;
				case Bodyguard:
					guardPlayers.add(players.getPlayer(player
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
			// 襲撃を受けるプレイヤーを決定
			Random rnd = new Random(System.currentTimeMillis());
			Player attackedPlayer = attackPlayers.get(
					rnd.nextInt(attackPlayers.size()));
			mKillPlayers.add(attackedPlayer);
			// 襲撃から守る
			for (Player player : guardPlayers) {
				if (player.getUID() == attackedPlayer.getUID()) {
					mKillPlayers.clear();
				}
			}
			// 殺されたプレイヤーのステータスを変更
			for (Player player : mKillPlayers) {
				player.setStatus(STATUS.Killed);
			}
			// 疑われているプレイヤーを決定
			ArrayList<Map.Entry<Long, Integer>> sortedVotes = sortVotes();
			mDoutePlayer = players.getPlayer(sortedVotes.get(0).getKey());
			// 選択IDを初期化
			players.initSelectedPlayers();
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
	}

	// 朝のメッセージ
	public String getMoningMassage(Context context, Players players) {
		Log.i(TAG, "getMoningMassage");
		try {
			Resources resource = context.getResources();
			String died_message_text = resource
					.getString(R.string.moning_died_message_text);
			String no_died_text = resource
					.getString(R.string.moning_no_died_text);
			String message = "";
			if (mDays > 1) {
				actionResult(context, players);
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
				message += String.format(doubt_message_text,
						mDoutePlayer.getName());
			}
			return message;
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return null;
	}

	// 審判のメッセージ
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
				mLynchedPlayerUID = sortedVotes.get(0).getKey();
				players.getPlayer(sortedVotes.get(0).getKey()).setStatus(
						STATUS.Lynched);
				massage = String.format(died_message_text,
						players.getPlayer(sortedVotes.get(0).getKey())
								.getName());
				setVotedPlayers(new ArrayList<Player>());
			} else {
				// 2位と同票まで候補にする
				String voteable = "";
				ArrayList<Player> revotePlayers = new ArrayList<Player>();
				if(sortedVotes.get(0).getValue() > sortedVotes.get(1).getValue()){
					revotePlayers.add(players
							.getPlayer(sortedVotes.get(0).getKey()));
					voteable = players
							.getPlayer(sortedVotes.get(0).getKey()).getName() + " ";
				}
				for (Entry<Long, Integer> sortedentry : sortedVotes) {
					if (sortedentry.getValue() == sortedVotes.get(1).getValue()) {
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

	// 投票のソート
	private ArrayList<Map.Entry<Long, Integer>> sortVotes() {
		ArrayList<Map.Entry<Long, Integer>> entries = new ArrayList<Map.Entry<Long, Integer>>(
				mVotes.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<Long, Integer>>() {
			@Override
			public int compare(Entry<Long, Integer> entry1,
					Entry<Long, Integer> entry2) {
				return ((Integer) entry2.getValue()).compareTo((Integer) entry1
						.getValue());
			}
		});
		return entries;
	}
}
