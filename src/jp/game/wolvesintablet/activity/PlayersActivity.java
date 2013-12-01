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
import jp.game.wolvesintablet.Porson.GENDER;
import static jp.game.wolvesintablet.Constant.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

public class PlayersActivity extends Activity {
	private static final String TAG = "PlayersActivity";
	private Players mPlayers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_players);
			mPlayers = Players.getInstance();

			ToggleButton toggleButton_all = (ToggleButton) findViewById(R.id.players_all_toggleButton);
			toggleButton_all.setChecked(true);
			playersListViewUpdate();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void playersListViewUpdate() {
		Log.i(TAG, "playersListViewUpdate");
		try {
			ListView listView_players = (ListView) findViewById(R.id.players_players_listView);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_multiple_choice);
			for (int i = 0; i < mPlayers.getRegistedPlayers().size(); i++) {
				adapter.add(mPlayers.getRegistedPlayer(i).getName());
			}
			listView_players.setAdapter(adapter);
			for (int i = 0; i < mPlayers.getRegistedPlayers().size(); i++) {
				if (mPlayers.getRegistedPlayer(i).getStatus() == STATUS.Alive) {
					listView_players.setItemChecked(i, true);
				} else {
					listView_players.setItemChecked(i, false);
				}
			}
			listView_players
					.setOnItemClickListener(new ListViewOnItemClickListener());
			ToggleButton toggleButton_all = (ToggleButton) findViewById(R.id.players_all_toggleButton);
			onClickAllButton(toggleButton_all);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	private class ListViewOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			try {
				ListView listView_players = (ListView) findViewById(R.id.players_players_listView);
				if (mPlayers.getRegistedPlayer(position).getStatus() == STATUS.Alive) {
					mPlayers.getRegistedPlayer(position).setStatus(STATUS.NotPlaying);
					listView_players.setItemChecked(position, false);
				} else {
					mPlayers.getRegistedPlayer(position).setStatus(STATUS.Alive);
					listView_players.setItemChecked(position, true);
				}
			} catch (Exception e) {
				ErrorReport.LogException(PlayersActivity.this, e);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		try {
			switch (requestCode) {
			case ACTIVITY_ADDPLAYER:
				String name = data.getStringExtra(Intent_Players_Add_Name);
				GENDER gender;
				if (data.getBooleanExtra(Intent_Players_Add_Gender, true)) {
					gender = GENDER.Male;
				} else {
					gender = GENDER.Female;
				}
				if (name != null && name.length() > 0) {
					mPlayers.addPlayer(name, null, gender);
					// リストビュー更新
					playersListViewUpdate();
				}
				break;
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickAddButton(View view) {
		Log.i(TAG, "onClickAddButton");
		try {
			Intent intent = new Intent(this, PlayersAddActivity.class);
			startActivityForResult(intent, ACTIVITY_ADDPLAYER);
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickDelButton(View view) {
		Log.i(TAG, "onClickDelButton");
		try {
			ListView listView_players = (ListView) findViewById(R.id.players_players_listView);
			for (int i = mPlayers.getRegistedPlayers().size() - 1; i >= 0; i--) {
				if (listView_players.isItemChecked(i)) {
					mPlayers.removePlayer(mPlayers.getRegistedPlayer(i));
				}
			}
			// リストビュー更新
			playersListViewUpdate();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickAllButton(View view) {
		Log.i(TAG, "onClickAllButton");
		try {
			ListView listView_players = (ListView) findViewById(R.id.players_players_listView);
			ToggleButton toggleButton_all = (ToggleButton) findViewById(R.id.players_all_toggleButton);
			if (toggleButton_all.isChecked()) {
				for (int i = 0; i < mPlayers.getRegistedPlayers().size(); i++) {
					listView_players.setItemChecked(i, true);
					mPlayers.getRegistedPlayer(i).setStatus(STATUS.Alive);
				}
			} else {
				for (int i = 0; i < mPlayers.getRegistedPlayers().size(); i++) {
					listView_players.setItemChecked(i, false);
					mPlayers.getRegistedPlayer(i).setStatus(STATUS.NotPlaying);
				}
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			mPlayers.savePlayers(this);
			// 画面の終了
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
