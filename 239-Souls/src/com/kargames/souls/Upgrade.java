package com.kargames.souls;

public enum Upgrade {
	SPEED("Speed", 10, 15, 25, 40),	
	CARGO("Cargo", 10, 25, 50, 100), 
	BATTERY("Battery", 15, 30, 60, 120), 
	LIGHT("Light", 10, 20, 30, 50); 

	public int level;
	public static final int MAX_LEVEL = 3;
	public float[] amounts;
	public String name;
	
	private Upgrade(String name, float u0, float u1, float u2, float u3) {
		this.name = name;
		amounts = new float[]{u0, u1, u2, u3};
	}
	
	public int getCost() {
		return level*level*10 + 10;
	}
	
	public void increment(App app) {
		if (level < MAX_LEVEL) level++;
		if (this == SPEED) {
			app.screens.gameScreen.player.speed = amounts[level]*10;
		} else if (this == CARGO) {
			app.screens.gameScreen.player.cargo.max = amounts[level];
		} else if (this == BATTERY) {
			app.screens.gameScreen.player.energy.max = amounts[level];
		} else if (this == LIGHT) {
			app.screens.gameScreen.player.light = amounts[level];
		}
	}
}
	
