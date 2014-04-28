package com.kargames.bbq;

public enum Upgrade {
	SPEED("Speed (X)", 10, 15, 25, 40),	
	BATTERY("Battery (B)", 20, 30, 45, 60), 
	LIGHT("Light (Y)", 10, 20, 30, 50); 

	public int level;
	public static final int MAX_LEVEL = 3;
	public float[] amounts;
	public String name;
	
	private Upgrade(String name, float u0, float u1, float u2, float u3) {
		this.name = name;
		amounts = new float[]{u0, u1, u2, u3};
	}
	
	public int getCost() {
		return level*level*5 + 5;
	}
	
	public void increment(App app) {
		if (level < MAX_LEVEL) level++;
		if (this == SPEED) {
			app.screens.gameScreen.player.speed = amounts[level]*10;
		} else if (this == BATTERY) {
			app.screens.gameScreen.player.energy.max = amounts[level];
		} else if (this == LIGHT) {
			app.screens.gameScreen.player.light = amounts[level];
		}
	}
}
	
