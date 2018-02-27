package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.auto.*;
import org.usfirst.frc.team5895.robot.framework.Looper;
import org.usfirst.frc.team5895.robot.framework.Recorder;
import org.usfirst.frc.team5895.robot.lib.BetterJoystick;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends IterativeRobot {

	Looper loop;
	Elevator elevator;
	CubeIntake intake;
	DriveTrain drive;
	Blinkin blinkin;
	Limelight lime;
	GameData gameData;
	PowerDistributionPanel pdp;

	boolean clawUp;

	Recorder r;
	
	BetterJoystick ljoystick;
	BetterJoystick rjoystick;

	public void robotInit() {
		intake = new CubeIntake();
		elevator = new Elevator();
		ljoystick = new BetterJoystick(0);
		rjoystick = new BetterJoystick(1);
		drive = new DriveTrain();
		blinkin = new Blinkin();
		gameData = new GameData();
		lime = new Limelight();
		pdp = new PowerDistributionPanel();

		loop = new Looper(10);
		loop.add(elevator::update);
		loop.add(intake::update);
		loop.add(drive::update);
		loop.start();

		//set up recorder
		r = new Recorder(100);
		r.add("Time", Timer::getFPGATimestamp);
		r.add("Drive Distance", drive::getDistanceTraveled);
		r.add("Drive Velocity", drive::getVelocity);
		r.add("Elevator Height", elevator::getHeight);
		r.add("Elevator State", elevator::getState);
		r.add("Intake LeftClawSensor", intake::getLeftVoltage);
		r.add("Intake RightClawSensor", intake::getRightVoltage);
		r.add("Intake State", intake::getState);
		for (int i = 0; i < 16; i++) {
			final int x = i;
			r.add("Current " + i, () -> pdp.getCurrent(x));
		}

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

		if(intake.hasCube()) {
			blinkin.lightsHasCube();
		}
		else {
			blinkin.lightsNormal();
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
