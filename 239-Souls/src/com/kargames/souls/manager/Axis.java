package com.kargames.souls.manager;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;

public class Axis {
	
	public static float getHorizontal() {
		return getVal(Control.LEFT, Control.RIGHT, 1, false);
	}
	public static float getVertical() {
		return getVal(Control.DOWN, Control.UP, 0, true);
	}
	
	public static float getVal(Control neg, Control pos, int axis, boolean flip) {
		float val = 0;
		if (neg.isPressed()) {
			val--;
		}
		if (pos.isPressed()) {
			val++;
		}
		
		val += getControllerValue(axis, flip);
		if (val > 1) val = 1;
		if (val < -1) val = -1;
		
		return val;
	}
	
	public static float getControllerValue(int axis, boolean flip) {
		float val = 0;
		Array<Controller> controllers = Controllers.getControllers();
		if (controllers.size > 0) {
			Controller c = controllers.get(0);
			float x = c.getAxis(axis);
			if (Math.abs(x) > 0.15f) {
				val += x*1.5f;
				if (flip) {
					val = -val;
				}
			}
		}
		if (val > 1) val = 1;
		if (val < -1) val = -1;
		return val;
	}
}
