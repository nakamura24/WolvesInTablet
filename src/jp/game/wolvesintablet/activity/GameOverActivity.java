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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends Activity {
	private static final String TAG = "GameOverActivity";
	private Players mPlayers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_gameover);

			mPlayers = Players.getInstance();
			String message = "";
			TextView title_textView = (TextView) findViewById(R.id.gameover_title_textView);
			title_textView.setText(mPlayers.getWinners(this));
			TextView content_textView = (TextView) findViewById(R.id.gameover_content_textView);
			for (Player player : mPlayers.getPlayingPlayers()) {
				message += player.getName() + " - " + player.getRoleName(this) + "\n";
			}
			content_textView.setText(message);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
