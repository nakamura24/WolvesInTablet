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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class OpeningActivity extends Activity {
	private static final String TAG = "OpeningActivity";
	private Players mPlayers;
	private Roles mRoles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_opening);
			mPlayers = Players.getInstance();
			mRoles = Roles.getInstance();

			// プレイヤー人数の警告
			playersAlertDialog();

			// 初期化
			mPlayers.setRole();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// プレイヤー人数の警告
	private void playersAlertDialog() {
		Log.i(TAG, "playersAlertDialog");
		try {
			if (mPlayers.getPlayingPlayers().size() < MinPlayers
					|| mPlayers.getPlayingPlayers().size() > mRoles.getmMaxPlayers()) {
				Log.d(TAG, "AlertWarningDialog");

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
				// アラートダイアログのタイトルを設定します
				alertDialogBuilder.setTitle(R.string.common_text_warning);
				// アラートダイアログのメッセージを設定します
				Resources resource = getResources();
				String warning_players = "";
				if (mPlayers.getPlayingPlayers().size() < MinPlayers) {
					warning_players = resource
							.getString(R.string.roles_text_warning_min);
					warning_players = String
							.format(warning_players, MinPlayers);
				}
				if (mPlayers.getPlayingPlayers().size() > mRoles.getmMaxPlayers()) {
					warning_players = resource
							.getString(R.string.roles_text_warning_max);
				}
				alertDialogBuilder.setMessage(warning_players);
				// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
				alertDialogBuilder.setPositiveButton(R.string.common_text_ok,
						new PositiveOnClickListener());
				AlertDialog alertDialog = alertDialogBuilder.create();
				// アラートダイアログを表示します
				alertDialog.show();
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	private class PositiveOnClickListener implements
			DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			try {
				// 画面の終了
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			} catch (Exception e) {
				ErrorReport.LogException(OpeningActivity.this, e);
			}
		}
	}

	public void onClickOkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			Intent intent = new Intent(this, NightActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
