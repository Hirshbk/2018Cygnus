package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.lib.TrajectoryDriveController;
import org.usfirst.frc.team5895.robot.lib.NavX;
import org.usfirst.frc.team5895.robot.lib.BetterEncoder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

//all parameters are tested using Basan 

public class DriveTrain {

	private Talon rightMotor,leftMotor;
	private BetterEncoder leftEncoder,rightEncoder;
	private double leftspeed, rightspeed;
	private NavX navX;
	private TrajectoryDriveController p_switch_near,p_switch_far,p_scale_near,p_scale_far,p_straight;
	private TrajectoryDriveController p_in_use;
	private enum Mode_Type {TELEOP,AUTO_SPLINE};
	private Mode_Type mode = Mode_Type.TELEOP;
	
	public DriveTrain() {
		leftMotor = new Talon(0);
		rightMotor = new Talon(1);
		
		leftEncoder = new BetterEncoder(0,1, true, Encoder.EncodingType.k4X);
		rightEncoder = new BetterEncoder(2,3, false, Encoder.EncodingType.k4X);
		
		leftEncoder.setDistancePerPulse(4/12.0*Math.PI/360);
		rightEncoder.setDistancePerPulse(4/12.0*Math.PI/360);
		
		navX = new NavX();
		
		try { // check back everything. generate missing spline
		p_switch_near = new TrajectoryDriveController("/home/lvuser/Test/NearSwitch.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
		p_switch_far = new TrajectoryDriveController("/home/lvuser/Test/FarSwitch.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.006);
		p_scale_near = new TrajectoryDriveController("/home/lvuser/Test/NearScale.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
		p_scale_far = new TrajectoryDriveController("/home/lvuser/Test/FarScale.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.0065);
		p_straight = new TrajectoryDriveController("/home/lvuser/Test/Straight.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.01);
	
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
	
	public void auto_switch_near() {
		resetEncoderAndNavX();
		p_in_use = p_switch_near;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void auto_switch_far() {
		resetEncoderAndNavX();
		p_in_use = p_switch_far;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void auto_scale_near() {
		resetEncoderAndNavX();
		p_in_use = p_scale_near;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void auto_scale_far() {
		resetEncoderAndNavX();
		p_in_use = p_scale_far;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void auto_straight() {
		resetEncoderAndNavX();
		p_in_use = p_straight;
		mode = Mode_Type.AUTO_SPLINE;
	}
	
	public void arcadeDrive(double speed, double turn) {
		leftspeed = speed - turn;
		rightspeed = speed + turn;
		mode = Mode_Type.TELEOP;
	}
	
	public void update() {
		
		switch(mode) {
		
			case AUTO_SPLINE:
				double[] m = new double[2];
				m = p_in_use.getOutput(leftEncoder.getDistance(), rightEncoder.getDistance(), -getAngle()*3.14/180); // change navX angle to radian

				leftMotor.set(-m[0]);
				rightMotor.set(m[1]);
				break;
			
			case TELEOP: 
				leftMotor.set(leftspeed);
				rightMotor.set(-rightspeed);
				break;
				
			
		}
	}

}
