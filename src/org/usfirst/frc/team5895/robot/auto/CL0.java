package org.usfirst.frc.team5895.robot.auto;

import org.usfirst.frc.team5895.robot.Blinkin;
import org.usfirst.frc.team5895.robot.CubeIntake;
import org.usfirst.frc.team5895.robot.DriveTrain;
import org.usfirst.frc.team5895.robot.Elevator;
import org.usfirst.frc.team5895.robot.Limelight;
import org.usfirst.frc.team5895.robot.framework.Waiter;

/**
 * center of field, left switch, & left scale.
 * @author lalewis-19
 */
public class CL0 {
	
	public static final void run(DriveTrain drive, Elevator elevator, Limelight lime, CubeIntake intake,
			Blinkin blinkin) {
		
		drive.resetNavX();
		intake.intake();
		Waiter.waitFor(200);
		elevator.setTargetPosition(40.0/12);
		intake.down();
		drive.autoCenterLeftSwitchFront();
		Waiter.waitFor(drive::isPFinished,4000);
		drive.arcadeDrive(0, 0);
		intake.ejectSlow();
		drive.arcadeDrive(0.3, 0);
		Waiter.waitFor(700);
		drive.arcadeDrive(0, 0);
		drive.turnTo(75);
		Waiter.waitFor(drive::atAngle, 2000);
		drive.arcadeDrive(0, 0);
		elevator.setTargetPosition(15.0/12);
		intake.intake();
		Waiter.waitFor(1000);
		drive.autoCenterLeftSwitchCube();
		Waiter.waitFor(drive::isPFinished, 2000);
		drive.arcadeDrive(0, 0);
		drive.autoCenterLeftSwitchRev();
		Waiter.waitFor(1000);
		elevator.setTargetPosition(40.0/12);
		Waiter.waitFor(drive::isPFinished, 2000);
		drive.arcadeDrive(0, 0);
		drive.turnTo(-75);
		Waiter.waitFor(drive::atAngle, 2000);
		drive.arcadeDrive(0, 0);
		drive.arcadeDrive(-0.3, 0);
		Waiter.waitFor(750);
		drive.arcadeDrive(0, 0);
		intake.ejectSlow();
	}

}
