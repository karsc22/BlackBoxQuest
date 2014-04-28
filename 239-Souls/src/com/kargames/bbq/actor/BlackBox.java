package com.kargames.bbq.actor;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kargames.bbq.App;

public class BlackBox extends Image{
	
	Body body;
	public BlackBox(App app, Body body) {
		super(app.textures.blackBox);
		this.body = body;
		body.setUserData(this);
		setSize(3, 3);
		setPosition(body.getPosition().x - 1.5f, body.getPosition().y - 1.5f);
	}
}
