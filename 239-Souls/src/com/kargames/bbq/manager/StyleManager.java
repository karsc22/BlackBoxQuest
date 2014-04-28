package com.kargames.bbq.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.kargames.bbq.App;

public class StyleManager {
	
	public LabelStyle labelStyle;
	public LabelStyle bigLabelStyle;
	public BitmapFont font;
	public BitmapFont bigFont;
	public TextButtonStyle tbs;
	
	public StyleManager(App app) {
		font = new BitmapFont(Gdx.files.internal("skins/Arial-64.fnt"));
		bigFont = new BitmapFont(Gdx.files.internal("skins/Arial-64.fnt"));
		int size = 16;
		NinePatch button = new NinePatch(new Texture(Gdx.files.internal(
				"skins/button.png")), size ,size ,size ,size);
		NinePatch buttonOver = new NinePatch(new Texture(Gdx.files.internal(
				"skins/button-over.png")), size ,size ,size ,size);
		NinePatch buttonDown = new NinePatch(new Texture(Gdx.files.internal(
				"skins/button-down.png")), size ,size ,size ,size);
//		button.setTopHeight(size);
//		button.setBottomHeight(size);
//		button.setLeftWidth(size);
//		button.setRightWidth(size);
		font.setScale(0.5f);
		
		
		labelStyle = new LabelStyle();
		bigLabelStyle = new LabelStyle();
		
		labelStyle.font = font;
		bigLabelStyle.font = bigFont;
		
		tbs = new TextButtonStyle();
		tbs.font = font;

		tbs.up = new NinePatchDrawable(button);
		tbs.down = new NinePatchDrawable(buttonDown);
		tbs.over = new NinePatchDrawable(buttonOver);
		
	}
}
