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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

// メインの処理
public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private Roles mRoles;
	private Players mPlayers;
	private GameRule mGameRule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_main);

			// キャッチされない例外により、スレッドが突然終了したときや、
			// このスレッドに対してほかにハンドラが定義されていないときに
			// 呼び出されるデフォルトのハンドラを設定します。
			Thread.setDefaultUncaughtExceptionHandler(new ErrorReport(this));

			// プレイヤーの読み込み
			mPlayers = Players.getInstance();
			mPlayers.loadPlayers(this);

			// 役職の読み込み
			mRoles = Roles.getInstance();
			mRoles.loadRoles(this);

			// ゲームデータの初期化
			mGameRule = GameRule.getInstance();
			mGameRule.initialize();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "onStart");
		super.onStart();
		try {
			// エラーレポートの送信確認ダイアログを表示
			ErrorReport.SendBugReportDialog(this.getApplicationContext());
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		try {
			switch (requestCode) {
			case ACTIVITY_PLAYERS:
				break;
			case ACTIVITY_ROLES:
				break;
			case ACTIVITY_HELP:
				break;
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// StartButton のコールバック
	public void onClickStartButton(View view) {
		Log.i(TAG, "onClickStartButton");
		try {
			Intent intent = new Intent(this, OpeningActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// PlayersButton のコールバック
	public void onClickPlayersButton(View view) {
		Log.i(TAG, "onClickPlayersButton");
		try {
			Intent intent = new Intent(this, PlayersActivity.class);
			startActivityForResult(intent, ACTIVITY_PLAYERS);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// RolesButton のコールバック
	public void onClickRolesButton(View view) {
		Log.i(TAG, "onClickRolesButton");
		try {
			Intent intent = new Intent(this, RolesActivity.class);
			startActivityForResult(intent, ACTIVITY_ROLES);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// HelpButton のコールバック
	public void onClickHelpButton(View view) {
		Log.i(TAG, "onClickHelpButton");
		try {
			Intent intent = new Intent(this, HelpActivity.class);
			startActivityForResult(intent, ACTIVITY_HELP);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
