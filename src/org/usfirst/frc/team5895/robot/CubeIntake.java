package org.usfirst.frc.team5895.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class CubeIntake {
	private static enum Mode_Type {INTAKING, EJECTING, HOLDING, WAITING, DISABLED};
	private Mode_Type mode = Mode_Type.EJECTING;
	private TalonSRX leftClawMotor, rightClawMotor;
	private Solenoid solenoidClamp, solenoidClaw;
	private DigitalInput clawSensor;
	double intakeSpeed, lastTime;
	private boolean solenoidState;
	boolean lastHasCube;
	private boolean isDown;
	
	
	public CubeIntake (){
		leftClawMotor = new TalonSRX(ElectricalLayout.MOTOR_CLAW_1);
		rightClawMotor = new TalonSRX(ElectricalLayout.MOTOR_CLAW_2);
		solenoidClamp = new Solenoid (ElectricalLayout.SOLENOID_INTAKE_CLAMP);
		solenoidClaw = new Solenoid(ElectricalLayout.SOLENOID_INTAKE_CLAW);
		clawSensor = new DigitalInput(ElectricalLayout.SENSOR_INTAKE);
		lastHasCube=false;
	    isDown = false;
	    
		}

	/**
	 * sets the claw to intaking mode
	 */
	public void intake(){
		mode = Mode_Type.INTAKING;
		}
	
	/**
	 * ejects a cube
	 */
	public void eject(){
		mode= Mode_Type.EJECTING;
		}
	
	/**
	 * disables claw
	 */
	public void disable(){
		mode = Mode_Type.DISABLED;
	}
	
	/**
	 * lifts claw
	 */
	public void up() {
		isDown = false;	
	}
	
	/**
	 * drops claw
	 */
	public void down (){
		isDown = true;
	}
	
	/**
	 * returns current claw state
	 * @return - 0 if disabled
	 * @return - 1 if intaking
	 * @return - 2 if ejecting
	 * @return - 3 if holding
	 * @return - 4 if waiting
	 * @return - -1 if none of the above (which shouldn't happen or else you have Problems)
	 */
	public double getState() {
		switch (mode) {
			case DISABLED:
				return 0;
			case INTAKING:
				return 1;
			case EJECTING:
				return 2;
			case HOLDING:
				return 3;
			case WAITING:
				return 4;
			default:
				return -1;
		}
	}
	
	public void update(){
		
		if (solenoidClaw.get() != isDown) {
			   solenoidClaw.set(isDown);
			}
		
		boolean hasCube = !clawSensor.get();
		
		intakeSpeed = 0;
		
		switch(mode) {
		
		case INTAKING:
		     
		     intakeSpeed = 0.5;
		     
		     if((lastHasCube == false) && (hasCube)) {
				lastTime = Timer.getFPGATimestamp(); //stamp the time we start waiting 
			 	mode = Mode_Type.WAITING; //once we have the cube, we prepare to hold and clamp
			 }
			solenoidState = false; //solenoid only clamps once it is holding 
			break;
			
		case WAITING:
		
			intakeSpeed = 0.5;
			
        	double curTime = Timer.getFPGATimestamp(); //stamps current time 
            if (curTime - lastTime > 0.2) { //compares the time we started waiting to current time
            	mode = Mode_Type.HOLDING; //if it has been waiting for 200ms, it begins to hold
            } else {
            	mode = Mode_Type.WAITING; //if not, it keeps holding
            }
            solenoidState=false; 
			break;
		
		case HOLDING:
			intakeSpeed= .2;
			solenoidState = true; // solenoid used to clamp cube while holding 
			break;
		
		case EJECTING:
			intakeSpeed = -.5;
			solenoidState = false; 
			break; 
		
		case DISABLED:
			intakeSpeed = 0;
			solenoidState = false;
	
		leftClawMotor.set(ControlMode.PercentOutput, intakeSpeed);
		rightClawMotor.set(ControlMode.PercentOutput, intakeSpeed);
		solenoidClamp.set(solenoidState);
		
		}
	}
}