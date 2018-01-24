package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.lib.DistanceSensor;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class CubeIntake {
	private static enum Mode_Type {INTAKING, EJECTING, HOLDING};
	private Mode_Type mode = Mode_Type.EJECTING;
	private Talon motor1, motor2;
	private Solenoid solenoid;
	private boolean solenoidState;
	private DistanceSensor sensor1, sensor2;
	double motorspeed1, motorspeed2, volts1, volts2, x1, x2, avgDist, speed;
	
	public CubeIntake (){
		motor1 = new Talon(3);
		motor2 = new Talon(4);
		solenoid = new Solenoid (0);
		sensor1 = new DistanceSensor(); 
		sensor2 = new DistanceSensor(); 
		}

	public void intake(){
		mode = Mode_Type.INTAKING;
		}
	
	public void eject(){
		mode= Mode_Type.EJECTING;
		}
	
	public void update(){
		if(mode != Mode_Type.EJECTING) {
			if (sensor1.getDistance() < 5 ||sensor2.getDistance() < 5) {
				mode = Mode_Type.HOLDING; // test to see if this should be "and" statement or "or"
			}else {
				mode = Mode_Type.INTAKING;	
			}
		}
		
		motorspeed1 = 0;
		motorspeed2 = 0;
		
		switch(mode) {
		
		case INTAKING:
			 x1 = sensor1.getDistance();
		     x2 = sensor2.getDistance();
		     
			 if (x1 < 20 || x2 < 20) {
		            
		            avgDist = (x1+x2)/2;
		            speed = 16.316*Math.pow(avgDist+20, -0.844367);
		            
		            motorspeed1 =  speed/4 + x1/45;
		            motorspeed2 = speed/4 + x2/45;
		            
		        } else {
		            motorspeed1 = 1.0;
		            motorspeed2 = 1.0;        
		        }
		        
			solenoidState = false;
			break;
		
		case HOLDING:
			motorspeed1 = 0;
			motorspeed2 = 0;
			solenoidState = true; // solenoid used to clamp cube while holding 
			break;
		
		case EJECTING:
			motorspeed1 = -1;
			motorspeed2 = -1;
			solenoidState = false; 
			break; 
			}
	
		motor1.set(motorspeed1);
		motor2.set(motorspeed2);
		solenoid.set(solenoidState);
		
	}
}
	

