package com.mygdx.game;

import com.badlogic.gdx.math.Matrix4;

public class PerspectiveCam extends Cam {

	private static final float DEFAULT_FOVX = 45.0f;
	private static final float DEFAULT_FOVY = 45.0f;
	private static final float DEFAULT_NEAR = 0.1f;
	private static final float DEFAULT_FAR = 100.0f;
	private Matrix4 projection;
	
	public PerspectiveCam() {
		super();
		setProjection(DEFAULT_NEAR, DEFAULT_FAR, DEFAULT_FOVX, DEFAULT_FOVY);
	}

	// Ver pagina 91 del libro
	public void setProjection(float l, float r, float b, float t, float n,
			float f) {
	}

	//Matriz que esta en http://ogldev.atspace.co.uk/www/tutorial12/tutorial12.html
	private void setProjection(float near, float far, float fovX, float fovY) {
		float[] values = { 
				(float) Math.atan(fovX / 2), 0, 0, 0,
				0,(float) Math.atan(fovY / 2), 0, 0,
				0, 0,-(far + near) / (far - near), -2 * (near * far) / (far - near),
				0, 0, -1, 0 };
		projection = new Matrix4(values).tra();
	}

	// Projection matrix P
	public Matrix4 getProjectionMatrix() {
		return new Matrix4(projection);
	}

}
