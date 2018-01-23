package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class CubeIntake {
	private static enum Mode_Type {INTAKING, EJECTING, HOLDING};
	private Mode_Type mode = Mode_Type.EJECTING;
	private Talon motor;
	private Solenoid solenoid;
	private boolean solenoidState;
	private DigitalInput sensor;
	
	public CubeIntake (){
		motor = new Talon(3);
		solenoid = new Solenoid (0);
		sensor = new DigitalInput(4); 	//* eventually will use analog sensor 
		}

	public void intake(){
		mode = Mode_Type.INTAKING;
		}
	
	public void eject(){
		mode= Mode_Type.EJECTING;
		}
	
	public void update(){
		if(mode != Mode_Type.EJECTING) {
			if (! sensor.get()) {
				mode = Mode_Type.HOLDING;
			}else {
				mode = Mode_Type.INTAKING;	
			}
		}
		
		double motorspeed = 0; 
		switch(mode) {
		
		case INTAKING:
			motorspeed = 1;
			solenoidState = false;
			break;
		
		case HOLDING:
			motorspeed = 0;
			solenoidState = true; //* solenoid used to clamp cube while holding 
			break;
		
		case EJECTING:
			motorspeed = -1;
			solenoidState = false; 
			break; 
			}
	
		motor.set(motorspeed);
		solenoid.set(solenoidState);
		
	}
}
	

