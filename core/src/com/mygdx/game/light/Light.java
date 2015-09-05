package com.mygdx.game.light;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.GenericObject;


public abstract class Light {

	Color lightColor = new Color(1,1,1,1);
	Color specularColor = new Color(1,1,1,1);
	
	public Color getLightColor() {
		return new Color(lightColor);
	}
	public void setLightColor(Color lightColor) {
		this.lightColor = new Color(lightColor);
	}
	
	public Color getSpecularColor() {
		return new Color(specularColor);
	}
	
	public void setSpecularColor(Color specularColor) {
		this.specularColor = new Color(specularColor);
	}

	public abstract void render(GenericObject object);
	
}
