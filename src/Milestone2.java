import lejos.nxt.Button;


public class Milestone2 {
	
	
	  public void gridnav(Tracker tracker) {
		  Button.waitForAnyPress();
		  turnInDirection(tracker, -1, 8);
		  tracker.turn(-1);
		  turnInDirection(tracker, 1, 8);
		  turnInDirection(tracker, -2, 4);
		  tracker.turn(2);
		  turnInDirection(tracker, 2, 4);
	}
		 
	  private void turnInDirection(Tracker tracker, int quater, int numberOfTurns) {
		  for (int k = 0; k < numberOfTurns; k++) {
			  tracker.trackLine();
			  tracker.trackLine();
			  tracker.stop();
			  tracker.turn(quater);
		  }
		  
	  }

}
