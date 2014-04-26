package com.kargames.souls.util;

public class Timer {
	
	long time;
	
	String name;
	
	public Timer(String name) {
		this.name = name;
		start();
	}
	
	public void start() {
		time = System.nanoTime();
	}
	
	public void print(String text) {
		long now = System.nanoTime();
		U.p(name + " : " + text + " : " + U.shortenTime(now - time));
		time = now;
	}
}
