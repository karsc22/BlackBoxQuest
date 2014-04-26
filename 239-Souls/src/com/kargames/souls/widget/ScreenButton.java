package com.kargames.souls.widget;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kargames.souls.App;
import com.kargames.souls.screen.BaseScreen;

public class ScreenButton extends TextButton {
	public ScreenButton(final App app, String text, final BaseScreen screen) {
		super(text, app.skin);
		addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				app.screens.setNextScreen(screen);
			}
		});
	}
}
