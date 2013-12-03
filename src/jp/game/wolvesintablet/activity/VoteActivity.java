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
import jp.game.wolvesintablet.Player.STATUS;
import static jp.game.wolvesintablet.Constant.*;
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
import android.widget.Button;
import android.widget.ListView;

// 投票の処理
public class VoteActivity extends Activity {
	private static final String TAG = "VoteActivity";
	private Players mPlayers;
	private boolean[] players;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_vote);

			mPlayers = Players.getInstance();
			players = new boolean[mPlayers.getPlayingPlayers().size()];
			for (int i = 0; i < players.length; i++) {
				if (mPlayers.getPlayingPlayers().get(i).getStatus() != STATUS.Alive) {
					players[i] = true;
				}
			}

			// リストビュー更新
			ListView_update();

			Button ok_button = (Button) findViewById(R.id.ok_button);
			ok_button.setVisibility(View.GONE);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// リストビュー更新
	private void ListView_update() {
		Log.i(TAG, "ListView_update");
		try {
			ListView listView_players = (ListView) findViewById(R.id.vote_players_listView);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_multiple_choice);
			for (int i = 0; i < mPlayers.getPlayingPlayers().size(); i++) {
				adapter.add(mPlayers.getPlayingPlayers().get(i).getName());
			}
			listView_players.setAdapter(adapter);
			listView_players.setItemsCanFocus(false);
			listView_players
					.setOnItemClickListener(new ListViewOnItemClickListener());
			for (int i = 0; i < mPlayers.getPlayingPlayers().size(); i++) {
				listView_players.setItemChecked(i, players[i]);
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		try {
			switch (requestCode) {
			case ACTIVITY_VOTE_ACTION:
				break;
			}
			// リストビュー更新
			ListView_update();
			int finish = 0;
			for (int i = 0; i < players.length; i++) {
				if (players[i])
					finish++;
			}
			if (players.length <= finish) {
				Button ok_button = (Button) findViewById(R.id.ok_button);
				ok_button.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			Intent intent = new Intent(this, JudgementActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	private class ListViewOnItemClickListener implements OnItemClickListener {
		int mPosition;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			try {
				this.mPosition = position;
				if (!players[position]) {
					checkPlayerAlertDialog(position);
				}
				ListView listView_players = (ListView) findViewById(R.id.vote_players_listView);
				listView_players.setItemChecked(position, players[position]);
			} catch (Exception e) {
				ErrorReport.LogException(VoteActivity.this, e);
			}
		}

		// 本人かどうかの確認
		private void checkPlayerAlertDialog(final int position) {
			Log.i(TAG, "checkPlayerAlertDialog");
			try {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						VoteActivity.this);
				// アラートダイアログのメッセージを設定します
				Resources resource = getResources();
				String message = resource.getString(R.string.vote_action_text);
				message = String.format(message, mPlayers.getPlayingPlayers()
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
				ErrorReport.LogException(VoteActivity.this, e);
			}
		}

		private class NegativeOnClickListener implements
				DialogInterface.OnClickListener {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "onClick");
				try {
					players[mPosition] = true;
					Intent intent = new Intent(VoteActivity.this,
							VoteActionActivity.class);
					intent.putExtra(Intent_Player_UID, mPlayers
							.getPlayingPlayers().get(mPosition).getUID());
					startActivityForResult(intent, ACTIVITY_VOTE_ACTION);
				} catch (ActivityNotFoundException e) {
					ErrorReport.LogException(VoteActivity.this, e);
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
					ErrorReport.LogException(VoteActivity.this, e);
				}
			}
		}
	}
}
