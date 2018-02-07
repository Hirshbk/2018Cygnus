package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GameData {

	String gameData; 
	String side;
	
	boolean leftSwitch, leftScale; {
		leftSwitch = true; 
		leftScale = true;
	}
	
		
		side = SmartDashboard.getString("DB/String 0", "nothing");
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
	//right side, right switch, right scale
	public boolean RRR() {
		if (side.toUpperCase().contains("R")
				&& leftSwitch == false
				&& leftScale == false)
			return true;
		else
			return false;
	}
	//right side, right switch, left scale
	public boolean RRL() {
		if (side.toUpperCase().contains("R")
				&& leftSwitch == false
				&& leftScale == true)
			return true;
		else
			return false;
	}
	//right side, left switch, right scale
	public boolean RLR() {
		if (side.toUpperCase().contains("R")
				&& leftSwitch == true
				&& leftScale == false)
			return true;
		else
			return false;
	}
	//right side, left switch, left scale
	public boolean RLL() {
		if (side.toUpperCase().contains("R")
				&& leftSwitch == true
				&& leftScale == true)
			return true;
		else
			return false;
	}
	//left side, left switch, left scale
	public boolean LLL() {
		if (side.toUpperCase().contains("L")
				&& leftSwitch == true
				&& leftScale == true)
			return true;
		else
			return false;
	}
	//left side, left switch, right scale
	public boolean LLR() {
		if (side.toUpperCase().contains("L")
				&& leftSwitch == true
				&& leftScale == false)
			return true;
		else
			return false;
	}
	//left side, right switch, left scale
	public boolean LRL() {
		if (side.toUpperCase().contains("L")
				&& leftSwitch == false
				&& leftScale == true)
			return true;
		else
			return false;
	}
	//left side, right switch, right scale
	public boolean LRR() {
		if (side.toUpperCase().contains("L")
				&& leftSwitch == false
				&& leftScale == false)
			return true;
		else
			return false;
	}
}

	

	

