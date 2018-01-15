package trajectory;

import util.ChezyMath;

/**
 * A WaypointSequence is a sequence of Waypoints.  #whatdidyouexpect
 *
 * @author Art Kalb
 * @author Stephen Pinkerton
 * @author Jared341
 */
public class WaypointSequence {

  public static class Waypoint {

	    private double x;
	    private double y;
	    private double theta;
	  
	  /**
	   * 
	   * @param horizontal left/right in feet, 
	   * @param vertical forward/backward in feet
	   * @param angle in radians counterclockwise to horizontal
	   */
    public Waypoint(double horizontal, double vertical, double angle) {
    	
    	//OKAY SO it's switched where y is horizontal and x is vertical because 254 is weird and they had it
    	//turned 90 degrees but I didn't like that so I changed it (extremely inelegantly but listen it works)
    	//also angles are in degrees now because radians are hard for freshmen
    	
    	y = horizontal;
    	x = vertical;
    	theta = -angle * Math.PI / 180;
    }
    
    public Waypoint(Waypoint tocopy) {
      this.x = tocopy.x;
      this.y = tocopy.y;
      this.theta = tocopy.theta;
    }

    public double getX(){
    	return x;
    }
    
    public double getY(){
    	return y;
    }
    
    public double getTheta() {
    	return theta;
    }
  }

  Waypoint[] waypoints_;
  int num_waypoints_;

  public WaypointSequence(int max_size) {
    waypoints_ = new Waypoint[max_size];
  }

  public void addWaypoint(Waypoint w) {
    if (num_waypoints_ < waypoints_.length) {
      waypoints_[num_waypoints_] = w;
      ++num_waypoints_;
    }
  }

  public int getNumWaypoints() {
    return num_waypoints_;
  }

  public Waypoint getWaypoint(int index) {
    if (index >= 0 && index < getNumWaypoints()) {
      return waypoints_[index];
    } else {
      return null;
    }
  }
  
  public WaypointSequence invertY() {
    WaypointSequence inverted = new WaypointSequence(waypoints_.length);
    inverted.num_waypoints_ = num_waypoints_;
    for (int i = 0; i < num_waypoints_; ++i) {
      inverted.waypoints_[i] = waypoints_[i];
      inverted.waypoints_[i].y *= -1;
      inverted.waypoints_[i].theta = ChezyMath.boundAngle0to2PiRadians(
              2*Math.PI - inverted.waypoints_[i].theta);
    }
    
    return inverted;
  }
}
