package com.kargames.bbq.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kargames.bbq.App;
import com.kargames.bbq.manager.Axis;
import com.kargames.bbq.util.Resource;

public class Player extends Image {

	public Body body;
	
	public float speed;
	App app;

	public Resource energy;

	public float light;
	
	boolean right;
	
	public boolean isTouchingSub;
	
	public Player(App app, Body body) {
		super(app.textures.ship);
		this.app = app;
		setSize(1, 1);
		this.body = body;

		energy = new Resource(20);
		energy.regen = -1;

		speed = 100;
		light = 10;
		body.setUserData(this);
		right = false;
	}
	
	public boolean isOutOfWater() {
		return (body.getPosition().y > -0.5f);
	}
	
	@Override
	public void act(float delta) {
		if (isTouchingSub){
			energy.regen = energy.max / 5f;
		} else {
			energy.regen = -1;
		}
		
		energy.update(delta);
		
		
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
		
		if (h > 0) {right = true; }
		if (h < 0) {right = false; }
		app.textures.ship.getSprite().setFlip(right, false);
		
//			System.out.println(h + ", " + v);
		
		body.applyForceToCenter(h*10, v*10, true);
		
		
		// slow down when in water
		Vector2 speed = body.getLinearVelocity();
		mod = 200;
		body.applyForceToCenter(-speed.x * delta * mod, -speed.y * delta*mod, true);
		setPosition(body.getPosition().x - 0.5f, body.getPosition().y - 0.5f);
	}

	public void setTouchingSub(boolean touching) {
		isTouchingSub = touching;
	}

	public boolean isTouchingSub() {
		return isTouchingSub;
	}


//	public float getCenterX() {
//		return getX() + getWidth()/2;
//	}
//	public float getCenterY() {
//		return getY() + getHeight()/2;
//	}
}
