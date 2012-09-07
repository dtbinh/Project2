
import lejos.nxt.*;
import lejos.util.Delay;

/**
 * Record light sensor data for line follower experiments.
 * Displays left, right sensor values.  Press ENTER to record, ESCAPE to end data collection
 * and transmit.  
 * Left sensor = S1, right sensor = S4;
 * Run DownloadDL to receive the data and display it on your PC>.
 * @author Roger Glassey
 *
 */
public class LineDataCollect
{

   LightSensor _leftEye = new LightSensor(SensorPort.S1);
   LightSensor _rightEye = new LightSensor(SensorPort.S4);
   Datalogger logger = new Datalogger();

   /**
    * start collecting data
    */
   public void go()
   {
      boolean more = true;
      while (more)
      {
         show();
         if (Button.readButtons() == Button.ENTER.getId())
         {
            Delay.msDelay(500);
            logger.writeLog(_leftEye.readNormalizedValue());
            logger.writeLog(_rightEye.readNormalizedValue());
            Sound.beep();

         }
         if (Button.readButtons() == Button.ESCAPE.getId())
         {
            logger.transmit();
            more = false;
         }
      }
   }

   public void calibrate()
   {
      while (0 < Button.readButtons());
      for (byte i = 0; i < 2; i++)
      {
         while (0 == Button.readButtons())//wait for press
         {
            LCD.drawInt(_leftEye.readValue(), 4, 6, 1 + i);
            LCD.drawInt(_rightEye.readValue(), 4, 12, 1 + i);
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
            _leftEye.calibrateLow();
            _rightEye.calibrateLow();
         } else if (i == 1)
         {
            _rightEye.calibrateHigh();
            _leftEye.calibrateHigh();
         }
      }
   }
   /**
    * display left, right sensor values 
    *
    */ 

   private void show()
   {
      LCD.drawInt(_leftEye.readNormalizedValue(), 4, 0, 1);
      LCD.drawInt(_rightEye.readNormalizedValue(), 4, 8, 1);
  
   }
   
}

