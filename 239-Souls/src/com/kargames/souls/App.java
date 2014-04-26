package com.kargames.souls;

import java.nio.IntBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.BufferUtils;
import com.kargames.souls.manager.ScreenManager;
import com.kargames.souls.manager.TextureManager;

// Using libgdx-nightly-2014 03 09

public class App extends com.badlogic.gdx.Game{
	
	public ScreenManager screens;
	public TextureManager textures;
	
	public Skin skin;
	
	public boolean isPaused;
	
	public App() {
		super();
	}
	
	@Override
	public void create() {
		
		skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

		IntBuffer buf = BufferUtils.newIntBuffer(16);
		Gdx.gl.glGetIntegerv(GL20.GL_MAX_TEXTURE_SIZE, buf);
		int maxSize = buf.get(0);
		System.out.println("max texture size = " + maxSize);
		
		Gdx.input.setCatchBackKey(true);
		textures = new TextureManager();
		screens = new ScreenManager(this);
		screens.init();

		setScreen(screens.gameScreen);
//		setScreen(screens.menuScreen); 
	}
}
