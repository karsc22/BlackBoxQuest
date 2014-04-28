package com.kargames.bbq.util;

public class U {
	public static double diffAngles(double angle1, double angle2) {
		double theta = Math.abs(angle1 - angle2);
		if (theta > Math.PI) {
			theta = 2*Math.PI - theta;
		}
		return theta;
	}
	
	public static void p(String text) {
		System.out.println(text);
	}
	
	public static String toTime(int secs) {
		int hr = secs / 3600;
		int min = (secs / 60) % 60;
		int sec = secs % 60;
		if (hr < 1) {
			return String.format("%02d:%02d", min, sec);
		}
		return String.format("%02d:%02d:%02d", hr, min, sec);
	}
	
	public static String shortenMem(long num) {
		return num/1048576 + "." + (num/104857) % 10 + "M";
	}
	
	// time in secs, return time in ms
	public static String shortenTime(float time) {
		int micros = (int) (time * 1000000);
		return micros / 1000 + "." + micros % 1000 + " ms";
	}

	// time in us, return time in ms
	public static String shortenTime(long time) {
		int micros = (int) (time / 1000);
		return micros / 1000 + "." + micros % 1000 + " ms";
	}
	
	public static void printArray(float[][] array) {
		for (int y = 0; y < array[0].length; y++) {
			System.out.print("[");
			for (int x = 0; x < array.length; x++) {
				System.out.print( (int)(array[x][y] * 10) / 10f  + ", "  );
			}
			System.out.println("]");
		}
		System.out.println();
	}
}
