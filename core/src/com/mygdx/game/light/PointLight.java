package com.mygdx.game.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.environment.ShadowMap;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Cam;
import com.mygdx.game.Scene;
import com.mygdx.game.GenericObject;

public class PointLight extends Light{

	ShaderProgram shader;
	ShadowMap shadowMap = new ShadowMapImpl();
	public PointLight(){		}
	
	public PointLight(Vector3 position,Color lightColor){
		this.lightColor = lightColor;
		setPosition(position);
		String vs = Gdx.files.internal("defaultVS.glsl").readString();
		String fs = Gdx.files.internal("pointLightFS.glsl").readString();
		shader = new ShaderProgram(vs, fs);
        System.out.println(shader.getLog());
	}

	public void render(GenericObject object) {

		object.getImg().bind();
        Scene scene = Scene.getCurrentScene();
		Cam cam = scene.getCurrentCam();
		Vector3 position = cam.getPosition();
		//TODO preguntar para varias luces como hay que hacer
		shader.begin();
		shader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
		shader.setUniformMatrix("u_worldMatrix", object.getTRS()); //aca trabajar
		shader.setUniformi("u_texture", 0);
		shader.setUniform4fv("light_color", new float[]{lightColor.r,lightColor.g,lightColor.b,lightColor.a}, 0, 4);
		shader.setUniform4fv("light_position", new float[]{0,1,0,1}, 0, 4);
		//Especular
		shader.setUniform4fv("eye", new float[]{position.x,position.y,position.z,1}, 0, 4);
		shader.setUniform4fv("specular_color", new float[]{specularColor.r,specularColor.g,specularColor.b,1}, 0, 4);
//			shaderProgram.setUniform4fv("specular_color", new float[]{1,1,1,1}, 0, 4);
		//Ambiente
		shader.setUniform4fv("ambient_color", new float[]{0,0,1,1}, 0, 4);
		object.getMesh().render(shader, GL20.GL_TRIANGLES);

		shader.end();
	}


	@Override
	public ShadowMap getShadowMap() {
		return shadowMap;
	}

	@Override
	public void render(Scene scene) {

	}

	@Override
	public Matrix4 getProjectionMatrix() {
		// TODO Auto-generated method stub
		return null;
	}
}
