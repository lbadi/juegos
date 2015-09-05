package com.mygdx.game.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Cam;
import com.mygdx.game.Environment;
import com.mygdx.game.GenericObject;

public class SpotLight extends PointLight{

	private Vector3 lightDirection = new Vector3(0, -1, 0);
	private float innerAngle = 26;
	private float outterAngle = 30;
	private ShaderProgram shader;

    public SpotLight() {
        super();
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
        shader = new ShaderProgram(vs, fs);
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
        Environment environment = Environment.getInstance();
		Cam cam = environment.getCurrentCam();
		Vector3 position = cam.getPosition();

		shader.begin();
		shader.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix()).mul(object.getTRS())); //aca trabajar
		shader.setUniformMatrix("u_worldMatrix", object.getTRS()); //aca trabajar
		shader.setUniformi("u_texture", 0);
		shader.setUniform4fv("light_color", new float[]{lightColor.r, lightColor.g, lightColor.b, lightColor.a}, 0, 4);
		shader.setUniform4fv("light_position", new float[]{super.position.x, super.position.y, super.position.z,1}, 0, 4);
		//Especular
		shader.setUniform4fv("eye", new float[]{position.x,position.y,position.z,1}, 0, 4);
		shader.setUniform4fv("specular_color", new float[]{specularColor.r,specularColor.g,specularColor.b,1}, 0, 4);
		//Ambiente
		shader.setUniform4fv("ambient_color", new float[]{0,0,1,1}, 0, 4);

		shader.setUniform4fv("light_direction", new float[]{lightDirection.x,lightDirection.y,lightDirection.z,1}, 0, 4);
		shader.setUniformf("cosine_inner", (float) Math.cos(innerAngle));
		shader.setUniformf("cosine_outter", (float) Math.cos(outterAngle));
		object.getMesh().render(shader, GL20.GL_TRIANGLES);
		System.out.println("Outter : " + outterAngle + " Inner: " + innerAngle);
		shader.end();
	}
}
