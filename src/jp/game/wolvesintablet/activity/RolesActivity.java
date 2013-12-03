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
import jp.game.wolvesintablet.Role.ROLE;
import static jp.game.wolvesintablet.Constant.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

// 役職調整の処理
public class RolesActivity extends Activity {
	private static final String TAG = "RolesActivity";
	private Roles mRoles;
	private Players mPlayers;
	private int number_of_players = 0;
	private int[] Values = new int[ROLE.values().length];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_roles);
			
			mPlayers = Players.getInstance();
			mRoles = Roles.getInstance();

			Resources resource = getResources();
			String textView_number = resource
					.getString(R.string.roles_number_textView);
			String textView_max = resource.getString(R.string.roles_max_button);
			number_of_players = mPlayers.getPlayingPlayers().size();
			playersAlertDialog(number_of_players);
			TextView role_textView_number = (TextView) findViewById(R.id.roles_number_textView);
			role_textView_number.setText(String.format(textView_number,
					number_of_players));
			Button role_button_max = (Button) findViewById(R.id.roles_max_button);
			role_button_max.setText(String.format(textView_max,
					mRoles.getmMaxPlayers()));
			RadioButton radio_none = (RadioButton) findViewById(R.id.roles_none_radioButton);
			radio_none.setChecked(!mRoles.getRolesRandom());
			RadioButton radio_roles_ramdam = (RadioButton) findViewById(R.id.roles_ramdam_radioButton);
			radio_roles_ramdam.setChecked(mRoles.getRolesRandom()
					&& !mRoles.getWerewolvesRandom());
			RadioButton radio_werewolves_random = (RadioButton) findViewById(R.id.roles_werewolves_random_radioButton);
			radio_werewolves_random.setChecked(mRoles.getWerewolvesRandom());

			// 役職の人数を表示
			addTableRows();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	// 役職の人数を表示
	private void addTableRows() {
		Log.i(TAG, "addTableRows");
		try {
			TableLayout tablelayout_roles = (TableLayout) findViewById(R.id.roles_tableLayout);
			Resources resource = getResources();
			String[] role_names = resource.getStringArray(R.array.roles_names);
			for (ROLE role : ROLE.values()) {
				if (mRoles.get(role, MinPlayers) < 0)
					continue;
				TableRow table_row = new TableRow(this);
				TextView TextView_role_name = new TextView(this);
				TextView_role_name.setText(role_names[role.ordinal()]);
				TextView TextView_role_number = new TextView(this);
				TextView_role_number.setId(role.ordinal());
				Values[role.ordinal()] = mRoles.get(role, number_of_players);
				TextView_role_number.setText(String.valueOf(Values[role
						.ordinal()]));
				Button inclement = new Button(this);
				inclement.setText(" ＋ ");
				inclement.setId(0x0100 | role.ordinal());
				inclement.setOnClickListener(new InclementOnClickListener());
				Button declement = new Button(this);
				declement.setText(" － ");
				declement.setId(0x0200 | role.ordinal());
				declement.setHeight(12);
				declement.setOnClickListener(new DeclementOnClickListener());
				table_row.addView(TextView_role_name);
				table_row.addView(TextView_role_number);
				table_row.addView(inclement);
				table_row.addView(declement);
				tablelayout_roles.addView(table_row);
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	private class InclementOnClickListener implements View.OnClickListener {
		public void onClick(View view) {
			Log.i(TAG, "onClick");
			try {
				view.getId();
				TextView TextView_role_number = (TextView) findViewById(view
						.getId() & 0x00ff);
				Values[view.getId() & 0x00ff]++;
				switch (view.getId() & 0x00ff) {
				case 7:
					if (Values[view.getId() & 0x00ff] == 1)
						Values[view.getId() & 0x00ff]++;
					break;
				case 13:
					Values[view.getId() & 0x00ff] = 1;
					break;
				}
				TextView_role_number
						.setText(String.valueOf(Values[view.getId() & 0x00ff]));
			} catch (Exception e) {
				ErrorReport.LogException(RolesActivity.this, e);
			}
		}
	}

	private class DeclementOnClickListener implements View.OnClickListener {
		public void onClick(View view) {
			Log.i(TAG, "onClick");
			try {
				view.getId();
				TextView TextView_role_number = (TextView) findViewById(view
						.getId() & 0x00ff);
				if (Values[view.getId() & 0x00ff] > 0)
					Values[view.getId() & 0x00ff]--;
				switch (view.getId() & 0x00ff) {
				case 0:
					if (Values[view.getId() & 0x00ff] == 0)
						Values[view.getId() & 0x00ff] = 1;
					break;
				case 7:
					if (Values[view.getId() & 0x00ff] == 1)
						Values[view.getId() & 0x00ff]--;
					break;
				case 13:
					Values[view.getId() & 0x00ff] = 0;
					break;
				}
				TextView_role_number
						.setText(String.valueOf(Values[view.getId() & 0x00ff]));
			} catch (Exception e) {
				ErrorReport.LogException(RolesActivity.this, e);
			}
		}
	}

	// 人数の警告ダイアログ
	private void playersAlertDialog(final int number_of_players) {
		Log.i(TAG, "playersAlertDialog");
		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			// アラートダイアログのタイトルを設定します
			alertDialogBuilder.setTitle(R.string.common_text_warning);
			// アラートダイアログのメッセージを設定します
			Resources resource = getResources();
			String warning_players = "";
			if (number_of_players < MinPlayers) {
				warning_players = resource
						.getString(R.string.roles_text_warning_min);
			}
			if (number_of_players > mRoles.getmMaxPlayers()) {
				warning_players = resource
						.getString(R.string.roles_text_warning_max);
			}
			warning_players = String.format(warning_players, MinPlayers);
			int count = 0;
			for (int i = 0; i < ROLE.values().length; i++) {
				count += Values[i];
			}
			if (number_of_players < count) {
				warning_players = resource
						.getString(R.string.roles_text_warning_role);
			}
			alertDialogBuilder.setMessage(warning_players);
			// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			alertDialogBuilder.setPositiveButton(R.string.common_text_ok,
					new PositiveOnClickListener());
			if (number_of_players < MinPlayers
					|| number_of_players > mRoles.getmMaxPlayers()
					|| number_of_players < count) {
				AlertDialog alertDialog = alertDialogBuilder.create();
				// アラートダイアログを表示します
				alertDialog.show();
			}
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	private class PositiveOnClickListener implements
			DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Log.i(TAG, "onClick");
			try {
				if (number_of_players < MinPlayers) {
					// 画面の終了
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
				}
			} catch (Exception e) {
				ErrorReport.LogException(RolesActivity.this, e);
			}
		}
	}

	public void onClickMaxButton(View view) {
		Log.i(TAG, "onClickMaxButton");
		try {
			// 最大人数入力ダイアログ
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			// アラートダイアログのタイトルを設定します
			Resources resource = getResources();
			final String textView_max = resource
					.getString(R.string.roles_max_button);
			alertDialogBuilder.setTitle(String.format(textView_max,
					mRoles.getmMaxPlayers()));
			// アラートダイアログのメッセージを設定します
			final EditText editText = new EditText(this);
			editText.setText(String.valueOf(mRoles.getmMaxPlayers()));
			alertDialogBuilder.setView(editText);
			// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			alertDialogBuilder.setNegativeButton(R.string.common_text_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.i(TAG, "onClick");
							try {
								int max = Integer.parseInt(editText.getText()
										.toString());
								if (max > mRoles.getmMaxPlayers()) {
									mRoles.changeMax(max);
									Button role_button_max = (Button) findViewById(R.id.roles_max_button);
									role_button_max.setText(String.format(
											textView_max,
											mRoles.getmMaxPlayers()));
								}
							} catch (Exception e) {
								ErrorReport.LogException(RolesActivity.this, e);
							}
						}
					});
			// アラートダイアログのキャンセルが可能かどうかを設定します
			alertDialogBuilder.setCancelable(true);
			AlertDialog alertDialog = alertDialogBuilder.create();
			// アラートダイアログを表示します
			alertDialog.show();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(TAG, "onClickOkButton");
		try {
			playersAlertDialog(number_of_players);
			int count = 0;
			for (int i = 0; i < ROLE.values().length; i++) {
				count += Values[i];
			}
			if (number_of_players < count)
				return;
			for (ROLE role : ROLE.values()) {
				TextView TextView_role_number = (TextView) findViewById(role
						.ordinal());
				if (TextView_role_number == null)
					continue;
				mRoles.set(role, number_of_players, Values[role.ordinal()]);
			}
			// 役職設定保存
			mRoles.saveRoles(this);

			// 画面の終了
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			ErrorReport.LogException(this, e);
		}
	}
}
