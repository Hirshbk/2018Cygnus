package visualization;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import util.TextFileDeserializer;
import util.TextFileReader;

public class SplineImage {
	
	Double[] rotatedX, rotatedY; //for when we change the axes to fit the picture
	Double[] scaledX, scaledY; //for scaling the image so it matches field picture distances
	
	double scaledStartX, scaledStartY; //for scaling the start point to match picture distances 
	
	int fieldImageWidth = 330; //same as field image
	int fieldImageHeight = 326; //same as field image
	
	Color green = new Color(33, 255, 33); //green is not a creative color
	
	/**
	 *  generates a graph of the spline points and saves it as a .png in the pictures folder
	 * @param splineName the name of the spline to be drawn
	 * @param startX the start distance along the alliance wall in feet
	 * @param startY the start distance away from the alliance wall in feet
	 */
	public void generate(String splineName, double startX, double startY) {
		
		//change directory name to 2018Competition github once spline files are on github
		String directoryName = "Splines/" + splineName + ".txt";
		TextFileReader reader = new TextFileReader(directoryName);
		TextFileDeserializer deserializer = new TextFileDeserializer();
		deserializer.deserialize(reader.readWholeFile());
		
		//scales the points from feet to px and rotates the axes
		scaledStartX = startX * fieldImageWidth / 27;
		scaledStartY = fieldImageHeight - startY * fieldImageHeight / 27;
		
		//changes the axes so that it's from the perspective of the robot
		rotatedX = deserializer.getArray('y'); //rotate because the x axis is at the bottom of the image
		rotatedY = multiply(-1, deserializer.getArray('x')); //rotate and flip because y is negative 
		
		//changes the lines from feet to px and translates them to the start position
		scaledX = add(scaledStartX, multiply(fieldImageWidth / 27, rotatedX));
		scaledY = add(scaledStartY, multiply(fieldImageHeight / 27, rotatedY));
		
		try {

		      // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed into integer pixels
		      BufferedImage splineGraph = new BufferedImage(fieldImageWidth, fieldImageHeight, BufferedImage.TYPE_INT_ARGB);

		      Graphics2D graphics = splineGraph.createGraphics();
		      graphics.setColor(green);
		      
		      //draws a circle at every point (x,y)
		      for(int i = 0; i < scaledX.length; i++) {
		    	  drawCircle(graphics, scaledX[i], scaledY[i], 2);
		      }
		      
		      //writes spline file to Pictures folder
		      ImageIO.write(splineGraph, "PNG", new File(splineName + "_Graph.PNG"));
		      
		      System.out.println("image drawn");
		    } catch (IOException ie) {
		      ie.printStackTrace();
		      System.out.println("image not drawn");
		    }
	}
	
	/**
	 * draws a circle centered at x,y
	 * @param g graphics thing to use
	 * @param x center point x coordinate
	 * @param y center point y coordinate
	 * @param radius radius of the circle
	 */
	public void drawCircle(Graphics2D g, double x, double y, int radius) {

		  int diameter = radius * 2;

		  //shift x and y by the radius of the circle in order to correctly center it
		  g.fillOval((int) Math.round(x - radius), (int) Math.round(y - radius), diameter, diameter); 

		}
	
	/**
	 * multiplies an array by a constant
	 * @param c the constant to multiply by
	 * @param array the array to multiply by
	 * @return the array multiplied by the constant
	 */
	public Double[] multiply(double c, Double[] array) {
		Double[] multipliedArray = new Double[array.length];
		for(int i = 0; i < array.length; i++) {
			multipliedArray[i] = (array[i] * c);
		}
		return multipliedArray;
	}
	
	/**
	 * adds a constant to every element in an array
	 * @param c constant to add to the array
	 * @param array the array to add the constant to
	 * @return the array with the constant added to every element
	 */
	public Double[] add(double c, Double[] array) {
		Double[] addedArray = new Double[array.length];
		for(int i = 0; i < array.length; i++) {
			addedArray[i] = (array[i] + c);
		}
		return addedArray;
	}
	
}
