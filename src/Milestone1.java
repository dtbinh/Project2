import lejos.nxt.Button;


public class Milestone1 {
	
	 /**
	  follow the track for 4 complete circuits, turn around, and complete 4 circuits in opposite direction
	   */
	  public void trackAndTurn(Tracker tracker) {
		  Button.waitForAnyPress();
		  while(tracker.getCount() < 8) {
			  tracker.trackLine();
		  }
		  tracker.stop();
		  tracker.turn(2);
		  while(tracker.getCount() < 16) {
			  tracker.trackLine();
		  }
		  tracker.stop();
		  tracker.setCount(0);
	  }
	  
	  /**
	   * makes the robot do 4 figure 8 circuits
	   uses trackAnEight(int param) 
	   */
	  public void trackAnEight(Tracker tracker) {
		  Button.waitForAnyPress();
		  int numberOfTurns = 0;
		  trackAnEight(tracker, numberOfTurns);
	  }
	  
	  /**
	   * Help method of trackAnEight(),
	   * @param numberOfTurns
	   */
	  public boolean trackAnEight(Tracker tracker, int numberOfTurns) {
		  if (numberOfTurns >= 8) {
			  tracker.stop();
			  return true;
		  }
		  while(tracker.getCount() < 1) { 
			  tracker.trackLine();
		  }
		  tracker.stop();
		  tracker.turn(1);
		  while(tracker.getCount() < 2) {
			  tracker.trackLine();
		  }
		  tracker.stop();
		  tracker.turn(-1);
		  while(tracker.getCount() < 3) { 
			  tracker.trackLine();
		  }
		  tracker.stop();
		  tracker.turn(-1);
		  while(tracker.getCount() < 4) {
			  tracker.trackLine();
		  }
		  tracker.stop();
		  tracker.turn(1);
		  tracker.setCount(0);
		  numberOfTurns = numberOfTurns + 4;
		  System.out.println(numberOfTurns);
		  trackAnEight(tracker, numberOfTurns);
		  return false;
	  }

}
