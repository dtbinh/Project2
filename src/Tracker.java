
import lejos.robotics.navigation.DifferentialPilot;
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
    pilot.setTravelSpeed(20);
    pilot.setRotateSpeed(180);
    pilot.setAcceleration(800);
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
	  			pilot.travel(7.9);
	  			break;
	  		}
	  	}
  }
  
  
  public void turn (int quater) {
	  pilot.rotate(quater*90); // minus = left, plus = right
  }
  
  public int getCount() {
	  return count;
  }
  
  public void setCount(int count) {
	  this.count = count;
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



