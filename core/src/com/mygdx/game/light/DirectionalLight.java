package com.mygdx.game.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class DirectionalLight extends Light{

	Vector3 lightVector = new Vector3(0,0,0);
	
	public DirectionalLight() {	}
	
	public DirectionalLight(Vector3 lightVector, Color lightColor) {
		this.lightVector = new Vector3(lightVector);
		this.lightColor = lightColor;
	}
	
	public Vector3 getLightVector() {
		return new Vector3(lightVector);
	}
	
	public void setLightVector(Vector3 lightVector) {
		this.lightVector = new Vector3(lightVector);
	}
}
