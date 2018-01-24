package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.lib.DistanceSensor;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class CubeIntake {
	private static enum Mode_Type {INTAKING, EJECTING, HOLDING, WAITING};
	private Mode_Type mode = Mode_Type.EJECTING;
	private Talon motor1, motor2;
	private Solenoid solenoid;
	private boolean solenoidState;
	private DistanceSensor sensor1, sensor2;
	private Timer timer;
	double motorspeed1, motorspeed2, volts1, volts2, x1, x2, avgDist, speed, lastTime;
	boolean haveCube;
	
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
	
	public void waiting(){
		double lastTime = timer.getFPGATimestamp();
		mode = Mode_Type.WAITING; 
	}
	
	public void update(){
		
		motorspeed1 = 0;
		motorspeed2 = 0;
		
		switch(mode) {
		
		case INTAKING:
			 x1 = sensor1.getDistance();
		     x2 = sensor2.getDistance();
		     
		     if(x1 < 5 || x2 < 5) {
		    	 haveCube = true; //once the cube is close enough, it is stable enough to hold
		     }
		     else if ( (x1 < 20 && x1>= 5)|| (x2 < 20 && x2>=5)) {
		            
		            avgDist = (x1+x2)/2;
		            speed = 16.316*Math.pow(avgDist+20, -0.844367);
		         // output speed generated w/ points and regression(using calculator)
		            
		            motorspeed1 =  speed/4 + x1/45;
		            motorspeed2 = speed/4 + x2/45;    
		          //if one side of the box is closer, rollers on that side of the claw go slower to even them out
		      }
		     else {
		            motorspeed1 = 1.0;
		            motorspeed2 = 1.0;        
		      }
			
			 if (haveCube){
				lastTime = timer.getFPGATimestamp(); //stamp the time we start waiting 
			 	mode = Mode_Type.WAITING; //once we have the cube, we prepare to hold and clamp
			 }
			
			solenoidState = false;
			break;
			
		case WAITING:
		
			x1 = sensor1.getDistance();
		    x2 = sensor2.getDistance();
		    
			avgDist = (x1+x2)/2;
            speed = 16.316*Math.pow(avgDist+20, -0.844367); 
            
            motorspeed1 =  speed/4 + x1/45; 
            motorspeed2 = speed/4 + x2/45;
            
        	double curTime = timer.getFPGATimestamp(); //stamps current time 
            if (curTime - lastTime > 0.2) { //compares the time we started waiting to current time
            	if (haveCube) {
            		mode = Mode_Type.HOLDING; //if it has been waiting for 200ms, it begins to hold
            	}else {
            		mode = Mode_Type.WAITING; //if not, it keeps holding
            	}
            }
            
            solenoidState=false;
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
	

