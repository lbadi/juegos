package com.mygdx.game;

import com.mygdx.game.light.Light;

public class Enviroment {

	private Cam cam;
	private Light light;
	
	public Enviroment(Cam cam, Light light) {
		this.cam = cam;
		this.light = light;
		// TODO Auto-generated constructor stub
	}
	public Cam getCam() {
		return cam;
	}
	public Light getLight() {
		return light;
	}
	public void setCam(Cam cam) {
		this.cam = cam;
	}
	public void setLight(Light light) {
		this.light = light;
	}
}
