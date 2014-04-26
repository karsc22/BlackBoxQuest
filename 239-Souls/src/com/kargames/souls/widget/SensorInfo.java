package com.kargames.souls.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kargames.souls.App;

public class SensorInfo extends Table {
	Label accelX;
	Label accelY;
	Label accelZ;
	Label azimuth;
	Label pitch;
	Label roll;
	Label rotation;
	
	public SensorInfo(App app) {
		super(app.skin);
		setFillParent(true);
		top().right();
		defaults().top().right();

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
		accelX.setText(""+Gdx.input.getAccelerometerX());
		accelY.setText(""+Gdx.input.getAccelerometerY());
		accelZ.setText(""+Gdx.input.getAccelerometerZ());

		azimuth.setText(""+Gdx.input.getAzimuth());
		pitch.setText(""+Gdx.input.getPitch());
		roll.setText(""+Gdx.input.getRoll());
		
		rotation.setText(""+Gdx.input.getRotation());
	}
	
	
}
