package com.kargames.souls;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class SoulsDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "239-Souls";
		cfg.width = 1280;
		cfg.height = 720;
		
		new LwjglApplication(new App(), cfg);
	}
}
