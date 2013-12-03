/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.wolvesintablet.activity;

import java.util.ArrayList;

import jp.game.wolvesintablet.*;
import jp.game.wolvesintablet.Player.STATUS;
import static jp.game.wolvesintablet.Constant.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

//　夜の役割毎行動
public class NightRoleActivity extends Activity {
	private static final String TAG = "NightRoleActivity";
	private Players mPlayers;
	private Player mPlayer;
	private GameRule mGameRule;
	private ArrayList<Player> mSelectablePlayers = new ArrayList<Player>();

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
			if (mGameRule.getDays() > 1) {
				if (mPlayer.getStatus() == STATUS.Alive) {
					// 役職の行動
					roleAction();
				} else {
					// 殺されたプレイヤーの行動
					gameOverPlayer();
				}
			} else {
				// 役職を表示
				roleView();
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// 役職を表示
	public void roleView() {
		Log.i(TAG, "roleView");
		try {
			setContentView(R.layout.activity_role_view);
			TextView name_textView = (TextView) findViewById(R.id.roleview_name_textView);
			TextView description_textView = (TextView) findViewById(R.id.roleview_description_textView);
			ImageView role_imageView = (ImageView) findViewById(R.id.roleview_role_imageView);
			name_textView.setText(mPlayer.getName() + " - ("
					+ mPlayer.getRoleName(this) + ")");
			description_textView.setText(mPlayer.getRoleDetail(this));
			role_imageView.setImageResource(mPlayer.getRoleImageId(this));
			// 仲間を表示するためのメッセージ
			TextView message_textView = (TextView) findViewById(R.id.roleview_message_textView);
			message_textView.setText(mPlayers.getRolePartners(this, mPlayer));
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// 役職の行動
	public void roleAction() {
		Log.i(TAG, "roleAction");
		try {
			setContentView(R.layout.activity_role_action);
			TextView name_textView = (TextView) findViewById(R.id.roleaction_name_textView);
			name_textView.setText(mPlayer.getName() + " - ("
					+ mPlayer.getRoleName(this) + ")");
			TextView message_textView = (TextView) findViewById(R.id.roleaction_message_textView);
			message_textView.setText(mPlayer.getRolesMessage(this));
			if (mPlayer.checkPreOptionAction()) {
				Button confirm_button = (Button) findViewById(R.id.roleaction_confirm_button);
				confirm_button.setVisibility(View.VISIBLE);
			}
			// 選択可能なプレイヤーのリスト
			mSelectablePlayers.clear();
			for (Player player : mPlayers.getAlivePlayers()) {
				if (player.getUID() != mPlayer.getUID()) {
					mSelectablePlayers.add(player);
				}
			}
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
			ListView listView_players = (ListView) findViewById(R.id.roleaction_listView_players);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_checked);
			for (int i = 0; i < mSelectablePlayers.size(); i++) {
				adapter.add(mSelectablePlayers.get(i).getName());
			}
			listView_players.setAdapter(adapter);
			listView_players.setItemsCanFocus(false);
			listView_players
					.setOnItemClickListener(new ListViewOnItemClickListener());
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// リストアイテムがクリックされた時の処理
	private class ListViewOnItemClickListener implements OnItemClickListener {
		private int mPosition = 0;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			this.mPosition = position;
			try {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						NightRoleActivity.this);
				// アラートダイアログのメッセージを設定します
				String message = mPlayer.getRoleAction(NightRoleActivity.this);
				message = String.format(message,
						mSelectablePlayers.get(position).getName());
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
				ErrorReport.LogException(NightRoleActivity.this, e);
			}
		}

		private class NegativeOnClickListener implements
				DialogInterface.OnClickListener {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "onClick");
				try {
					mPlayer.setSelectedPlayerUID(mSelectablePlayers.get(
							mPosition).getUID());
					if (mPlayer.checkPostOptionAction()) {
						Button confirm_button = (Button) findViewById(R.id.roleaction_confirm_button);
						confirm_button.setVisibility(View.VISIBLE);
					}
				} catch (ActivityNotFoundException e) {
					ErrorReport.LogException(NightRoleActivity.this, e);
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
					ErrorReport.LogException(NightRoleActivity.this, e);
				}
			}
		}
	}

	// 殺されたプレイヤーの行動
	public void gameOverPlayer() {
		Log.i(TAG, "gameOverPlayer");
		try {
			setContentView(R.layout.activity_gameover);
			TextView content_textView = (TextView) findViewById(R.id.gameover_content_textView);
			String message = "";
			for (Player player : mPlayers.getPlayingPlayers()) {
				message += player.getName() + " - " + player.getRoleName(this)
						+ "\n";
			}
			content_textView.setText(message);
			mPlayer.setStatus(STATUS.Died);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		try {
			switch (requestCode) {
			case ACTIVITY_NIGHT_OPTION:
				break;
			}
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

	public void onClickConfirmButton(View view) {
		Log.i(TAG, "onClickConfirmButton");
		try {
			Intent intent = new Intent(this, NightOptionActivity.class);
			intent.putExtra(Intent_Player_UID, mPlayer.getUID());
			startActivityForResult(intent, ACTIVITY_NIGHT_OPTION);
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
