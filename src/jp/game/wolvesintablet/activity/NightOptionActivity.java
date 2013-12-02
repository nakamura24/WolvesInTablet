/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.wolvesintablet.activity;

import jp.game.wolvesintablet.*;
import static jp.game.wolvesintablet.Constant.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class NightOptionActivity extends Activity {
	private static final String TAG = "NightRoleActivity";
	private Players mPlayers;
	private Player mPlayer;
	private GameRule mGameRule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			mPlayers = Players.getInstance();
			mGameRule = GameRule.getInstance();
			Intent intent = getIntent();
			long UID = intent.getLongExtra(Intent_RoleView_UID, 0);
			mPlayer = mPlayers.getPlayer(UID);
			if (mPlayer == null) {
				// 画面の終了
				Intent finishIntent = new Intent();
				setResult(RESULT_OK, finishIntent);
				finish();
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickRoleImage(View view) {
		Log.i(TAG, "onClickRoleImage");
		try {
			// 画面の終了
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			// 画面の終了
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
