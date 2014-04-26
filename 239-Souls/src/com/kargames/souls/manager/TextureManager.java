package com.kargames.souls.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class TextureManager {

	public TextureRegion libgdx;
	public TextureRegion part;
	public TextureRegion bubble;
	public Texture pixel;

	public TextureManager() {
		libgdx = new TextureRegion(new Texture(Gdx.files.internal("data/libgdx.png")));
		part = new TextureRegion(new Texture(Gdx.files.internal("levels/part1.png")));
		bubble = new TextureRegion(new Texture(Gdx.files.internal("levels/bubble.png")));
		Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		map.setColor(1, 1, 1, 1f);
		map.fill();
		
		pixel = new Texture(map);
	}
}
