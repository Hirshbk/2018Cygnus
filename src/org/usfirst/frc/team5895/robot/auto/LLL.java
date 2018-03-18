package org.usfirst.frc.team5895.robot.auto;

import org.usfirst.frc.team5895.robot.Blinkin;
import org.usfirst.frc.team5895.robot.CubeIntake;
import org.usfirst.frc.team5895.robot.DriveTrain;
import org.usfirst.frc.team5895.robot.Elevator;
import org.usfirst.frc.team5895.robot.Limelight;
import org.usfirst.frc.team5895.robot.framework.Waiter;

/**
 * Left side of field, left switch, & left scale.
 * @author lalewis-19
 */
public class LLL {
	
	public static final void run(DriveTrain drive, Elevator elevator, Limelight lime, CubeIntake intake,
			Blinkin blinkin) {
		
		// switch > near
		// switch > near
		
		drive.resetNavX();
		intake.intake();
		Waiter.waitFor(200);
		drive.autoLeftLeftScale();
		Waiter.waitFor(1000);
		elevator.setTargetPosition(82/12);
		Waiter.waitFor(drive::isPFinished, 2500);
		intake.ejectSlow();
		Waiter.waitFor(500);
		elevator.setTargetPosition(0.0);
/*		drive.turnTo(-210);
		Waiter.waitFor(drive::atAngle, 3000);
		drive.stopTurning(); 
		drive.arcadeDrive(0, 0);
		drive.autoLeftScaleCube();
		intake.down();
		intake.intake();
		Waiter.waitFor(drive::isPFinished, 5000);
		elevator.setTargetPosition(40.0/12);
		Waiter.waitFor(2000);
		intake.ejectFast();
*/		
	}

}
