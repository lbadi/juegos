package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMaterial;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
//import com.leapmotion.leap.Controller;
//import com.leapmotion.leap.Frame;
//import com.leapmotion.leap.Hand;
import com.mygdx.game.controller.SimpleInputController;

public class MyGdxGame extends ApplicationAdapter {
	Texture img;
	Mesh spaceshipMesh;
	ShaderProgram shaderProgram;
//    Controller leapController;
	OrthoCam cam;

	@Override
	public void create () {
//        leapController = new Controller();
		img = new Texture("ship.png");
		String vs = Gdx.files.internal("defaultVS.glsl").readString();
		String fs = Gdx.files.internal("defaultFS.glsl").readString();
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
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);
		cam = new OrthoCam();
		Gdx.input.setInputProcessor(new SimpleInputController(cam));

    }

    @Override
	public void render() {
//        Frame leapFrame = leapController.frame();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		img.bind();
		shaderProgram.begin();
//		cam.setPosition(new Vector3(1, 1, 0));
        float yaw = 0;
        float pitch = 0;
        float roll = 0;
		float z = 0;
//        for(Hand hand: leapFrame.hands()) {
//            yaw = (float) Math.toDegrees(hand.direction().yaw());
//            pitch = (float) Math.toDegrees(hand.direction().pitch());
//            roll = (float) Math.toDegrees(hand.palmNormal().roll());
//			z = hand.palmPosition().getZ();
//        }
//		System.out.println("yaw: " + yaw + " pitch: " + pitch + " roll: " + roll);
//		shaderProgram.setUniformMatrix("u_worldView", new Matrix4(new Quaternion().setEulerAngles(yaw, pitch, roll))); //aca trabajar
		cam.setProjection(-2, 2, -2, 2, 2, -2);
		cam.setProjection(-1, 1, -1, 1, 1, -1);
		cam.move();
//		Vector3 lookAt = new Vector3((float)0.1, (float)0.1, (float)0.1);
		
//		System.out.println(lookAt);
//		cam.lookAt(lookAt);
//		shaderProgram.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix())); //aca trabajar
		shaderProgram.setUniformMatrix("u_worldView", cam.getViewMatrix().mul(cam.getProjectionMatrix()));
		shaderProgram.setUniformi("u_texture", 0);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
		shaderProgram.end();
	}

}
//
//package com.mygdx.game;
//
//import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.ApplicationListener;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.assets.loaders.ModelLoader;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.PerspectiveCamera;
//import com.badlogic.gdx.graphics.g3d.Environment;
//import com.badlogic.gdx.graphics.g3d.Model;
//import com.badlogic.gdx.graphics.g3d.ModelBatch;
//import com.badlogic.gdx.graphics.g3d.ModelInstance;
//import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
//import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
//import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
//import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.leapmotion.leap.Controller;
//import com.leapmotion.leap.Frame;
//import com.leapmotion.leap.Hand;
//
///**
// * See: http://blog.xoppa.com/loading-models-using-libgdx/
// * @author Xoppa
// */
//public class MyGdxGame extends ApplicationAdapter {
//	public Environment environment;
//	public PerspectiveCamera cam;
//	public CameraInputController camController;
//	public ModelBatch modelBatch;
//	public Model model;
//	public ModelInstance instance;
//	private Controller leapController;
//
//	@Override
//	public void create() {
//		leapController = new Controller();
//		modelBatch = new ModelBatch();
//		environment = new Environment();
//		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
//		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
//
//		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		cam.position.set(1f, 1f, 30f);
//		cam.lookAt(0,0,0);
//		cam.near = 1f;
//		cam.far = 300f;
//		cam.update();
//
//		ModelLoader<?> loader = new ObjLoader();
//		model = loader.loadModel(Gdx.files.internal("spirit/B-2_Spirit.obj"));
//		instance = new ModelInstance(model);
//
//		camController = new CameraInputController(cam);
//		Gdx.input.setInputProcessor(camController);
//	}
//
//	@Override
//	public void render() {
//		camController.update();
//		Frame leapFrame = leapController.frame();
//		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//
//		modelBatch.begin(cam);
//		float yaw = 0;
//        float pitch = 0;
//        float roll = 0;
//        for(Hand hand: leapFrame.hands()) {
//            yaw = (float) Math.toDegrees(hand.direction().yaw());
//            pitch = (float) Math.toDegrees(hand.direction().pitch());
//            roll = (float) Math.toDegrees(hand.palmNormal().roll());
//        }
//		instance.transform.set(new Matrix4(new Quaternion().setEulerAngles(yaw, pitch, roll)));
//		modelBatch.render(instance, environment);
//		modelBatch.end();
//	}
//
//	@Override
//	public void dispose() {
//		modelBatch.dispose();
//		model.dispose();
//	}
//
//	@Override
//	public void resize(int width, int height) {
//	}
//
//	@Override
//	public void pause() {
//	}
//
//	@Override
//	public void resume() {
//	}
//}