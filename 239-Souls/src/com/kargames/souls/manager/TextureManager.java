package com.kargames.souls.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class TextureManager {

	public TextureRegion libgdx;

	public TextureManager() {
		libgdx = new TextureRegion(new Texture(Gdx.files.internal("data/libgdx.png")));
	}
}
