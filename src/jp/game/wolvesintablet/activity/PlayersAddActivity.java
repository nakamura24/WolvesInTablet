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
import android.widget.EditText;
import android.widget.RadioButton;

// プレイヤー追加の処理
public class PlayersAddActivity extends Activity {
	private static final String TAG = "PlayersAddActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_players_add);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			EditText editText_player = (EditText) findViewById(R.id.players_add_player_editText);
			RadioButton radio_male = (RadioButton) findViewById(R.id.players_add_male_radioButton);
			// 画面の終了
			Intent intent = new Intent();
			intent.putExtra(Intent_Players_Add_Name, editText_player.getText()
					.toString());
			intent.putExtra(Intent_Players_Add_Gender, radio_male.isChecked());
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
