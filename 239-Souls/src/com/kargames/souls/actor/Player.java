package com.kargames.souls.actor;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kargames.souls.App;
import com.kargames.souls.manager.Axis;
import com.kargames.souls.util.Resource;

public class Player extends Image {

	public Body body;
	
	public float speed;
	App app;
	public Resource cargo;

	public Resource energy;

	public float light;
	public Player(App app, Body body) {
		super(app.textures.libgdx);
		this.app = app;
		setSize(1, 1);
		this.body = body;

		energy = new Resource(15);
		energy.regen = -1;

		cargo = new Resource(10);
		speed = 100;
		light = 10;
		body.setUserData(this);
	}
	
	public boolean isOutOfWater() {
		return (body.getPosition().y > -0.5f);
	}
	
	@Override
	public void act(float delta) {
		if (isOutOfWater()){
			energy.regen = energy.max / 5f;
		} else {
			energy.regen = -1;
		}
		
		energy.update(delta);
		
		float grav = 0;
		if (body.getPosition().y > 0) {
			grav = body.getPosition().y * 10;
			if (grav > 10) grav = 10;
			body.applyForceToCenter(0, -grav, true);
		}
		
		float mod = speed;
		if (energy.value < 0.1) {
			mod /= 3;
			body.applyForceToCenter(0, 4, true);
		}
		float h = Axis.getHorizontal() * delta * mod;
		float v = Axis.getVertical() * delta * mod;
		if (MathUtils.random(2f) < Math.abs(h) + Math.abs(v) && body.getPosition().y < 0.5f) {
			app.screens.gameScreen.makeBubbles(h, v);
		}
//			System.out.println(h + ", " + v);
		
		body.applyForceToCenter(h*(10 - grav), v*(10 - grav), true);
		
		
		// slow down when in water
		Vector2 speed = body.getLinearVelocity();
		mod = 200;
		body.applyForceToCenter(-speed.x * delta * mod, -speed.y * delta*mod, true);
		setPosition(body.getPosition().x - 0.5f, body.getPosition().y - 0.5f);
	}



//	public float getCenterX() {
//		return getX() + getWidth()/2;
//	}
//	public float getCenterY() {
//		return getY() + getHeight()/2;
//	}
}
