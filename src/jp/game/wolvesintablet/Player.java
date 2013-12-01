package jp.game.wolvesintablet;

public class Player extends Role {
	public enum STATUS {
		Alive, Lynched, Killed, Died, NotPlaying
	}

	private STATUS mStatus;
	private String mSelectedPlayer;
	private boolean mLover;
	
	public Player(){
		this.mStatus = STATUS.Alive;
		this.mSelectedPlayer = null;
		this.mLover = false;
	}

	public STATUS getStatus() {
		return this.mStatus;
	}

	public void setStatus(STATUS status) {
		this.mStatus = status;
	}

	public String getSelectedPlayer() {
		return this.mSelectedPlayer;
	}

	public void setSelectedPlayer(String SelectedPlayer) {
		this.mSelectedPlayer = SelectedPlayer;
	}

	public boolean getLover() {
		return this.mLover;
	}

	public void setLover(boolean Lover) {
		this.mLover = Lover;
	}
}
