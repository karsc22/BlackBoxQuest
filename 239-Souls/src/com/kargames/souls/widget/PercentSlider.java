package com.kargames.souls.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kargames.souls.App;

public class PercentSlider extends Table{

	Label label;
	Slider slider;
	
	public PercentSlider(App app) {
		slider = new Slider(0, 100, 1, false, app.skin);
		slider.setValue(50);
		label = new Label("50%", app.skin);
		addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				label.setText((int)slider.getValue() + "%");
			}
		});
		add(slider).padRight(5);
		add(label);
	}
}
