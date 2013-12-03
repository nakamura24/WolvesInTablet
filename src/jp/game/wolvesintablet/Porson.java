/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.wolvesintablet;

public class Porson {
	public enum GENDER {
		Male, Female
	}

	protected long mUID;
	protected String mName;
	protected String mSound;
	protected GENDER mGender;
	
	public Porson(){
		this.mUID = 0;
		this.mName = null;
		this.mSound = null;
		this.mGender = GENDER.Male;
	}
	
	public Porson(Porson Porson){
		this.mUID = Porson.mUID;
		this.mName = Porson.mName;
		this.mSound = Porson.mSound;
		this.mGender = Porson.mGender;
	}
	
	public long getUID() {
		return this.mUID;
	}

	public void setUID(long UID) {
		this.mUID = UID;
	}

	public String getName() {
		return this.mName;
	}

	public void setName(String Name) {
		this.mName = Name;
	}

	public String getSound() {
		return this.mSound;
	}

	public void setSound(String Sound) {
		this.mSound = mName;
	}

	public GENDER getGender() {
		return this.mGender;
	}

	public void setGender(GENDER Gender) {
		this.mGender = Gender;
	}
}
