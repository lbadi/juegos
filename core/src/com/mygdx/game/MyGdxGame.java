package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
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
import com.mygdx.game.light.DirectionalLight;
import com.mygdx.game.light.Light;
import com.mygdx.game.light.SpotLight;
import com.mygdx.game.networking.*;
import com.mygdx.game.networking.client.GameClient;
import com.mygdx.game.objects.GenericObject;
import com.mygdx.game.objects.Scene;

import java.io.IOException;
import java.util.Scanner;

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
		System.out.println("Ingrese id: ");
//		Scanner in = new Scanner(System.in);
//		int id = in.nextInt();
		int id = 1;

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
			GenericObject go = new GenericObject(i, new Vector3(i * 2 - 4, 0.4f, 0), spaceshipMesh, img);
			scene.addObject(go);
			if(go.getId() == id)
				mainShip = go;
		}
		//		/*Definimos la posicion del objeto principal*/
//		mainShip = new GenericObject(20, new Vector3(0,-1f, -4), spaceshipMesh, img);
//		mainShip.setRotationY((float) Math.PI);
//		mainShip.setRotationX(-(float) Math.PI / 12);
//		mainShip.setScaleVector(1f, 1f, 1f);
//		mainShip.setId(id);
		scene.addObject("MainShip", mainShip);

		img2 = new Texture("mars.jpg");
		data = loader.loadModelData(Gdx.files.internal("sphere.obj"));
		Mesh mesh = new Mesh(true,
				data.meshes.get(0).vertices.length,
				data.meshes.get(0).parts[0].indices.length,
				VertexAttribute.Position(),VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
		mesh.setVertices(data.meshes.get(0).vertices);
		mesh.setIndices(data.meshes.get(0).parts[0].indices);
		GenericObject backgroundSpace = new GenericObject(new Vector3(0,-25,0),mesh,img2);
		backgroundSpace.setScaleVector(50f, 50f, 50f);
//		backgroundSpace.setRotationX(+(float) Math.PI);
		scene.addObject("SpaceBackground", backgroundSpace);
		
//		String vs = Gdx.files.internal("defaultVS.glsl").readString();
//		String fs = Gdx.files.internal("directional_fs.glsl").readString();
//		ShaderProgram shader = new ShaderProgram(vs, fs);
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LESS);


        Cam cam = new PerspectiveCam();
		cam.setId(10);
		cam.setRotation(new Vector3(-(float) Math.PI / 6, (float) Math.PI, 0));
		cam.setPosition(new Vector3(0, 2, -4));

		mainShip.setRotationX(-(float) Math.PI / 12);
		mainShip.setRotationY((float) Math.PI);

		cam.setFather(mainShip);

		//TODO Arreglar lo de padre e hijo y completar los casos que faltan.
		env = Scene.getCurrentScene();
//		env.addLight("directional", new DirectionalLight(new Vector3(0, 15, 0), new Vector3(0, 1, 0), new Color(1, 1, 1, 1)));
//        env.addLight("point", new PointLight(new Vector3(1.5f,0,0), new Color(1, 1, 1, 1)));
//		env.addLight("light", new DirectionalLight(new Vector3(0, 15, 0), new Vector3(0, 1, 0), new Color(1, 1, 1, 1)));

		SpotLight light = new SpotLight(new Vector3(2, 50, 0), new Vector3((float) (Math.PI * 1.5f), 0, 0), new Color(1, 1, 1, 1));
		env.addLight("light", light);
		light.setInnerAngle(50f);
		light.setOutterAngle(65f);
		light.setId(12345);
		env.addCam("camera", cam);

		currentInputs = new Inputs(id);
		Gdx.input.setInputProcessor(new SimpleInputController(currentInputs));
        env.setDefaultLight("light");

//		env.addLight("mainShipLight", new SpotLight(new Vector3(2, 50, 0), new Vector3((float) (Math.PI * 1.5f), 0, 0), new Color(1, 1, 1, 1)));
//		SpotLight l = (SpotLight) env.getLight("mainShipLight");
//		l.setInnerAngle(45.4f);
//		l.setOutterAngle(48f);

//		env.getDefaultLight().setRotationX(-(float) Math.PI / 2);

		try {
			client = new GameClient(2345 + id);
			client.connect(new NetworkAddress("localhost", 9001));
		} catch (IOException e) {
			e.printStackTrace();
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

//		SpotLight l = (SpotLight) env.getLight("mainShipLight");
//		l.setPosition(mainShip.getPosition());
//		l.setRotationY(mainShip.getRotationY());
//		l.setRotationX(mainShip.getRotationX() - 0.2f);
//		l.setRotationZ(mainShip.getRotationZ());


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
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//		mainShip.move();
//		scene.getCurrentCam().setPosition(new Vector3(0, 2, 5));
//		scene.getCurrentCam().setRotation(new Vector3((float) Math.PI / 12, 0, 0));

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
			addInputs();
			client.sendInputs(currentInputs);
		}
	}

	private void addInputs() {
		if(mainShip.movingForward)
			currentInputs.addInput(Input.MOVE_FORWARD);
		if(mainShip.movingBackward)
			currentInputs.addInput(Input.MOVE_BACKWARD);
		if(mainShip.pitchingDown)
			currentInputs.addInput(Input.PITCH_DOWN);
		if(mainShip.pitchingUp)
			currentInputs.addInput(Input.PITCH_UP);
		if(mainShip.yawingRight)
			currentInputs.addInput(Input.YAW_RIGHT);
		if(mainShip.yawingLeft)
			currentInputs.addInput(Input.YAW_LEFT);
		if(mainShip.rollingLeft)
			currentInputs.addInput(Input.ROLL_LEFT);
		if(mainShip.rollingRight)
			currentInputs.addInput(Input.ROLL_RIGHT);
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
