package com.kargames.souls.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kargames.souls.App;

public class SensorInfo extends Table {
	
	   public static final int BUTTON_X = 2;
	   public static final int BUTTON_Y = 3;
	   public static final int BUTTON_A = 0;
	   public static final int BUTTON_B = 1;
	   public static final int BUTTON_BACK = 6;
	   public static final int BUTTON_START = 7;
//	   public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
//	   public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
//	   public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
//	   public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
	   public static final int BUTTON_LB = 4;
	   public static final int BUTTON_L3 = 8;
	   public static final int BUTTON_RB = 5;
	   public static final int BUTTON_R3 = 9;
	   public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
	   public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
	   public static final int AXIS_LEFT_TRIGGER = 4; //value 0 to 1f
	   public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
	   public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
	   public static final int AXIS_RIGHT_TRIGGER = 4; //value 0 to -1f
	
	Label accelX;
	Label accelY;
	Label accelZ;
	Label azimuth;
	Label pitch;
	Label roll;
	Label rotation;
	Controller controller;
	
	public SensorInfo(App app) {
		super(app.skin);
		setFillParent(true);
		top().right();
		defaults().top().right();
		System.out.println(Controllers.getControllers().size);
		controller = Controllers.getControllers().get(0);
		

		accelX = new Label("", app.skin);
		accelY = new Label("", app.skin);
		accelZ = new Label("", app.skin);
		azimuth = new Label("", app.skin);
		pitch = new Label("", app.skin);
		roll = new Label("", app.skin);
		rotation = new Label("", app.skin);
		
		add("accelX: ");
		add(accelX).row();

		add("accelY: ");
		add(accelY).row();

		add("accelZ: ");
		add(accelZ).row();

		add("azimuth: ");
		add(azimuth).row();

		add("pitch: ");
		add(pitch).row();

		add("roll: ");
		add(roll).row();

		add("rotation: ");
		add(rotation).row();
	}


	@Override
	public void act(float delta) {
		super.act(delta);
//		accelX.setText(""+Gdx.input.getAccelerometerX());
//		accelY.setText(""+Gdx.input.getAccelerometerY());
//		accelZ.setText(""+Gdx.input.getAccelerometerZ());
//		azimuth.setText(""+Gdx.input.getAzimuth());
//		pitch.setText(""+Gdx.input.getPitch());
//		roll.setText(""+Gdx.input.getRoll());
//		rotation.setText(""+Gdx.input.getRotation());
		
		accelX.setText(""+controller.getAxis(AXIS_LEFT_X));
		accelY.setText(""+controller.getAxis(AXIS_LEFT_Y));
		accelZ.setText(""+controller.getAxis(AXIS_RIGHT_X));
		azimuth.setText(""+controller.getButton(BUTTON_A));
		pitch.setText(""+controller.getButton(BUTTON_B));
		roll.setText(""+controller.getButton(BUTTON_X));
		rotation.setText(""+controller.getButton(BUTTON_Y));
	}
	
	
}
