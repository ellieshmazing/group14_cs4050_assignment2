package assignment.players;

public class DataKey {
	//Player name is the name they're referred by
	private String playerName;
	//Player position is the numerical representation of their position
	//1 - Point Guard, 2 - Shooting Guard, 3 - Small Forward, 4 - Power Forward, 5 - Center
	private int playerPosition;

	// default constructor
	public DataKey() {
		this(null, 0);
	}
        
	public DataKey(String name, int position) {
		playerName = name;
		playerPosition = position;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getPlayerPosition() {
		return playerPosition;
	}

	/**
	 * Returns 0 if this DataKey is equal to k, returns -1 if this DataKey is smaller
	 * than k, and it returns 1 otherwise. 
	 */
	public int compareTo(DataKey k) {
            if (this.getPlayerPosition() == k.getPlayerPosition()) {
                int compare = this.playerName.compareTo(k.getPlayerName());
                if (compare == 0){
                     return 0;
                } 
                else if (compare < 0) {
                    return -1;
                }
            }
            else if(this.getPlayerPosition() < k.getPlayerPosition()){
                    return -1;
            }
            return 1;
            
	}
}
