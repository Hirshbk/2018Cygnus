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
	private TrajectoryDriveController pStraight;
	private TrajectoryDriveController pRRX1,pRRX2,pRRX3,pRRX4,pRRR;
	private TrajectoryDriveController pInUse;
	private enum Mode_Type {TELEOP,AUTO_SPLINE, AUTO_BACKWARDS_SPLINE, AUTO_MIRROR_SPLINE};
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
			// drive straight
			pStraight = new TrajectoryDriveController("/home/lvuser/Test/Straight.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			// start at Right
			pRRX1 = new TrajectoryDriveController("/home/lvuser/Test/SwitchRR.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pRRX2 = new TrajectoryDriveController("/home/lvuser/Test/RRX2.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pRRX3 = new TrajectoryDriveController("/home/lvuser/Test/RRX3.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pRRX4 = new TrajectoryDriveController("/home/lvuser/Test/RRX4.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pRRR = new TrajectoryDriveController("/home/lvuser/Test/RRR.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
	
		}catch (Exception e) {
			DriverStation.reportError("All auto files not on robot!", false);
		}
	}
	
	public void resetEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
		
	}
	public void resetNavX() {
		navX.reset();
	}
	
	public double getAngle() {
		return navX.getAngle(); // navX reads angle in degree
	}
	
	// default = drive straight across auto line
	public void autoForWardStraight() {
		resetEncoders();
		pInUse = pStraight;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void auto_back_straight() {
		resetEncoders();
		pInUse = pStraight;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	
	//start at Right of the field
	public void autoRRX1() {
		resetEncoders();
		pInUse = pRRX1;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	
	public void autoRRX2() {
		resetEncoders();
		pInUse = pRRX2;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	public void auto_RRX3() {
		resetEncoders();
		pInUse = pRRX3;
		mode = Mode_Type.AUTO_SPLINE;
	}
	public void autoRRX4() {
		resetEncoders();
		pInUse = pRRX4;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	public void autoRRR() {
		resetEncoders();
		pInUse = pRRR;
		mode = Mode_Type.AUTO_SPLINE;
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
	
	public boolean isPFinished() {
		
		return (pInUse.isFinished());
		
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
				
			case AUTO_MIRROR_SPLINE:
				DriverStation.reportError("updating", false);
				
				double[] m_mirror = new double[2];
				m_mirror = pInUse.getOutput(leftEncoder.getDistance(), rightEncoder.getDistance(), -getAngle()*3.14/180); // mirror spline

				leftDriveMaster.set(ControlMode.PercentOutput, m_mirror[1]);
				rightDriveMaster.set(ControlMode.PercentOutput, -m_mirror[0]);
				
				break;
			
			case TELEOP: 
				leftDriveMaster.set(ControlMode.PercentOutput, leftspeed);
				rightDriveMaster.set(ControlMode.PercentOutput, -rightspeed);
				break;
				
			
		}
	}

}