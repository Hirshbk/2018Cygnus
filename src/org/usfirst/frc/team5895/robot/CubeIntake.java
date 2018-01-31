package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.lib.DistanceSensor;
import org.usfirst.frc.team5895.robot.src.org.usfirst.frc.team5895.robot.CubeIntake.Mode_Type;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class CubeIntake {
	private static enum Mode_Type {INTAKING, EJECTING, HOLDING, WAITING, OFF};
	private Mode_Type mode = Mode_Type.EJECTING;
	private Talon motor1, motor2;
	private Solenoid solenoidClamp, solenoidClaw;
	private Timer timer;
	private DigitalInput sensor;
	double motorspeed1, motorspeed2, lastTime;
	private boolean solenoidState;
	boolean lastHasCube;
	private boolean isDown;
	
	
	public CubeIntake (){
		motor1 = new Talon(ElectricalLayout.MOTOR_DRIVE_LEFT);
		motor2 = new Talon(ElectricalLayout.MOTOR_DRIVE_RIGHT);
		solenoidClamp = new Solenoid (ElectricalLayout.SOLENOID_INTAKE_CLAMP);
		solenoidClaw = new Solenoid(ElectricalLayout.SOLENOID_INTAKE_CLAW);
		sensor = new DigitalInput(ElectricalLayout.SENSOR_INTAKE);
		lastHasCube=false;
	    isDown = false;
	    
		}

	public void intake(){
		mode = Mode_Type.INTAKING;
		}
	
	public void eject(){
		mode= Mode_Type.EJECTING;
		}
	
	public void waiting(){
		mode = Mode_Type.WAITING; 
		}
	
	public void off(){
		mode = Mode_Type.OFF;
	}
	
	public void up() {
		isDown = false;	
	}
	
	public void down (){
		isDown = true;
	}
	
	public void update(){
		
		if (solenoidClaw.get() != isDown) {
			   solenoidClaw.set(isDown);
			}
		
		boolean hasCube = !sensor.get();
		
		motorspeed1 = 0;
		motorspeed2 = 0;
		
		switch(mode) {
		
		case INTAKING:
		     
		     motorspeed1=.5;
		     motorspeed2=.5;
		     
		     if((lastHasCube == false) && (hasCube)) {
				lastTime = timer.getFPGATimestamp(); //stamp the time we start waiting 
			 	mode = Mode_Type.WAITING; //once we have the cube, we prepare to hold and clamp
			 }
			solenoidState = false; //solenoid only clamps once it is holding 
			break;
			
		case WAITING:
		
			motorspeed1=.5;
			motorspeed2=.5;
			
        	double curTime = timer.getFPGATimestamp(); //stamps current time 
            if (curTime - lastTime > 0.2) { //compares the time we started waiting to current time
            	mode = Mode_Type.HOLDING; //if it has been waiting for 200ms, it begins to hold
            } else {
            	mode = Mode_Type.WAITING; //if not, it keeps holding
            }
            solenoidState=false; 
			break;
		
		case HOLDING:
			motorspeed1 = .2;
			motorspeed2 = .2;
			solenoidState = true; // solenoid used to clamp cube while holding 
			break;
		
		case EJECTING:
			motorspeed1 = -.5;
			motorspeed2 = -.5;
			solenoidState = false; 
			break; 
		
		case OFF:
			motorspeed1 = 0;
			motorspeed2 = 0;
			solenoidState = false;
	
		motor1.set(motorspeed1);
		motor2.set(motorspeed2);
		solenoidClamp.set(solenoidState);
		
		}
	}
}