package org.usfirst.frc.team5895.robot.lib.pathfinder;

import java.io.File;
import edu.wpi.first.wpilibj.DriverStation;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

/**
 * FitMethod: HERMITE.CUBIC or HERMITE.QUINTIC
 * SAMPLE: HIGH (100 000 points), LOW (10 000 points), FAST (1 000)
 *
 */

public class PathfinderGenerator {
	private boolean saveCSV;
	private final String CSVLocation = "/home/lvuser/PathfinderFiles/"; 
	
	public PathfinderGenerator(boolean saveCSV){
		this.saveCSV = saveCSV;
	}
	
	public PathfinderGenerator() {
		this(false);
	}
	
	public void makeCSV(Trajectory trajectory, String name) {
		if(saveCSV) {
			File myFile = new File(CSVLocation + name + ".csv");
			Pathfinder.writeToCSV(myFile, trajectory);
		}
	}
	
	public double d2r( double degree) {
		return Pathfinder.d2r(degree);
	}
	
	public Trajectory Straight() {
		double dt = 0.01; // in second
		double max_vel = 5; // in f/s
		double max_acc = 10; // in f/s/s
		double max_jer = 60.0; // in f/s/s/s
		/**
		 * x, y in feet
		 * angle in radian if not using d2r
		 * d2r will change angle in degree to radian
		 * positive number means (forward, left, left) 
		 */
		Waypoint[] points = new Waypoint[] { 
    		    new Waypoint(0, 0, d2r(0)),     
    		    new Waypoint(10, 0, d2r(0)),                        
		};

		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
		Trajectory trajectory = Pathfinder.generate(points, config);
    	
		//create CSV file
		String name = Thread.currentThread().getStackTrace()[1].getMethodName(); // get current method name
    	makeCSV(trajectory, name);
    	
    	return trajectory;		
	}
	
	public Trajectory RightLeftScale() {
		double dt = 0.01; // in second
		double max_vel = 8; // in f/s
		double max_acc = 10; // in f/s/s
		double max_jer = 60.0; // in f/s/s/s
		/**
		 * x, y in feet
		 * angle in radian if not using d2r
		 * d2r will change angle in degree to radian
		 * positive number means (forward, left, left) 
		 */
		Waypoint[] points = new Waypoint[] { 
    		    new Waypoint(0, 0, d2r(0)),     
    		    new Waypoint(10, 0, d2r(0)), 
    		    new Waypoint(16, 10, d2r(89.0)),     
    		    new Waypoint(16.0, 14, d2r(89.0)),
    		    new Waypoint(18.0, 16.0, d2r(0)),     
    		    new Waypoint(21.5, 15.5, d2r(-30.0)),
		};

		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
		Trajectory trajectory = Pathfinder.generate(points, config);
    	
		//create CSV file
		String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
    	
    	return trajectory;		
	}
	
	public Trajectory RightRightScale() {
		double dt = 0.01; // in second
		double max_vel = 6; // in f/s
		double max_acc = 11; // in f/s/s
		double max_jer = 60.0; // in f/s/s
		/**
		 * x, y in feet
		 * angle in radian if not using d2r
		 * d2r will change angle in degree to radian
		 * positive number means (forward, left, left) 
		 */
		Waypoint[] points = new Waypoint[] { 
    		    new Waypoint(0, 0, d2r(0)),     
    		    new Waypoint(16, 0, d2r(0)), 
    		    new Waypoint(20, 1, d2r(30.0)),     
		};

		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
		Trajectory trajectory = Pathfinder.generate(points, config);
    	
		//create CSV file
		String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
    	
    	return trajectory;		
	}
	
	public Trajectory CenterRightSwitchFront() {
		double dt = 0.01; // in second
		double max_vel = 6; // in f/s
		double max_acc = 11; // in f/s/s
		double max_jer = 60.0; // in f/s/s
		/**
		 * x, y in feet
		 * angle in radian if not using d2r
		 * d2r will change angle in degree to radian
		 * positive number means (forward, left, left) 
		 */
		Waypoint[] points = new Waypoint[] { 
    		    new Waypoint(0, 0, d2r(0)),     
    		    new Waypoint(9, -4, d2r(0)),   
		};

		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
		Trajectory trajectory = Pathfinder.generate(points, config);
    	
		//create CSV file
		String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
    	
    	return trajectory;		
	}
    
