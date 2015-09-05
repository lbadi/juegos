package com.mygdx.game.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class SpotLight extends PointLight{

	Vector3 lightDirection = new Vector3(0, -1, 0);
	float inner_cos = 0.90f;
	float outter_cos = 0.50f;
	
	public SpotLight(){
		super();
	}
	
	public SpotLight(Vector3 position, Vector3 lightDirection, Color color
					, float inner_cos, float outter_cos){
		super(position, color);
		this.lightDirection = lightDirection;
		this.outter_cos = outter_cos;
		this.inner_cos = inner_cos;
	}
	
	public void setInner_cos(float inner_cos) {
		this.inner_cos = inner_cos;
	}
	public void setOutter_cos(float outter_cos) {
		this.outter_cos = outter_cos;
	}
	
	public float getInner_cos() {
		return inner_cos;
	}
	public float getOutter_cos() {
		return outter_cos;
	}
	
	public Vector3 getLightDirection() {
		return lightDirection;
	}
}
