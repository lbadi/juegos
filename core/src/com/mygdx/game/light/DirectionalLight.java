package com.mygdx.game.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.environment.ShadowMap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Cam;
import com.mygdx.game.Scene;
import com.mygdx.game.GenericObject;
import com.mygdx.game.OrthoCam;
import projection.OrthoProjection;

public class DirectionalLight extends Light{

	Vector3 lightVector = new Vector3(0,0,0);
	ShaderProgram shader;
	OrthoCam projectionCamera = new OrthoCam();
	
	public DirectionalLight() {

	}
	
	public DirectionalLight(Vector3 position, Vector3 lightVector, Color lightColor) {
		setPosition(position);
		this.lightVector = new Vector3(lightVector);
		this.lightColor = lightColor;

		initShader();

	}

	private void initShader() {
		String vs = Gdx.files.internal("shaders/lights/defaultVS.glsl").readString();
		String fs = Gdx.files.internal("shaders/lights/directional/directional_fs.glsl").readString();
		String shadowVs = Gdx.files.internal("shaders/shadows/shadowMapVS.glsl").readString();
		String shadowFs = Gdx.files.internal("shaders/shadows/shadowMapFS.glsl").readString();
		String renderShadowVs = Gdx.files.internal("shaders/shadows/renderShadowMapVS.glsl").readString();
		String renderShadowFs = Gdx.files.internal("shaders/shadows/renderShadowMapFS.glsl").readString();
		shader = new ShaderProgram(vs, fs);
		shadowShader = new ShaderProgram(shadowVs, shadowFs);
		renderShadowShader = new ShaderProgram(renderShadowVs, renderShadowFs);
		System.out.println(shadowShader.getLog());
		System.out.println(shader.getLog());
		fullScreenQuad = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.TexCoords(0));
		fullScreenQuad.setVertices(new float[] {-1, -1, 0f, 0, 0,
												-1, 1, 0f, 0, 1,
												1, -1, 0f, 1, 0,
												1, 1, 0f, 1, 1 });
		fullScreenQuad.setIndices(new short[]{0, 1, 2, 1, 2, 3});
	}

	public Vector3 getLightVector() {
		return new Vector3(lightVector);
	}
	
	public void setLightVector(Vector3 lightVector) {
		this.lightVector = new Vector3(lightVector);
	}

	@Override
	public ShadowMap getShadowMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(Scene scene) {
		generateShadowMap(scene);
		renderObjects(scene);
//		renderShadowMap();
	}

	private Mesh fullScreenQuad;

	private void renderShadowMap() {
		shadowMapBuffer.getColorBufferTexture().bind();
		renderShadowShader.begin(); {
			renderShadowShader.setUniformi("u_texture", 0);
			fullScreenQuad.render(renderShadowShader, GL20.GL_TRIANGLES);
		}
		renderShadowShader.end();
	}

	private ShaderProgram shadowShader;
	private ShaderProgram renderShadowShader;
	private FrameBuffer shadowMapBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 2048, 2048, true);

	private void generateShadowMap(Scene scene) {
		shadowMapBuffer.begin(); {
			Gdx.gl20.glClearColor(0, 0, 0, 0);
			Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			Gdx.gl20.glDisable(GL20.GL_BLEND);
			for (GenericObject object : scene.getAllObjects()) {
				object.getImg().bind(0);
				shadowShader.begin();
				shadowShader.setUniformMatrix("u_worldView", getProjectionMatrix().mul(getViewMatrix()).mul(object.getTRS())); //aca trabajar
				object.getMesh().render(shadowShader, GL20.GL_TRIANGLES);
				shadowShader.end();
			}
		}
		shadowMapBuffer.end();
		Gdx.gl20.glEnable(GL20.GL_BLEND);
	}

	private void renderObjects(Scene scene){
		Vector3 lightDirection = getDirection();
		Cam cam = scene.getCurrentCam();
		shadowMapBuffer.getColorBufferTexture().bind(1);
		for (GenericObject object : scene.getAllObjects()) {
			object.getImg().bind(0);
			Vector3 position = cam.getPosition();

			shader.begin();
			shader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
			shader.setUniformMatrix("u_worldMatrix", object.getTRS()); //aca trabajar
			shader.setUniformMatrix("u_lightMVP", getProjectionMatrix().mul(getViewMatrix()).mul(object.getTRS()));

//			shader.setUniformMatrix("u_worldView", getProjectionMatrix().mul(getViewMatrix()).mul(object.getTRS())); //aca trabajar
			
			
			shader.setUniformi("u_texture", 0);
			shader.setUniformi("u_shadowMap", 1);

//			shaderProgram.setUniform4fv("u_specular_material", 0);
			shader.setUniform4fv("light_color", new float[]{lightColor.r,lightColor.g,lightColor.b,lightColor.a}, 0, 4);
			shader.setUniform4fv("light_vector", new float[]{lightVector.x, lightVector.y, lightVector.z, 1}, 0, 4);
			//Especular
			shader.setUniform4fv("eye", new float[]{position.x,position.y,position.z,1}, 0, 4);
			shader.setUniform4fv("specular_color", new float[]{specularColor.r,specularColor.g,specularColor.b,1}, 0, 4);
			//Ambiente
			shader.setUniform4fv("ambient_color", new float[]{0,0,1,1}, 0, 4);
			object.getMesh().render(shader, GL20.GL_TRIANGLES);

			shader.end();
		}
	}

	private OrthoProjection orthoProjection = new OrthoProjection();

	@Override
	public Matrix4 getProjectionMatrix() {
		return orthoProjection.getProjectionMatrix();
	}

	public Matrix4 getViewMatrix(){
		return getTRS().inv();
	}
}
