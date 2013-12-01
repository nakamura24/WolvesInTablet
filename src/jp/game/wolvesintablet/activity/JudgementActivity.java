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
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class JudgementActivity extends Activity {
	private static final String TAG = "JudgementActivity";
	private Players mPlayers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_judgement);

			mPlayers = Players.getInstance();
			TextView textView_player = (TextView) findViewById(R.id.judgement_message_textView);
			textView_player.setText(getJudgementMassage());
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// 審判のメッセージ
	public String getJudgementMassage() {
		Log.i(TAG, "getJudgementMassage");
		try {
			Resources resource = getResources();
			String textView_revote_message = resource
					.getString(R.string.judgement_revote_message_textView);
			String textView_died_message = resource
					.getString(R.string.judgement_died_message_textView);
			String textView_no_died = resource
					.getString(R.string.judgement_no_died_textView);
			String message = "";
//			if (mPlayers.getVoteablePlayers().size() > 1) {
//				for (String player : mPlayers.getVoteablePlayers()) {
//					message += player + " ";
//				}
//				message = String.format(textView_revote_message, message);
//			} else if (mPlayers.getVoteablePlayers().size() == 1) {
//				String LynchedPlayer = mPlayers.getVoteablePlayers().get(0);
//				message = LynchedPlayer;
//				if (mPlayers.hasLover(LynchedPlayer)) {
//					message = "";
//					for (String player : mPlayers.getLovers()) {
//						message += player + " ";
//					}
//				}
//				message = String.format(textView_died_message, message);
//			} else {
//				message = textView_no_died;
//			}
			return message;
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
		return null;
	}

	public void onClickOkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
//			if (mPlayers.checkRevote()) {
//				Intent intent = new Intent(this, VoteActivity.class);
//				startActivity(intent);
//			} else {
//				if (mPlayers.checkGameOver() == GameRuleClass.GameOver_Continue) {
					Intent intent = new Intent(this, NightActivity.class);
					startActivity(intent);
//				} else {
//					Intent intent = new Intent(this, GameOverActivity.class);
//					startActivity(intent);
//				}
//			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
