package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.Spark;

public class Blinkin {
	
	Spark blinkin;

	public Blinkin() {
		blinkin = new Spark(2);
	}
	
	/**
	 * if the robot is in hatMode, the LED turns solid, dark green (0.75)
	 */
	public void lightsHatmode() {
		blinkin.set(0.75);
	}
	
	/**
	 * if the robot is not in hatMode and in normal drive, the LED turns solid white (0.93)
	 */
	public void lightsNormalDrive() {
		blinkin.set(0.93);
	}
	
	/**
	 * if the robot detects the cube, the LED blinks gold (-0.07)
	 */
	public void lightsHasCube() {
		blinkin.set(-0.07); 
	}
}


