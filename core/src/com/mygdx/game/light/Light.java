package com.mygdx.game.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.ShadowMap;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.game.objects.GenericObject;
import com.mygdx.game.objects.Scene;


public abstract class Light extends GenericObject{

	Color lightColor = new Color(1,1,1,1);
	Color specularColor = new Color(1,1,1,1);
	
	public Color getLightColor() {
		return new Color(lightColor);
	}
	
	public abstract ShadowMap getShadowMap();
	
	public void setLightColor(Color lightColor) {
		this.lightColor = new Color(lightColor);
	}
	
	public Color getSpecularColor() {
		return new Color(specularColor);
	}
	
	public void setSpecularColor(Color specularColor) {
		this.specularColor = new Color(specularColor);
	}
	public Matrix4 getBiasMatrix(){
		float[] values = { 
		0.5f, 0.0f, 0.0f, 0.0f,
		0.0f, 0.5f, 0.0f, 0.0f,
		0.0f, 0.0f, 0.5f, 0.0f,
		0.5f, 0.5f, 0.5f, 1.0f
		};
		return new Matrix4(values).tra();
	}
	
    public abstract void render(Scene scene);
	public abstract Matrix4 getProjectionMatrix();
}
