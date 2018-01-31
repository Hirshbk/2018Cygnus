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
   
    //drive straight for 8 feet, gear side first
    {
      final String path_name = "Straight";
      
      WaypointSequence p = new WaypointSequence(10);
      p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
      p.addWaypoint(new WaypointSequence.Waypoint(0, 8.0, 0));

      Path path = PathGenerator.makePath(p, config,
          kWheelbaseWidth, path_name);

      serializeAndWrite(path, path_name);
    }
    {
        final String path_name = "Backwards";
        
        WaypointSequence p = new WaypointSequence(10);
        p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(1.0, 8.0, 0));

        Path path = PathGenerator.makePath(p, config,
            kWheelbaseWidth, path_name);

        serializeAndWrite(path, path_name);
      }
    //side gear turning right
    {
        final String path_name = "Turn";
        
        WaypointSequence p = new WaypointSequence(10);
        p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(1.0, 8.0, 0));
 
        Path path = PathGenerator.makePath(p, config,
            kWheelbaseWidth, path_name);

        serializeAndWrite(path, path_name);
      }
    {
        final String path_name = "Path1";
        
        WaypointSequence p = new WaypointSequence(10);        
        p.addWaypoint(new WaypointSequence.Waypoint(0, 2, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(0.0, 17.0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(-4.0, 20.0, -89.9));
        p.addWaypoint(new WaypointSequence.Waypoint(-16, 20.0, -89.9));
        p.addWaypoint(new WaypointSequence.Waypoint(-18.5, 22.0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(-18.5, 27.0, 0));
        
        Path path = PathGenerator.makePath(p, config,
            kWheelbaseWidth, path_name);

        serializeAndWrite(path, path_name);
      }
    {
        final String path_name = "Path2";
        
        WaypointSequence p = new WaypointSequence(10);
        p.addWaypoint(new WaypointSequence.Waypoint(0, 2, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(0, 27, 0));
 
        Path path = PathGenerator.makePath(p, config,
            kWheelbaseWidth, path_name);

        serializeAndWrite(path, path_name);
      }
    {
        final String path_name = "Path3";
        
        WaypointSequence p = new WaypointSequence(10);
        p.addWaypoint(new WaypointSequence.Waypoint(0, 2, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(-1, 14.0, 0));
 
        Path path = PathGenerator.makePath(p, config,
            kWheelbaseWidth, path_name);

        serializeAndWrite(path, path_name);
      }
    {
        final String path_name = "Path4";
        
        WaypointSequence p = new WaypointSequence(10);
        p.addWaypoint(new WaypointSequence.Waypoint(0, 2, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(0.5, 16.0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(-2.5, 20.0, -89.9));
        p.addWaypoint(new WaypointSequence.Waypoint(-9.5, 20.0, -89.9));
        p.addWaypoint(new WaypointSequence.Waypoint(-14.5, 20.0, -89.9));
        
        Path path = PathGenerator.makePath(p, config,
            kWheelbaseWidth, path_name);

        serializeAndWrite(path, path_name);
      }
    {
        final String path_name = "Path4wTurn";
        
        WaypointSequence p = new WaypointSequence(10);
        p.addWaypoint(new WaypointSequence.Waypoint(0, 2, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(0.5, 16.0, 0));
        p.addWaypoint(new WaypointSequence.Waypoint(-2.5, 20.0, -89.9));
        p.addWaypoint(new WaypointSequence.Waypoint(-9.5, 20.0, -89.9));
        p.addWaypoint(new WaypointSequence.Waypoint(-12.5, 19.0, -135.0));
        p.addWaypoint(new WaypointSequence.Waypoint(-14.5, 18.0, -89.9));
        
 
        Path path = PathGenerator.makePath(p, config,
            kWheelbaseWidth, path_name);

        serializeAndWrite(path, path_name);
      }
    
  }
}
