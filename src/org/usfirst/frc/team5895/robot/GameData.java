package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GameData {

	private String gameData; 
	private String side;
	private String doScale;
	private String doSwitch;
	private String autoRoutine;
	
	public GameData() {
	} 
	
	/**
	 * gets the game data from the field
	 */
	public String getGameData() {
		
		//gets the side that we start on from the dashboard
		side = SmartDashboard.getString("DB/String 0", "C");
		doSwitch = SmartDashboard.getString("DB/String 1", "Y");
		doScale = SmartDashboard.getString("DB/String 2", "N");
		
		//gets the game data from the field
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		return gameData;
				
	}
	
	/**
	 * gets the auto routine to run from the game data and dashboard inputs
	 * @return the auto routine to run 
	 */
	public String getAutoRoutine() {
		autoRoutine.concat(side);
		if(doSwitch.toUpperCase().contains("Y")) {
			autoRoutine.concat(String.valueOf(gameData.charAt(0)));
		} else {
			autoRoutine.concat("0");
		}
		if(doScale.toUpperCase().contains("Y")) {
			autoRoutine.concat(String.valueOf(gameData.charAt(1)));
		} else {
			autoRoutine.concat("0");
		}
		return autoRoutine;
	}
	
	/**
	 * returns the labeled dashboard inputs for logging
	 * @return the placement of the robot on the field (side)
	 * @return whether we want to do switch (do switch)
	 * @return whether we want to do scale (do scale)
	 */
	public String getDashboardInput() {
		return "Side: " + side + " Do Switch: " + doSwitch + " Do Scale: " + doScale;
	}
}