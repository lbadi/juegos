package com.mygdx.game.light;


import com.mygdx.game.Scene;
import projection.PerspectiveProjection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Cam;
import com.mygdx.game.GenericObject;
import com.mygdx.game.utils.ScreenshotFactory;

import java.util.List;

public class SpotLight extends PointLight {

    private Vector3 lightDirection = new Vector3(0, -1, 0);
    private float innerAngle = 26;
    private float outterAngle = 30;
    private ShaderProgram shader;
    private ShaderProgram shadowShader;
    private ShaderProgram renderShadowShader;
    private PerspectiveProjection perspectiveProjection = new PerspectiveProjection();

    private Mesh fullScreenQuad;

    public SpotLight() {
        super();
        initShader();
    }

    public SpotLight(Vector3 position, Vector3 lightDirection, Color color) {
        super(position, color);
        this.lightDirection = lightDirection;
        initShader();
    }

    public SpotLight(Vector3 position, Vector3 lightDirection, Color color, float inner_cos, float outter_cos) {
        super(position, color);
        this.lightDirection = lightDirection;
        this.outterAngle = outter_cos;
        this.innerAngle = inner_cos;
        initShader();
    }

    private void initShader() {
        String vs = Gdx.files.internal("defaultVS.glsl").readString();
        String shadowVs = Gdx.files.internal("shadowVs.glsl").readString();
        String renderShadowVs = Gdx.files.internal("renderShadowVs.glsl").readString();
        String fs = Gdx.files.internal("spotLightFS.glsl").readString();
        String shadowFs = Gdx.files.internal("shadowMapFS.glsl").readString();
        String renderShadowFs = Gdx.files.internal("renderShadowMapFS.glsl").readString();
        shader = new ShaderProgram(vs, fs);
        shadowShader = new ShaderProgram(shadowVs, shadowFs);
        renderShadowShader = new ShaderProgram(renderShadowVs, renderShadowFs);
        System.out.println(shadowShader.getLog());
        System.out.println(shader.getLog());
        fullScreenQuad
                = new Mesh(true,
                4,
                6,
                VertexAttribute.Position(), VertexAttribute.TexCoords(0));
        fullScreenQuad.setVertices(new float[]
                {-1, -1, 0f, 0, 0,
                        -1, 1, 0f, 0, 1,
                        1, -1, 0f, 1, 0,
                        1, 1, 0f, 1, 1
                });
        fullScreenQuad.setIndices(new short[]{0, 1, 2, 1, 2, 3});
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
//        generateShadowMap(object);
//    	renderLight(object);
        ////////////////////
//        object.getImg().bind(0);
//    	buffer.getColorBufferTexture().bind(0);

        //---------
//    	IntBuffer depthBuffer = BufferUtils.newIntBuffer(1);
//
//    	  // AFAIK this puts 1 texture name into depthBuffer.
//    	  Gdx.gl20.glGenTextures(1, depthBuffer);
//    	  int depthBufferValue = depthBuffer.get();
//
//    	  // I now bind the texture, so I can use it.
//    	  Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_2D, depthBufferValue);
//    	  Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_DEPTH_COMPONENT, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight(), 0, GL20.GL_DEPTH_COMPONENT, GL20.GL_UNSIGNED_INT,  BufferUtils.newIntBuffer(1));
//    	  Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_DEPTH_COMPONENT, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, GL20.GL_DEPTH_COMPONENT, GL20.GL_UNSIGNED_INT, null);
//    	  
//    	  IntBuffer depthFrameBuffer = BufferUtils.newIntBuffer(1);
//    	  Gdx.gl20.glGenFramebuffers(1, depthFrameBuffer);
//    	  int depthFrameBufferValue = depthFrameBuffer.get();
//    	  Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, depthFrameBufferValue);
//    	  Gdx.gl20.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_DEPTH_ATTACHMENT, GL20.GL_TEXTURE_2D, depthFrameBufferValue, 0);
//    	Gdx.gl20.glBindBuffer(buffer.getDepthBufferHandle(), 0);

        //----------------
        //Descomentar esto
//		getShadowMap().getDepthMap().texture.bind(1);
//        Scene environment = Scene.getInstance();
//		Cam cam = environment.getCurrentCam();
//		Vector3 position = cam.getPosition();
//		shader.begin();
//		shader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
//		shader.setUniformMatrix("u_worldMatrix", object.getTRS()); //aca trabajar
//		shader.setUniformi("u_texture", 0);
////		shader.setUniformi("u_texture2", 1);
//		shader.setUniform4fv("light_color", new float[]{lightColor.r, lightColor.g, lightColor.b, lightColor.a}, 0, 4);
//		shader.setUniform4fv("light_position", new float[]{getPosition().x, getPosition().y, getPosition().z,1}, 0, 4);
//		//Especular
//		shader.setUniform4fv("eye", new float[]{position.x,position.y,position.z,1}, 0, 4);
//		shader.setUniform4fv("specular_color", new float[]{specularColor.r,specularColor.g,specularColor.b,1}, 0, 4);
//		//Ambiente
//		shader.setUniform4fv("ambient_color", new float[]{0,0,1,1}, 0, 4);
//
//		shader.setUniform4fv("light_direction", new float[]{lightDirection.x,lightDirection.y,lightDirection.z,1}, 0, 4);
//		shader.setUniformf("cosine_inner", (float) Math.cos(innerAngle));
//		shader.setUniformf("cosine_outter", (float) Math.cos(outterAngle));
//		object.getMesh().render(shader, GL20.GL_TRIANGLES);
////		object.getImg().dispose();
////		System.out.println("Outter : " + outterAngle + " Inner: " + innerAngle);
//		shader.end();

        //Prueba

    }


