package com.kargames.bbq.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kargames.bbq.App;
import com.kargames.bbq.Renderer;
import com.kargames.bbq.manager.Control;
import com.kargames.bbq.widget.BackButton;
import com.kargames.bbq.widget.FillTable;

public class ControlScreen extends BaseScreen{
	Label desiredKeyLabel;
	Label warningLabel;
	private ControlLabel currentLabel;
	public ControlScreen(App app) {
		super(app);
		FillTable ft = new FillTable();
		ft.defaults().pad(5);
		final Table top = new Table();
		final Table mid = new Table();
		mid.setSkin(app.skin);
		mid.defaults().pad(5);
		
		desiredKeyLabel = new Label("Press the desired key", app.skin);
		warningLabel = new Label("", app.skin);
		warningLabel.setColor(Color.RED);
		top.add(new Label("Controls", app.styles.bigLabelStyle)).row();
		top.add(new Label("Works with XBOX controller", app.styles.labelStyle)).row();
		top.add(new Label("Zoom: scroll or right stick", app.styles.labelStyle)).row();
		top.add(desiredKeyLabel).row();
		top.add(warningLabel).row();
		desiredKeyLabel.setVisible(false);
		mid.add("Action");
		mid.add("Button 1");
		mid.add("Button 2").row();
		
		for (Control control : Control.values()) {
			mid.add(control.getName());
			mid.add(new ControlLabel(control, 0));
			mid.add(new ControlLabel(control, 1)).row();
		}
		
		stage.addListener(new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (desiredKeyLabel.isVisible()) {
					setKey(keycode);
				}
				return false;
			}
		});
		
		Table bottom = new Table();
		bottom.defaults().pad(5);
		TextButton restoreDefaults = new TextButton("Restore\nDefaults", app.styles.tbs);
		restoreDefaults.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				for (Control control : Control.values()) {
					control.restoreDefaults();
				}
				for (Actor actor : mid.getChildren()) {
					if (actor instanceof ControlLabel) {
						((ControlLabel)actor).updateText();
					}
				}
			}
		});
		
		bottom.add(restoreDefaults);
		
		bottom.add(new BackButton(app)).colspan(3);
		ft.add(top).row();
		ft.add(mid).row();
		ft.add(bottom).row();
		stage.addActor(ft);
	}
	
	@Override
	public void draw() {
		Renderer.renderBackground(0);
		super.draw();
	}

	private void setKey(int keycode) {
		warningLabel.setVisible(false);
		for (Control control : Control.values()) {
			if (keycode == control.key0 || keycode == control.key1) {
				if (!currentLabel.control.equals(control)) {
					warningLabel.setText("That key is already used by \'"
						+ control.getName() +"\'.  Please choose another");
					warningLabel.setVisible(true);
				}
			}
		}
		if (!warningLabel.isVisible()) {
			desiredKeyLabel.setVisible(false);
			if (currentLabel.num == 0) {
				currentLabel.control.key0 = keycode;
			} else {
				currentLabel.control.key1 = keycode;
			}
			currentLabel.setText(Input.Keys.toString(keycode));
			currentLabel.setColor(Color.WHITE);
		}
	}
	
	private class ControlLabel extends Label {
		private Control control;
		private int num;
		public ControlLabel(Control control, int num) {
			super(control.getString(num), app.skin);
			this.control = control;
			this.num = num;
			
			addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					desiredKeyLabel.setVisible(true);
					
					currentLabel = ControlLabel.this;
					currentLabel.setColor(Color.RED);
				}
			});
		}
		
		public void updateText() {
			if (num == 0) {
				setText(Input.Keys.toString(control.key0));
			} else {
				setText(Input.Keys.toString(control.key1));
			}
		}
	}
}
