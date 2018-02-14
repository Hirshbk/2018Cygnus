package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.lib.DistanceSensor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class CubeIntake {
	private static enum Mode_Type {INTAKING, EJECTING, HOLDING, WAITING, SPINNING_LEFT, SPINNING_RIGHT, DISABLED};
	private Mode_Type mode = Mode_Type.EJECTING;
	
	private VictorSPX leftClawMotor, rightClawMotor;
	private Solenoid solenoidClamp, solenoidClaw;
	private DistanceSensor leftClawSensor, rightClawSensor;
	
	double intakeSpeed, lastTime;
	double leftSpeed, rightSpeed;
	private boolean solenoidState;
	boolean lastHasCube;
	private boolean isDown;
	
	
	public CubeIntake (){
		leftClawMotor = new VictorSPX(ElectricalLayout.MOTOR_CLAW_1);
		rightClawMotor = new VictorSPX(ElectricalLayout.MOTOR_CLAW_2);
		leftClawMotor.setInverted(true);
		
		solenoidClamp = new Solenoid (ElectricalLayout.SOLENOID_INTAKE_CLAMP);
		solenoidClaw = new Solenoid(ElectricalLayout.SOLENOID_INTAKE_CLAW);
		
		leftClawSensor = new DistanceSensor(ElectricalLayout.SENSOR_INTAKE_LEFT);
		rightClawSensor = new DistanceSensor(ElectricalLayout.SENSOR_INTAKE_RIGHT);
		
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
	 * spins the cube in the intake
	 */
	public void spinRight() {
		mode = Mode_Type.SPINNING_RIGHT;
		lastTime = Timer.getFPGATimestamp();
	}
	public void spinLeft() {
		mode = Mode_Type.SPINNING_LEFT;
		lastTime = Timer.getFPGATimestamp();
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
	 * @return - 1 if disabled
	 * @return - 2 if intaking
	 * @return - 3 if ejecting
	 * @return - 4 if holding
	 * @return - 5 if waiting
	 * @return - 0 if none of the above (which shouldn't happen or else you have Problems)
	 */
	public double getState() {
		switch (mode) {
			case DISABLED:
				return 1;
			case INTAKING:
				return 2;
			case EJECTING:
				return 3;
			case HOLDING:
				return 4;
			case WAITING:
				return 5;
			default:
				return 0;
		}
	}
	
	public boolean hasCube() {
		return (leftClawSensor.getDistance() < 10 && rightClawSensor.getDistance() < 10);
	}
	
	public void update(){
		
		leftSpeed = 0;
		rightSpeed = 0;
		
		switch(mode) {
		
		case INTAKING:
		     
		     leftSpeed = 0.5;
		     rightSpeed = 0.5;
		     
			/*	if (leftClawSensor.getDistance() > 3 && rightClawSensor.getDistance() < 3) {
					spinLeft();
				} else if (leftClawSensor.getDistance() < 3 && rightClawSensor.getDistance() > 3) {
					spinRight();
				} */
				
		     if((lastHasCube == false) && (hasCube())) {
				lastTime = Timer.getFPGATimestamp(); //stamp the time we start waiting 
			 	mode = Mode_Type.WAITING; //once we have the cube, we prepare to hold and clamp
			 }
			solenoidState = false; //solenoid only clamps once it is holding 
			break;
			
		case WAITING:
		
			leftSpeed = 0.4;
			rightSpeed = 0.5;
			
        	double waitTime = Timer.getFPGATimestamp(); //stamps current time 
            if (waitTime - lastTime > 0.2) { //compares the time we started waiting to current time
            	mode = Mode_Type.HOLDING; //if it has been waiting for 200ms, it begins to hold
            } else {
            	mode = Mode_Type.WAITING; //if not, it keeps waiting
            }
            solenoidState=false; 
			break;
		
		case HOLDING:
			leftSpeed = 0.2;
			rightSpeed = 0.2;
			solenoidState = true; // solenoid used to clamp cube while holding 
			break;
		
		case EJECTING:
			leftSpeed = -0.5;
			rightSpeed = -0.5;
			solenoidState = false; 
			break; 
		
		case SPINNING_RIGHT:
			leftSpeed = -0.25;
			rightSpeed = 0.25;
			double spinRightTime = Timer.getFPGATimestamp(); //stamps current time 
            if (spinRightTime - lastTime > 0.4) { //compares the time we started waiting to current time
            	mode = Mode_Type.INTAKING; //if it has been waiting for 200ms, it begins to hold
            } else {
            	mode = Mode_Type.SPINNING_RIGHT; //if not, it keeps waiting
            }
			break;
			
		case SPINNING_LEFT:
			leftSpeed = 0.25;
			rightSpeed = -0.25;
			double spinLeftTime = Timer.getFPGATimestamp(); //stamps current time 
            if (spinLeftTime - lastTime > 0.5) { //compares the time we started waiting to current time
            	mode = Mode_Type.INTAKING; //if it has been waiting for 200ms, it begins to hold
            } else {
            	mode = Mode_Type.SPINNING_LEFT; //if not, it keeps waiting
            }
			break;
			
		case DISABLED:
			leftSpeed = 0;
			rightSpeed = 0;
			solenoidState = false;
			break;
		}
		
		leftClawMotor.set(ControlMode.PercentOutput, leftSpeed);
		rightClawMotor.set(ControlMode.PercentOutput, rightSpeed);
		
	}
}