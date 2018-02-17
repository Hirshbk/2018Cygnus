package org.usfirst.frc.team5895.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class ThreeMotors {
	
	double speed;
	VictorSPX MotorA;
	VictorSPX MotorB;
	TalonSRX MotorC;
	
	public ThreeMotors(double spd, int A, int B, int C) {
		
		MotorA = new VictorSPX(A);
		MotorB = new VictorSPX(B);
		MotorC = new TalonSRX(C);
		
		speed = spd;
		
	}
	
	public void go() {
		
		MotorA.set(ControlMode.PercentOutput, speed);
		MotorB.set(ControlMode.PercentOutput, speed);
		MotorC.set(ControlMode.PercentOutput, speed);
		
	}
	
	public void stop() {
		
		MotorA.set(ControlMode.PercentOutput, 0);
		MotorB.set(ControlMode.PercentOutput, 0);
		MotorC.set(ControlMode.PercentOutput, 0);
		
	}

}
