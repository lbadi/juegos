package com.mygdx.game.light;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.ShadowMap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.game.Cam;
import com.mygdx.game.GenericObject;


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
    public void render(List<GenericObject> objects){
    	for(GenericObject object : objects){
    		render(object);
    	}
    }
	public abstract void render(GenericObject object);
	public abstract Matrix4 getProjectionMatrix();
}
