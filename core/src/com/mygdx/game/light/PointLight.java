package com.mygdx.game.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class PointLight extends Light{

	Vector3 position = new Vector3(0,2,0);
	
	public PointLight(){	}
	
	public PointLight(Vector3 position,Color lightColor){
		this.lightColor = lightColor;
		this.position = position;
	}
	
	public Vector3 getPosition() {
		return new Vector3(position);
	}
	public void setPosition(Vector3 position) {
		this.position = new Vector3(position);
	}
}
