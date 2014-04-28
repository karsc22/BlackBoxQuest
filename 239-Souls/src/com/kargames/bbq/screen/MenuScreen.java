package com.kargames.bbq.screen;

import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.Value;
import com.kargames.bbq.App;
import com.kargames.bbq.Renderer;
import com.kargames.bbq.Upgrade;
import com.kargames.bbq.actor.Bubble;
import com.kargames.bbq.manager.Control;
import com.kargames.bbq.widget.BackButton;
import com.kargames.bbq.widget.FillTable;
import com.kargames.bbq.widget.ScreenButton;

public class MenuScreen extends BaseScreen {

	Stage gameStage;

	int numBubbles = 100;
	Bubble[] bubbles;
	int currentBubble = 0;

	PositionalLight playerLight;
	
	RayHandler rayHandler;
	boolean releasedA;
	
	public MenuScreen(final App app) {
		super(app);
		
		FillTable top = new FillTable();
		top.defaults().pad(10);
		top.top();
		top.add(new Label("Black Box Quest", app.styles.bigLabelStyle)).padTop(
				Value.percentHeight(0.1f)).row();
		top.add(new Label("Ludum Dare 29", app.styles.labelStyle));
		
		FillTable table = new FillTable();
		table.bottom();
		table.defaults().pad(5).width(200).padBottom(Value.percentHeight(0.1f));

		ScreenButton playButton = new ScreenButton(app, "Play (A)", app.screens.gameScreen);
//		ScreenButton optionsButton = new ScreenButton(app, "Options", app.screens.optionsScreen);
		ScreenButton controlsButton = new ScreenButton(app, "Controls", app.screens.controlScreen);
		
		TextButton exitButton = new BackButton(app, "Exit");

		table.add(playButton);
//		table.add(optionsButton);
		table.add(controlsButton);
		table.add(exitButton);

		stage.addActor(table);
		stage.addActor(top);
		
		gameStage = new Stage();
		Image ship = new Image(app.textures.ship);
		ship.setSize(3, 3);
		ship.setPosition(-1.5f, -1.5f);
		gameStage.addActor(ship);
		

		bubbles = new Bubble[numBubbles];
		
		for (int i = 0; i < numBubbles; i++) {
			bubbles[i] = new Bubble(app);
			gameStage.addActor(bubbles[i]);
		}
		
		

		World world = new World(new Vector2(0, 0), false);
		rayHandler = new RayHandler(world);
		playerLight = new PointLight(rayHandler, 128);

		playerLight.setSoft(true);
		playerLight.setSoftnessLength(5);
		playerLight.setStaticLight(false);
		
	}
	

	@Override
	public void show() {
		super.show();
		releasedA = true;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		makeBubbles(-3, 0);
		gameStage.act();
		
		rayHandler.setCombinedMatrix(gameStage.getCamera().combined);

		playerLight.setColor(1, 1, 1, 0.8f);
//		playerLight.setColor(1, 1, 1, 0.9f);
		playerLight.setDistance(20);
		rayHandler.update();
		
		
		Array<Controller> controllers = Controllers.getControllers();
		if (controllers.size > 0) {
			Controller c = controllers.get(0);
			if (c.getButton(Control.BUTTON_A) && releasedA) {
				app.screens.setNextScreen(app.screens.gameScreen);
				releasedA = false;
			}
		}
		
		if (Gdx.input.isKeyPressed(Keys.A) && releasedA) {
			app.screens.setNextScreen(app.screens.gameScreen);
			releasedA = false;
		}
		
	}

	@Override
	public void draw() {
		app.textures.ship.getSprite().setFlip(false, false);
		Renderer.renderBackground(0);
		rayHandler.render();
		gameStage.draw();
		super.draw();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float mod = 1f;
		gameStage.setViewport(16*mod, 9*mod, true);
		gameStage.getCamera().position.set(0,0,0);
		gameStage.getCamera().update();
	}
	
	public void makeBubbles(float h, float v) {
		Bubble b = bubbles[currentBubble];
		currentBubble++;
		if (currentBubble >= numBubbles) {
			currentBubble = 0;
		}
		b.init(1f, 0, -h*3, -v*3);
	}

}
