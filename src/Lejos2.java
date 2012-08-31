
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;


public class Lejos2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//LineDataCollect collecter = new LineDataCollect();
		//collecter.calibrate();
		DifferentialPilot pilot = new DifferentialPilot(5.6, 11.9, Motor.C, Motor.A, false);
		LightSensor leftEye = new LightSensor(SensorPort.S1);
		LightSensor rightEye = new LightSensor(SensorPort.S4);
		Tracker tracker = new Tracker(pilot, leftEye, rightEye);
		tracker.trackLine();
	}

}