    public FrameBuffer frameBuffer;
    public static final int DEPTHMAPIZE = 1024;

    public void renderLight(GenericObject object) {
        if (frameBuffer == null) {
            frameBuffer = new FrameBuffer(Format.RGBA8888, DEPTHMAPIZE, DEPTHMAPIZE, true);
        }
        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Scene scene = Scene.getInstance();
        Cam cam = scene.getCurrentCam();
        shadowShader.begin();
        shadowShader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
        shadowShader.setUniformi("u_texture", 0);
        object.getMesh().render(shadowShader, GL20.GL_TRIANGLES);
//    	fullScreenQuad.render(shadowShader, GL20.GL_TRIANGLES);
        shadowShader.end();

        if (count++ % 200 == 0) {
            ScreenshotFactory.saveScreenshot(frameBuffer.getWidth(), frameBuffer.getHeight(), "depthmap");
        }
        frameBuffer.end();
    }


    int count = 0;
    Texture texture = new Texture("ship.png");


//    public FrameBuffer generateShadowMap(List<GenericObject> object) {
//        buffer.begin();
//        object.getImg().bind(0);
//
//        Scene scene = Scene.getInstance();
//        Cam cam = scene.getCurrentCam();
//        shadowShader.begin();
//        shadowShader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
//        shadowShader.setUniformi("u_texture", 0);
//        object.getMesh().render(shadowShader, GL20.GL_TRIANGLES);
//        shadowShader.end();
////    	if(count++ % 200 == 0)
////    	ScreenshotFactory.saveScreenshot(buffer.getWidth(), buffer.getHeight(), "depthmap");
//        buffer.end();
//        //Imprimo el frameBuffer
////    	object.getImg().bind(0);
////    	buffer.getColorBufferTexture().draw(new Pixmap(Gdx.files.internal("ship.png")), 0, 0);
//        buffer.getColorBufferTexture().bind();
////    	int depth = buffer.getDepthBufferHandle();
////    	Gdx.gl.glActiveTexture(depth);
////    	Gdx.gl.glBindTexture(0, depth);
////    	Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0 + 0);
////    	Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_2D, depth);
////    	texture.bind(0);
////    	getShadowMap().getDepthMap().texture.bind(1);
//        Vector3 position = cam.getPosition();
//        renderShadowShader.begin();
////		renderShadowShader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
//        renderShadowShader.setUniformi("u_texture", 0);
////		object.getMesh().render(renderShadowShader, GL20.GL_TRIANGLES);
//        fullScreenQuad.render(renderShadowShader, GL20.GL_TRIANGLES);
//        renderShadowShader.end();
//
//        return buffer;
//    }

    private FrameBuffer buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

    @Override
    public void render(Scene scene) {
        Cam cam = scene.getCurrentCam();
        buffer.begin(); {
            Gdx.gl20.glClearColor(0, 0, 0, 0);
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl20.glDisable(GL20.GL_BLEND);
            for (GenericObject object : scene.getAllObjects()) {
                object.getImg().bind(0);
                shadowShader.begin();
                shadowShader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
                shadowShader.setUniformi("u_texture", 0);
                object.getMesh().render(shadowShader, GL20.GL_TRIANGLES);
                shadowShader.end();
            }
        }
        buffer.end();
        Gdx.gl20.glEnable(GL20.GL_BLEND);

//        int depth = buffer.getDepthBufferHandle();
//    	Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0 + 0);
//    	Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_2D, depth);
//
//        Gdx.gl20.glBindRenderbuffer(GL20.GL_FRAMEBUFFER, depth);


        buffer.getColorBufferTexture().bind();
        renderShadowShader.begin(); {
            renderShadowShader.setUniformi("u_texture", 0);
            fullScreenQuad.render(renderShadowShader, GL20.GL_TRIANGLES);
        }
        renderShadowShader.end();
    }


    @Override
    public Matrix4 getProjectionMatrix() {
        return perspectiveProjection.getProjectionMatrix();
    }

}
