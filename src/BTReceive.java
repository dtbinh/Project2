
import lejos.nxt.*;
import lejos.nxt.comm.*;
import java.io.*;


/**
 * taken from examples 
 * use with  BTSend on your PC
 * @author Roger Glassey
 */
public class BTReceive {

   public static void main(String [] args)  throws Exception 
   {
      String connected = "Connected";
      String waiting = "Waiting";

      while (true)
      {
         LCD.drawString(waiting,0,0);
         LCD.refresh();

         BTConnection btc = Bluetooth.waitForConnection(); // this method is very patient. 

         LCD.clear();
         LCD.drawString(connected,0,0);
         LCD.refresh();	

         InputStream is = btc.openInputStream();
         OutputStream os = btc.openOutputStream();
         DataInputStream dis = new DataInputStream(is);
         DataOutputStream dos = new DataOutputStream(os);
         int[] data = new int[10];
         for(int k = 0 ; k<10; k++)
         {
            for(int i=0;i<10;i++) 
            {
               int ii = dis.readInt();
               data[i] = ii;
               LCD.drawInt(ii,4,0,i%8);
            }
            LCD.refresh();
            for(int i = 0 ; i<10 ; i++)
            {
               dos.writeInt(-data[i]);
               LCD.drawInt(i,0,14,0);
            }
            dos.flush();// without this, no data is sent. 
         }
         dis.close();
         dos.close();
         btc.close();
         Button.waitForAnyPress();
      }
   }
}


