package com.kargames.bbq.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class TextureManager {

	public SpriteDrawable ship;
	public TextureRegion part;
	public TextureRegion bubble;
	public TextureRegion sub;
	public TextureRegion blackBox;
	public Texture pixel;
	public TextureRegion arrow;

	public TextureManager() {
		ship = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/ship.png"))));
		part = new TextureRegion(new Texture(Gdx.files.internal("images/part1.png")));
		bubble = new TextureRegion(new Texture(Gdx.files.internal("images/bubble.png")));
		sub = new TextureRegion(new Texture(Gdx.files.internal("images/sub.png")));
		blackBox = new TextureRegion(new Texture(Gdx.files.internal("images/blackBox.png")));
		arrow = new TextureRegion(new Texture(Gdx.files.internal("images/arrow.png")));
		Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		map.setColor(1, 1, 1, 1f);
		map.fill();
		pixel = new Texture(map);
	}
}
