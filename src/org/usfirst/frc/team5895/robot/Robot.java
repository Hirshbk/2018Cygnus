package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.auto.*;
import org.usfirst.frc.team5895.robot.framework.Looper;
import org.usfirst.frc.team5895.robot.framework.Recorder;
import org.usfirst.frc.team5895.robot.lib.BetterJoystick;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends IterativeRobot {

	Looper loop;
	Elevator2 elevator;
	BetterJoystick ljoystick;
	BetterJoystick rjoystick;
	CubeIntake intake;

	DriveTrain drive;
	Blinkin blinkin;

	boolean clawUp;
	GameData gameData;
	Limelight lime;

	Recorder r;

	public void robotInit() {
		intake = new CubeIntake();
		elevator = new Elevator2();
		ljoystick = new BetterJoystick(0);
		rjoystick = new BetterJoystick(1);
		drive = new DriveTrain();
		blinkin = new Blinkin();
		gameData = new GameData();
		lime = new Limelight();

		loop = new Looper(10);
		loop.add(elevator::update);
		loop.add(intake::update);
		loop.add(drive::update);
		loop.start();

		r = new Recorder(100);
		r.add("Time", Timer::getFPGATimestamp);
		r.add("time", Timer::getFPGATimestamp);
		r.add("x", drive::getXPosition);
		r.add("y", drive::getYPosition);
		r.add("velocity", drive::getVelocity);
		r.add("distance", drive::getDistanceTraveled);

	}

	public void autonomousInit() {

		r.startRecording();
		
		gameData.getGameData();
		if (gameData.RRR()) {
			LLL.run(drive, elevator, lime, intake, blinkin);
		}
		else if (gameData.RRL()) {
			RRL.run(drive, elevator, lime, intake, blinkin);
		}
		else if (gameData.RLR()) {
			RLR.run(drive, elevator, lime, intake, blinkin);
		}
		else if (gameData.RLL()) {
			RLL.run(drive, elevator, lime, intake, blinkin);
		}
		else if (gameData.LLL()) {
			LLL.run(drive, elevator, lime, intake, blinkin);
		}
		else if (gameData.LLR()) {
			LLR.run(drive, elevator, lime, intake, blinkin);
		}
		else if (gameData.LRL()) {
			LRL.run(drive, elevator, lime, intake, blinkin);
		}
		else if (gameData.LRR()) {
			LRR.run(drive, elevator, lime, intake, blinkin);
		}
		else
			DriverStation.reportError("Auto Error", false);
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
		}

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



	public void disabledInit() {
		r.stopRecording();
	}

}
