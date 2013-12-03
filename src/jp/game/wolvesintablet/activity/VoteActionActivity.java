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
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VoteActionActivity extends Activity {
	private static final String TAG = "VoteActionActivity";
	private GameRule mGameRule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_vote_action);
			mGameRule = GameRule.getInstance();

			// リストビュー更新
			ListView_update();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// リストビュー更新
	private void ListView_update() {
		Log.i(TAG, "ListView_update");
		try {
			ListView listView_players = (ListView) findViewById(R.id.vote_action_players_listView);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_checked);
			for (int i = 0; i < mGameRule.getVotedPlayers().size(); i++) {
				adapter.add(mGameRule.getVotedPlayers().get(i).getName());
			}
			listView_players.setAdapter(adapter);
			listView_players.setItemsCanFocus(false);
			listView_players
					.setOnItemClickListener(new ListViewOnItemClickListener());
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	private class ListViewOnItemClickListener implements OnItemClickListener {
		private int mPosition = 0;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			this.mPosition = position;
			try {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						VoteActionActivity.this);
				// アラートダイアログのメッセージを設定します
				Resources resource = getResources();
				String message = resource
						.getString(R.string.vote_action_execute_text);
				message = String.format(message, mGameRule.getVotedPlayers()
						.get(position).getName());
				alertDialogBuilder.setMessage(message);
				// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
				alertDialogBuilder.setNegativeButton(R.string.common_text_ok,
						new NegativeOnClickListener());
				alertDialogBuilder.setPositiveButton(
						R.string.common_text_cancel,
						new PositiveOnClickListener());
				AlertDialog alertDialog = alertDialogBuilder.create();
				// アラートダイアログを表示します
				alertDialog.show();
			} catch (Exception e) {
				ErrorReport.LogException(VoteActionActivity.this, e);
			}
		}

		private class NegativeOnClickListener implements
				DialogInterface.OnClickListener {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "onClick");
				try {
					mGameRule.votePlayer(mGameRule.getVotedPlayers()
							.get(mPosition).getUID());
					// 画面の終了
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
				} catch (ActivityNotFoundException e) {
					ErrorReport.LogException(VoteActionActivity.this, e);
				}
			}
		}

		private class PositiveOnClickListener implements
				DialogInterface.OnClickListener {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					// リストビュー更新
					ListView_update();
				} catch (Exception e) {
					ErrorReport.LogException(VoteActionActivity.this, e);
				}
			}
		}
	}
}
