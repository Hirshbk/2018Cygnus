package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GameData {

	String gameData; 
	String side;
	String doScale;
	
	//true if left, false if right
	boolean switchSide = true;
	boolean scaleSide = true;
	
	public GameData() {

	} 
	
	/**
	 * gets the game data from the field
	 */
	public void getGameData() {
		
		//gets the side that we start on from the dashboard
		side = SmartDashboard.getString("DB/String 0", "nothing");
		doScale = SmartDashboard.getString("DB/String 1", "nothing");
		
		//gets the game data from the field
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				
				//switchSide is true if the switch position is left, false otherwise
				if(gameData.charAt(0) == 'L'){
					switchSide = true;
				} else if(gameData.charAt(0) == 'R'){
					switchSide= false;
				} else {
					DriverStation.reportError("Did not read field data", false);
				}
				
				//scaleSide is true if the scale position is left, false otherwise
				if(gameData.charAt(1) == 'L') {
					scaleSide= true;
				} else if(gameData.charAt(1) == 'R'){
					scaleSide=false;
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
				&& switchSide == false
				&& scaleSide == false
				&& doScale.toUpperCase().contains("Y"));
	}
	
	/**
	 * right side, right switch, left scale
	 * @return true if the order is RRL, false otherwise
	 */
	public boolean RRL() {
		return (side.toUpperCase().contains("R")
				&& switchSide == false
				&& scaleSide == true
				&& doScale.toUpperCase().contains("Y"));
	}
	/**
	 * right side, right switch, no scale
	 * @return true if the order is RR0, false otherwise
	 */
	public boolean RR0() {
		return (side.toUpperCase().contains("R")
				&& switchSide == false
				&& doScale.toUpperCase().contains("N"));
	}
	/**
	 * right side, left switch, right scale
	 * @returntrue if the order is RLR, false otherwise
	 */
	public boolean RLR() {
		return (side.toUpperCase().contains("R")
				&& switchSide == true
				&& scaleSide == false
				&& doScale.toUpperCase().contains("Y"));
	}
	
	/**
	 * right side, left switch, left scale
	 * @return true if the order is RLL, false otherwise
	 */
	public boolean RLL() {
		return (side.toUpperCase().contains("R")
				&& switchSide == true
				&& scaleSide == true
				&& doScale.toUpperCase().contains("Y"));
	}
	/**
	 * right side, left switch, no scale
	 * @return true if the order is RL0, false otherwise
	 */
	public boolean RL0() {
		return (side.toUpperCase().contains("R")
				&& switchSide == true
				&& doScale.toUpperCase().contains("N"));
	}
	/**
	 * left side, left switch, left scale
	 * @return true if the order is LLL, false otherwise
	 */
	public boolean LLL() {
		return (side.toUpperCase().contains("L")
				&& switchSide == true
				&& scaleSide == true
				&& doScale.toUpperCase().contains("Y"));
	}
	/**
	 * left side, left switch, right scale
	 * @return true if the order is LLR, false otherwise
	 */
	public boolean LLR() {
		return (side.toUpperCase().contains("L")
				&& switchSide == true
				&& scaleSide == false
				&& doScale.toUpperCase().contains("Y"));
	}
	/**
	 * left side, left switch, no scale
	 * @return true if the order is LL0, false otherwise
	 */
	public boolean LL0() {
		return (side.toUpperCase().contains("L")
				&& switchSide == true
				&& doScale.toUpperCase().contains("N"));
	}
	/**
	 * left side, right switch, left scale
	 * @return true if the order is LRL, false otherwise
	 */
	/**
	 * left side, right switch, left scale
	 * @return true if the order is LRL, false otherwise
	 */
	public boolean LRL() {
		return (side.toUpperCase().contains("L")
				&& switchSide == false
				&& scaleSide == true
				&& doScale.toUpperCase().contains("Y"));
	}
	/**
	 * left side, right switch, right scale
	 * @return true if the order is LRR, false otherwise
	 */
	public boolean LRR() {
		return (side.toUpperCase().contains("L")
				&& switchSide == false
				&& scaleSide == false
				&& doScale.toUpperCase().contains("Y"));
	}
	/**
	 * left side, right switch, no scale
	 * @return true if the order is LR0, false otherwise
	 */
	public boolean LR0() {
		return (side.toUpperCase().contains("L")
				&& switchSide == false
				&& doScale.toUpperCase().contains("N"));
	}
	/**
	 * center, right switch, right scale
	 * @return true if the order is CRR, false otherwise
	 */
	public boolean CRR() {
		return (side.toUpperCase().contains("C")
				&& switchSide == false
				&& scaleSide == false
				&& doScale.toUpperCase().contains("Y"));
	}
	/**
	 * center, right switch, right scale
	 * @return true if the order is CRL, false otherwise
	 */
	public boolean CRL() {
		return (side.toUpperCase().contains("C")
				&& switchSide == false
				&& scaleSide == true
				&& doScale.toUpperCase().contains("Y"));
	}
	/**
	 * center, right switch, no scale
	 * @return true if the order is CR0, false otherwise
	 */
	public boolean CR0() {
		return (side.toUpperCase().contains("C")
				&& switchSide == false
				&& doScale.toUpperCase().contains("N"));
	}
	/**
	 * center, left switch, right scale
	 * @return true if the order is CLR, false otherwise
	 */
	public boolean CLR() {
		return (side.toUpperCase().contains("C")
				&& switchSide == true
				&& scaleSide == false
				&& doScale.toUpperCase().contains("Y"));
	}
	/**
	 * center, left switch, left scale
	 * @return true if the order is CLL, false otherwise
	 */
	public boolean CLL() {
		return (side.toUpperCase().contains("C")
				&& switchSide == true
				&& scaleSide == true
				&& doScale.toUpperCase().contains("Y"));
	}
	/**
	 * center, left switch, no scale
	 * @return true if the order is CL0, false otherwise
	 */
	public boolean CL0() {
		return (side.toUpperCase().contains("C")
				&& switchSide == true
				&& scaleSide == false
				&& doScale.toUpperCase().contains("N"));
	}
}

	

	

