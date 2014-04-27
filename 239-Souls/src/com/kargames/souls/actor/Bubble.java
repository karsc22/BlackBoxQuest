package com.kargames.souls.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kargames.souls.App;

public class Bubble extends Image{

	public Vector2 speed;
	App app;
	float size;
	public Bubble(App app) {
		super(app.textures.bubble);
		size = MathUtils.random(0.15f, 0.28f);
		setSize(size, size);
		setOrigin(getWidth()/2, getHeight()/2);
		this.app = app;
		speed = new Vector2();
		setColor(1, 1, 1, 0);
	}

	public void init(Vector2 pos, float xspeed, float yspeed) {
		init(pos.x, pos.y, xspeed, yspeed);
	}
	public void init(float x, float y, float xspeed, float yspeed) {
		float randomness = 0.3f;
		setPosition(x + randomness*MathUtils.random(-1f, 1f) - size/2, 
				y + randomness * MathUtils.random(-1f,1f)- size/2);
		speed.set(xspeed  + MathUtils.random(-5f, 5f), 
				yspeed+ MathUtils.random(-5f, 5f));
		setColor(1, 1, 1, 1);
		clearActions();
		addAction(Actions.fadeOut(1));
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		moveBy(speed.x * delta, speed.y * delta);
		if (getY() > 0) {
			setColor(1, 1, 1, 0);
		}
		float randomness = 30;
		speed.x *= 0.95f;
		speed.y *= 0.95f;
		speed.x += delta * MathUtils.random(-1f, 1f) * randomness;
		speed.y += delta + delta * MathUtils.random(-1f, 1f)* randomness;
	}
}
