package com.mygdx.game.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class SpotLight extends PointLight{

	Vector3 lightDirection = new Vector3(1, 0, 0);
	
	public SpotLight(){}
	
	public SpotLight(Vector3 position, Vector3 lightDirection, Color color){
		super(position, color);
		this.lightDirection = lightDirection;
	}
}
