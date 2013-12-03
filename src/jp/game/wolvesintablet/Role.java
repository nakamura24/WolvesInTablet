/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.wolvesintablet;

import android.content.Context;
import android.content.res.Resources;

public class Role extends Porson {
	public enum ROLE {
		Werewolf, Villager, Seer, Medium, Possessed, Bodyguard, Owlman, Freemason, Werehamster, Mythomaniac, Devil, Hunter, Apothecary, Matchmaker, Witch, Scryer, Oracle, Hierophant, Magistrate, Gravedigger, Crone, Warlock, Necromancer, Thief, Toughgay, CultLeader, Cultist, Mystic, Fool,
	}

	protected ROLE mRole;
	protected boolean mHilling;
	protected boolean mPoison;

	public Role() {
		this.mRole = ROLE.Villager;
		this.mHilling = false;
		this.mPoison = false;
	}

	public Role(Role role) {
		this.mUID = role.mUID;
		this.mName = role.mName;
		this.mSound = role.mSound;
		this.mGender = role.mGender;
		this.mRole = role.mRole;
		this.mHilling = role.mHilling;
		this.mPoison = role.mPoison;
	}

	public void setRole(ROLE role) {
		switch (role) {
		case Witch:
			this.mHilling = true;
			this.mPoison = true;
			break;
		default:
			break;
		}
		this.mRole = role;
	}

	public ROLE getRole() {
		return this.mRole;
	}

	public boolean getHilling() {
		return this.mHilling;
	}

	public void setHilling(boolean Hilling) {
		this.mHilling = Hilling;
	}

	public boolean getPoison() {
		return this.mPoison;
	}

	public void setPoison(boolean Poison) {
		this.mPoison = Poison;
	}

	// 役割の画像IDを取得
	public int getRoleImageId(Context context) {
		int id = 0;
		try {
			final int[] drawables = new int[] { R.drawable.werewolf_male,
					R.drawable.villager_male, R.drawable.seer,
					R.drawable.medium, R.drawable.possessed_male,
					R.drawable.bodyguard, R.drawable.owlman,
					R.drawable.freemasons, R.drawable.werehamster,
					R.drawable.mythomaniac, R.drawable.devil,
					R.drawable.hunter, R.drawable.apothecary,
					R.drawable.matchmaker, R.drawable.witch, R.drawable.scryer,
					R.drawable.oracle, R.drawable.hierophant,
					R.drawable.magistrate, R.drawable.scryer, R.drawable.crone,
					R.drawable.warlock, R.drawable.necromancer,
					R.drawable.thief, R.drawable.toughgay,
					R.drawable.cult_leader, R.drawable.cultist,
					R.drawable.mystic, R.drawable.fool, };
			switch (mRole) {
			case Werewolf:
				if (getGender() == GENDER.Male) {
					id = R.drawable.werewolf_male;
				} else {
					id = R.drawable.werewolf_female;
				}
				break;
			case Villager:
				if (getGender() == GENDER.Male) {
					id = R.drawable.villager_male;
				} else {
					id = R.drawable.villager_female;
				}
				break;
			default:
				id = drawables[mRole.ordinal()];
				break;
			}
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return id;
	}

	// 役割名を取得
	public String getRoleName(Context context) {
		try {
			Resources resource = context.getResources();
			String[] role_names = resource.getStringArray(R.array.roles_names);
			return role_names[mRole.ordinal()];
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return null;
	}

	// 役割の説明を取得
	public String getRoleDetail(Context context) {
		try {
			Resources resource = context.getResources();
			String[] roles_details = resource
					.getStringArray(R.array.roles_details);
			return roles_details[mRole.ordinal()];
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return null;
	}

	// 役割の行動メッセージを取得
	public String getRolesMessage(Context context) {
		try {
			Resources resource = context.getResources();
			String[] roles_messages = resource
					.getStringArray(R.array.roles_messages);
			return roles_messages[mRole.ordinal()];
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return null;
	}

	// 役割の行動確認メッセージを取得
	public String getRoleAction(Context context) {
		try {
			Resources resource = context.getResources();
			String[] roles_actions = resource
					.getStringArray(R.array.roles_actions);
			return roles_actions[mRole.ordinal()];
		} catch (Exception e) {
			ErrorReport.LogException(context, e);
		}
		return null;
	}

	// 役割にオプション行動があるかチェック（選択前）
	public boolean checkPreOptionAction() {
		switch (mRole) {
		case Medium:
			return true;
		default:
			break;
		}
		return false;
	}

	// 役割にオプション行動があるかチェック（選択後）
	public boolean checkPostOptionAction() {
		switch (mRole) {
		case Seer:
			return true;
		case Mythomaniac:
			return true;
		default:
			break;
		}
		return false;
	}
}
