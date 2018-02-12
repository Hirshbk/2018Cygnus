package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.lib.BetterDigitalInput;
import org.usfirst.frc.team5895.robot.lib.DistanceSensor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class ElevatorTest {
	private TalonSRX elevatorMaster;
	private VictorSPX elevatorFollower1, elevatorFollower2;
	private BetterDigitalInput topLimitSwitch, bottomLimitSwitch;
	private Solenoid brakeSolenoid;
	boolean brakeOn;

	public static final int kSlotIdx = 0;
	public static final int kPIDLoopIdx = 0;
	public static final int kTimeoutMs = 0;
	
	private double footConversion = 1.0 / 2048.0 * 1.432 / 12.0;
	private double motorOutput;
	
	public ElevatorTest() {
		elevatorMaster = new TalonSRX(ElectricalLayout.MOTOR_ELEVATOR_MASTER);
		elevatorFollower1 = new VictorSPX(ElectricalLayout.MOTOR_ELEVATOR_FOLLOWER_1);
		elevatorFollower2 = new VictorSPX(ElectricalLayout.MOTOR_ELEVATOR_FOLLOWER_2);
		
		elevatorFollower1.set(ControlMode.Follower, ElectricalLayout.MOTOR_ELEVATOR_MASTER);
		elevatorFollower2.set(ControlMode.Follower, ElectricalLayout.MOTOR_ELEVATOR_MASTER);
		
		topLimitSwitch = new BetterDigitalInput(ElectricalLayout.SENSOR_ELEVATOR_TOP);
		bottomLimitSwitch = new BetterDigitalInput(ElectricalLayout.SENSOR_ELEVATOR_BOTTOM);
		brakeSolenoid = new Solenoid(ElectricalLayout.SOLENOID_ELEVATOR_BRAKE);

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
		
		/* zero the distance sensor */
		elevatorMaster.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);
		
		/*set up the encoder */
		elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		elevatorMaster.setSelectedSensorPosition(0, 0, 10);
	}
	
	public double getHeight() {
		
		double height = elevatorMaster.getSelectedSensorPosition(0) * footConversion; // 2048 ticks per rev, pitch diameter: 1.432in
		
		return height;
	}

	public void setSpeed(double speed) {
		motorOutput = speed;
	}

	public void enableBrake() {
		brakeOn = true;
	}
	
	public void disableBrake() {
		brakeOn = false;
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
		
		if(brakeOn) {
			elevatorMaster.set(ControlMode.PercentOutput, 0);
		} else {
			elevatorMaster.set(ControlMode.PercentOutput, motorOutput);
		}	
		
		DriverStation.reportError("" + getHeight(), false);
		
	}
}