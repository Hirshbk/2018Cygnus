/**
 * motion magic control mode
 */
package org.usfirst.frc.team5895.robot;

import java.util.concurrent.TimeUnit;
import org.usfirst.frc.team5895.robot.lib.BetterDigitalInput;
import org.usfirst.frc.team5895.robot.lib.DistanceSensor;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

public class Elevator {
	private TalonSRX talon;
	private BetterDigitalInput topLimitSwitch, bottomLimitSwitch;
	private DistanceSensor distSensor;
	boolean aboveScale;
	
	public static final int kSlotIdx = 0;
	public static final int kPIDLoopIdx = 0;
	public static final int kTimeoutMs = 10;
	
	public Elevator() {
		talon = new TalonSRX(0);
		topLimitSwitch = new BetterDigitalInput(1);
		bottomLimitSwitch = new BetterDigitalInput(2);
		distSensor = new DistanceSensor();
	}

	public void setTalonSRX() {
		
		/* first choose the sensor */
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
		talon.setSensorPhase(true);
		talon.setInverted(false);
		
		/* Set relevant frame periods to be at least as fast as periodic rate*/
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, kTimeoutMs);
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, kTimeoutMs);

		/* set the peak and nominal outputs, 12V means full */
		talon.configNominalOutputForward(0, kTimeoutMs);
		talon.configNominalOutputReverse(0, kTimeoutMs);
		talon.configPeakOutputForward(0.15, kTimeoutMs);
		talon.configPeakOutputReverse(-0.15, kTimeoutMs);
		
		/* set closed loop gains in slot0 - see documentation */
		talon.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
		talon.config_kF(0, 0.2, kTimeoutMs);
		talon.config_kP(0, 0.6, kTimeoutMs);
		talon.config_kI(0, 0, kTimeoutMs);
		talon.config_kD(0, 0, kTimeoutMs);
		
		/* set acceleration and vcruise velocity - see documentation */
		talon.configMotionCruiseVelocity(2662, kTimeoutMs);
		talon.configMotionAcceleration(5000, kTimeoutMs);
		
		/* zero the sensor */
		talon.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);
	}
	
	/* Motion Magic */
	public void setTargetPosition(double targetHeight) {
		double targetPos = targetHeight / 360.0 * 13.0 * 4096;
		talon.set(ControlMode.MotionMagic, targetPos); 
	}
	
	/**
	 * NOTE:
	 * The below 5 methods have "double targetPos =" and some number
	 * These are dummy numbers, in feet, based LOOSELY on the height of each target.
	 * 
	 * @param targetHeight
	 */
	
	public void highScale(double targetHeight) {
		double targetPos = 6.5;
		talon.set(ControlMode.MotionMagic, targetPos); 
	}
	public void midScale(double targetHeight) {
		double targetPos = 6;
		talon.set(ControlMode.MotionMagic, targetPos); 
	}
	public void lowScale(double targetHeight) {
		double targetPos = 5.5;
		talon.set(ControlMode.MotionMagic, targetPos); 
	}
	public void switch1(double targetHeight) { //must be "switch1" because just "switch" doesn't work
		double targetPos = 2;
		talon.set(ControlMode.MotionMagic, targetPos); 
	}
	public void floor(double targetHeight) {
		double targetPos = 0;
		talon.set(ControlMode.MotionMagic, targetPos); 
	}
	
	// uses the analog distance sensor to detect whether the claw is above the scale
	public boolean aboveScale() {	
		if(distSensor.getDistance() < 3) {
			aboveScale = true;
		}
		else {
			aboveScale = false;
		}
		return aboveScale;
	}
	
	// elevator automatically falls to the bottom after detecting no scale
	public void autoDrop() {
		if(!aboveScale) {
			double targetPos = 0;
			talon.set(ControlMode.MotionMagic, targetPos);
		}
	}
	
	public void update() {
		
		/* calculate the percent motor output */
		double motorOutput = talon.getMotorOutputPercent();
		talon.set(ControlMode.PercentOutput, motorOutput);

		if(topLimitSwitch.getRisingEdge()) {
			talon.configPeakOutputForward(0, kTimeoutMs);	
		}
		else if(topLimitSwitch.getFallingEdge()) {
			talon.configPeakOutputForward(0.15, kTimeoutMs);
		}
		
		if (bottomLimitSwitch.getRisingEdge()) {
			talon.configPeakOutputReverse(0, kTimeoutMs);	
		}
		else if (bottomLimitSwitch.getFallingEdge()) {
			talon.configPeakOutputReverse(-0.15, kTimeoutMs);
		}
	}
}