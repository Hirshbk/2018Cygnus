/**
 * motion magic control mode
 */
package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.concurrent.TimeUnit;
import org.usfirst.frc.team5895.robot.lib.Constants;
import org.usfirst.frc.team5895.robot.lib.Instrum;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

public class Elevator {
	TalonSRX talon = new TalonSRX(0);
	Joystick joystick = new Joystick(0);
	StringBuilder sb = new StringBuilder();

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

	/**
	 * This function is called periodically during operator control
	 */
	public void update() {
		/* get gamepad axis - forward stick is positive */
		double leftYstick = -1.0 * joystick.getY();
		/* calculate the percent motor output */
		double motorOutput = talon.getMotorOutputPercent();
		/* prepare line to print */
		sb.append("\tOut%:");
		sb.append(motorOutput);
		sb.append("\tVel:");
		sb.append(talon.getSelectedSensorVelocity(Constants.kPIDLoopIdx));

		if(joystick.getRawButton(1)) {
		setTargetPosition(0);
		}
		else if(joystick.getRawButton(2)) {
			setTargetPosition(30);
		}
		else if(joystick.getRawButton(3)) {
			setTargetPosition(45);
		}
		else if(joystick.getRawButton(4)) {
			setTargetPosition(60);
		}
		else {
			/* Percent voltage mode */
			talon.set(ControlMode.PercentOutput, leftYstick);
		}
		
		/* instrumentation */
		Instrum.Process(talon, sb);
		try { TimeUnit.MILLISECONDS.sleep(10); } catch(Exception e) {}
		
	}
	
	public void setTargetPosition(double targetAngle) {
		
			/* Motion Magic */
			double targetPos = targetAngle / 360.0 * 13.0 * 4096; /* 45 degrees  4096 ticks/rev */
			
			talon.set(ControlMode.MotionMagic, targetPos); 

			/* append more signals to print when in speed mode. */
			sb.append("\terr:");
			sb.append(talon.getClosedLoopError(Constants.kPIDLoopIdx));
			sb.append("\tpos:");
			sb.append(talon.getSelectedSensorPosition(Constants.kPIDLoopIdx));
			sb.append("\ttrg:");
			sb.append(targetPos);

	}
}