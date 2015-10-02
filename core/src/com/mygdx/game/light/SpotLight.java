package com.mygdx.game.light;


import java.nio.IntBuffer;

import projection.PerspectiveProjection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;
import com.mygdx.game.Cam;
import com.mygdx.game.Environment;
import com.mygdx.game.GenericObject;

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
	private Mesh fullScreenQuad;

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
        String shadowVs = Gdx.files.internal("shadowVs.glsl").readString();
        String fs = Gdx.files.internal("spotLightFS.glsl").readString();
        String shadowFs = Gdx.files.internal("shadowMapFS.glsl").readString();
        shader = new ShaderProgram(vs, fs);
        shadowShader = new ShaderProgram(shadowVs, shadowFs);
        System.out.println(shadowShader.getLog());
        System.out.println(shader.getLog());
        fullScreenQuad 
        		  = new Mesh(true,
        				4,
        				6,
        				VertexAttribute.Position());
        		fullScreenQuad.setVertices(new float[] 
        				{-1, -1, 0.5f, 
        				 -1, 1, 0.5f,
        				 1, 1, 0.5f,
        				 1, -1, 0.5f
        				});
        		fullScreenQuad.setIndices(new short[] { 0, 1, 2, 0, 2, 3 } );
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
    	
    	/*
    	SHADOWS
    	*/
    	FrameBuffer buffer = generateShadowMap(object);
    	////////////////////
//		object.getImg().bind(0);
    	//buffer.getColorBufferTexture().bind(0);
    	
    	//---------
    	IntBuffer depthBuffer = BufferUtils.newIntBuffer(1);

    	  // AFAIK this puts 1 texture name into depthBuffer.
    	  Gdx.gl20.glGenTextures(1, depthBuffer);
    	  int depthBufferValue = depthBuffer.get();

    	  // I now bind the texture, so I can use it.
    	  Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_2D, depthBufferValue);
    	  Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_DEPTH_COMPONENT, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight(), 0, GL20.GL_DEPTH_COMPONENT, GL20.GL_UNSIGNED_INT,  BufferUtils.newIntBuffer(1));
    	  Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_DEPTH_COMPONENT, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, GL20.GL_DEPTH_COMPONENT, GL20.GL_UNSIGNED_INT, null);
    	  
    	  IntBuffer depthFrameBuffer = BufferUtils.newIntBuffer(1);
    	  Gdx.gl20.glGenFramebuffers(1, depthFrameBuffer);
    	  int depthFrameBufferValue = depthFrameBuffer.get();
    	  Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, depthFrameBufferValue);
    	  Gdx.gl20.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_DEPTH_ATTACHMENT, GL20.GL_TEXTURE_2D, depthFrameBufferValue, 0);
    	Gdx.gl20.glBindBuffer(buffer.getDepthBufferHandle(), 0);
    	
    	//----------------
//		getShadowMap().getDepthMap().texture.bind(1);
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
    	
	}
    
    Texture texture = new Texture(Gdx.files.internal("badlogic.jpg"));
	
    FrameBuffer buffer = new FrameBuffer(Format.RGBA8888, 1024, 1024, true);
    public FrameBuffer generateShadowMap(GenericObject object){
    	buffer.begin();
    	Gdx.gl20.glClearColor(0, 1, 0, 1);
    	Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
    	Environment environment = Environment.getInstance();
    	Cam cam = environment.getCurrentCam();
    	shadowShader.begin();
    	shadowShader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS()));
    	object.getMesh().render(shadowShader, GL20.GL_TRIANGLES);
//    	fullScreenQuad.render(shadowShader, GL20.GL_TRIANGLES);
    	shadowShader.end();
    	buffer.end();
    	return buffer;
    }
    
    @Override
	public Matrix4 getProjectionMatrix() {
		return perspectiveProjection.getProjectionMatrix();
	}
    
}
