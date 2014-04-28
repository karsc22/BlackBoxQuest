package com.kargames.souls;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kargames.bbq.App;

public class BlackBoxQuestDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Black Box Quest";
		cfg.width = 1280;
		cfg.height = 720;
		
		new LwjglApplication(new App(), cfg);
	}
}
