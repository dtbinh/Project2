
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;


public class Lejos2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//LineDataCollect collector = new LineDataCollect();
		//collector.calibrate();
		//collector.go();
		DifferentialPilot pilot = new DifferentialPilot(5.6, 11.9, Motor.C, Motor.A, false);
		LightSensor leftEye = new LightSensor(SensorPort.S1);
		LightSensor rightEye = new LightSensor(SensorPort.S4);
		//leftEye.setHigh();
		//leftEye.setLow(low);
		//rightEye.setHigh();
		//rightEye.setLow();
		Tracker tracker = new Tracker(pilot, leftEye, rightEye);
		tracker.calibrate(); 
		//tracker.trackAndTurn();
		//tracker.trackAnEight();
		tracker.gridNavigation();
	}

}
