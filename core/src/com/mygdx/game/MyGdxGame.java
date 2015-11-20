package com.mygdx.game;

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
import com.mygdx.game.cam.Cam;
import com.mygdx.game.cam.PerspectiveCam;
import com.mygdx.game.controller.SimpleInputController;
import com.mygdx.game.light.Light;
import com.mygdx.game.light.SpotLight;
import com.mygdx.game.networking.*;
import com.mygdx.game.networking.client.GameClient;
import com.mygdx.game.objects.GenericObject;
import com.mygdx.game.objects.Scene;

import java.io.IOException;

public class MyGdxGame extends ApplicationAdapter {
	Texture img;
	Texture img2;
	Mesh spaceshipMesh;
	private GenericObject mainShip;
	ShaderProgram shaderProgram;
	Scene env ;
	boolean firstTime = true;

	private GameClient client;

	private Scene scene = Scene.newScene("space");
	
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
			scene.addObject(new GenericObject(new Vector3(i * 2 - 4, 0.4f, 0), spaceshipMesh, img));
		}
		//		/*Definimos la posicion del objeto principal*/
		mainShip = new GenericObject(20, new Vector3(0,-1f, -4), spaceshipMesh, img);
		mainShip.setRotationY((float) Math.PI);
		mainShip.setRotationX(-(float) Math.PI / 12);
		mainShip.setScaleVector(1f, 1f, 1f);
		scene.addObject("MainShip", mainShip);

		img2 = new Texture("mars.jpg");
		data = loader.loadModelData(Gdx.files.internal("cube.obj"));
		Mesh mesh = new Mesh(true,
				data.meshes.get(0).vertices.length,
				data.meshes.get(0).parts[0].indices.length,
				VertexAttribute.Position(),VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
		mesh.setVertices(data.meshes.get(0).vertices);
		mesh.setIndices(data.meshes.get(0).parts[0].indices);
		GenericObject backgroundSpace = new GenericObject(new Vector3(0,-3,0),mesh,img2);
		backgroundSpace.setScaleVector(100f, 1f, 100f);
//		backgroundSpace.setRotationX(+(float) Math.PI);
		scene.addObject("SpaceBackground", backgroundSpace);
		
//		String vs = Gdx.files.internal("defaultVS.glsl").readString();
//		String fs = Gdx.files.internal("directional_fs.glsl").readString();
//		ShaderProgram shader = new ShaderProgram(vs, fs);
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LESS);


        Cam cam = new PerspectiveCam();
		cam.setId(0);
        cam.setPosition(new Vector3(0, 5, 4));
		cam.setRotationX(-(float) Math.PI / 4);
        mainShip.setFather(cam);
        //TODO Arreglar lo de padre e hijo y completar los casos que faltan.
		env = Scene.getCurrentScene();
//		env.addLight("directional", new DirectionalLight(new Vector3(0, 15, 0), new Vector3(0, 1, 0), new Color(1, 1, 1, 1)));
//        env.addLight("point", new PointLight(new Vector3(1.5f,0,0), new Color(1, 1, 1, 1)));
//		env.addLight("light", new DirectionalLight(new Vector3(0, 15, 0), new Vector3(0, 1, 0), new Color(1, 1, 1, 1)));
        env.addLight("light", new SpotLight(new Vector3(2, 50, 0), new Vector3((float) (Math.PI * 1.5f), 0, 0), new Color(1, 1, 1, 1)));
		env.addCam("camera", cam);
		currentInputs = new Inputs();
		Gdx.input.setInputProcessor(new SimpleInputController(currentInputs));
        env.setDefaultLight("light");

		env.addLight("mainShipLight", new SpotLight(new Vector3(2, 50, 0), new Vector3((float) (Math.PI * 1.5f), 0, 0), new Color(1, 1, 1, 1)));
		SpotLight l = (SpotLight) env.getLight("mainShipLight");
		l.setInnerAngle(45.4f);
		l.setOutterAngle(48f);

		env.getDefaultLight().setRotationX(-(float) Math.PI / 2);

		try {
			client = new GameClient(2345);
			client.connect(new NetworkAddress("localhost", 1234));
		} catch (IOException e) {
			client = null;
		}
	}
//GL
    @Override
	public void render() {
		sendInputs();
		updateState();
		firstTime = true;
		Gdx.gl.glClearColor(0, 0, 0, 1);
		//Si hago un fondo blanco, cuando hago blend se rompe todo
//		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//Aca activo el blending para hacer muchas luces

		SpotLight l = (SpotLight) env.getLight("mainShipLight");
		l.setPosition(env.getCurrentCam().getPosition());
		l.setRotationY(env.getCurrentCam().getRotationY());
		l.setRotationX(env.getCurrentCam().getRotationX() - 0.2f);
		l.setRotationZ(env.getCurrentCam().getRotationZ());


		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
		for(Light light: env.getLights()){
			if(firstTime == false){
				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_ONE,GL20.GL_ONE);
			}
			light.render(Scene.getCurrentScene());
			firstTime = false;
        }
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
		env.getCurrentCam().move();

		//UI
//		batch.begin();
//        stage.draw();
//        batch.end();
		currentInputs.clear();
	}

	private GameState latestGameState;
	private Inputs currentInputs;

	private void sendInputs() {
		if(client != null && currentInputs != null) {
			if(scene.getCurrentCam().movingForward)
				currentInputs.addInput(Input.MOVE_FORWARD);
			if(scene.getCurrentCam().movingBackward)
				currentInputs.addInput(Input.MOVE_BACKWARD);
			client.sendInputs(currentInputs);
		}
	}

	private void updateState() {
		if(client != null) {
			latestGameState = client.updateState();
			if(latestGameState != null) {
				for(NetworkObject no: latestGameState.getObjects()) {
					for(GenericObject go: scene.getSceneAsObjects()) {
						if(no.id == go.getId()) {
							go.setPosition(no.position);
							go.setRotationX(no.rotation.x);
							go.setRotationY(no.rotation.y);
							go.setRotationZ(no.rotation.z);
							go.setScaleVector(no.scale.x, no.scale.y, no.scale.z);
						}
					}
				}
			}
		}
	}

}
