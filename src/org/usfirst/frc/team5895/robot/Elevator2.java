/**
 * motion magic control mode
 */
package org.usfirst.frc.team5895.robot;

import java.util.concurrent.TimeUnit;
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
	private DistanceSensor distSensor;
	boolean aboveScale;
	
	private enum Mode_Type {MOVING, BRAKING, DISENGAGING, CLIMBING, PERCENT};
	private Mode_Type mode = Mode_Type.MOVING;
	
	public static final int kSlotIdx = 0;
	public static final int kPIDLoopIdx = 0;
	public static final int kTimeoutMs = 10;
	
	private double targetPos;
	private double footConversion = 1 / 360.0 * 13.0 * 4096;
	private double brakeTimestamp;
	
	public Elevator2() {
		elevatorMaster = new TalonSRX(ElectricalLayout.MOTOR_ELEVATOR_MASTER);
		elevatorFollower1 = new TalonSRX(ElectricalLayout.MOTOR_ELEVATOR_FOLLOWER_1);
		elevatorFollower2 = new TalonSRX(ElectricalLayout.MOTOR_ELEVATOR_FOLLOWER_2);
		
		elevatorFollower1.set(ControlMode.Follower, ElectricalLayout.MOTOR_ELEVATOR_MASTER);
		elevatorFollower2.set(ControlMode.Follower, ElectricalLayout.MOTOR_ELEVATOR_MASTER);
		
		topLimitSwitch = new BetterDigitalInput(ElectricalLayout.SENSOR_ELEVATOR_TOP);
		bottomLimitSwitch = new BetterDigitalInput(ElectricalLayout.SENSOR_ELEVATOR_BOTTOM);
		brakeSolenoid = new Solenoid(ElectricalLayout.SOLENOID_ELEVATOR_BRAKE);
		distSensor = new DistanceSensor();
	}

	public void setTalonSRX() {
		
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
	
	public double getHeight() {
		
		double height = elevatorMaster.getSelectedSensorPosition(0)/2048.*1.432/12.0; // 2048 ticks per rev, pitch diameter: 1.432in
		
		return height;
	}
	
	/* Motion Magic */
	public void setTargetPosition(double targetHeight) {
		targetPos = targetHeight * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING;
	}
	
	/**
	 * NOTE:
	 * The below 5 methods have "double targetPos =" and some number
	 * These are dummy numbers, in feet, based LOOSELY on the height of each target.
	 * 
	 * @param targetHeight
	 */
	
	public void highScale() {
		targetPos = 6.5  * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING;
	}
	
	public void midScale() {
		targetPos = 6 * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING; 
	}
	
	public void lowScale() {
		targetPos = 5.5 * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING;
	}
	
	public void switchHeight() { //must be "switchHeight" because just "switch" doesn't work
		targetPos = 2 * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING; 
	}
	
	public void floor() {
		targetPos = 0 * footConversion;
		brakeTimestamp = Timer.getFPGATimestamp();
		mode = Mode_Type.DISENGAGING; 
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
	
	public boolean atTarget() {
		if((elevatorMaster.getSelectedSensorPosition(0) / footConversion) - targetPos < 1.0/12.0) {
			return true;
		} else {
			return false;
		}
	}
	
	// elevator automatically falls to the bottom after detecting no scale
	public void autoDrop() {
		if(!aboveScale) {
			targetPos = 0;
			mode = Mode_Type.DISENGAGING;
		}
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
		if(atTarget() && (elevatorMaster.getSelectedSensorVelocity(0) / footConversion < 1)) {
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
			
			/* calculate the percent motor output */
			double motorOutput = elevatorMaster.getMotorOutputPercent();
			elevatorMaster.set(ControlMode.PercentOutput, motorOutput);
			
			break;
		
		}
	}
}