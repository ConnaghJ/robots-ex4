package rp.robotics.gridmap;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;


public class JunctionDetect {
	private static boolean halt; 
	private static DifferentialPilot pilot;
	private static LightSensor sensorL;
	private static LightSensor sensorR;
	private static int turnLeft;
	private static int turnRight;

	public static void run(){
		System.out.println("RUN");
		pilot.forward();
		while(!halt)
		{
			/*if (turnRight < 90)
			{
				System.out.println("RIGHT");
			}*/
			if (turnRight > 70 && turnLeft > 70)
			{
				Junction();
			}
		}
	}
	
	public static void Junction()
	{
		pilot.travel(4);
		System.out.println("JUNCTION DETECTED");
		pilot.stop();
		Button.waitForAnyPress();
		pilot.forward();
	}
	
	public static void main(String[] args) {
		System.out.println("MAIN");
		
		pilot = new DifferentialPilot(2.0, 4.0, Motor.B, Motor.A);
		pilot.setTravelSpeed(10);
		
		halt = false;

		sensorL = new LightSensor(SensorPort.S4);
		sensorL.setFloodlight(true);
		sensorR = new LightSensor(SensorPort.S1);
		sensorR.setFloodlight(true);
		
		Button.waitForAnyPress();
		sensorL.calibrateHigh();
		sensorR.calibrateHigh();
		Button.waitForAnyPress();
		sensorL.calibrateLow();
		sensorR.calibrateLow();
		
		SensorPort.S1.addSensorPortListener(new SensorPortListener(){

			public void stateChanged(SensorPort aSource, int aOldValue,int aNewValue)
			{
				turnRight = aNewValue;
			}
		});
		
		SensorPort.S4.addSensorPortListener(new SensorPortListener(){
			
			public void stateChanged(SensorPort aSource, int aOldValue,int aNewValue)
			{
				turnLeft = aNewValue;
			}
		});
		pilot.setTravelSpeed(8);
		Button.waitForAnyPress();
		Delay.msDelay(1000);
		
		Button.ENTER.addButtonListener(new ButtonListener() {

			public void buttonReleased(Button _b) {
				//do nothing
			}

			public void buttonPressed(Button _b) {
				halt = true;
				pilot.stop();
			}
		});
		
		run();
		
	}
}
