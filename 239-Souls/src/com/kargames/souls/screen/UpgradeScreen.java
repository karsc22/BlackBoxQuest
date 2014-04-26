package com.kargames.souls.screen;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kargames.souls.App;
import com.kargames.souls.Upgrade;

public class UpgradeScreen extends Table{

	Label partsLabel;
	App app;
	public UpgradeScreen(App app) {
		super(app.skin);
		this.app = app;
		this.setFillParent(true);
		debug();
		defaults().pad(10);
		add("Upgrades").colspan(2).row();
		
		partsLabel = new Label("Parts: " + 0, app.skin);
		add(partsLabel).colspan(2).row();
		
		for (Upgrade upgrade : Upgrade.values()) {
			add(upgrade.name);
			add(new UpgradeButton(app, upgrade)).width(100).height(100).row();
		}
		setVisible(false);
		
		Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		map.setColor(0, 0, 0, 0.7f);
		map.fill();
		
		Texture t = new Texture(map);

		
		TextureRegionDrawable trd = new TextureRegionDrawable(new TextureRegion(t));
		
		this.setBackground(trd);
	}
	
	@Override
	public void act(float delta) {
		partsLabel.setText("Parts: " + app.screens.gameScreen.numParts);
		super.act(delta);
	}

	public class UpgradeButton extends TextButton {
		
		Upgrade upgrade;
		boolean max = false;
		public UpgradeButton(final App app, final Upgrade upgrade) {
			super("", app.skin);
			this.upgrade = upgrade;
			addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (app.screens.gameScreen.numParts >= upgrade.getCost() && !max) {
						app.screens.gameScreen.numParts -= upgrade.getCost();
						upgrade.increment(app);
					}
				}
			});
		}

		@Override
		public void act(float delta) {
			max = upgrade.level >= Upgrade.MAX_LEVEL;
			if (max){
				setText("lvl: max");
				setDisabled(true);
			} else {
				setText("lvl: " + upgrade.level + "\ncost:" + upgrade.getCost());
				setDisabled(app.screens.gameScreen.numParts < upgrade.getCost());
			}
			super.act(delta); 
		}
		
	}
}
