package com.kargames.souls.actor;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kargames.souls.App;

public class Sub extends Image {
	
	Body body;
	public Sub(App app, Body body) {
		super(app.textures.sub);
		this.body = body;
		body.setUserData(this);
		setSize(8, 4);
		setPosition(body.getPosition().x - 4, body.getPosition().y - 2);
	}
}
