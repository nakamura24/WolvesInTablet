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
import java.util.Random;
import static jp.game.wolvesintablet.Constant.*;
import jp.game.wolvesintablet.Player.STATUS;
import jp.game.wolvesintablet.Porson.GENDER;
import jp.game.wolvesintablet.Role.ROLE;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

public class Players {
	private static final String TAG = "Players";
	private static Players instance = new Players();

	public enum WINNER {
		Villagers, Werewolves, Werehamsters, Devils, Cultists, Mystics, Lovers
	}

	private ArrayList<Player> mPlayers = new ArrayList<Player>();
	private WINNER mWinners;

	public static Players getInstance() {
		return instance;
	}

	public ArrayList<Player> getRegistedPlayers() {
		return mPlayers;
	}

	public Player getRegistedPlayer(int i) {
		return mPlayers.get(i);
	}

	public Player getPlayer(long UID) {
		for (Player player : mPlayers) {
			if (player.getUID() == UID) {
				return player;
			}
		}
		return null;
	}

	public ArrayList<Player> getPlayingPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player player : mPlayers) {
			if (player.getStatus() != STATUS.NotPlaying) {
				players.add(player);
			}
		}
		return players;
	}

	public ArrayList<Player> getAlivePlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player player : mPlayers) {
			if (player.getStatus() == STATUS.Alive) {
				players.add(player);
			}
		}
		return players;
	}

	public void addPlayer(String Name, String Sound, GENDER Gender) {
		Player player = new Player();
		player.setUID(System.currentTimeMillis());
		player.setName(Name);
		player.setSound(Sound);
		player.setGender(Gender);
		player.setStatus(STATUS.Alive);
		player.setRole(ROLE.Villager);
		mPlayers.add(player);
	}

	public void removePlayer(Player player) {
		mPlayers.remove(player);
	}

	public ArrayList<Player> getWerewolves() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player player : mPlayers) {
			if (player.getRole() == ROLE.Werewolf) {
				players.add(player);
			}
		}
		return players;
	}

	public ArrayList<Player> getMasons() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player player : mPlayers) {
			if (player.getRole() == ROLE.Freemason) {
				players.add(player);
			}
		}
		return players;
	}

	public ArrayList<Player> getCultists() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player player : mPlayers) {
			if (player.getRole() == ROLE.CultLeader
					|| player.getRole() == ROLE.Cultist) {
				players.add(player);
			}
		}
		return players;
	}

	public ArrayList<Player> getLovers() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player player : mPlayers) {
			if (player.getLover()) {
				players.add(player);
			}
		}
		return players;
	}

	// プレイヤーの読み込み
	public void loadPlayers(Context context) {
		Log.i(TAG, "loadPlayers");
		try {
			mPlayers.clear();
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			int players = sharedPreferences.getInt(Pref_Player_Registed, 0);
			for (int i = 0; i < players; i++) {
				Player player = new Player();
				player.setUID(sharedPreferences.getLong(Pref_Player_UID
						+ String.valueOf(i), 0));
				player.setName(sharedPreferences.getString(Pref_Player_Name
						+ String.valueOf(i), null));
				player.setSound(sharedPreferences.getString(Pref_Player_Sound
						+ String.valueOf(i), null));
				int gender = sharedPreferences.getInt(Pref_Player_Gender
						+ String.valueOf(i), GENDER.Male.ordinal());
				if (GENDER.Male.ordinal() == gender) {
					player.setGender(GENDER.Male);
				} else {
					player.setGender(GENDER.Female);
				}
				mPlayers.add(player);
			}
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
	}

	// プレイヤーの保存
	public void savePlayers(Context context) {
		Log.i(TAG, "savePlayers");
		try {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			int i = 0;
			for (Player player : mPlayers) {
				Editor editer = sharedPreferences.edit();
				editer.putLong(Pref_Player_UID + String.valueOf(i),
						player.getUID());
				editer.putString(Pref_Player_Name + String.valueOf(i),
						player.getName());
				editer.putString(Pref_Player_Sound + String.valueOf(i),
						player.getSound());
				editer.putInt(Pref_Player_Gender + String.valueOf(i), player
						.getGender().ordinal());
				editer.commit();
				i++;
			}
			Editor editer = sharedPreferences.edit();
			editer.putInt(Pref_Player_Registed, mPlayers.size());
			editer.commit();
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
	}

	// 役割の割り振り
	public void setRole() {
		Log.i(TAG, "setRole");
		int players = getPlayingPlayers().size();
		Roles roles = Roles.getInstance();
		int count;
		Random rnd = new Random(System.currentTimeMillis());
		for (ROLE role : ROLE.values()) {
			switch (role) {
			case Werewolf:
				count = roles.get(role, players);
				if (roles.getWerewolvesRandom()) {
					count = rnd.nextInt(count) + 1;
				}
				break;
			case Villager:
				count = 0;
				break;
			case Freemason:
				count = roles.get(role, players);
				break;
			default:
				count = roles.get(role, players);
				if (roles.getRolesRandom() || roles.getWerewolvesRandom()) {
					count = rnd.nextInt(count + 1);
				}
				break;
			}
			for (int i = 0; i < count;) {
				int index = rnd.nextInt(players);
				if (mPlayers.get(index).getStatus() == STATUS.Alive
						&& mPlayers.get(index).getRole() == ROLE.Villager) {
					mPlayers.get(index).setRole(role);
					i++;
				}
			}
		}
	}

	// 役割の仲間を取得
	public String getRolePartners(Context context, Player player) {
		Log.i(TAG, "getRolePartners");
		try {
			String partners = "";
			switch (player.getRole()) {
			case Werewolf:
				for (Player werewolf : getWerewolves()) {
					partners += werewolf.getName() + " ";
				}
				break;
			case Freemason:
				for (Player mason : getMasons()) {
					partners += mason.getName() + " ";
				}
				break;
			case CultLeader:
			case Cultist:
				for (Player cultist : getCultists()) {
					partners += cultist.getName() + " ";
				}
				break;
			default:
				break;
			}
			for (Player lover : getLovers()) {
				partners += lover.getName() + " ";
			}
			return partners.trim();
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return "";
	}

	// ゲームオーバーしているかのチェック
	public int checkGameOver(Context context) {
		Log.i(TAG, "checkGameOver");
		try {
			int villager = 0;
			int werewolf = 0;
			for (Player player : mPlayers) {
				switch (player.getRole()) {
				case Werewolf:
					if (player.getStatus() == STATUS.Alive)
						werewolf++;
					break;
				default:
					if (player.getStatus() == STATUS.Alive)
						villager++;
					break;
				}
			}
			if (werewolf <= 0) {
				mWinners = WINNER.Villagers;
				return GameOver_GameOver;
			}
			if (werewolf >= villager) {
				mWinners = WINNER.Werewolves;
				return GameOver_GameOver;
			}
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return GameOver_Continue;
	}

	// 勝利者のメッセージ取得
	public String getWinners(Context context) {
		Log.i(TAG, "getWinners");
		try {
			Resources resource = context.getResources();
			String[] gameover_winners = resource
					.getStringArray(R.array.gameover_winners);
			return gameover_winners[mWinners.ordinal()];
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return "";
	}
}
