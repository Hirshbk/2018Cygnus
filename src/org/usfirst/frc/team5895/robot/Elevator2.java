/**
 * motion magic control mode
 */
package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.lib.BetterDigitalInput;
import org.usfirst.frc.team5895.robot.lib.DistanceSensor;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Elevator2 {
	private TalonSRX elevatorMaster, elevatorFollower1, elevatorFollower2;
	private BetterDigitalInput topLimitSwitch, bottomLimitSwitch;
	private Solenoid brakeSolenoid;
	private DistanceSensor leftDistanceSensor, rightDistanceSensor;
	boolean aboveScale;
	
	private enum Mode_Type {MOVING, BRAKING, DISENGAGING, CLIMBING, PERCENT, DISABLED};
	private Mode_Type mode = Mode_Type.MOVING;
	
	public static final int kSlotIdx = 0;
	public static final int kPIDLoopIdx = 0;
	public static final int kTimeoutMs = 0;
	
	private double targetPos;
	private double footConversion = 1.0 / 2048.0 * 1.432 / 12.0;
	private double brakeTimestamp;
	private double percentSetting;
	
	public Elevator2() {
		elevatorMaster = new TalonSRX(ElectricalLayout.MOTOR_ELEVATOR_MASTER);
		elevatorFollower1 = new TalonSRX(ElectricalLayout.MOTOR_ELEVATOR_FOLLOWER_1);
		elevatorFollower2 = new TalonSRX(ElectricalLayout.MOTOR_ELEVATOR_FOLLOWER_2);
		
		elevatorFollower1.set(ControlMode.Follower, ElectricalLayout.MOTOR_ELEVATOR_MASTER);
		elevatorFollower2.set(ControlMode.Follower, ElectricalLayout.MOTOR_ELEVATOR_MASTER);
		
		topLimitSwitch = new BetterDigitalInput(ElectricalLayout.SENSOR_ELEVATOR_TOP);
		bottomLimitSwitch = new BetterDigitalInput(ElectricalLayout.SENSOR_ELEVATOR_BOTTOM);
		brakeSolenoid = new Solenoid(ElectricalLayout.SOLENOID_ELEVATOR_BRAKE);
		leftDistanceSensor = new DistanceSensor(ElectricalLayout.SENSOR_ELEVATOR_DISTANCE_LEFT);
		rightDistanceSensor = new DistanceSensor(ElectricalLayout.SENSOR_ELEVATOR_DISTANCE_RIGHT);
	
		/* first choose the sensor */
		elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
		elevatorMaster.setSensorPhase(true);
		elevatorMaster.setInverted(false);
		
		/* Set relevant frame periods to be at least as fast as periodic rate*/
		elevatorMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, kTimeoutMs);
		elevatorMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, kTimeoutMs);

		/* set the peak and nominal outputs, 12V means full */
		elevatorMaster.configNominalOutputForward(0, kTimeoutMs);
		elevatorMaster.configNominalOutputReverse(0, kTimeoutMs);
		elevatorMaster.configPeakOutputForward(0.15, kTimeoutMs);
		elevatorMaster.configPeakOutputReverse(-0.15, kTimeoutMs);
		
		/* set closed loop gains in slot0 - see documentation */
		elevatorMaster.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
		elevatorMaster.config_kF(0, 0.2, kTimeoutMs);
		elevatorMaster.config_kP(0, 0.6, kTimeoutMs);
		elevatorMaster.config_kI(0, 0, kTimeoutMs);
		elevatorMaster.config_kD(0, 0, kTimeoutMs);
		
		/* set acceleration and vcruise velocity - see documentation */
		elevatorMaster.configMotionCruiseVelocity(2662, kTimeoutMs);
		elevatorMaster.configMotionAcceleration(5000, kTimeoutMs);
		
		/* zero the distance sensor */
		elevatorMaster.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);
		
		/*set up the encoder */
		elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		elevatorMaster.setSelectedSensorPosition(0, 0, 10);
	}
	
	/**
	 * gets the height of the elevator in feet
	 * @return the height of the elevator in feet
	 */
	public double getHeight() {
		
		double height = elevatorMaster.getSelectedSensorPosition(0) * footConversion; // 2048 ticks per rev, pitch diameter: 1.432in
		
		return height;
	}
	
	/* Motion Magic */
	/**
	 * sets the elevator to go to a specified height 
	 * @param targetHeight the height to go to in feet
	 */
	public void setTargetPosition(double targetHeight) {
		targetPos = targetHeight * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING;
	}
	
	/* NOTE:
	 * The below 5 methods have "double targetPos =" and some number
	 * These are dummy numbers, in feet, based LOOSELY on the height of each target.
	 */
	
	/**
	 * sets the elevator to go to the tallest height of the scale
	 * (either when the other side is winning or there's already a layer of cubes)
	 */
	public void highScale() {
		targetPos = 6.5  * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING;
	}
	
	/**
	 * sets the elevator to go the height of the scale when it's balanced
	 */
	public void midScale() {
		targetPos = 6 * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING; 
	}
	
	/**
	 * sets the elevator to go to the low height of the scale 
	 * (when we're winning)
	 */
	public void lowScale() {
		targetPos = 5.5 * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING;
	}
	
	/**
	 * sets the elevator to go to the height of the switch
	 */
	public void switchHeight() { //must be "switchHeight" because just "switch" doesn't work
		targetPos = 2 * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING; 
	}
	
	/**
	 * sets the elevator to go to its lowest height
	 */
	public void floor() {
		targetPos = 0 * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING; 
	}
	
	/**
	 * drives the elevator at a percent of voltage
	 * @param speed the percent of voltage to move at
	 */
	public void percentDrive(double speed) {
		percentSetting = speed;
		mode = Mode_Type.PERCENT;
	}
	
	/**
	 * uses the analog distance sensor to detect whether the elevator is above the scale
	 * @return true if the elevator is above the scale, false if not
	 */
	public boolean aboveScale() {	
		return ((leftDistanceSensor.getDistance() < 3) && (rightDistanceSensor.getDistance() < 3));
	}
	
	/**
	 * checks if the elevator is at the target position and moving slowly
	 * @return true if it is both at position and with low velocity, false otherwise
	 */
	public boolean atTarget() {
		return (((elevatorMaster.getSelectedSensorPosition(0) / footConversion) - targetPos < 1.0/12.0) 
				&& (elevatorMaster.getSelectedSensorVelocity(0) / footConversion < 1));
	}
	
	/**
	 * sets the elevator to go down to the floor automatically if we're not above the scale
	 */
	public void autoDrop() {
		if(!aboveScale) {
			targetPos = 0;
			mode = Mode_Type.DISENGAGING;
		}
	}
	
	/**
	 * disables the elevator
	 */
	public void disable() {
		mode = Mode_Type.DISABLED;
	}
	
	public void update() {

		//this sets the max current based on if the limit switch is triggered
		if(topLimitSwitch.getRisingEdge()) {
			elevatorMaster.configPeakOutputForward(0, kTimeoutMs);	
		}
		else if(topLimitSwitch.getFallingEdge()) {
			elevatorMaster.configPeakOutputForward(0.15, kTimeoutMs);
		}
		
		if (bottomLimitSwitch.getRisingEdge()) {
			elevatorMaster.configPeakOutputReverse(0, kTimeoutMs);	
		}
		else if (bottomLimitSwitch.getFallingEdge()) {
			elevatorMaster.configPeakOutputReverse(-0.15, kTimeoutMs);
		}
			
		//automatically brakes if it's at target and not moving quickly
		//I have no idea what the velocity units are so I just put 1, change when we actually have a robot
		if(atTarget()) {
			mode = Mode_Type.BRAKING;
		}
		
		switch(mode) {
		
		case MOVING:
			
			elevatorMaster.set(ControlMode.MotionMagic, targetPos); 
			
			break;
			
		case BRAKING:
			
			brakeSolenoid.set(true);
			elevatorMaster.set(ControlMode.PercentOutput, 0);
			
			break;
		
		case DISENGAGING:
			
			brakeSolenoid.set(false);
			if(Timer.getFPGATimestamp() - brakeTimestamp > 50) {
				mode = Mode_Type.MOVING;
			}
			
			break;
			
		case CLIMBING:
			
			elevatorMaster.set(ControlMode.PercentOutput, 0.2);
			if(bottomLimitSwitch.get() == false) {
				mode = Mode_Type.BRAKING;
			}
			
			break;
			
		case PERCENT:
		
			elevatorMaster.set(ControlMode.PercentOutput, percentSetting);
			
			break;
			
		case DISABLED:
			brakeSolenoid.set(false);
			elevatorMaster.set(ControlMode.PercentOutput, 0);
			break;
		}
	}
}