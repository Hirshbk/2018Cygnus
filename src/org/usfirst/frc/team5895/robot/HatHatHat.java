package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class HatHatHat {
	private Solenoid leftSolenoid1,leftSolenoid2,rightSolenoid1,rightSolenoid2;
	private DigitalInput hatSensor;
	private enum Mode_Type {LEFT_EXTEND,RIGHT_EXTEND,HOLDING}; //create states
	private Mode_Type mode = Mode_Type.HOLDING;
	private double lastTime;
	private boolean leftSolenoidState = false, rightSolenoidState = false; //assume false = down, true = up 
	
	public HatHatHat() {
		//re-enter port when we know!
		leftSolenoid1 = new Solenoid(ElectricalLayout.SOLENOID_HAT_LEFT_1); 
		leftSolenoid2 = new Solenoid(ElectricalLayout.SOLENOID_HAT_LEFT_2);
		rightSolenoid1 = new Solenoid(ElectricalLayout.SOLENOID_HAT_RIGHT_1);
		rightSolenoid2 = new Solenoid(ElectricalLayout.SOLENOID_HAT_RIGHT_2);
		hatSensor = new DigitalInput(SENSOR_HAT);
	}
	
	public void hold() {
		mode = Mode_Type.HOLDING;
	}
	
	public void leftExtend() {
		if(mode == Mode_Type.HOLDING){
			mode = Mode_Type.LEFT_EXTEND;
		}
	}
	
	public void rightExtend() {
		if(mode == Mode_Type.HOLDING){
			mode = Mode_Type.RIGHT_EXTEND;
		}
	}
	
	public int getState() {
		switch (mode) {
		case HOLDING:
			return 0;
		case LEFT_EXTEND:
			return -1;
		case RIGHT_EXTEND:
			return 1;
		default:
			return Integer.MAX_VALUE; //no state
		}
	}
	
	public void update() {
		if(hatSensor.get()) { //assume no cube = true
			mode = Mode_Type.HOLDING;
		}
		
		if(mode == Mode_Type.LEFT_EXTEND || mode == Mode_Type.RIGHT_EXTEND) {
			double curTime = Timer.getFPGATimestamp();
            if (curTime - lastTime > 2.0) {
            	mode = Mode_Type.HOLDING; 
            }
		}
		
		switch(mode) {
			case HOLDING:
				leftSolenoidState = false;
				rightSolenoidState = false;
				break;
			case LEFT_EXTEND:
				leftSolenoidState = true;
				rightSolenoidState = false;
				lastTime = Timer.getFPGATimestamp();
				break;
			case RIGHT_EXTEND:
				leftSolenoidState = false;
				rightSolenoidState = true;
				lastTime = Timer.getFPGATimestamp();
				break;	
			default:
				mode = Mode_Type.HOLDING; //if incorrect mode happens, change to holding.
		}
		leftSolenoid1.set(leftSolenoidState);
		leftSolenoid2.set(leftSolenoidState);
		rightSolenoid1.set(rightSolenoidState);
		rightSolenoid2.set(rightSolenoidState);		
	}
		
}