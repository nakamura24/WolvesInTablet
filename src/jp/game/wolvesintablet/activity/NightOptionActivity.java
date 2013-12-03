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
import jp.game.wolvesintablet.Role.ROLE;
import static jp.game.wolvesintablet.Constant.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
			long UID = intent.getLongExtra(Intent_Player_UID, 0);
			mPlayer = mPlayers.getPlayer(UID);
			if (mPlayer == null) {
				// 画面の終了
				Intent finishIntent = new Intent();
				setResult(RESULT_OK, finishIntent);
				finish();
			}
			optionAction();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void optionAction() {
		Log.i(TAG, "optionAction");
		try {
			Player selectedPlayer = new Player();
			switch (mPlayer.getRole()) {
			case Seer:
				Log.d(TAG, "SelectedPlayerUID = " + mPlayer.getSelectedPlayerUID());
				selectedPlayer = new Player(mPlayers.getPlayer(mPlayer
						.getSelectedPlayerUID()));
				if (selectedPlayer.getRole() != ROLE.Werewolf) {
					selectedPlayer.setRole(ROLE.Villager);
				}
				roleView(selectedPlayer);
				break;
			case Medium:
				Log.d(TAG, "LynchedPlayerUID = " + mGameRule.getLynchedPlayerUID());
				selectedPlayer = new Player(mPlayers.getPlayer(mGameRule
						.getLynchedPlayerUID()));
				if (selectedPlayer.getRole() != ROLE.Werewolf) {
					selectedPlayer.setRole(ROLE.Villager);
				}
				roleView(selectedPlayer);
				break;
			case Mythomaniac:
				Log.d(TAG, "SelectedPlayerUID = " + mPlayer.getSelectedPlayerUID());
				selectedPlayer = new Player(mPlayers.getPlayer(mPlayer
						.getSelectedPlayerUID()));
				if (selectedPlayer.getRole() == ROLE.Werewolf) {
					mPlayer.setRole(ROLE.Werewolf);
				} else if (selectedPlayer.getRole() == ROLE.Seer) {
					mPlayer.setRole(ROLE.Seer);
				} else {
					mPlayer.setRole(ROLE.Villager);
				}
				roleView(mPlayer);
				break;
			default:
				break;
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
