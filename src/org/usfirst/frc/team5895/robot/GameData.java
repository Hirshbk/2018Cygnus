package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.DriverStation;

public class GameData {

	String gameData; 
	boolean leftSwitch, leftScale; {
		leftSwitch = true; 
		leftScale = true;
	}
	
	public void GameData() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if(gameData.charAt(0) == 'L'){
			leftSwitch= true;
		} else if(gameData.charAt(0) == 'R'){
			leftSwitch= false;
		} else {
			DriverStation.reportError("Did not read field data", false);
		}
		
		if(gameData.charAt(1) == 'L') {
			leftScale= true;
		} else if(gameData.charAt(1) == 'R'){
			leftScale=false;
		} else  {
			DriverStation.reportError("Did not read field data", false);
		}
		
	} 

	public boolean getLeftSwitch() {
		return leftSwitch;
	}
	public boolean getLeftScale() {
		return leftScale;
	}
	
}

	

	

