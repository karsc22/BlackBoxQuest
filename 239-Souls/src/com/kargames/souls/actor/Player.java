package com.kargames.souls.actor;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
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
	
	
	
	@Override
	public void act(float delta) {
		if (body.getPosition().y > -0.5f){
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
		
		Array<Controller> controllers = Controllers.getControllers();
		if (controllers.size > 0) {
			Controller c = controllers.get(0);
			float mod = speed;
			if (energy.value < 0.1) {
				mod /= 3;
			}
			float h = Axis.getHorizontal();
			float v = Axis.getVertical();
//			System.out.println(h + ", " + v);
			
			body.applyForceToCenter(h*delta*mod*(10 - grav), v*delta*mod*(10 - grav), true);
		}
		
		
		Vector2 speed = body.getLinearVelocity();
		float mod = 200;
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
