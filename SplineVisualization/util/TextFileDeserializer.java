package util;
import java.util.ArrayList;

public class TextFileDeserializer {

    ArrayList<Double> xPosition = new ArrayList<Double>();
    ArrayList<Double> yPosition = new ArrayList<Double>();
    int num_elements;
	
    /**
     *  deserializes a string and puts it into an x and y arrayList
     * @param serialized the string to be deserialized
     */
  public void deserialize(String serialized) {
	  
	//first it splits it into each line
    String[] lines = serialized.split("\n");
    System.out.println("Parsing path string...");
    System.out.println("String has " + serialized.length() + " chars");
    
    num_elements = Integer.parseInt(lines[1]) * 2; //combined number for left and right
    
    //then it splits each line into its components
    for (int i = 0; i < num_elements; i++) {
      String[] number = lines[i+2].split(" ");
      
      //we take columns 6 and 7, which are the x and y coordinates
      double xi = FastParser.parseFormattedDouble(number[6]);
      double yi = FastParser.parseFormattedDouble(number[7]);
      
      xPosition.add(i, xi);
      yPosition.add(i, yi);
    }
    
    System.out.println("...finished parsing coordinates from string.");
  }
  
  /**
   * 
   * @param axis either x or y depending on whether you want the x coordinates or the y coordinates
   * @return a Double[] of either the x coordinates or the y coordinates
   */
  public Double[] getArray(char axis) {
	  if(axis == 'x') {
		  return xPosition.toArray(new Double[num_elements]);
	  } else if(axis == 'y') {
		  return yPosition.toArray(new Double[num_elements]);
	  } else {
		  System.out.println("array axis must be x or y");
		  return null;
	  }
  }
  
}
