
import java.util.Random;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import lejos.nxt.*;

/**
This class needs a higher level controller to implement the navigtion logic<br>
Responsibility: keep robot on the line till it senses a marker, then stop <br>
also controls turning to a new line at +- 90 or 180 deg<br>
Hardware: Two light sensors , shielded, 2 LU above floor.
Classes used:  Pilot, LightSensors<br>
Control Algorithm:  proportional control. estimate distance from centerline<br>
Calibrates both sensors to line, background
Updated 9/10/2007  NXT hardware
@author Roger Glassey
 */
public class Tracker
{

  /**
   * controls the motors
   */
  public DifferentialPilot pilot;
  /**
   *set by constructor , used by trackline()
   */
  private LightSensor leftEye;
  /**
   *set by constructor , used by trackline()
   */
  private LightSensor rightEye;
  /**
   * controls the direction of turns
   */
  private int turnDirection = 1;
  /**
   * count numbers of turns that been made
   */
  private int count = 0;


  /**
   *constructor - specifies which sensor ports are left and right
   */
//	public Tracker( Pilot thePilot,SensorPort leftI,SensorPort rightI)
  public Tracker(DifferentialPilot thePilot, LightSensor leftEye , LightSensor  rightEye)
  {
    pilot = thePilot;
    pilot.setTravelSpeed(15);
    pilot.setRotateSpeed(180);
    pilot.setAcceleration(400);
    this.leftEye = leftEye;
    this.leftEye.setFloodlight(true);
    this.rightEye = rightEye;
    this.rightEye.setFloodlight(true);
  }

  /**
  follow line till intersection (a black marker) is detected
  uses proportional  control <br>
  Error signal is supplied by CLdistance()<br>
  uses CLdistance(), pilot.steer()
  loop execution about 65 times per second in 1 sec.<br>
   */
  public void trackLine() {
	  float gain = (float) 0.7f;	// you may need to change this for smooth tracking
	  							// This method needs to detect a black maker.  
	  	while(true) {
	  		int lval = leftEye.getLightValue();
	  		int rval = rightEye.getLightValue(); 	 
	  		System.out.println(count);
	  		int error = CLDistance(lval, rval);    
	  		double control = (error*gain); // do better
	  		pilot.steer(control);
	  		if (lval < -25 || rval < -25) {
	  			count++;
	  			Sound.playTone(1000,100);
	  			pilot.travel(7.8);
	  			stop();
	  			break;
	  		}
	  	}
  }
  
  public void gridNavigation() {
	  Button.waitForAnyPress();
	  int numberOfTurns = 0;
	  gridNavigation(numberOfTurns);
  }
  
  public boolean gridNavigation(int numberOfTurns) {
	  if (numberOfTurns > 8) {
		  return true;
	  }
	  trackLine();
	  Random rng = new Random();
	  int turnDir = rng.nextInt(3)-1;
	  System.out.println(turnDir);
	  int lval = leftEye.getLightValue();
	  int rval = rightEye.getLightValue();
	  System.out.println(lval);
	  System.out.println(rval);
	  if (lval > 90 || rval > 90) {
			turnDir = 2;
	  }
	  pilot.rotate(90*turnDir);
	  numberOfTurns++;
	  gridNavigation(numberOfTurns);
	  return false;
  }


  
  /**
  follow the track for 4 complete circuits, turn around, and complete 4 circuits in opposite direction
   */
  public void trackAndTurn() {
	  Button.waitForAnyPress();
	  while(count < 8) {
		  trackLine();
	  }
	  stop();
	  pilot.rotate(180);
	  while(count < 16) {
		  trackLine();
	  }
	  stop();
	  count = 0;
  }
  
  /**
   * makes the robot do 4 figure 8 circuits
   uses trackAnEight(int param) 
   */
  public void trackAnEight() {
	  Button.waitForAnyPress();
	  int numberOfTurns = 0;
	  trackAnEight(numberOfTurns);
  }
  
  /**
   * Help method of trackAnEight(),
   * @param numberOfTurns
   */
  public boolean trackAnEight(int numberOfTurns) {
	  if (numberOfTurns >= 8) {
		  stop();
		  return true;
	  }
	  while(count < 1) { 
		  trackLine();
	  }
	  stop();
	  pilot.rotate(90);
	  while(count < 2) {
		  trackLine();
	  }
	  stop();
	  pilot.rotate(-90);
	  while(count < 3) { 
		  trackLine();
	  }
	  stop();
	  pilot.rotate(-90);
	  while(count < 4) {
		  trackLine();
	  }
	  stop();
	  pilot.rotate(90);
	  count=0;
	  numberOfTurns = numberOfTurns + 4;
	  System.out.println(numberOfTurns);
	  trackAnEight(numberOfTurns);
	  return false;
  }

  /**
   * helper method for Tracker; calculates distance from centerline, used as error by trackLine()
   * @param left light reading
   * @param right light reading
   * @return  distance
   */
  int CLDistance(int left, int right) {
	  int error = left - right; // if positive to much to the left, if negative to much to the right
	  return error;
  }
 
  public void stop()
  {
    pilot.stop();
  }

  /**
  calibrates for line first, then background, then marker with left sensor.  displays light sensor readings on LCD (percent)<br>
  Then displays left sensor (scaled value).  Move left sensor  over marker, press Enter to set marker value to sensorRead()/2
   */
  public void calibrate()
  {
      System.out.println("Calibrate Tracker");
    
      for (byte i = 0; i < 3; i++)
      {
        while (0 == Button.readButtons())//wait for press
        {
          LCD.drawInt(leftEye.getLightValue(), 4, 6, 1 + i);
          LCD.drawInt(rightEye.getLightValue(), 4, 12, 1 + i);
          if (i == 0)
          {
            LCD.drawString("LOW", 0, 1 + i);
          } else if (i == 1)
          {
            LCD.drawString("HIGH", 0, 1 + i);
          } 
        }
        Sound.playTone(1000 + 200 * i, 100);
        if (i == 0)
        {
          leftEye.calibrateLow();
          rightEye.calibrateLow();
        } else if (i == 1)
        {
          rightEye.calibrateHigh();
          leftEye.calibrateHigh();
        } 
        while (0 < Button.readButtons())
        {
          Thread.yield();//button released
        }
       
    }
    while (0 == Button.readButtons())// while no press
    {
      int lval = leftEye.getLightValue();
      int rval = rightEye.getLightValue();
      LCD.drawInt(lval, 4, 0, 5);
      LCD.drawInt(rval, 4, 4, 5);
      LCD.drawInt(CLDistance(lval, rval), 4, 12, 5);
      LCD.refresh();
    }
    LCD.clear();
  }
  
}



