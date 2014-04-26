package com.kargames.souls.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kargames.souls.App;
import com.kargames.souls.widget.DebugInfo;
import com.kargames.souls.widget.SensorInfo;

public class BaseScreen implements Screen {
	Stage stage;
	
	public BaseScreen prevScreen;
	
	float fadeTime = 0.2f;
	
	public static boolean showDebugTable = false;
	
	protected final App app;
	protected static DebugInfo debugInfo;
	protected static SensorInfo sensorInfo;

	
	public BaseScreen(final App app) {
		this.app = app;
		stage = new Stage();// {
		
		stage.addListener(new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.F12) {
					showDebugTable = !showDebugTable;
				} else if (keycode == Keys.F11) {
					debugInfo.setVisible(!debugInfo.isVisible());
				} else if (keycode == Keys.F10) {
					sensorInfo.setVisible(!sensorInfo.isVisible());
				} else if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					app.screens.back();
					return true;
				}
				return false;
			}
		});
		

		if (debugInfo == null) {
			debugInfo = new DebugInfo(this);
		}
		if (sensorInfo == null) {
			sensorInfo = new SensorInfo(app);
		}
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		stage.addActor(debugInfo);
		stage.addActor(sensorInfo);
	}
	
	public void update(float delta) {
		stage.act(delta);
	}
	
	public void draw() {
		stage.draw();
	}
	
	@Override
	public void render(float delta) {
		debugInfo.startPhysics();
		update(delta);
		debugInfo.stopPhysics();
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		debugInfo.startRender();
		draw();
		debugInfo.stopRender();
		 
		if (showDebugTable) {
			Table.drawDebug(stage);
		}
		
		debugInfo.update(delta);
	}
	
	public Stage getStage() {
		return stage;
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}


	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
	}


	public int getRenderCalls() {
		SpriteBatch batch = (SpriteBatch)stage.getSpriteBatch();
		int numCalls = batch.totalRenderCalls;
		batch.totalRenderCalls = 0;
		return numCalls;
	}

}
