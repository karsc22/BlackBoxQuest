package com.kargames.bbq;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;

public class Renderer {
	
	static Matrix4 mat = new Matrix4();
	static ShapeRenderer sr = new ShapeRenderer();
	static Color topColor = new Color(0, 0.5f, 1f, 1f);
	static Color botColor = new Color(0, 0, 1f, 1f);
	public static void renderBackground(float depth) {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		mat.setToOrtho2D(0, 0, w, h);
		sr.setProjectionMatrix(mat);
		sr.begin(ShapeType.Filled);
		if (depth > 1) depth = 1;
		float blue = 1;
		botColor.set(0, 0, blue, 1);
		topColor.set(0, blue/2f, blue, 1);
		sr.rect(0, 0, w, h, botColor, botColor, topColor, topColor);
		sr.end();
	}
}
