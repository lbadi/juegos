package com.mygdx.game.light;

import projection.PerspectiveProjection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Cam;
import com.mygdx.game.Environment;
import com.mygdx.game.GenericObject;
import com.mygdx.game.PerspectiveCam;

public class SpotLight extends PointLight{

	private Vector3 lightDirection = new Vector3(0, -1, 0);
	private float innerAngle = 26;
	private float outterAngle = 30;
	private ShaderProgram shader;
	private ShaderProgram shadowShader;
	private PerspectiveProjection perspectiveProjection = new PerspectiveProjection();
	
	//Pruebas
	private FrameBuffer frameBuffer = new FrameBuffer(Format.RGB565, 128, 128, false, true); 
	private FrameBuffer frameBuffer2 = new FrameBuffer(Format.RGB565, 128, 128, false);
	SpriteBatch spriteBatch = new SpriteBatch();

    public SpotLight() {
        super();
        initShader();
    }
    
    public SpotLight(Vector3 position, Vector3 lightDirection, Color color){
    	super(position,color);
    	this.lightDirection = lightDirection;
    	initShader();
    }

	public SpotLight(Vector3 position, Vector3 lightDirection, Color color, float inner_cos, float outter_cos){
		super(position, color);
		this.lightDirection = lightDirection;
		this.outterAngle = outter_cos;
		this.innerAngle = inner_cos;
		initShader();
	}

    private void initShader() {
        String vs = Gdx.files.internal("defaultVS.glsl").readString();
        String fs = Gdx.files.internal("spotLightFS.glsl").readString();
        String shadowFs = Gdx.files.internal("shadowMapFS.glsl").readString();
        shader = new ShaderProgram(vs, fs);
        shadowShader = new ShaderProgram(vs, shadowFs);
        System.out.println(shader.getLog());
    }
	
	public Vector3 getLightDirection() {
		return lightDirection;
	}

    public void setInnerAngle(float innerAngle) {
        this.innerAngle = innerAngle;
    }

    public void setOutterAngle(float outterAngle) {
        this.outterAngle = outterAngle;
    }

    public float getInnerAngle() {
        return innerAngle;
    }

    public float getOutterAngle() {
        return outterAngle;
    }

    @Override
	public void render(GenericObject object) {
//    	generateShadowMap(object);
		object.getImg().bind(0);
		getShadowMap().getDepthMap().texture.bind(1);
        Environment environment = Environment.getInstance();
		Cam cam = environment.getCurrentCam();
		Vector3 position = cam.getPosition();
		shader.begin();
		shader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
		shader.setUniformMatrix("u_worldMatrix", object.getTRS()); //aca trabajar
		shader.setUniformi("u_texture", 0);
//		shader.setUniformi("u_texture2", 1);
		shader.setUniform4fv("light_color", new float[]{lightColor.r, lightColor.g, lightColor.b, lightColor.a}, 0, 4);
		shader.setUniform4fv("light_position", new float[]{getPosition().x, getPosition().y, getPosition().z,1}, 0, 4);
		//Especular
		shader.setUniform4fv("eye", new float[]{position.x,position.y,position.z,1}, 0, 4);
		shader.setUniform4fv("specular_color", new float[]{specularColor.r,specularColor.g,specularColor.b,1}, 0, 4);
		//Ambiente
		shader.setUniform4fv("ambient_color", new float[]{0,0,1,1}, 0, 4);

		shader.setUniform4fv("light_direction", new float[]{lightDirection.x,lightDirection.y,lightDirection.z,1}, 0, 4);
		shader.setUniformf("cosine_inner", (float) Math.cos(innerAngle));
		shader.setUniformf("cosine_outter", (float) Math.cos(outterAngle));
		object.getMesh().render(shader, GL20.GL_TRIANGLES);
//		object.getImg().dispose();
//		System.out.println("Outter : " + outterAngle + " Inner: " + innerAngle);
		shader.end();
    	
    	
    	//Prueba
//    	spriteBatch.begin();
//    	 spriteBatch.draw(frameBuffer2.getColorBufferTexture(), 0, 0, 256, 256, 0, 0, frameBuffer2.getColorBufferTexture().getWidth(),
//    			 frameBuffer2.getColorBufferTexture().getHeight(), false, true);
//    	spriteBatch.draw(frameBuffer.getColorBufferTexture(), 256, 256, 256, 256, 0, 0, frameBuffer.getColorBufferTexture()
//    			.getWidth(), frameBuffer.getColorBufferTexture().getHeight(), false, true);
//    			spriteBatch.end();
	}
    
    Texture texture = new Texture(Gdx.files.internal("badlogic.jpg"));
	
    public void generateShadowMap(GenericObject object){
    	//object.getImg().bind(0);
//    	getShadowMap().getDepthMap().texture.bind(0);
    	Environment environment = Environment.getInstance();
    	Cam cam = environment.getCurrentCam();
//    	frameBuffer.begin();
//    	Gdx.gl20.glViewport(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
//    	Gdx.gl20.glClearColor(1f, 1f, 1f, 1);
//    	Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
//    	Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
//    	Gdx.gl20.glEnable(GL20.GL_STENCIL_TEST);
//    	Gdx.gl20.glColorMask(false, false, false, false);
//    	Gdx.gl20.glDepthMask(false);
//    	Gdx.gl20.glStencilFunc(GL20.GL_NEVER, 1, 0xFF);
//    	Gdx.gl20.glStencilOp(GL20.GL_REPLACE, GL20.GL_KEEP, GL20.GL_KEEP);
//    	Gdx.gl20.glStencilMask(0xFF);
//    	Gdx.gl20.glClear(GL20.GL_STENCIL_BUFFER_BIT);
//    	shadowShader.begin();
//    	shader.setUniformi("u_texture", 0);
////    	shadowShader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
////    	shadowShader.setUniformMatrix("u_worldMatrix", object.getTRS()); //aca trabajar
//    	object.getMesh().render(shadowShader, GL20.GL_TRIANGLES);
//    	shadowShader.end();
//    	
//    	
//    	Gdx.gl20.glColorMask(true, true, true, true);
//    	Gdx.gl20.glDepthMask(true);
//    	Gdx.gl20.glStencilMask(0x00);
//    	Gdx.gl20.glStencilFunc(GL20.GL_EQUAL, 1, 0xFF);
//    	shadowShader.begin();
//    	object.getMesh().render(shadowShader, GL20.GL_TRIANGLES);
//    	shadowShader.end();
//    	Gdx.gl20.glDisable(GL20.GL_STENCIL_TEST);
//    	frameBuffer.end();
    	
    	//otro frambuffer
    	frameBuffer2.begin();
    	Gdx.gl20.glViewport(0, 0, frameBuffer2.getWidth(), frameBuffer2.getHeight());
    	Gdx.gl20.glClearColor(1f, 1f, 1f, 1);
    	Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
    	texture.bind(0);
    	shadowShader.begin();
    	//shadowShader.setUniformi("u_texture", 0);
    	object.getMesh().render(shadowShader, GL20.GL_TRIANGLES);
    	shadowShader.end();
    	frameBuffer2.end();
    	
    }
    
    @Override
	public Matrix4 getProjectionMatrix() {
		return perspectiveProjection.getProjectionMatrix();
	}
    
}
