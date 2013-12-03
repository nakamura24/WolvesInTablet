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
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class NoonActivity extends Activity {
	private static final String TAG = "NoonActivity";
	private int time = Noon_Debate_Time;
	private Players mPlayers;
	private GameRule mGameRule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_noon);

			mPlayers = Players.getInstance();
			mGameRule = GameRule.getInstance();
			time = 3;
			String timer_format = "%d:%02d";
			String timer_text = String.format(timer_format, time, 0);
			TextView textView_timer = (TextView) findViewById(R.id.noon_timer_textView);
			textView_timer.setText(timer_text);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickInclementButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			time++;
			String timer_format = "%d:%02d";
			String timer_text = String.format(timer_format, time, 0);
			TextView textView_timer = (TextView) findViewById(R.id.noon_timer_textView);
			textView_timer.setText(timer_text);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickDeclementkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			if (time > 0)
				time--;
			String timer_format = "%d:%02d";
			String timer_text = String.format(timer_format, time, 0);
			TextView textView_timer = (TextView) findViewById(R.id.noon_timer_textView);
			textView_timer.setText(timer_text);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickStartButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			// カウントダウンする
			new CountDownTimer(time * 60000, 1000) {
				TextView textView_timer = (TextView) findViewById(R.id.noon_timer_textView);
				String timer_format = "%d:%02d";

				// カウントダウン処理
				public void onTick(long millisUntilFinished) {
					String timer_text = String.format(timer_format,
							millisUntilFinished / 60000,
							(millisUntilFinished / 1000) % 60);
					textView_timer.setText(timer_text);
				}

				// カウントが0になった時の処理
				public void onFinish() {
					String timer_format = "%d:%02d";
					String timer_text = String.format(timer_format, 0, 0);
					TextView textView_timer = (TextView) findViewById(R.id.noon_timer_textView);
					textView_timer.setText(timer_text);
					ToneGenerator toneGenerator = new ToneGenerator(
							AudioManager.STREAM_SYSTEM,
							ToneGenerator.MAX_VOLUME);
					toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP,
							Noon_Tone);
				}
			}.start();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			// 投票の前に設定
			mGameRule.setVotedPlayers(mPlayers.getAlivePlayers());

			Intent intent = new Intent(this, VoteActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
