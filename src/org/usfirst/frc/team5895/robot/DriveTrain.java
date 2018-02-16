package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.lib.TrajectoryDriveController;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

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

	private TalonSRX leftDriveMaster, rightDriveMaster;
	private VictorSPX leftDriveFollower, rightDriveFollower;
	private BetterEncoder leftEncoder,rightEncoder;
	private double leftspeed, rightspeed;
	private NavX navX;
	private TrajectoryDriveController pStraight;
	private TrajectoryDriveController pRRX1,pRRX2,pRRX3,pRRX4,pRRR,pRRL;
	private TrajectoryDriveController pCR0,pCRX1;
	private TrajectoryDriveController pInUse;
	private enum Mode_Type {TELEOP,AUTO_SPLINE, AUTO_BACKWARDS_SPLINE, AUTO_MIRROR_SPLINE,AUTO_MIRROR_BACKWARDS_SPLINE};
	private Mode_Type mode = Mode_Type.TELEOP;
	
	// tracking
	private double posX, posY; // feet
	private double lastDistance = 0d; // distance traveled the last time update() was called
	
	public DriveTrain() {
		
		//initialize drive motors
		leftDriveMaster = new TalonSRX(ElectricalLayout.MOTOR_DRIVE_LEFT_MASTER);
		leftDriveFollower = new VictorSPX(ElectricalLayout.MOTOR_DRIVE_LEFT_FOLLOWER);
		rightDriveMaster = new TalonSRX(ElectricalLayout.MOTOR_DRIVE_RIGHT_MASTER);
		rightDriveFollower = new VictorSPX(ElectricalLayout.MOTOR_DRIVE_RIGHT_FOLLOWER);
		
		//set the two followers to follow
		leftDriveFollower.set(ControlMode.Follower, ElectricalLayout.MOTOR_DRIVE_LEFT_MASTER);
		rightDriveFollower.set(ControlMode.Follower, ElectricalLayout.MOTOR_DRIVE_RIGHT_MASTER);
		
		//initialize encoders
		leftEncoder = new BetterEncoder(ElectricalLayout.ENCODER_DRIVE_LEFT_1, ElectricalLayout.ENCODER_DRIVE_LEFT_2, true, Encoder.EncodingType.k4X);
		rightEncoder = new BetterEncoder(ElectricalLayout.ENCODER_DRIVE_RIGHT_1, ElectricalLayout.ENCODER_DRIVE_RIGHT_2, false, Encoder.EncodingType.k4X);
		
		//set encoder distance per pulse so it's in feet
		leftEncoder.setDistancePerPulse(4/12.0*Math.PI/360);
		rightEncoder.setDistancePerPulse(4/12.0*Math.PI/360);
		
		//initialize navx
		navX = new NavX();
		
		//initializes trajectory generator from auto files
		//IF ONE ISN'T ON THE ROBOT ALL THE ONES AFTER IT WON'T WORK
		try { 
			// drive straight
			pStraight = new TrajectoryDriveController("/home/lvuser/Test/Straight.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			//start at Right
			pRRX1 = new TrajectoryDriveController("/home/lvuser/Test/SwitchRR.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pRRX2 = new TrajectoryDriveController("/home/lvuser/Test/RRX2.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pRRX3 = new TrajectoryDriveController("/home/lvuser/Test/RRX3.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pRRX4 = new TrajectoryDriveController("/home/lvuser/Test/RRX4.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pRRR = new TrajectoryDriveController("/home/lvuser/Test/RRR.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pRRL = new TrajectoryDriveController("/home/lvuser/Test/RRL.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			
			//start at Center
			pCR0 = new TrajectoryDriveController("/home/lvuser/Test/CR0.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
			pCRX1 = new TrajectoryDriveController("/home/lvuser/Test/CRX1.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
		}catch (Exception e) {
			DriverStation.reportError("All auto files not on robot!", false);
		}
	}
	
	/**
	 * resets the encoder values to 0
	 */
	public void resetEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
		
	}
	
	/**
	 * resets the NavX to 0
	 */
	public void resetNavX() {
		navX.reset();
	}
	
	/**
	 * gets the current angle of the NavX
	 * @return the NavX angle in degrees
	 */
	public double getAngle() {
		return navX.getAngle(); // navX reads angle in degree
	}
	
	/**
	 * drives straight across the auto line with the claw forward
	 */
	public void autoForwardStraight() {
		resetEncoders();
		pInUse = pStraight;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	/**
	 * drives straight across the auto line with the claw backward
	 */
	public void autoBackwardStraight() {
		resetEncoders();
		pInUse = pStraight;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	
	/**
	 * for right side, goes to the right switch
	 */
	public void autoRRX1() {
		resetEncoders();
		pInUse = pRRX1;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	
	/**
	 * for right side, comes after autoRRX1, turns away from the switch before picking up cube
	 */
	public void autoRRX2() {
		resetEncoders();
		pInUse = pRRX2;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	
	/**
	 * for right side, comes after autoRRX2, drives forward and picks up another cube
	 */
	public void autoRRX3() {
		resetEncoders();
		pInUse = pRRX3;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	/**
	 * for right side, comes after autoRRX3, drives backwards and turns after picking up cube
	 */
	public void autoRRX4() {
		resetEncoders();
		pInUse = pRRX4;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	
	/**
	 * for right side, comes after autoRRX4, goes to right scale
	 */
	//turn may be too sharp for splines, PID turn probably better
	public void autoRRR() {
		resetEncoders();
		pInUse = pRRR;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	/**
	 * for right side, comes after autoRRX4, goes to left scale
	 */
	public void autoRRL() {
		resetEncoders();
		pInUse = pRRL;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	//start at Left

	public void autoLLX1() {
		resetEncoders();
		pInUse = pRRX1;
		mode = Mode_Type.AUTO_MIRROR_BACKWARDS_SPLINE;
	}
	
	public void autoLLX2() {
		resetEncoders();
		pInUse = pRRX2;
		mode = Mode_Type.AUTO_MIRROR_BACKWARDS_SPLINE;
	}
	
	public void autoLLX3() {
		resetEncoders();
		pInUse = pRRX3;
		mode = Mode_Type.AUTO_MIRROR_SPLINE;
	}
	
	public void autoLLX4() {
		resetEncoders();
		pInUse = pRRX4;
		mode = Mode_Type.AUTO_MIRROR_BACKWARDS_SPLINE;
	}
	
	public void autoLLL() {
		resetEncoders();
		pInUse = pRRR;
		mode = Mode_Type.AUTO_MIRROR_SPLINE;
	}
	public void autoLLR() {
		resetEncoders();
		pInUse = pRRL;
		mode = Mode_Type.AUTO_MIRROR_SPLINE;
	}
	
	//start at Center
	/**
	 * from center to front of right switch, drive forwards
	 */
	public void autoCR0() {
		resetEncoders();
		pInUse = pCR0;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	/**
	 * for center to front of left switch, drive forwards
	 */
	public void autoCL0() {
		resetEncoders();
		pInUse = pCR0;
		mode = Mode_Type.AUTO_MIRROR_SPLINE;
	}
	
	/**
	 * for center to front of right switch, drive backwards
	 */
	public void autoBackCR0() {
		resetEncoders();
		pInUse = pCR0;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	
	/**
	 * for center to front of left switch, drive backwards
	 */
	public void autoBackCL0() {
		resetEncoders();
		pInUse = pCR0;
		mode = Mode_Type.AUTO_MIRROR_BACKWARDS_SPLINE;
	}
	
	/**
	 * for center to side of the right switch
	 */
	public void autoCRX1() {
		resetEncoders();
		pInUse = pCRX1;
		mode = Mode_Type.AUTO_BACKWARDS_SPLINE;
	}
	
	/**
	 * for center to side of the left switch 
	 */
	public void autoCLX1() {
		resetEncoders();
		pInUse = pCRX1;
		mode = Mode_Type.AUTO_MIRROR_BACKWARDS_SPLINE;
	}
	
	/**
	 * normal arcade drive code for teleop use
	 * @param speed the forward speed to go at
	 * @param turn the speed to turn at
	 */
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
	
	/**
	 * checks whether the current path is finished
	 * @return true if the path is finished, false otherwise
	 */
	public boolean isPFinished() {
		
		return (pInUse.isFinished());
		
	}
	
	/**
	 * gets the x position of the drivetrain
	 * @return the x position of the drivetrain in feet
	 */
	public double getXPosition() {
		return posX;
	}
	
	/**
	 * gets the y position of the drivetrain
	 * @return the y position of the drivetrain in feet
	 */
	public double getYPosition() {
		return posY;
	}
	
	public void update() {
		
		//changing position and distance
		double distance = getDistanceTraveled()-lastDistance;
		
		posX += distance*Math.cos(Math.toRadians(getAngle()));
		posY += distance*Math.sin(Math.toRadians(getAngle()));
		
		distance = getDistanceTraveled();
		
		switch(mode) {
			
			//spline driving with the claw forward
			case AUTO_SPLINE:
				double[] m = new double[2];
				m = pInUse.getOutput(rightEncoder.getDistance(), leftEncoder.getDistance(), getAngle()*3.14/180); 

				leftDriveMaster.set(ControlMode.PercentOutput, -m[1]);
				rightDriveMaster.set(ControlMode.PercentOutput, m[0]);
				break;
				
			//spline driving with the claw backward
			case AUTO_BACKWARDS_SPLINE:
				double[] m_back = new double[2];
				m_back = pInUse.getOutput(-leftEncoder.getDistance(), -rightEncoder.getDistance(), getAngle()*3.14/180); 

				leftDriveMaster.set(ControlMode.PercentOutput, m_back[0]);
				rightDriveMaster.set(ControlMode.PercentOutput, -m_back[1]);
				break;
			
			//mirrored spline driving for the other side of the field 
			//with the claw forward
			case AUTO_MIRROR_SPLINE:
				double[] m_mirror = new double[2];
				m_mirror = pInUse.getOutput(leftEncoder.getDistance(), rightEncoder.getDistance(), -getAngle()*3.14/180); 

				leftDriveMaster.set(ControlMode.PercentOutput, -m_mirror[0]);
				rightDriveMaster.set(ControlMode.PercentOutput, m_mirror[1]);
				break;
				
			//mirrored spline driving for the other side of the field
			//with the claw backward
			case AUTO_MIRROR_BACKWARDS_SPLINE: 
				double[] m_mirror_back = new double[2];
				m_mirror_back = pInUse.getOutput(-rightEncoder.getDistance(), -leftEncoder.getDistance(), -getAngle()*3.14/180); 
				
				leftDriveMaster.set(ControlMode.PercentOutput, m_mirror_back[1]);
				rightDriveMaster.set(ControlMode.PercentOutput, -m_mirror_back[0]);
				break;
			
			//teleop driving with joystick control
			case TELEOP: 
				leftDriveMaster.set(ControlMode.PercentOutput, leftspeed);
				rightDriveMaster.set(ControlMode.PercentOutput, -rightspeed);
				break;
				
			
		}
	}

}