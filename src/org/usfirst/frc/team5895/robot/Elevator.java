/**
 * motion magic control mode
 */
package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.concurrent.TimeUnit;
import org.usfirst.frc.team5895.robot.lib.Constants;
import org.usfirst.frc.team5895.robot.lib.Instrum;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

public class Elevator {
	private TalonSRX talon;
	private DigitalInput forwardLimitSwitch, reverseLimitSwitch;
	
	public void Elevator() {
		talon = new TalonSRX(0);
		forwardLimitSwitch = new DigitalInput(1);
		reverseLimitSwitch = new DigitalInput(2);
	}

	public void setTalonSRX() {
		
		/* first choose the sensor */
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
		talon.setSensorPhase(true);
		talon.setInverted(false);
		
		/* Set relevant frame periods to be at least as fast as periodic rate*/
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);

		/* set the peak and nominal outputs, 12V means full */
		talon.configNominalOutputForward(0, Constants.kTimeoutMs);
		talon.configNominalOutputReverse(0, Constants.kTimeoutMs);
		talon.configPeakOutputForward(0.15, Constants.kTimeoutMs);
		talon.configPeakOutputReverse(-0.15, Constants.kTimeoutMs);
		
		/* set closed loop gains in slot0 - see documentation */
		talon.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
		talon.config_kF(0, 0.2, Constants.kTimeoutMs);
		talon.config_kP(0, 0.6, Constants.kTimeoutMs);
		talon.config_kI(0, 0, Constants.kTimeoutMs);
		talon.config_kD(0, 0, Constants.kTimeoutMs);
		
		/* set acceleration and vcruise velocity - see documentation */
		talon.configMotionCruiseVelocity(2662, Constants.kTimeoutMs);
		talon.configMotionAcceleration(5000, Constants.kTimeoutMs);
		
		/* zero the sensor */
		talon.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
	}

/*	//receives joystick data (percent voltage)
	public void driverControl(double percent) {
		talon.set(ControlMode.PercentOutput, percent);
	}
*/
	
	public void update() {
		
		/* calculate the percent motor output */
		double motorOutput = talon.getMotorOutputPercent();
		talon.set(ControlMode.PercentOutput, motorOutput);

		if(forwardLimitSwitch.get()) {
			talon.configPeakOutputForward(0, Constants.kTimeoutMs);	
		}
		else if (reverseLimitSwitch.get()) {
			talon.configPeakOutputReverse(0, Constants.kTimeoutMs);	
		}
		
		/* instrumentation */
		Instrum.Process(talon);
		try { TimeUnit.MILLISECONDS.sleep(10); } catch(Exception e) {}
		
	}
	
	/* Motion Magic */
	public void setTargetPosition(double targetAngle) {
		double targetPos = targetAngle / 360.0 * 13.0 * 4096; /* 45 degrees  4096 ticks/rev */
		talon.set(ControlMode.MotionMagic, targetPos); 

	}
}