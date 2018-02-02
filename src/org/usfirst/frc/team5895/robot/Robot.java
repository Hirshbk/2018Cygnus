package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.framework.Looper;
import org.usfirst.frc.team5895.robot.framework.Recorder;
import org.usfirst.frc.team5895.robot.lib.BetterJoystick;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends IterativeRobot {
	
	Looper loop;
	Elevator elevator;
	BetterJoystick ljoystick;
	BetterJoystick rjoystick;
	CubeIntake intake;
	HatHatHat hat;
	DriveTrain driveT;
	Blinkin blinkin;
	boolean hatMode;
	boolean clawUp;
	
	Recorder r;
	
	public void robotInit() {
		intake = new CubeIntake();
		elevator = new Elevator();
		ljoystick = new BetterJoystick(0);
		rjoystick = new BetterJoystick(1);
		hat = new HatHatHat();
		driveT = new DriveTrain();
		blinkin = new Blinkin();
		
		loop = new Looper(10);
		loop.add(elevator::update);
		loop.add(intake::update);
		loop.add(driveT::update);
		loop.start();
		
		r = new Recorder(100);
		r.add("Time", Timer::getFPGATimestamp);
		r.add("time", Timer::getFPGATimestamp);
		r.add("x", driveT::getXPosition);
		r.add("y", driveT::getYPosition);
		r.add("velocity", driveT::getVelocity);
		r.add("distance", driveT::getDistanceTraveled);
		
	}

	public void autonomousInit() {
		
		r.startRecording();
		
	}

	public void teleopPeriodic() {
		
		//elevator motion magic
		double leftYstick = -1.0 * rjoystick.getRawAxis(1);
		
		if(rjoystick.getRisingEdge(2)) {
				elevator.setTargetPosition(66);
			}
			else if(rjoystick.getRisingEdge(3)) {
				elevator.setTargetPosition(54);
			}
			else if(rjoystick.getRisingEdge(4)) {
				elevator.setTargetPosition(78);
			}
			else if(ljoystick.getRisingEdge(2)) {
				elevator.setTargetPosition(0);
			}
			else if(ljoystick.getRisingEdge(4)) {
				elevator.setTargetPosition(20);
			
		
			if (ljoystick.getRisingEdge(3)){
				hatMode = ! hatMode; 
			}
			if (hatMode) {
				blinkin.lightsHatmode();
				
				if(ljoystick.getRisingEdge(1)){
					hat.leftExtend(); 
					intake.off();
				}
				if(rjoystick.getRisingEdge(1)) {
					hat.rightExtend();
					intake.off();
				}
			}
			else {
				if(intake.lastHasCube) {
					blinkin.lightsHasCube();
				}
				else {
					blinkin.lightsNormalDrive();
				}
				
				if(ljoystick.getRisingEdge(1)) {
					clawUp = !clawUp;
					if(clawUp){
						intake.up();
					}else{
						intake.down();
					}	
				}
				if(rjoystick.getRisingEdge(1)) {
					intake.eject(); 
				}
			}
		}
	}
	
	public void disabledInit() {
		r.stopRecording();
	}
	
}
