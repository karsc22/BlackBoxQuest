package com.kargames.bbq.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;

public enum Control {
	UP("Move up", Keys.UP, Keys.W),
	DOWN("Move down", Keys.DOWN, Keys.S),
	LEFT("Move left", Keys.LEFT, Keys.A),
	RIGHT("Move right", Keys.RIGHT, Keys.D);


	   public static final int BUTTON_X = 2;
	   public static final int BUTTON_Y = 3;
	   public static final int BUTTON_A = 0;
	   public static final int BUTTON_B = 1;
	public int key0;
	public int key1;

	private int defaultKey0;
	private int defaultKey1;
	
	private String name;
	
	private Control(String name, int key0, int key1) {
		this.name = name;
		this.key0 = key0;
		this.key1 = key1;
		defaultKey0 = key0;
		defaultKey1 = key1;
	}
	
	public boolean isPressed() {
		return Gdx.input.isKeyPressed(key0) || Gdx.input.isKeyPressed(key1);
	}
	
	public void restoreDefaults() {
		key0 = defaultKey0;
		key1 = defaultKey1;
	}
	
	public String getString(int num) {
		return Input.Keys.toString(num == 0 ? key0 : key1);
	}
	
	public String getName() {
		return name;
	}
}
