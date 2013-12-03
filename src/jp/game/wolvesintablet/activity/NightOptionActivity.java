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

//　夜の役職毎行動のオプション行動
public class NightOptionActivity extends Activity {
	private static final String TAG = "NightRoleActivity";
	private Players mPlayers;
	private GameRule mGameRule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			mPlayers = Players.getInstance();
			mGameRule = GameRule.getInstance();
			Intent intent = getIntent();
			long UID = intent.getLongExtra(Intent_Player_UID, 0);
			if (mPlayers.getPlayer(UID) == null) {
				// 画面の終了
				Intent finishIntent = new Intent();
				setResult(RESULT_OK, finishIntent);
				finish();
			}
			Player player = mGameRule.optionAction(this, mPlayers, UID);
			if(player != null){
				roleView(player);
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// 役職を表示
	public void roleView(Player player) {
		Log.i(TAG, "roleView");
		try {
			setContentView(R.layout.activity_role_view);
			TextView name_textView = (TextView) findViewById(R.id.roleview_name_textView);
			TextView description_textView = (TextView) findViewById(R.id.roleview_description_textView);
			ImageView role_imageView = (ImageView) findViewById(R.id.roleview_role_imageView);
			name_textView.setText(player.getName() + " - ("
					+ player.getRoleName(this) + ")");
			description_textView.setText(player.getRoleDetail(this));
			role_imageView.setImageResource(player.getRoleImageId(this));
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
