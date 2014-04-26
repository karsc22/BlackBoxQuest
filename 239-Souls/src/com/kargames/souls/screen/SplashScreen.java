package com.kargames.souls.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kargames.souls.App;
import com.kargames.souls.widget.FillTable;

public class SplashScreen extends BaseScreen {
	
	public final float showTime = 0.5f;

	public SplashScreen(final App app) {
		super(app);
		FillTable ft = new FillTable();
		Image splash = new Image(app.textures.libgdx);
		ft.add(splash);
		stage.addActor(ft);
		splash.setVisible(false);
		
		splash.addAction(sequence(fadeOut(0), visible(true), delay(fadeTime), 
				fadeIn(showTime), delay(showTime), fadeOut(showTime), new RunnableAction() {
			@Override
			public void run() {
				app.screens.jumpToScreen(app.screens.menuScreen);
			}
		}));
		
	}
	
	
}
