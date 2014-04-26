package com.kargames.souls.actor;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kargames.souls.App;

public class Part extends Image{
	final static float size = 1;
	
	public float val;
	public Body body;
	public Part(App app, Body body, float val) {
		super(app.textures.part);
		this.body = body;
		setSize(size, size);
		this.val = val;
		setPosition(body.getPosition().x - size/2, body.getPosition().y - size/2);
//		setOrigin(getWidth()/2, getHeight()/2);
		body.setUserData(this);
	}
}
