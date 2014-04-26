package com.kargames.souls.util;

import java.io.Serializable;

public class Resource implements Serializable {
	private static final long serialVersionUID = -1585760519052450416L;
	public float value;
	public float max;
	public float regen;

	public Resource(float max) {
		this.max = max;
		this.value = max;
		this.regen = 0;
	}

	public void update(float dt) {
		value += dt * regen;
		clamp();
	}

	public void change(float amount) {
		value += amount;
		clamp();
	}

	public float getRatio() {
		return value / max;
	}

	private void clamp() {
		if (value > max)
			value = max;
		if (value <= 0) {
			value = 0;
		}
	}

	public void setMax() {
		value = max;
	}
	
	public String toString() {
		return (int)value + " / " + (int)max;
	}
}
