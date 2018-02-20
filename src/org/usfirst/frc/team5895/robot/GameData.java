package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GameData {

	String gameData; 
	String side;
	
	boolean leftSwitch = true;
	boolean leftScale = true;
	
	public GameData() {
		//gets the side that we start on from the dashboard
		side = SmartDashboard.getString("DB/String 0", "nothing");
	} 
	
	/**
	 * gets the game data from the field
	 */
	public void getGameData() {
		//gets the game data from the field
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				
				//leftSwitch is true if the switch position is left, false otherwise
				if(gameData.charAt(0) == 'L'){
					leftSwitch= true;
				} else if(gameData.charAt(0) == 'R'){
					leftSwitch= false;
				} else {
					DriverStation.reportError("Did not read field data", false);
				}
				
				//leftScale is true if the scale position is left, false otherwise
				if(gameData.charAt(1) == 'L') {
					leftScale= true;
				} else if(gameData.charAt(1) == 'R'){
					leftScale=false;
				} else  {
					DriverStation.reportError("Did not read field data", false);
				}
	}
	
	/**
	 * right side, right switch, right scale
	 * @return true if the order is RRR, false otherwise
	 */
	public boolean RRR() {
		return (side.toUpperCase().contains("R")
				&& leftSwitch == false
				&& leftScale == false);
	}
	
	/**
	 * right side, right switch, left scale
	 * @return true if the order is RRL, false otherwise
	 */
	public boolean RRL() {
		return (side.toUpperCase().contains("R")
				&& leftSwitch == false
				&& leftScale == true);
	}
	
	/**
	 * right side, left switch, right scale
	 * @returntrue if the order is RLR, false otherwise
	 */
	public boolean RLR() {
		return (side.toUpperCase().contains("R")
				&& leftSwitch == true
				&& leftScale == false);
	}
	
	/**
	 * right side, left switch, left scale
	 * @return true if the order is RLL, false otherwise
	 */
	public boolean RLL() {
		return (side.toUpperCase().contains("R")
				&& leftSwitch == true
				&& leftScale == true);
	}
	
	/**
	 * left side, left switch, left scale
	 * @return true if the order is LLL, false otherwise
	 */
	public boolean LLL() {
		return (side.toUpperCase().contains("L")
				&& leftSwitch == true
				&& leftScale == true);
	}
	
	/**
	 * left side, left switch, right scale
	 * @return true if the order is LLR, false otherwise
	 */
	public boolean LLR() {
		return (side.toUpperCase().contains("L")
				&& leftSwitch == true
				&& leftScale == false);
	}
	/**
	 * left side, right switch, left scale
	 * @return true if the order is LRL, false otherwise
	 */
	public boolean LRL() {
		return (side.toUpperCase().contains("L")
				&& leftSwitch == false
				&& leftScale == true);
	}
	/**
	 * left side, right switch, right scale
	 * @return true if the order is LRR, false otherwise
	 */
	public boolean LRR() {
		return (side.toUpperCase().contains("L")
				&& leftSwitch == false
				&& leftScale == false);
	}
}

	

	

