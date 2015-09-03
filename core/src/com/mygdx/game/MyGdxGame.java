package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.leapmotion.leap.Controller;
//import com.leapmotion.leap.Frame;
//import com.leapmotion.leap.Hand;
import com.mygdx.game.controller.SimpleInputController;
import com.mygdx.game.light.DirectionalLight;
import com.mygdx.game.light.PointLight;

public class MyGdxGame extends ApplicationAdapter {
	Texture img;
	Mesh spaceshipMesh;
	ShaderProgram shaderProgram;
	List<GenericObject> objects = new ArrayList<GenericObject>();
	Enviroment env ;
	
	//UI
	 private SpriteBatch batch;
	 private Skin skin;
	 private Stage stage;
	@Override
	public void create () {
		
		//UI
//		batch = new SpriteBatch();
//        skin = new Skin(Gdx.files.internal("ui.json"));
//        stage = new Stage();
		
		img = new Texture("ship.png");
		String vs = Gdx.files.internal("defaultVS.glsl").readString();
		String fs = Gdx.files.internal("defaultFS.glsl").readString();
		String pointLightFs = Gdx.files.internal("pointLightFS.glsl").readString();
//		shaderProgram = new ShaderProgram(vs, pointLightFs);
		shaderProgram = new ShaderProgram(vs, fs);
		System.out.println(shaderProgram.getLog());
		ModelLoader loader = new ObjLoader();
		ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));
		spaceshipMesh = new Mesh(true,
				data.meshes.get(0).vertices.length,
				data.meshes.get(0).parts[0].indices.length,
				VertexAttribute.Position(),VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
		spaceshipMesh.setVertices(data.meshes.get(0).vertices);
		spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);
		for(int i = 0; i<5; i++){
			objects.add(new GenericObject(new Vector3(i*2- 4, 0, 0),spaceshipMesh));
		}
		
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);
		
//		cam = new OrthoCam();
		Cam cam = new PerspectiveCam();
		
		DirectionalLight directionalLight = new DirectionalLight(new Vector3(1,1,0), new Color(1, 1, 1, 1));
		env = new Enviroment(cam, directionalLight);
//		PointLight pointLight = new PointLight(new Vector3(1.5f,0,0), new Color(1, 1, 1, 1));
//		env = new Enviroment(cam, pointLight);
		Gdx.input.setInputProcessor(new SimpleInputController(env));
    }

    @Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		img.bind();
		setShader((DirectionalLight)env.getLight(), new Color(1, 1, 1, 1), env.getCam(), objects);
//		setShader((PointLight)env.getLight(), new Color(1, 1, 1, 1), env.getCam(), objects);
		env.getCam().move();
		
		//UI
//		batch.begin();
//        stage.draw();
//        batch.end();
	}
    
    private void setShader(DirectionalLight directionalLight,
    		Color ambientColor, Cam cam, List<GenericObject> objects){
    	Vector3 position = cam.getPosition();
		Color lightColor = directionalLight.getLightColor();
		Color specularColor = directionalLight.getSpecularColor();
		Vector3 lightVector = directionalLight.getLightVector();
		//TODO preguntar para varias luces como hay que hacer
		for(GenericObject o : objects){
			shaderProgram.begin();
			shaderProgram.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(o.getTRS())); //aca trabajar
			shaderProgram.setUniformMatrix("u_worldMatrix", o.getTRS()); //aca trabajar

			shaderProgram.setUniformi("u_texture", 0);
			shaderProgram.setUniform4fv("light_color", new float[]{lightColor.r,lightColor.g,lightColor.b,lightColor.a}, 0, 4);
			shaderProgram.setUniform4fv("light_vector", new float[]{lightVector.x,lightVector.y,lightVector.z,1}, 0, 4);
//			shaderProgram.setUniform4fv("light_position", new float[]{0,2,-1,1}, 0, 4);
			//Especular
			shaderProgram.setUniform4fv("eye", new float[]{position.x,position.y,position.z,1}, 0, 4);
			shaderProgram.setUniform4fv("specular_color", new float[]{specularColor.r,specularColor.g,specularColor.b,1}, 0, 4);
			//Ambiente
			shaderProgram.setUniform4fv("ambient_color", new float[]{0,0,1,1}, 0, 4);
	        o.getMesh().render(shaderProgram, GL20.GL_TRIANGLES);
	        
			shaderProgram.end();
		}
    }
    
    private void setShader(PointLight directionalLight,
    		Color ambientColor, Cam cam, List<GenericObject> objects){
    	Vector3 position = cam.getPosition();
		Color lightColor = directionalLight.getLightColor();
		Color specularColor = directionalLight.getSpecularColor();
		Vector3 lightPosition = directionalLight.getPosition();
		//TODO preguntar para varias luces como hay que hacer
		for(GenericObject o : objects){
			shaderProgram.begin();
			shaderProgram.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(o.getTRS())); //aca trabajar
			shaderProgram.setUniformMatrix("u_worldMatrix", o.getTRS()); //aca trabajar
			shaderProgram.setUniformi("u_texture", 0);
			shaderProgram.setUniform4fv("light_color", new float[]{lightColor.r,lightColor.g,lightColor.b,lightColor.a}, 0, 4);
//			shaderProgram.setUniform4fv("light_vector", new float[]{lightVector.x,lightVector.y,lightVector.z,1}, 0, 4);
			shaderProgram.setUniform4fv("light_position", new float[]{1,0,0,1}, 0, 4);
			//Especular
			shaderProgram.setUniform4fv("eye", new float[]{position.x,position.y,position.z,1}, 0, 4);
			shaderProgram.setUniform4fv("specular_color", new float[]{specularColor.r,specularColor.g,specularColor.b,1}, 0, 4);
//			shaderProgram.setUniform4fv("specular_color", new float[]{1,1,1,1}, 0, 4);
			//Ambiente
			shaderProgram.setUniform4fv("ambient_color", new float[]{0,0,1,1}, 0, 4);
	        o.getMesh().render(shaderProgram, GL20.GL_TRIANGLES);
	        
			shaderProgram.end();
		}
    }

}
