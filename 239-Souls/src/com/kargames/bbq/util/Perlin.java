package com.kargames.bbq.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class Perlin {
	public static float[][] generateNoise(int size) {
		float[][] perlin = new float[size][size];
		
		int pixels = 2;
		for (int i = 0; pixels < size; i++) {
			float[][] temp = Perlin.generateRandom(size, i, 0, 0);
			
			for (int y = 0; y < temp.length; y++) {
				for (int x = 0; x < temp[y].length; x++) {
					perlin[x][y] += temp[x][y] / pixels;
				}
			}
			pixels *= 2;
		}
		
		return perlin;
	}
	
	public static float[][] generateRandom(int size, int octave, int offsetx, int offsety) {
		float[][] result = new float[size][size];

		
		int pixels = 4;
		for (int i = 0; i < octave; i++) {
			pixels *= 2;
		}
		
		int pixelSize = size / pixels;
		
		float[][] pixelArray = new float[pixels+1][pixels+1];
		float[][] temp = new float[size+1][pixels+1];

		for (int x = 0; x <= pixels; x++) {
			for (int y = 0; y <= pixels; y++) {
//				pixelArray[x][y] = MathUtils.random();
				pixelArray[x][y] = rand(x + offsetx*pixels, y + offsety*pixels);
			}
		}
		
		for (int y = 0; y <= pixels; y++) {
			for (int x = 0; x < size; x++) {
				temp[x][y] = interpolate(pixelArray[x / pixelSize][y], pixelArray[x / pixelSize + 1][y], (x / (float) pixelSize) % 1);
			}
		}
		

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				result[x][y] = interpolate(temp[x][y/pixelSize], temp[x][y/pixelSize+1], (y / (float)pixelSize) % 1);
			}
		}
		
		return result;
	}
	
	public static float rand(int x, int y) {
    	int n = x + y * 57;
    	n = (n<<13) ^ n;
    	double d = 1.0 - ( (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0;
    	return (float) (d + 1) / 2f;
	}
	

	public static float interpolate(float a, float b, float alpha) {
		return a * (1 - alpha) + b * alpha;
	}
	
	
	public static Pixmap getMap(float[][] data) {
		Pixmap map = new Pixmap(data.length, data.length, Format.RGBA8888);
		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[y].length; x++) {
				float c = data[x][y];
				if (c > 0.7f) {
					map.setColor(1,1, 1, 1);
				} else if (c > 0.42f){
					map.setColor(c - 0.42f, 1.2f - c, c/5, 1);
				} else if (c > 0.4f) {
					map.setColor(1, 1, c/2, 1);
				} else {
					map.setColor(c/2, c/2, 1, 1);
				}
				
				map.drawPixel(x, y);
			}
		}
		return map;
	}
}
