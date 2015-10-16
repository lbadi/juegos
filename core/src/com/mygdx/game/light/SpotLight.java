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


public class SpotLight extends PointLight {
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

    public SpotLight(Vector3 position, Vector3 lightRotation, Color color) {
        super(position, color);
        setRotationX(lightRotation.x);
        setRotationY(lightRotation.y);
        initShader();
    }
    
    public SpotLight(Vector3 position, Vector3 lightRotation, Color color, float inner_cos, float outter_cos) {
        super(position, color);
        setRotationX(lightRotation.x);
        setRotationY(lightRotation.y);
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
    }


    public FrameBuffer frameBuffer;

//            ScreenshotFactory.saveScreenshot(frameBuffer.getWidth(), frameBuffer.getHeight(), "depthmap");

    int count = 0;
    Texture texture = new Texture("ship.png");



    private FrameBuffer buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

    @Override
    public void render(Scene scene) {
        Cam cam = scene.getCurrentCam();
        //Generate shadow map
       
//        setRotationX( 1 );
        buffer.begin(); {
            Gdx.gl20.glClearColor(0, 0, 0, 0);
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl20.glDisable(GL20.GL_BLEND);
            for (GenericObject object : scene.getAllObjects()) {
                object.getImg().bind(0);
                shadowShader.begin();
                shadowShader.setUniformMatrix("u_worldView", getProjectionMatrix().mul(getViewMatrix()).mul(object.getTRS())); //aca trabajar
//                shadowShader.setUniformi("u_texture", 0);
                object.getMesh().render(shadowShader, GL20.GL_TRIANGLES);
                shadowShader.end();
            }
        }
        buffer.end();
        Gdx.gl20.glEnable(GL20.GL_BLEND);

//        renderShadowMap();
        //-----------------
        renderObjects(scene);
    }
    
    private void renderShadowMap(){
      buffer.getColorBufferTexture().bind();
      renderShadowShader.begin(); {
          renderShadowShader.setUniformi("u_texture", 0);
          fullScreenQuad.render(renderShadowShader, GL20.GL_TRIANGLES);
      }
      renderShadowShader.end();
    }

    private void renderObjects(Scene scene){
    	Vector3 lightDirection = getDirection();
    	Cam cam = scene.getCurrentCam();
    	buffer.getColorBufferTexture().bind(1);
    	for (GenericObject object : scene.getAllObjects()) {
       	 object.getImg().bind(0);
			Vector3 position = cam.getPosition();
			shader.begin();
			shader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
			shader.setUniformMatrix("u_worldMatrix", object.getTRS()); //aca trabajar
			
			shader.setUniformMatrix("u_lightMVP",  getProjectionMatrix().mul(getViewMatrix()).mul(object.getTRS()).mul(getBiasMatrix()));
			shader.setUniformi("u_texture", 0);
			shader.setUniformi("u_shadowMap", 1);
			
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
    	}
    }
    @Override
    public Matrix4 getProjectionMatrix() {
        return perspectiveProjection.getProjectionMatrix();
    }
    
    public Matrix4 getViewMatrix(){
    	return getTRS().inv();
    }

}
