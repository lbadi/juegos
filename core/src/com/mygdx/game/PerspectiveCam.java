package com.mygdx.game;

import projection.PerspectiveProjection;

import com.badlogic.gdx.math.Matrix4;

public class PerspectiveCam extends Cam {


	private PerspectiveProjection projection = new PerspectiveProjection();
	
	public PerspectiveCam() {
		super();
	}


	//Matriz que esta en http://ogldev.atspace.co.uk/www/tutorial12/tutorial12.html
	public void setProjection(float near, float far, float fovX, float fovY) {
		projection.setProjection(near, far, fovX, fovY);
	}

	// Projection matrix P
	public Matrix4 getProjectionMatrix() {
		return projection.getProjectionMatrix();
	}

	@Override
	public void setProjection(float l, float r, float b, float t, float n,
			float f) {
		// TODO Auto-generated method stub
	}

}
