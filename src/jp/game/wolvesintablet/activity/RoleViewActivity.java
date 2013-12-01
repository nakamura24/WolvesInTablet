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
import android.widget.ImageView;
import android.widget.TextView;

public class RoleViewActivity extends Activity {
	private static final String TAG = "RoleViewActivity";
	private Players mPlayers;
	private Player mPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_role_view);
			mPlayers = Players.getInstance();
			Intent intent = getIntent();
			long UID = intent.getLongExtra(Intent_RoleView_UID, 0);
			mPlayer = mPlayers.getPlayer(UID);
			if (mPlayer == null) {
				// 画面の終了
				Intent finishIntent = new Intent();
				setResult(RESULT_OK, finishIntent);
				finish();
			}
			TextView name_textView = (TextView) findViewById(R.id.roleview_name_textView);
			TextView description_textView = (TextView) findViewById(R.id.roleview_description_textView);
			ImageView role_imageView = (ImageView) findViewById(R.id.roleview_role_imageView);
			name_textView.setText(mPlayer.getName() + " - ("
					+ mPlayer.getRoleName(this) + ")");
			description_textView.setText(mPlayer.getRoleDetail(this));
			role_imageView.setImageResource(mPlayer.getRoleImageId(this));
			// 仲間を表示するためのメッセージ
			TextView message_textView = (TextView) findViewById(R.id.roleview_message_textView);
			message_textView.setText(mPlayers.getRolePartners(this, mPlayer));
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
}
