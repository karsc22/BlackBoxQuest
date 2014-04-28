package com.kargames.bbq.manager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.kargames.bbq.App;
import com.kargames.bbq.screen.BaseScreen;
import com.kargames.bbq.screen.ControlScreen;
import com.kargames.bbq.screen.GameScreen;
import com.kargames.bbq.screen.MenuScreen;
import com.kargames.bbq.screen.OptionsScreen;
import com.kargames.bbq.screen.SplashScreen;

public class ScreenManager {
	public final App app;
	public float FADE_TIME = 0.15f;
	public GameScreen gameScreen;
	public MenuScreen menuScreen;
	public OptionsScreen optionsScreen;
	public SplashScreen splashScreen;
	public ControlScreen controlScreen;
	
	public TransitionType transitionType;
	public BaseScreen creditsScreen;
	
	public enum TransitionType {
		NONE,
		FADE,
		SLIDE
	}
	
	public ScreenManager(final App app) {
		this.app = app;
	}
	
	public void init() {
		splashScreen = new SplashScreen(app);
		gameScreen = new GameScreen(app);
		optionsScreen = new OptionsScreen(app);
		controlScreen = new ControlScreen(app);
		menuScreen = new MenuScreen(app);
		transitionType = TransitionType.FADE;
	}
	
	public BaseScreen getScreen() {
		return (BaseScreen) app.getScreen();
	}

	public void back() { 
		if (getScreen().prevScreen == null) {
			// TODO: could ask confirmation here
			Gdx.app.exit();
		} else {
			setScreen(getScreen().prevScreen, false);
		}
	}

	public void setNextScreen(final BaseScreen screen) {
		screen.prevScreen = getScreen();
		setScreen(screen, true);
	}

	public void jumpToScreen(final BaseScreen screen) {
		setScreen(screen, true);
	}

	private void setScreen(final BaseScreen screen, final boolean fromLeft) {
		if (transitionType == TransitionType.NONE) {
			app.setScreen(screen);
		} else if (transitionType == TransitionType.FADE) {
			getScreen().getStage().addAction(sequence(fadeOut(FADE_TIME),
				new RunnableAction() {
					public void run() {
						screen.getStage().getRoot().setColor(1, 1, 1, 0);
						screen.getStage().addAction(fadeIn(FADE_TIME));
						app.setScreen(screen);
					}
				}));
		} else if (transitionType == TransitionType.SLIDE) {
			float x = 1000;
			if (fromLeft) {
				x = -x;
			}
			getScreen().getStage().getRoot().addAction(sequence(moveTo(
					x, 0, FADE_TIME, Interpolation.exp5In), new RunnableAction() {
				public void run() {
					Actor root = screen.getStage().getRoot();
					if (fromLeft) {
						root.setX(1000);
					} else {
						root.setX(-1000);
					}
					screen.getStage().addAction(moveTo(0, 0, FADE_TIME, Interpolation.exp5Out));
					app.setScreen(screen);
				}
			}));
		}
	}
}