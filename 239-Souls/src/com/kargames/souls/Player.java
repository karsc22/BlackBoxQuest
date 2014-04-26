package com.kargames.souls;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kargames.souls.manager.Axis;

public class Player extends Image {

	public Body body;
	public Player(App app) {
		super(app.textures.libgdx);
		setSize(1, 1);
	}
	
	
	
	@Override
	public void act(float delta) {
		
		
		float grav = 0;
		if (body.getPosition().y > 0) {
			grav = body.getPosition().y * 10;
			if (grav > 10) grav = 10;
			body.applyForceToCenter(0, -grav, true);
		}
		
		Array<Controller> controllers = Controllers.getControllers();
		if (controllers.size > 0) {
			Controller c = controllers.get(0);
			float mod = 100;
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



	public float getCenterX() {
		return getX() + getWidth()/2;
	}
	public float getCenterY() {
		return getY() + getHeight()/2;
	}
}
