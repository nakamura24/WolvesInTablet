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

// 審判の処理
public class JudgementActivity extends Activity {
	private static final String TAG = "JudgementActivity";
	private Players mPlayers;
	private GameRule mGameRule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_judgement);
			
			mGameRule = GameRule.getInstance();
			mPlayers = Players.getInstance();

			TextView textView_player = (TextView) findViewById(R.id.judgement_message_textView);
			textView_player.setText(mGameRule.getJudgementMassage(this,
					mPlayers));
			TextView result_textView = (TextView) findViewById(R.id.judgement_result_textView);
			result_textView.setText(mGameRule.getVoteResult());
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			if (mGameRule.getVotedPlayers().size() > 0) {
				// 再投票
				Intent intent = new Intent(this, VoteActivity.class);
				startActivity(intent);
			} else {
				if (mPlayers.checkGameOver(this) == GameOver_Continue) {
					// 夜の行動
					Intent intent = new Intent(this, NightActivity.class);
					startActivity(intent);
				} else {
					// ゲームオーバー
					Intent intent = new Intent(this, GameOverActivity.class);
					startActivity(intent);
				}
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
