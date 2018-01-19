package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.framework.Looper;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;


public class Robot extends IterativeRobot {
	
	Looper loop;
	Elevator elevator;
	Joystick joystick;
	
	public void robotInit() {
		
		elevator = new Elevator();
		joystick = new Joystick(0);
		
		loop = new Looper(10);
		loop.add(elevator::update);
		loop.start();
	}

	public void autonomousInit() {
		
	}


	public void teleopPeriodic() {
		
		//elevator motion magic
		double leftYstick = -1.0 * joystick.getRawAxis(1);
		
		if(joystick.getRawButton(1)) {
			elevator.setTargetPosition(0);
			}
			else if(joystick.getRawButton(2)) {
				elevator.setTargetPosition(30);
			}
			else if(joystick.getRawButton(3)) {
				elevator.setTargetPosition(45);
			}
			else if(joystick.getRawButton(4)) {
				elevator.setTargetPosition(60);
			}
			else {
				/* Percent voltage mode */
				elevator.driverControl(leftYstick);
			}
			

	}

}
