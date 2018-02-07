package org.usfirst.frc.team5895.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
	
	// make sure to use the new library that doesn't have "wpilibj" in the path
	private NetworkTable table;
	
	// input
	private boolean hasTarget;
	private double horizontalOffset; // -27 to 27 degrees
	private double verticalOffset; // -20.5 to 20.5 degrees
	private double area; // % of image
	private double rotation; // -90 to 0 degrees
	private double latency; // ms (Add at least 11ms for image capture latency)
	
	// output
	private LedMode led = LedMode.ON;
	private CamMode cam = CamMode.VISION_PROCESSING; // operation mode
	private double pipeline = 0; // current pipeline
	
	/**
	 * Sets enum types of LED mode: ON, OFF, BLINKING
	 * 
	 */
	public enum LedMode {
		ON(0),OFF(1),BLINKING(2);
		
		private double value;
		
		LedMode(double value){
			this.value = value;
		}
		
		public double getValue() {
			return value;
		}
	}
	
	/**
	 * Sets enum types of Camera mode: VISION_PROCESSING (Vision mode), DRIVER_CAMERA (Raw Image)
	 *
	 */
	public enum CamMode {
		VISION_PROCESSING(0),DRIVER_CAMERA(1);
		
		private double value;
		
		CamMode(double value){
			this.value = value;
		}
		
		public double getValue() {
			return value;
		}
	}
	/**
	 * Start NetworkTable
	 * Initialize NetworkTable of Limelight
	 * 
	 */
	public Limelight() {
		NetworkTableInstance.getDefault().startClient(); //
        table = NetworkTableInstance.getDefault().getTable("limelight");
	}
	
	/**
	 * Update all methods in need of routine refreshing
	 * 
	 */
	public void update() {
		updateHasTarget();
		updateHorizontalOffset();
		updateVerticalOffset();
		updateTargetArea();
		updateRotation();
		updateLatency();
		updateLedMode();
		updateCamMode();
	}
	
	/**
	 * Update boolean hasTarget
	 */
	public void updateHasTarget() {
		double val = table.getEntry("tv").getDouble(-1);
		if (val == 0d) {
			hasTarget = false;
		} else if (val == 1d) {
			hasTarget = true;
		} 
	}
	
	public void updateHorizontalOffset() {
		horizontalOffset = table.getEntry("tx").getDouble(-1);
	}
	
	public void updateVerticalOffset() {
		verticalOffset = table.getEntry("ty").getDouble(-1);
	}
	
	
	public void updateTargetArea() {
		area = table.getEntry("ta").getDouble(-1);
	}
	
	public void updateRotation() {
		rotation = table.getEntry("ts").getDouble(-1);
	}

	public void updateLatency() {
		latency = table.getEntry("tl").getDouble(-1);
	}

	public void updateLedMode() {
		table.getEntry("ledMode").setDouble(led.getValue());
	}
	
	public void updateCamMode() {
		table.getEntry("camMode").setDouble(cam.getValue());
	}
	
	public void updatePipeline() {
		table.getEntry("pipeline").setDouble(pipeline);
	}
	
	public void setLedMode(LedMode led) {
		this.led = led;
	}
	
	public void setCamMode(CamMode cam) {
		this.cam = cam;
	}
	
	public void setPipeline(double pipeline) {
		this.pipeline = Math.max(Math.min(pipeline, 9), 0);
	}
	
	public boolean hasTarget() {
		return hasTarget;
	}
	
	public double getHorizontalOffset() {
		return horizontalOffset;
	}
	
	public double getVerticalOffset() {
		return verticalOffset;
	}
	
	public double getArea() {
		return area;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public double getLatency() {
		return latency;
	}
	
	public LedMode getLED() {
		return led;
	}
	
	public CamMode getCAM() {
		return cam;
	}
	
	public double getPipeline() {
		return pipeline;
	}
	
}