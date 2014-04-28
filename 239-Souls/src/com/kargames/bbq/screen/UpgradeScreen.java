package com.kargames.bbq.screen;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.tablelayout.Value;
import com.kargames.bbq.App;
import com.kargames.bbq.Upgrade;

public class UpgradeScreen extends Table{

//	Label partsLabel;
	App app;
	public UpgradeScreen(App app) {
		super(app.skin);
		this.app = app;
		setFillParent(true);
		debug();
		bottom();
		defaults().pad(10).padBottom(Value.percentHeight(0.15f));
		add(new Label("Upgrades", app.styles.labelStyle)).colspan(3).padBottom(10).row();
		
		for (Upgrade upgrade : Upgrade.values()) {
			add(new UpgradeButton(app, upgrade));
		}
		setVisible(false);
		
		Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		map.setColor(0, 0, 0, 0.7f);
		map.fill();
		
		Texture t = new Texture(map);

		
		TextureRegionDrawable trd = new TextureRegionDrawable(new TextureRegion(t));
		
//		this.setBackground(trd);
	}
	
	@Override
	public void act(float delta) {
//		partsLabel.setText("Parts: " + app.screens.gameScreen.numParts);
		super.act(delta);
	}
	
	public void upgrade(Upgrade upgrade) {
		if (app.screens.gameScreen.money >= upgrade.getCost() && 
				(upgrade.level < Upgrade.MAX_LEVEL)) {
			app.screens.gameScreen.money -= upgrade.getCost();
			upgrade.increment(app);
		}
	}

	public class UpgradeButton extends TextButton {
		
		Upgrade upgrade;
		public UpgradeButton(final App app, final Upgrade upgrade) {
			super("", app.styles.tbs);
			this.upgrade = upgrade;
			addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					upgrade(upgrade);
				}
			});
		}

		@Override
		public void act(float delta) {
			if (upgrade.level >= Upgrade.MAX_LEVEL){
				setText(upgrade.name + "\n" + (int)upgrade.amounts[upgrade.level] + "\nMaxed Out");
				setDisabled(true);
			} else {
				setText(upgrade.name + "\n" + (int)upgrade.amounts[upgrade.level] +
						" -> " + (int)upgrade.amounts[upgrade.level+1] +"\ncost: $" 
						+ upgrade.getCost());
				setDisabled(app.screens.gameScreen.money < upgrade.getCost());
			}
			super.act(delta); 
		}
		
	}
}