    public Trajectory CenterRightScale() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(0)),
                new Waypoint(10.2167, 11.0625, d2r(0)),
                new Waypoint(17, 11.0625, d2r(0)),
                new Waypoint(20.5, 10.0625, d2r(-15)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
        
        return trajectory;      
    }
    
    public Trajectory RightRightSwitchFront() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(0)),
                new Waypoint(8, -4.25, d2r(0)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
        
        return trajectory;      
    }
    
    public Trajectory RightLeftSwitchBack() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(0)),
                new Waypoint(8, 0, d2r(0)),
                new Waypoint(16, -12, d2r(-89.99)),
                new Waypoint(15.5, -14, d2r(-135)),
                new Waypoint(14, -15, d2r(-170)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
        
        return trajectory;      
    }
    
    public Trajectory RightLeftSwitchFront() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(0)),
                new Waypoint(4.66666667, -10.35166665, d2r(89.9)),
                new Waypoint(9.33333333, -16.7033333, d2r(0)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
        
        return trajectory;      
    }
    
    public Trajectory RightRightSwitchSide() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(0)),
                new Waypoint(8, 0, d2r(0)),
                new Waypoint(12.9167, -1.6967, d2r(-89.99)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
        
        return trajectory;      
    }
    
    public Trajectory RightSwitchBlock() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(13.9167, -2.2967, d2r(89.99)),
                new Waypoint(16, 0, d2r(0)),
                new Waypoint(24.8333, 1, d2r(0)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
        
        return trajectory;      
    }
    
    public Trajectory RightRightScale2() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(0)),
                new Waypoint(16, 0, d2r(0)),
                new Waypoint(21.25, -1, d2r(-30)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
        
        return trajectory;      
    }
   
    public Trajectory RightScaleLeftSwitch() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(20, -2, d2r(-120)),
                new Waypoint(19, -5, d2r(-89.99)),
                new Waypoint(19, -8, d2r(-89.99)),
                new Waypoint(17, -17.5, d2r(-135)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
    
    public Trajectory RightLeftScale2() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(0)),
                new Waypoint(10, 0, d2r(0)),
                new Waypoint(15.75, -10, d2r(-89.99)),
                new Waypoint(15.75, -14, d2r(-89.99)),
                new Waypoint(18.25, -16, d2r(0)),
                new Waypoint(20.5, -15.5, d2r(30)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
    
    public Trajectory RightLeftDrive() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(0)),
                new Waypoint(10, 0, d2r(0)),
                new Waypoint(15.75, -10, d2r(-89.99)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
    
    public Trajectory LeftScaleRightSwitch() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(22, -17, d2r(-179.99)),
                new Waypoint(21.5, -16.5, d2r(-225)),
                new Waypoint(21, -15, d2r(-270)),
                new Waypoint(21.25, -8, d2r(-270)),
                new Waypoint(20.5, -4.25, d2r(-225)),
                new Waypoint(20, -4, d2r(-180)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
    
    public Trajectory RightScaleCube() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(-160)),
                new Waypoint(-6 * Math.cos(Math.toRadians(20)), -6 * Math.sin(Math.toRadians(20)), d2r(-160)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
    
    public Trajectory RightScaleCubeBack() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(-160)),
                new Waypoint(-3 * Math.cos(Math.toRadians(20)), -3 * Math.sin(Math.toRadians(20)), d2r(-160)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
    
    public Trajectory SCurve() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(0)),
                new Waypoint(10, 3, d2r(0)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
    
    public Trajectory CenterRightSwitchCube() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(-60)),
                new Waypoint(2.0, -2.0 * Math.sqrt(3.0), d2r(-60)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
    
    public Trajectory CenterRightSwitchRev() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(-60)),
                new Waypoint(1.5, -1.5 * Math.sqrt(3.0), d2r(-60)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
    
    public Trajectory RightScaleBackwards() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(-30)),
                new Waypoint(-1.0*Math.sqrt(3), 1.0, d2r(-30)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
    
    public Trajectory SecondScaleCube() {
        double dt = 0.01; // in second
        double max_vel = 6; // in f/s
        double max_acc = 11; // in f/s/s
        double max_jer = 60.0; // in f/s/s
        /**
         * x, y in feet
         * angle in radian if not using d2r
         * d2r will change angle in degree to radian
         * positive number means (forward, left, left) 
         */
        Waypoint[] points = new Waypoint[] { 
                new Waypoint(0, 0, d2r(-60)),
                new Waypoint(6.5, 1, d2r(-60)),
        };
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, dt, max_vel, max_acc, max_jer);
        Trajectory trajectory = Pathfinder.generate(points, config);
        
        //create CSV file
        String name = Thread.currentThread().getStackTrace()[1].getMethodName(); //get current method name
    	makeCSV(trajectory, name);
         
        return trajectory;      
    }
	
}
