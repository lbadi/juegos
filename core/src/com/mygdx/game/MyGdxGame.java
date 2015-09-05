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
import com.mygdx.game.light.SpotLight;

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
//		String fs = Gdx.files.internal("defaultFS.glsl").readString();
//		String pointLightFs = Gdx.files.internal("pointLightFS.glsl").readString();
		String spotLightFs = Gdx.files.internal("spotLightFS.glsl").readString();
		shaderProgram = new ShaderProgram(vs, spotLightFs);
//		shaderProgram = new ShaderProgram(vs, pointLightFs);
//		shaderProgram = new ShaderProgram(vs, fs);
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
		
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3(1,1,0), new Color(1, 1, 1, 1));
//		env = new Enviroment(cam, directionalLight);
//		PointLight pointLight = new PointLight(new Vector3(1.5f,0,0), new Color(1, 1, 1, 1));
//		env = new Enviroment(cam, pointLight);
		//TODO hacer que los objetos se puedan attachear para que por ejemplo la luz siga a la camara.
		SpotLight spotLight = new SpotLight();
		env = new Enviroment(cam, spotLight);
		Gdx.input.setInputProcessor(new SimpleInputController(env));
    }
//GL
    @Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		img.bind();
//		setShader((DirectionalLight)env.getLight(), new Color(1, 1, 1, 1), env.getCam(), objects);
//		setShader((PointLight)env.getLight(), new Color(1, 1, 1, 1), env.getCam(), objects);
		setShader((SpotLight)env.getLight(), new Color(1, 1, 1, 1), env.getCam(), objects);
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
		//TODO Blending y merge, DepthTest
		
		//Para shadow Mapping:
		//DepthBuffer sirva para rendir target final y para textura.

		for(GenericObject o : objects){
			shaderProgram.begin();
			shaderProgram.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(o.getTRS())); //aca trabajar
			shaderProgram.setUniformMatrix("u_worldMatrix", o.getTRS()); //aca trabajar

			shaderProgram.setUniformi("u_texture", 0);
//			shaderProgram.setUniform4fv("u_specular_material", 0);
			shaderProgram.setUniform4fv("light_color", new float[]{lightColor.r,lightColor.g,lightColor.b,lightColor.a}, 0, 4);
			shaderProgram.setUniform4fv("light_vector", new float[]{lightVector.x,lightVector.y,lightVector.z,1}, 0, 4);
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
			shaderProgram.setUniform4fv("light_position", new float[]{0,1,0,1}, 0, 4);
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
    
    private void setShader(SpotLight spotLight,
    		Color ambientColor, Cam cam, List<GenericObject> objects){
    	Vector3 position = cam.getPosition();
		Color lightColor = spotLight.getLightColor();
		Color specularColor = spotLight.getSpecularColor();
		Vector3 lightPosition = spotLight.getPosition();
		Vector3 lightDirection = spotLight.getLightDirection();
		float cosine_inner = spotLight.getInner_cos();
		float cosine_outter = spotLight.getOutter_cos();
		
		for(GenericObject o : objects){
			shaderProgram.begin();
			shaderProgram.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(o.getTRS())); //aca trabajar
			shaderProgram.setUniformMatrix("u_worldMatrix", o.getTRS()); //aca trabajar
			shaderProgram.setUniformi("u_texture", 0);
			shaderProgram.setUniform4fv("light_color", new float[]{lightColor.r,lightColor.g,lightColor.b,lightColor.a}, 0, 4);
//			shaderProgram.setUniform4fv("light_vector", new float[]{lightVector.x,lightVector.y,lightVector.z,1}, 0, 4);
			shaderProgram.setUniform4fv("light_position", new float[]{lightPosition.x,lightPosition.y,lightPosition.z,1}, 0, 4);
			//Especular
			shaderProgram.setUniform4fv("eye", new float[]{position.x,position.y,position.z,1}, 0, 4);
			shaderProgram.setUniform4fv("specular_color", new float[]{specularColor.r,specularColor.g,specularColor.b,1}, 0, 4);
//			shaderProgram.setUniform4fv("specular_color", new float[]{1,1,1,1}, 0, 4);
			//Ambiente
			shaderProgram.setUniform4fv("ambient_color", new float[]{0,0,1,1}, 0, 4);
			
			shaderProgram.setUniform4fv("light_direction", new float[]{lightDirection.x,lightDirection.y,lightDirection.z,1}, 0, 4); 
			shaderProgram.setUniformf("cosine_inner", cosine_inner);
			shaderProgram.setUniformf("cosine_outter", cosine_outter);
	        o.getMesh().render(shaderProgram, GL20.GL_TRIANGLES);
	        
	        System.out.println("Outter : " + spotLight.getOutter_cos() + " Inner: " + spotLight.getInner_cos());
			shaderProgram.end();
		}
    }

}
