package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.framework.Looper;

import edu.wpi.first.wpilibj.IterativeRobot;


public class Robot extends IterativeRobot {
	
	Looper loop;
	Elevator elevator;
	
	public void robotInit() {
		
		elevator = new Elevator();
		
		loop = new Looper(10);
		loop.add(elevator::update);
		loop.start();
	}

	public void autonomousInit() {
		
	}


	public void teleopPeriodic() {

	}

}
