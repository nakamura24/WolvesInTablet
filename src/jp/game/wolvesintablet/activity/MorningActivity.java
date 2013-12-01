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
import android.widget.TextView;

public class MorningActivity extends Activity {
	private static final String Tag = "MorningActivity";
	private GameRule mGameRule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_morning);

			mGameRule = GameRule.getInstance();
			TextView textView_message = (TextView) findViewById(R.id.moning_message_textView);
			textView_message.setText(mGameRule.getMoningMassage(this));
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			Players players = Players.getInstance();
			if (players.checkGameOver(this) == GameOver_Continue) {
				Intent intent = new Intent(this, NoonActivity.class);
				startActivity(intent);
			} else {
//				Intent intent = new Intent(this, GameOverActivity.class);
//				startActivity(intent);
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
