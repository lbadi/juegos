package com.mygdx.game.cam;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.objects.GenericObject;

public abstract class Cam extends GenericObject {

	public Cam() {
		super(new Vector3(0, 0, 2), null, null);
	}

	// View matrix V
	public Matrix4 getViewMatrix() {
		return getTRS().inv();
	}

	// Ver pagina 91 del libro
	public abstract void setProjection(float l, float r, float b, float t,
			float n, float f);

	// Projection matrix P
	public abstract Matrix4 getProjectionMatrix();

}
