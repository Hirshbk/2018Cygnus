package trajectory;

import trajectory.io.TextFileSerializer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Jared341
 */
public class Main {
	
	/**
	 * Joins two build paths
	 * @param directoryPath1 the first path to be joined, usually directory
	 * @param directoryPath2 the second path to be joined, usually file name
	 * @return file2.getPath(), the end address, the file name inside the directory
	 */
  public static String joinPath(String directoryPath1, String directoryPath2)
  {
      File file1 = new File(directoryPath1);
      File file2 = new File(file1, directoryPath2);
      return file2.getPath();
  }
  
  /**
   * Tries to write a path to a directory
   * @param directoryPath the place in the directory to write to
   * @param data the serialized motor values to be given
   * @return true if it successfully writes, false otherwise
   */
  private static boolean writeFile(String directoryPath, String data) {
    try {
      File file = new File(directoryPath);

      // if file doesn't exists, then create it
      if (!file.exists()) {
          file.createNewFile();
      }

      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(data);
      bw.close();
    } catch (IOException e) {
      return false;
    }
    
    return true;
  }
  /**
   * Serializes the data then tries to write it
   * @param path the spline path to be written
   * @param path_name The name of the file in the directory
   * 
   * For written file: 
   * Each segment is in the format:
   *   pos vel acc jerk heading dt x y
   * 
   */
  public static void serializeAndWrite(Path path, String path_name) {
	  String directory = "./Splines";
	  
	  // Outputs to the directory supplied as the first argument.
      TextFileSerializer js = new TextFileSerializer();
      String serialized = js.serialize(path);
      //System.out.print(serialized);
      String fullpath = joinPath(directory, path_name + ".txt");
      if (!writeFile(fullpath, serialized)) {
        System.err.println(fullpath + " could not be written!!!!1");
        System.exit(1);
      } else {
        System.out.println("Wrote " + fullpath);
      }
  }
  
  
  /**
   * configures trajectory and creates splines
   * waypoint format: (y, -x, -theta)
   * @param args
   */
  public static void main(String[] args) {
   
    
    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 80.0;
    config.max_jerk = 60.0;
    config.max_vel = 10.0;
    
    final double kWheelbaseWidth = 27.0/12; //correct
   
    //drive straight for 10 feet 
    { 
      final String path_name = "Straight"; 
      
      WaypointSequence p = new WaypointSequence(10);
      p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
      p.addWaypoint(new WaypointSequence.Waypoint(0, 10, 0));

      Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
      serializeAndWrite(path, path_name);
    }
    
    //start at Center spline
    { 
        final String path_name = "CenterSwitchRFront"; 
        
        WaypointSequence p = new WaypointSequence(10);

        p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(4.5, 9.3333, 0));
        
        Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
        serializeAndWrite(path, path_name);
    }
    
    {
        final String path_name = "CenterScaleRight";
        
        WaypointSequence p = new WaypointSequence(10);

        p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(9.0625, 8.2167, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(9.0625, 17, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(8.0625, 20, -45));
        
        Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
        serializeAndWrite(path, path_name);
    }
    
    //start at Right spline
    
    {
        final String path_name = "RightRightSwitchFront";
        
        WaypointSequence p = new WaypointSequence(10);

        p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(-4.25, 8, 0));

        Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
        serializeAndWrite(path, path_name);
    }
    
    {
        final String path_name = "RightLeftSwitchBack";
        
        WaypointSequence p = new WaypointSequence(10);

        p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(0, 8, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(-12, 16, -89.99));
        p.addWaypoint(new WaypointSequence.Waypoint(-14, 15.5, -135));
        p.addWaypoint(new WaypointSequence.Waypoint(-15, 14, -170));

        Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
        serializeAndWrite(path, path_name);
    }
    
    {
    	final String path_name = "RightLeftSwitchFront"; //haven't test
    
    	WaypointSequence p = new WaypointSequence(10);

    	p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
    	p.addWaypoint(new WaypointSequence.Waypoint(-6.35166665, 4.66666667, 89.9));
    	p.addWaypoint(new WaypointSequence.Waypoint(-12.7033333, 9.33333333, 0));

    	Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
    	serializeAndWrite(path, path_name);
  	}
    
    {
    	final String path_name = "RightRightSwitchSide"; //haven't test
    
    	WaypointSequence p = new WaypointSequence(10);

    	p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
    	p.addWaypoint(new WaypointSequence.Waypoint(0, 8, 0));
    	p.addWaypoint(new WaypointSequence.Waypoint(-1.6967, 12.9167, -89.99));

    	Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
    	serializeAndWrite(path, path_name);
  	}
  	
    {
        final String path_name = "RightSwitchBlock"; //haven't test 
        
        WaypointSequence p = new WaypointSequence(10);

        p.addWaypoint(new WaypointSequence.Waypoint(-2.2967, 13.9167, 89.99));
        p.addWaypoint(new WaypointSequence.Waypoint(0, 16, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(1, 24.8333, 0));
       
        Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
        serializeAndWrite(path, path_name);
    }
    
    {
        final String path_name = "RightScaleRight";
        
        WaypointSequence p = new WaypointSequence(10);

        p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(0, 17, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(-1, 20, -45));
        
        Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
        serializeAndWrite(path, path_name);
    }
    
    {
        final String path_name = "RightScaleLeftSwitch";
        
        WaypointSequence p = new WaypointSequence(10);

        p.addWaypoint(new WaypointSequence.Waypoint(-2, 21.5, -135));
        p.addWaypoint(new WaypointSequence.Waypoint(-5, 20, -89.99));
        p.addWaypoint(new WaypointSequence.Waypoint(-12, 20, -89.99));
        p.addWaypoint(new WaypointSequence.Waypoint(-14, 19.5, -135));
        p.addWaypoint(new WaypointSequence.Waypoint(-15, 18, -170));

        Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
        serializeAndWrite(path, path_name);
    }
    
    {
        final String path_name = "RightLeftScale";
        
        WaypointSequence p = new WaypointSequence(10);

        p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(0, 8, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(-12, 16, -89.99));
        p.addWaypoint(new WaypointSequence.Waypoint(-15, 16, -89.99));
        p.addWaypoint(new WaypointSequence.Waypoint(-16.5, 16.5, -45));
        p.addWaypoint(new WaypointSequence.Waypoint(-17, 18.5, 0));

        Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
        serializeAndWrite(path, path_name);
    }
    
    {
    	config.dt = .01;
        config.max_acc = 80.0;
        config.max_jerk = 60.0;
        config.max_vel = 5.0;
    	
    	final String path_name = "LeftScaleRightSwitch";
        
        WaypointSequence p = new WaypointSequence(10);

        p.addWaypoint(new WaypointSequence.Waypoint(-17, 22, -179.99));
        p.addWaypoint(new WaypointSequence.Waypoint(-16.5, 21.5, -225));;
        p.addWaypoint(new WaypointSequence.Waypoint(-15, 21, -270));
        p.addWaypoint(new WaypointSequence.Waypoint(-8, 21.25, -270));
        p.addWaypoint(new WaypointSequence.Waypoint(-4.25, 20.5, -225));
        p.addWaypoint(new WaypointSequence.Waypoint(-4, 20, -180));

        Path path = PathGenerator.makePath(p, config, kWheelbaseWidth, path_name);
        serializeAndWrite(path, path_name);
      }
  }
}
