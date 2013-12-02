/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.wolvesintablet;

public class Player extends Role {
	public enum STATUS {
		Alive, Lynched, Killed, Died, NotPlaying
	}

	private STATUS mStatus;
	private long mSelectedPlayerUID;
	private boolean mLover;

	public Player() {
		this.mStatus = STATUS.Alive;
		this.mSelectedPlayerUID = 0;
		this.mLover = false;
	}

	public STATUS getStatus() {
		return this.mStatus;
	}

	public void setStatus(STATUS status) {
		this.mStatus = status;
	}

	public long getSelectedPlayerUID() {
		return this.mSelectedPlayerUID;
	}

	public void setSelectedPlayerUID(long selectedPlayerUID) {
		this.mSelectedPlayerUID = selectedPlayerUID;
	}

	public boolean getLover() {
		return this.mLover;
	}

	public void setLover(boolean Lover) {
		this.mLover = Lover;
	}
}
