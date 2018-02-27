package org.usfirst.frc.team5895.robot.lib;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class NavX {

	private AHRS ahrs;
	private double lastRawAngle, lastRawPitch;
	private double offset;
	
	/**
	 * Constructs a new NavX object plugged into the serial MXP port
	 */
	public NavX() {
		ahrs = new AHRS(SPI.Port.kMXP);
		lastRawAngle = ahrs.getAngle();
		lastRawPitch = ahrs.getPitch();
		offset = 0;
		}	
	
	/**
	 * Resets the angle of the NavX
	 */
	public void reset() {
		lastRawAngle = 0;
		lastRawPitch = 0;
		offset = 0;
		ahrs.reset();
	}

	/**
	 * Returns the angle of the NavX.
	 * Turning past 360 degrees will go onto 361
	 * Turning before 0 degrees will go to -1
	 * 
	 * @return The angle, in degrees, of the NavX
	 */
	public double getAngle() {
		double rawAngle = ahrs.getAngle();
		
		if (rawAngle > 340 && lastRawAngle < 20) {
			offset -= 360.0;
		}
		else if (rawAngle < 20 && lastRawAngle > 340) {
			offset += 360;
		}
		
		lastRawAngle = rawAngle;
		
		return rawAngle + offset;
	}
	
	public double getPitch() {
		double rawPitch = ahrs.getPitch();

		if (rawPitch > 340 && lastRawPitch < 20) {
			offset -= 360.0;
		}
		else if (rawPitch < 20 && lastRawPitch > 340) {
			offset += 360;
		}
		
		lastRawPitch = rawPitch;
		
		return rawPitch + offset;
	}
}
