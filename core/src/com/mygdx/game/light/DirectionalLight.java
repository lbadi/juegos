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
import com.mygdx.game.OrthoCam;

public class DirectionalLight extends Light{

	Vector3 lightVector = new Vector3(0,0,0);
	ShaderProgram shader;
	OrthoCam projectionCamera = new OrthoCam();
	
	public DirectionalLight() {	}
	
	public DirectionalLight(Vector3 lightVector, Color lightColor) {
		this.lightVector = new Vector3(lightVector);
		this.lightColor = lightColor;
		String vs = Gdx.files.internal("defaultVS.glsl").readString();
		String fs = Gdx.files.internal("defaultFS.glsl").readString();
		shader = new ShaderProgram(vs, fs);
		projectionCamera.setFather(this);
		System.out.println(shader.getLog());
	}
	
	public Vector3 getLightVector() {
		return new Vector3(lightVector);
	}
	
	public void setLightVector(Vector3 lightVector) {
		this.lightVector = new Vector3(lightVector);
	}

	@Override
	public void render(GenericObject object) {
		object.getImg().bind();
        Scene scene = Scene.getInstance();
		Cam cam = scene.getCurrentCam();
		Vector3 position = cam.getPosition();
		//TODO preguntar para varias luces como hay que hacer
		//TODO Blending y merge, DepthTest

		//Para shadow Mapping:
		//DepthBuffer sirva para rendir target final y para textura.
		shader.begin();
		shader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
		shader.setUniformMatrix("u_worldMatrix", object.getTRS()); //aca trabajar

		shader.setUniformi("u_texture", 0);
//			shaderProgram.setUniform4fv("u_specular_material", 0);
		shader.setUniform4fv("light_color", new float[]{lightColor.r,lightColor.g,lightColor.b,lightColor.a}, 0, 4);
		shader.setUniform4fv("light_vector", new float[]{lightVector.x,lightVector.y,lightVector.z,1}, 0, 4);
		//Especular
		shader.setUniform4fv("eye", new float[]{position.x,position.y,position.z,1}, 0, 4);
		shader.setUniform4fv("specular_color", new float[]{specularColor.r,specularColor.g,specularColor.b,1}, 0, 4);
		//Ambiente
		shader.setUniform4fv("ambient_color", new float[]{0,0,1,1}, 0, 4);
		object.getMesh().render(shader, GL20.GL_TRIANGLES);

		shader.end();
	}

	@Override
	public ShadowMap getShadowMap() {
		// TODO Auto-generated method stub
		return null;
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
