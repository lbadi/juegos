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
import com.mygdx.game.light.PointLight;
import com.mygdx.game.light.SpotLight;

public class MyGdxGame extends ApplicationAdapter {
	Texture img;
	Mesh spaceshipMesh;
	ShaderProgram shaderProgram;
	List<GenericObject> objects = new ArrayList<GenericObject>();
	Environment env ;
	
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
			objects.add(new GenericObject(new Vector3(i*2- 4, 0, 0),spaceshipMesh));
		}
		
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);


		//TODO hacer que los objetos se puedan attachear para que por ejemplo la luz siga a la camara.
        Cam cam = new PerspectiveCam();
		env = Environment.getInstance();
        env.addLight("directional", new DirectionalLight(new Vector3(1,1,0), new Color(1, 1, 1, 1)));
        env.addLight("point", new PointLight(new Vector3(1.5f,0,0), new Color(1, 1, 1, 1)));
        env.addLight("spot", new SpotLight());
        env.addCam("camera", cam);
		Gdx.input.setInputProcessor(new SimpleInputController());
        env.setDefaultLight("point");
    }
//GL
    @Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		img.bind();
        for(GenericObject object: objects) {
            env.getDefaultLight().render(object);
        }
		env.getCurrentCam().move();
		
		//UI
//		batch.begin();
//        stage.draw();
//        batch.end();
	}

}
