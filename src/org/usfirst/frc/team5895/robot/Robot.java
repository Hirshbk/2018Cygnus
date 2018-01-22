package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.framework.Looper;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;


public class Robot extends IterativeRobot {
	
	Looper loop;
	Elevator elevator;
	Joystick ljoystick;
	Joystick rjoystick;
	CubeIntake intake;
	
	public void robotInit() {
		intake = new CubeIntake();
		elevator = new Elevator();
		ljoystick = new Joystick(0);
		rjoystick = new Joystick(1);
		
		loop = new Looper(10);
		loop.add(elevator::update);
		loop.add(intake::update);
		loop.start();
	}

	public void autonomousInit() {
		
	}


	public void teleopPeriodic() {
		
		//elevator motion magic
		double leftYstick = -1.0 * joystick.getRawAxis(1);
		
		if(rjoystick.getRawButton(1)) {
			elevator.setTargetPosition(0);
			}
			else if(rjoystick.getRawButton(2)) {
				elevator.setTargetPosition(30);
			}
			else if(rjoystick.getRawButton(3)) {
				elevator.setTargetPosition(45);
			}
			else if(rjoystick.getRawButton(4)) {
				elevator.setTargetPosition(60);
			}
			else {
				/* Percent voltage mode */
				elevator.driverControl(leftYstick);
			}
			
		if(ljoystick.getRawButton(1)) {
			intake.eject();
			}
			else if(ljoystick.getRawButton(2)) {
			intake.intake();
			}

	}

}
