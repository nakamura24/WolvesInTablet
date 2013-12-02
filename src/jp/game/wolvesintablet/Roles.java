/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.wolvesintablet;

import java.util.HashMap;
import jp.game.wolvesintablet.Role.ROLE;
import static jp.game.wolvesintablet.Constant.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Roles {
	private static Roles instance = new Roles();

	private static int[] mWerewolves = new int[] { 0, 0, 0, 0, 0, 1, 1, 1, 2,
			2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, };
	private static int[] mSeers = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] mMediums = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] mPossesseds = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] mBodyguards = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] mOwlmans = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] mFreemasons = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, };
	private static int[] mWerehamsters = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] mMythomaniacs = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] mDevils = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mHunters = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mApothecarys = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mMatchmakers = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mWitches = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mScryers = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mOracles = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mHierophants = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mMagistrates = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mGravediggers = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mCrones = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mCultLeaders = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mWarlocks = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mNecromancer = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mMystics = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mFools = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mThiefes = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] mToughgays = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };

	private HashMap<ROLE, int[]> mRoles = new HashMap<ROLE, int[]>();
	private int mMaxPlayers = 24;
	private boolean mRolesRandom = false;
	private boolean mWerewolvesRandom = false;

	private Roles() {
	}

	public static Roles getInstance() {
		return instance;
	}

	public int getmMaxPlayers() {
		return mMaxPlayers;
	}

	public boolean getRolesRandom() {
		return mRolesRandom;
	}

	public boolean getWerewolvesRandom() {
		return mWerewolvesRandom;
	}

	private void initRole() {
		mRoles.clear();
		mRoles.put(ROLE.Werewolf, mWerewolves);
		mRoles.put(ROLE.Seer, mSeers);
		mRoles.put(ROLE.Medium, mMediums);
		mRoles.put(ROLE.Possessed, mPossesseds);
		mRoles.put(ROLE.Bodyguard, mBodyguards);
		mRoles.put(ROLE.Owlman, mOwlmans);
		mRoles.put(ROLE.Freemason, mFreemasons);
		mRoles.put(ROLE.Werehamster, mWerehamsters);
		mRoles.put(ROLE.Mythomaniac, mMythomaniacs);
		mRoles.put(ROLE.Devil, mDevils);
		mRoles.put(ROLE.Hunter, mHunters);
		mRoles.put(ROLE.Apothecary, mApothecarys);
		mRoles.put(ROLE.Matchmaker, mMatchmakers);
		mRoles.put(ROLE.Witch, mWitches);
		mRoles.put(ROLE.Scryer, mScryers);
		mRoles.put(ROLE.Oracle, mOracles);
		mRoles.put(ROLE.Hierophant, mHierophants);
		mRoles.put(ROLE.Magistrate, mMagistrates);
		mRoles.put(ROLE.Gravedigger, mGravediggers);
		mRoles.put(ROLE.Crone, mCrones);
		mRoles.put(ROLE.CultLeader, mCultLeaders);
		mRoles.put(ROLE.Warlock, mWarlocks);
		mRoles.put(ROLE.Necromancer, mNecromancer);
		mRoles.put(ROLE.Mystic, mMystics);
		mRoles.put(ROLE.Fool, mFools);
		mRoles.put(ROLE.Thief, mThiefes);
		mRoles.put(ROLE.Toughgay, mToughgays);
	}

	// 役職の人数取得
	public int get(ROLE role, int number) {
		if (!mRoles.containsKey(role))
			return -1;
		if (mRoles.get(role).length <= number)
			return -1;
		return mRoles.get(role)[number];
	}

	// 役職の人数設定
	public void set(ROLE role, int number, int value) {
		if (!mRoles.containsKey(role))
			return;
		if (mRoles.get(role).length <= number)
			return;
		int[] values = mRoles.get(role);
		values[number] = value;
		mRoles.put(role, values);
	}

	// 最大プレイ人数を変更
	public void changeMax(int max_of_player) {
		for (ROLE role : ROLE.values()) {
			if (!mRoles.containsKey(role))
				continue;
			int[] values = new int[max_of_player + 1];
			for (int i = MinPlayers; i < mRoles.get(role).length; i++)
				values[i] = mRoles.get(role)[i];
			for (int i = mRoles.get(role).length; i <= max_of_player; i++)
				values[i] = mRoles.get(role)[mRoles.get(role).length - 1];
			mRoles.put(role, values);
		}
		mMaxPlayers = max_of_player;
	}

	// 役職の読み込み
	public void loadRoles(Context context) {
		try {
			initRole();
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			mMaxPlayers = sharedPreferences.getInt(Pref_Role_MaxPlayers,
					mMaxPlayers);
			mRolesRandom = sharedPreferences.getBoolean(Pref_Role_RolesRandom,
					false);
			mWerewolvesRandom = sharedPreferences.getBoolean(
					Pref_Role_WerewolvesRandom, false);
			for (ROLE role : ROLE.values()) {
				if (!mRoles.containsKey(role))
					continue;
				int[] values = new int[mMaxPlayers + 1];
				for (int i = MinPlayers; i <= mMaxPlayers; i++) {
					values[i] = sharedPreferences.getInt(role.toString()
							+ String.valueOf(i), get(role, i));
				}
				mRoles.put(role, values);
			}
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
	}

	// 役職の保存
	public void saveRoles(Context context) {
		try {
			// 役職設定保存
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			for (ROLE role : ROLE.values()) {
				Editor editer = sharedPreferences.edit();
				for (int i = MinPlayers; i <= mMaxPlayers; i++) {
					editer.putInt(role.toString() + String.valueOf(i),
							get(role, i));
				}
				editer.commit();
			}
			Editor editer = sharedPreferences.edit();
			editer.putInt(Pref_Role_MaxPlayers, mMaxPlayers);
			editer.putBoolean(Pref_Role_RolesRandom, mRolesRandom);
			editer.putBoolean(Pref_Role_WerewolvesRandom, mWerewolvesRandom);
			editer.commit();
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
	}
}
