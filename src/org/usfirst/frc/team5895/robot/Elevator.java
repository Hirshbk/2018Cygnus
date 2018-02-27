package org.usfirst.frc.team5895.robot;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Elevator {
	private TalonSRX elevatorMaster;
	private VictorSPX elevatorFollower1, elevatorFollower2;
	private Solenoid brakeSolenoid;
//	private AnalogInput leftDistanceSensor, rightDistanceSensor;
	boolean aboveScale;
	boolean brakeOn = false;
	
	private enum Mode_Type {MOVING, BRAKING, DISENGAGING, CLIMBING, PERCENT, DISABLED};
	private Mode_Type mode = Mode_Type.DISABLED;
	
	public static final int kSlotIdx = 0;
	public static final int kPIDLoopIdx = 0;
	public static final int kTimeoutMs = 10;
	
	private double targetPos;
	private double footConversion = 9.22 * Math.pow(10, -5);
	private double carriageOffset = 0.54;
	private double brakeTimestamp;
	private double percentSetting;
	
	public Elevator() {
		elevatorMaster = new TalonSRX(ElectricalLayout.MOTOR_ELEVATOR_MASTER);
		elevatorFollower1 = new VictorSPX(ElectricalLayout.MOTOR_ELEVATOR_FOLLOWER_1);
		elevatorFollower2 = new VictorSPX(ElectricalLayout.MOTOR_ELEVATOR_FOLLOWER_2);
		
		elevatorFollower1.follow(elevatorMaster);
		elevatorFollower2.follow(elevatorMaster);
		
		brakeSolenoid = new Solenoid(ElectricalLayout.SOLENOID_ELEVATOR_BRAKE);
//		leftDistanceSensor = new AnalogInput(ElectricalLayout.SENSOR_ELEVATOR_DISTANCE_LEFT);
//		rightDistanceSensor = new AnalogInput(ElectricalLayout.SENSOR_ELEVATOR_DISTANCE_RIGHT);
	
		/* first choose the sensor */
		elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
		elevatorMaster.setSensorPhase(false);
		elevatorMaster.setInverted(false);
		
		/* Set relevant frame periods to be at least as fast as periodic rate*/
		elevatorMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, kTimeoutMs);
		elevatorMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, kTimeoutMs);

		elevatorMaster.configNominalOutputForward(0, kTimeoutMs);
		elevatorMaster.configNominalOutputReverse(0, kTimeoutMs);
		elevatorMaster.configPeakOutputForward(1, kTimeoutMs);
		elevatorMaster.configPeakOutputReverse(-1, kTimeoutMs);
		
		/* set closed loop gains in slot0 - see documentation */
		elevatorMaster.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
		elevatorMaster.config_kF(0, 0.33, kTimeoutMs);
		elevatorMaster.config_kP(0, 0.5, kTimeoutMs);
		elevatorMaster.config_kI(0, 0, kTimeoutMs);
		elevatorMaster.config_kD(0, 0, kTimeoutMs);
		
		/* set acceleration and vcruise velocity - see documentation */
		elevatorMaster.configMotionCruiseVelocity(3900, kTimeoutMs);
		elevatorMaster.configMotionAcceleration(7000, kTimeoutMs);
		
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
		
		return elevatorMaster.getSelectedSensorPosition(0) * footConversion + carriageOffset; // 2048 ticks per rev, pitch diameter: 1.432in
	}
	
	/* Motion Magic */
	/**
	 * sets the elevator to go to a specified height 
	 * @param targetHeight the height to go to in feet
	 */
	public void setTargetPosition(double targetHeight) {
		targetPos = (targetHeight - carriageOffset) / footConversion;
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
	
	public void brake() {
		mode = Mode_Type.BRAKING;
	}
	
	public double getSpeed() {
		return elevatorMaster.getSelectedSensorVelocity(0);
	}
	
/*	
	/**
	 * uses the analog distance sensor to detect whether the elevator is above the scale
	 * @return true if the elevator is above the scale, false if not
	 *
	public boolean aboveScale() {	
		return ((leftDistanceSensor.getVoltage() > 3) && (rightDistanceSensor.getVoltage() > 3));
	}
	
	/**
	 * checks if the elevator is at the target position and moving slowly
	 * @return true if it is both at position and with low velocity, false otherwise
	 */
	public boolean atTarget() {
		return ((Math.abs(elevatorMaster.getSelectedSensorPosition(0) - targetPos) < 200.0) 
				&& (Math.abs(elevatorMaster.getSelectedSensorVelocity(0)) < 1.0));
	}
	
	/*
	 * sets the elevator to go down to the floor automatically if we're not above the scale
	 * 
	public void autoDrop() {
		if(!aboveScale) {
			targetPos = 0;
			mode = Mode_Type.DISENGAGING;
		}
	}
*/	
	
	public double getState() {
		return mode.ordinal();
	}
	
	/**
	 * disables the elevator
	 */
	public void disable() {
		mode = Mode_Type.DISABLED;
	}
	
	public void update() {
/*
		//this sets the max current based on if the limit switch is triggered
		if(elevatorMaster.) {
			elevatorMaster.configPeakOutputForward(0.0, kTimeoutMs);	
		}
		else if(topLimitSwitch.getFallingEdge()) {
			elevatorMaster.configPeakOutputForward(1.0, kTimeoutMs);
		}
		
		if (bottomLimitSwitch.getRisingEdge()) {
			elevatorMaster.configPeakOutputReverse(0.0, kTimeoutMs);	
		}
		else if (bottomLimitSwitch.getFallingEdge()) {
			elevatorMaster.configPeakOutputReverse(-1.0, kTimeoutMs);
		}
*/		
		//automatically brakes if it's at target and not moving quickly
		//I have no idea what the velocity units are so I just put 1, change when we actually have a robot
		
		switch(mode) {
		
		case DISENGAGING:
			
			brakeOn = false;
			if(Timer.getFPGATimestamp() - brakeTimestamp > 0.1) {
				mode = Mode_Type.MOVING;
			}
			
			break;
		
		case MOVING:
			brakeOn = false;
			elevatorMaster.set(ControlMode.MotionMagic, targetPos); 
			
			if(atTarget()) {
				mode = Mode_Type.BRAKING;
			}
			
			break;
			
		case BRAKING:
			
			brakeOn = true;
			elevatorMaster.set(ControlMode.PercentOutput, 0);
			
			break;
			
		case CLIMBING:
			
			elevatorMaster.set(ControlMode.PercentOutput, 0.2);
/*			if(bottomLimitSwitch.get() == false) {
				mode = Mode_Type.BRAKING;
			}
*/			
			break;
			
		case PERCENT:
			brakeOn = false;
			elevatorMaster.set(ControlMode.PercentOutput, percentSetting);
			
			break;
			
		case DISABLED:
			brakeOn = false;
			elevatorMaster.set(ControlMode.PercentOutput, 0);
			break;
		}
		brakeSolenoid.set(brakeOn);
		
//		DriverStation.reportError("" + elevatorMaster.getSelectedSensorPosition(0), false);
//		DriverStation.reportError("" + atTarget(), false);
//		DriverStation.reportError("" + targetPos, false);
//		System.out.println("" + getHeight());
		
	}
}