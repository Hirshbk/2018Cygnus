package org.usfirst.frc.team5895.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class CubeIntake {
	private static enum Mode_Type {INTAKING, EJECTING, HOLDING, SPINNING_RIGHT, SPINNING_LEFT, DISABLED};
	private Mode_Type mode = Mode_Type.DISABLED;
	private Talon leftClawMotor;
	private VictorSPX rightClawMotor;
	private AnalogInput leftClawSensor, rightClawSensor;
	double leftSpeed, rightSpeed;
	double lastTime;
	private boolean solenoidState;
	boolean lastHasCube;
	private boolean isDown;
	private Solenoid clawSolenoid, clampSolenoid;
	
	public CubeIntake (){
		leftClawMotor = new Talon(0);
		rightClawMotor = new VictorSPX(ElectricalLayout.MOTOR_CLAW_2);
		
		leftClawSensor = new AnalogInput(ElectricalLayout.SENSOR_INTAKE_LEFT);
		rightClawSensor = new AnalogInput(ElectricalLayout.SENSOR_INTAKE_RIGHT);
		

		clawSolenoid = new Solenoid(ElectricalLayout.SOLENOID_INTAKE_CLAW);
		clampSolenoid = new Solenoid(ElectricalLayout.SOLENOID_INTAKE_CLAMP);
		
		lastHasCube=false;
	    isDown = false;
	    
		leftClawMotor.setInverted(true);
		rightClawMotor.setInverted(false);
		
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
		lastTime = Timer.getFPGATimestamp();
		}
	
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
		mode = Mode_Type.DISABLED;
	}
	
	/**
	 * drops claw
	 */
	public void down(){
		isDown = true;
		mode = Mode_Type.INTAKING;
	}
	
	public boolean hasCube() {
		return (leftClawSensor.getVoltage() > 3 && rightClawSensor.getVoltage() > 3);
	}
	
	/**
	 * @return true if the cube is turned left in the intake, false otherwise
	 */
	public boolean tiltedLeft() {
		return (leftClawSensor.getVoltage() < 3 && rightClawSensor.getVoltage() > 3);
	}
	
	/**
	 * @return true if the cube is turned right in the intake, false otherwise
	 */
	public boolean tiltedRight() {
		return (leftClawSensor.getVoltage() > 3 && rightClawSensor.getVoltage() < 3);
	}
	
	public double getLeftDistance() {
		return leftClawSensor.getVoltage();
	}
	
	public double getRightDistance() {
		return rightClawSensor.getVoltage();
	}
	
	public void update(){
		
		switch(mode) {
		
		case INTAKING:
		     
		     leftSpeed = 1.0;
		     rightSpeed = 1.0;
		     
		     if(tiltedLeft()) {
		    	 lastTime = Timer.getFPGATimestamp();
		    	 mode = Mode_Type.SPINNING_LEFT;
		     }
			
		     if(tiltedRight()) {
		    	 lastTime = Timer.getFPGATimestamp();
		    	 mode = Mode_Type.SPINNING_RIGHT;
		     }
		     
		     if((lastHasCube == false) && (hasCube())) {
			 	mode = Mode_Type.HOLDING; //once we have the cube, we prepare to hold and clamp
			 }
			solenoidState = false; //solenoid only clamps once it is holding 
			break;
		
		case HOLDING:
			leftSpeed = 0.2;
			rightSpeed = 0.2;
			solenoidState = true; // solenoid used to clamp cube while holding 
			DriverStation.reportError("holding", false);
			break;
		
		case EJECTING:
			leftSpeed = -1.0;
			rightSpeed = -1.0;
			solenoidState = false; 
			double waitTime = Timer.getFPGATimestamp(); //stamps current time 
            if (waitTime - lastTime > 0.6) { //compares the time we started waiting to current time
            	mode = Mode_Type.INTAKING; //if it has been waiting for 200ms, it begins to hold
            } else {
            	mode = Mode_Type.EJECTING; //if not, it keeps waiting
            }
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
		
		leftClawMotor.set(leftSpeed);
		rightClawMotor.set(ControlMode.PercentOutput, rightSpeed);
		
		clawSolenoid.set(!isDown);
		clampSolenoid.set(!solenoidState);
		
		DriverStation.reportError("leftClawDistance" + getLeftDistance(), false);
		System.out.println("leftClawDistance" + getLeftDistance());
		
	}
}