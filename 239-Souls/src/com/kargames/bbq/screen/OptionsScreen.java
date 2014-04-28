package com.kargames.bbq.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kargames.bbq.App;
import com.kargames.bbq.widget.BackButton;
import com.kargames.bbq.widget.FillTable;
import com.kargames.bbq.widget.PercentSlider;


public class OptionsScreen extends BaseScreen {
	
	private DisplayMode[] modes;
	
	private CheckBox fullScreen;
	
	private PercentSlider musicSlider;
	private PercentSlider sfxSlider;
	

	public OptionsScreen(final App app) {
		super(app);
		modes = Gdx.graphics.getDisplayModes();
		
		Array<Resolution> resolutions = new Array<Resolution>();
		for (DisplayMode mode : modes) {
			Resolution res = new Resolution(mode);
			if (!resolutions.contains(res, false)) {
				resolutions.add(res);
			}
		}
		
		resolutions.sort();
		final SelectBox<Resolution> resSelector = new SelectBox<Resolution>(app.skin);
		resSelector.setItems(resolutions);
		
		TextButton apply = new TextButton("apply", app.skin);
		
		apply.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (Gdx.graphics.supportsDisplayModeChange()) {
					Resolution res = resSelector.getSelected();
					Gdx.graphics.setDisplayMode(res.width, res.height, fullScreen.isChecked());
				}
			}
		});
		
		fullScreen = new CheckBox("Full screen", app.skin);
		musicSlider = new PercentSlider(app);
		sfxSlider = new PercentSlider(app);

		FillTable all = new FillTable();
		all.defaults().pad(5).uniformX().expandX();
		all.columnDefaults(0).right();
		all.columnDefaults(1).left();
		all.setSkin(app.skin);
		
		Label title = new Label("Options", app.skin);
		title.setFontScale(2);
		
		all.add(title).colspan(2).center().row();
		all.add("Video:").colspan(2).center().row();
		all.add(fullScreen).colspan(2).center().row();
		all.add("Resolution:");
		all.add(resSelector).row();
		all.add("Audio:").colspan(2).center().row();
		all.add("Music volume:");
		all.add(musicSlider).row();
		all.add("SFX volume:");
		all.add(sfxSlider).row();
		all.add(apply);
		
		all.add(new BackButton(app)).row();
		
		stage.addActor(all);
	}
	
	private class Resolution implements Comparable<Resolution> {
		public final int width;
		public final int height;
		
		public Resolution(DisplayMode mode) {
			width = mode.width;
			height = mode.height;
			
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof Resolution)) {
				return false;
			}
			Resolution other = (Resolution) obj;
			return width == other.width && height == other.height;
		}

		@Override
		public String toString() {
			return width + "x" + height;
		}

		@Override
		public int compareTo(Resolution other) {
			int diff = width - other.width;
			if (diff == 0) {
				diff = height - other.height;
			}
			return diff;
		}
	}
	
}
