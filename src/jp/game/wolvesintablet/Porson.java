package jp.game.wolvesintablet;

public class Porson {
	public enum GENDER {
		Male,Female
	}
	private long mUID;
	private String mName;
	private String mSound;
	private GENDER mGender;

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
