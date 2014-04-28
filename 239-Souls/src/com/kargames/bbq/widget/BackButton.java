package com.kargames.bbq.widget;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kargames.bbq.App;

public class BackButton extends TextButton {

	public BackButton(final App app) {
		this(app, "Back");
	}
	
	public BackButton(final App app, String text) {
		super(text, app.styles.tbs);
		addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				app.screens.back();
			}
		});
	}
}
