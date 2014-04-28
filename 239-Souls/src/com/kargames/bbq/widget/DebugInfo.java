package com.kargames.bbq.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.kargames.bbq.screen.BaseScreen;
import com.kargames.bbq.util.U;

public class DebugInfo extends Table{

	private Label fpsLabel;
	private Label memoryLabel;
	private Label screenLabel;
	private Label renderTimeLabel;
	private Label physicsTimeLabel;
	private Label loadLabel;
	
	private float debugTime;
	
	private BaseScreen screen;

	private PerformanceCounter physicsPc;
	private PerformanceCounter renderPc;
	
	public DebugInfo(BaseScreen screen) {
		this.screen = screen;
		setFillParent(true);
		left().top();
		defaults().left();
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = new BitmapFont();

		fpsLabel = new Label("", labelStyle);
		memoryLabel = new Label("", labelStyle);
		screenLabel = new Label("", labelStyle);
		renderTimeLabel = new Label("", labelStyle);
		physicsTimeLabel = new Label("", labelStyle);
		loadLabel = new Label("", labelStyle);

		add(fpsLabel).row();
		add(memoryLabel).row();
		add(screenLabel).row();
		add(renderTimeLabel).row();
		add(physicsTimeLabel).row();
		add(loadLabel).row();


		physicsPc = new PerformanceCounter("physics");
		renderPc = new PerformanceCounter("render");

	}
	
	public void update(float delta) {
		debugTime -= delta;
		if (debugTime <= 0) {
			Graphics gr = Gdx.graphics;
			int fps = gr.getFramesPerSecond();
			fpsLabel.setText("FPS: " + fps);
			long total = Runtime.getRuntime().totalMemory();
			long used = total - Runtime.getRuntime().freeMemory();
			memoryLabel.setText("Mem: " + U.shortenMem(used) + " / " + U.shortenMem(total));
			screenLabel.setText("Screen: " + gr.getWidth() + "x" + 
					gr.getHeight() + ", " + gr.getDensity()*160 + " DPI");
			
			if (fps == 0) fps = 1;
			renderTimeLabel.setText("Render: " + U.shortenTime(renderPc.time.average)
					+ " (" + screen.getRenderCalls() / fps + " binds)");
			
			physicsTimeLabel.setText("Physics: " + U.shortenTime(physicsPc.time.average));
			
			
			renderPc.reset();
			physicsPc.reset();
			
			debugTime = 1;
		}
	}

	public void stopPhysics() {
		physicsPc.stop();
		physicsPc.tick();
	}

	public void startPhysics() {
		physicsPc.start();
	}

	public void startRender() {
		renderPc.start();
		
	}

	public void stopRender() {
		renderPc.stop();
		renderPc.tick();
	}
}
