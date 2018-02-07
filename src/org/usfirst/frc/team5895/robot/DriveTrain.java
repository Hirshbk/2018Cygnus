package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.lib.TrajectoryDriveController;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc.team5895.robot.lib.NavX;

import java.awt.geom.Point2D;

import org.usfirst.frc.team5895.robot.framework.Recorder;
import org.usfirst.frc.team5895.robot.lib.BetterEncoder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

//all parameters are tested using Basan 

public class DriveTrain {

	private TalonSRX leftDriveMaster, leftDriveFollower, rightDriveMaster, rightDriveFollower;
	private BetterEncoder leftEncoder,rightEncoder;
	private double leftspeed, rightspeed;
	private NavX navX;
	private TrajectoryDriveController pSwitchNear,pSwitchFar,pScaleNear,pScaleFar,pStraight,pBackStraight;
	private TrajectoryDriveController pInUse;
	private enum Mode_Type {TELEOP,AUTO_SPLINE, AUTO_BACKWARDS_SPLINE};
	private Mode_Type mode = Mode_Type.TELEOP;
	
	// tracking
	private double posX, posY; // feet
	private double lastDistance = 0d; // distance traveled the last time update() was called
	
	public DriveTrain() {
		
		leftDriveMaster = new TalonSRX(ElectricalLayout.MOTOR_DRIVE_LEFT_MASTER);
		leftDriveFollower = new TalonSRX(ElectricalLayout.MOTOR_DRIVE_LEFT_FOLLOWER);
		rightDriveMaster = new TalonSRX(ElectricalLayout.MOTOR_DRIVE_RIGHT_MASTER);
		rightDriveFollower = new TalonSRX(ElectricalLayout.MOTOR_DRIVE_RIGHT_FOLLOWER);
		
		leftDriveFollower.set(ControlMode.Follower, ElectricalLayout.MOTOR_DRIVE_LEFT_MASTER);
		rightDriveFollower.set(ControlMode.Follower, ElectricalLayout.MOTOR_DRIVE_RIGHT_MASTER);
		
		leftEncoder = new BetterEncoder(ElectricalLayout.ENCODER_DRIVE_LEFT_1, ElectricalLayout.ENCODER_DRIVE_LEFT_2, true, Encoder.EncodingType.k4X);
		rightEncoder = new BetterEncoder(ElectricalLayout.ENCODER_DRIVE_RIGHT_1, ElectricalLayout.ENCODER_DRIVE_RIGHT_2, false, Encoder.EncodingType.k4X);
		
		leftEncoder.setDistancePerPulse(4/12.0*Math.PI/360);
		rightEncoder.setDistancePerPulse(4/12.0*Math.PI/360);
		
		navX = new NavX();
		
		try { // check back everything. generate missing spline
			pSwitchNear = new TrajectoryDriveController("/home/lvuser/Test/NearSwitch.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pSwitchFar = new TrajectoryDriveController("/home/lvuser/Test/FarSwitch.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.006);
			pScaleNear = new TrajectoryDriveController("/home/lvuser/Test/NearScale.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pScaleFar = new TrajectoryDriveController("/home/lvuser/Test/FarScale.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.0065);
			pStraight = new TrajectoryDriveController("/home/lvuser/Test/Straight.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pBackStraight = new TrajectoryDriveController("/home/lvuser/Test/Backwards.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
	
		}catch (Exception e) {
			DriverStation.reportError("All auto files not on robot!", false);
		}
	}
	
	public void resetEncoderAndNavX() {
		leftEncoder.reset();
		rightEncoder.reset();
		navX.reset();
	}
	
	public double getAngle() {
		return navX.getAngle(); // navX reads angle in degree
	}
	
	public void autoSwitchNear() {
		resetEncoderAndNavX();
		pInUse = pSwitchNear;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void autoSwitchFar() {
		resetEncoderAndNavX();
		pInUse = pSwitchFar;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void autoScaleNear() {
		resetEncoderAndNavX();
		pInUse = pScaleNear;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void autoScaleFar() {
		resetEncoderAndNavX();
		pInUse = pScaleFar;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void autoStraight() {
		resetEncoderAndNavX();
		pInUse = pStraight;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void autoBackStraight() {
		resetEncoderAndNavX();
		pInUse = pBackStraight;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	
	public void arcadeDrive(double speed, double turn) {
		leftspeed = speed - turn;
		rightspeed = speed + turn;
		mode = Mode_Type.TELEOP;
	}
	
	/**
	 * @return the average velocity in feet per second from the left and right encoders.
	 */
	public double getVelocity() {
		return (leftEncoder.getRate()+rightEncoder.getRate())/2;
	}
	
	/**
	 * @return the average distance traveled in feet from the left and right encoders.
	 */
	public double getDistanceTraveled() {
		return (leftEncoder.getDistance()+rightEncoder.getDistance())/2;
	}
	
	public double getXPosition() {
		return posX;
	}
	
	public double getYPosition() {
		return posY;
	}
	
	public void update() {
		
		// position
		double distance = getDistanceTraveled()-lastDistance;
		
		posX += distance*Math.cos(Math.toRadians(getAngle()));
		posY += distance*Math.sin(Math.toRadians(getAngle()));
		
		distance = getDistanceTraveled();
		
		switch(mode) {
		
			case AUTO_SPLINE:
				double[] m = new double[2];
				m = pInUse.getOutput(leftEncoder.getDistance(), rightEncoder.getDistance(), -getAngle()*3.14/180); // change navX angle to radian

				leftDriveMaster.set(ControlMode.PercentOutput, -m[0]);
				rightDriveMaster.set(ControlMode.PercentOutput, m[1]);
				break;
				
			case AUTO_BACKWARDS_SPLINE:
				DriverStation.reportError("updating", false);
				
				double[] m_back = new double[2];
				m_back = pInUse.getOutput(-leftEncoder.getDistance(), -rightEncoder.getDistance(), getAngle()*3.14/180); // driving backwards

				leftDriveMaster.set(ControlMode.PercentOutput, m_back[0]);
				rightDriveMaster.set(ControlMode.PercentOutput, -m_back[1]);
				
				break;
			
			case TELEOP: 
				leftDriveMaster.set(ControlMode.PercentOutput, leftspeed);
				rightDriveMaster.set(ControlMode.PercentOutput, -rightspeed);
				break;
				
			
		}
	}

}