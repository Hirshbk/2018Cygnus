package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.framework.Looper;
import org.usfirst.frc.team5895.robot.lib.BetterJoystick;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;


public class Robot extends IterativeRobot {
	
	Looper loop;
	Elevator elevator;
	BetterJoystick ljoystick;
	BetterJoystick rjoystick;
	CubeIntake intake;
	HatHatHat hat;
	Claw claw;
	boolean hatMode;
	boolean clawUp;
	
	public void robotInit() {
		intake = new CubeIntake();
		elevator = new Elevator();
		ljoystick = new BetterJoystick(0);
		rjoystick = new BetterJoystick(1);
		hat = new HatHatHat();
		claw = new Claw();
		
		loop = new Looper(10);
		loop.add(elevator::update);
		loop.add(intake::update);
		loop.start();
	}

	public void autonomousInit() {
		
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
			
			/*else {
				//Percent voltage mode
				elevator.driverControl(leftYstick);
			}*/
		
			if (ljoystick.getRisingEdge(3)){
				hatMode = ! hatMode; 
			}
			if (hatMode) {
				if(ljoystick.getRisingEdge(1)){
					hat.leftExtend(); 
				}
				if(rjoystick.getRisingEdge(1)) {
					hat.rightExtend();
				}
			}
			else {
				if(ljoystick.getRisingEdge(1)) {
					clawUp = !clawUp;
					if(clawUp){
						claw.up();
					}else{
						claw.down();
					}
					
				}
				if(rjoystick.getRisingEdge(1)) {
					intake.eject(); 
				}
			}

	}

}
