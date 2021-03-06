/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.wolvesintablet;

public class Constant {
	// エラーレポートの設定
	public static final String ErrorReportKey = "ErrorReport";
	public static final String MailAddress = "zmf42190@gmail.com";

	// アクティビティのID
	public static final int ACTIVITY_PLAYERS = 0;
	public static final int ACTIVITY_ADDPLAYER = 1;
	public static final int ACTIVITY_ROLES = 2;
	public static final int ACTIVITY_HELP = 3;
	public static final int ACTIVITY_OPENING = 1000;
	public static final int ACTIVITY_NIGHT_ROLE = 2000;
	public static final int ACTIVITY_NIGHT_OPTION = 2001;
	public static final int ACTIVITY_VOTE_ACTION = 3001;

	// 固定定数
	public final static int MinPlayers = 5;
	public static final int GameOver_Continue = 1;
	public static final int GameOver_GameOver = 0;
	public static final int Noon_Debate_Time = 180;
	public static final int Noon_Tone = 3000;

	// 外部記憶のID
	public static final String Pref_Role_MaxPlayers = "Role_MaxPlayers";
	public static final String Pref_Role_RolesRandom = "Role_RolesRandom";
	public static final String Pref_Role_WerewolvesRandom = "Role_WerewolvesRandom";
	public static final String Pref_Player_Registed = "Player_Registed";
	public static final String Pref_Player_UID = "Player_UID";
	public static final String Pref_Player_Name = "Player_Name";
	public static final String Pref_Player_Sound = "Player_Sound";
	public static final String Pref_Player_Gender = "Player_Gender";

	// インテントのID
	public static final String Intent_Players_Add_Name = "Players_Add_Name";
	public static final String Intent_Players_Add_Gender = "Players_Add_Gender";
	public static final String Intent_Player_UID = "Player_UID";
}
