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
import com.mygdx.game.controller.SimpleInputController;
import com.mygdx.game.light.DirectionalLight;
import com.mygdx.game.light.Light;
import com.mygdx.game.light.SpotLight;

public class MyGdxGame extends ApplicationAdapter {
	Texture img;
	Texture img2;
	Mesh spaceshipMesh;
	ShaderProgram shaderProgram;
	List<GenericObject> objects = new ArrayList<GenericObject>();
	Environment env ;
	boolean firstTime = true;
	
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
		ModelLoader loader = new ObjLoader();
		ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));
		spaceshipMesh = new Mesh(true,
				data.meshes.get(0).vertices.length,
				data.meshes.get(0).parts[0].indices.length,
				VertexAttribute.Position(),VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
		spaceshipMesh.setVertices(data.meshes.get(0).vertices);
		spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);
		for(int i = 0; i<5; i++){
			objects.add(new GenericObject(new Vector3(i*2- 4, 0, 0),spaceshipMesh,img));
		}
//		/*Definimos la posicion del objeto principal*/
		objects.get(2).setPosition(new Vector3(0,-0.5f,-2));
		objects.get(2).setRotationY((float)Math.PI);
		objects.get(2).setRotationX(-(float)Math.PI/12);
		objects.get(2).setScaleVector(1, 1, 1);
		img2 = new Texture("spaceBIG.png");
		data = loader.loadModelData(Gdx.files.internal("cube.obj"));
		Mesh mesh = new Mesh(true,
				data.meshes.get(0).vertices.length,
				data.meshes.get(0).parts[0].indices.length,
				VertexAttribute.Position(),VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
		mesh.setVertices(data.meshes.get(0).vertices);
		mesh.setIndices(data.meshes.get(0).parts[0].indices);
		GenericObject backgroundSpace = new GenericObject(new Vector3(0,-1,0),mesh,img2);
		backgroundSpace.setScaleVector(100f, 0.1f, 100f);
		objects.add(backgroundSpace);
		
//		String vs = Gdx.files.internal("defaultVS.glsl").readString();
//		String fs = Gdx.files.internal("defaultFS.glsl").readString();
//		ShaderProgram shader = new ShaderProgram(vs, fs);
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LESS);


        Cam cam = new PerspectiveCam();
        cam.setPosition(new Vector3(0,0,5));
        objects.get(2).setFather(cam);
        //TODO Arreglar lo de padre e hijo y completar los casos que faltan.
        objects.get(2).setFather(cam);
		env = Environment.getInstance();
//        env.addLight("directional", new DirectionalLight(new Vector3(1,1,0), new Color(1, 1, 1, 1)));
//        env.addLight("point", new PointLight(new Vector3(1.5f,0,0), new Color(1, 1, 1, 1)));
        env.addLight("spot", new SpotLight());
        env.addLight("spot2", new SpotLight(new Vector3(3, 2, 0),new Vector3(0, -1, 0),new Color(1,1,1,1)));
        env.addCam("camera", cam);
		Gdx.input.setInputProcessor(new SimpleInputController());
        env.setDefaultLight("spot2");
    }
//GL
    @Override
	public void render() {
    	firstTime = true;
		Gdx.gl.glClearColor(0, 0, 0, 1);
		//Si hago un fondo blanco, cuando hago blend se rompe todo
//		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//Aca activo el blending para hacer muchas luces
		
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
		for(Light light: env.getLights()){
			if(firstTime == false){
				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_ONE,GL20.GL_ONE);
			}
			light.render(objects);
			firstTime = false;
        }
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
		env.getCurrentCam().move();
		
		//UI
//		batch.begin();
//        stage.draw();
//        batch.end();
	}

}
