package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public abstract class Cam {

	private Vector3 position = new Vector3(0, 0, 2);
	// Direction of the cam

	private float rotationY = 0; // [0...360]
	private float rotationX = 0;// [-90 .. 90]
	
	private Vector3 fowardDirection = new Vector3(0,0,-1);
	private Vector3 leftDirection = new Vector3(1,0,0);

	private float fowardSpeed = 0;
	private float horizontalSpeed = 0;

	public void setPosition(Vector3 position) {
		this.position = position;
	}
	
	public Vector3 getFowardDirection() {
		return new Vector3(fowardDirection);
	}
	public Vector3 getLeftDirection() {
		return new Vector3(leftDirection);
	}

	public Vector3 getPosition() {
		return new Vector3(position);
	}

	public Vector3 getDirection() {
		return getFowardDirection().mul(getRy().mul(getRx()));
	}
	
	public float getRotationX() {
		return rotationX;
	}
	public float getRotationY() {
		return rotationY;
	}

	public void setRotationX(float rotationX) {
		if (rotationX > Math.PI/2) {
			this.rotationX = (float) (Math.PI/2);
		} else if (rotationX < -Math.PI/2) {
			this.rotationX = (float) (-Math.PI/2);
		} else {
			this.rotationX = rotationX;
		}
	}

	public void setRotationY(float rotationY) {
//		if (rotationY > 2*Math.PI) {
//			this.rotationY = 0f;
//		} else if (rotationY < 0) {
//			this.rotationY = (float) ( 2*Math.PI);
//		} else {
//			this.rotationY = rotationY;
//		}
		this.rotationY = rotationY;
	}

	public Matrix4 getTranslationMatrix(){
		Vector3 pos = getPosition();
		float[] values = { 1,0,0,0,
							0,1,0,0,
							0,0,1,0,
							pos.x,pos.y,pos.z,1
		};
		Matrix4 translationMatrix = new Matrix4(values);
		return translationMatrix;
	}

	// View matrix V
	public Matrix4 getViewMatrix() {
		Matrix4 rot = getRy().mul(getRx());
		return getTranslationMatrix().mul(rot).inv();
	}
	
	public Matrix4 getRx(){
		float cosX =(float) Math.cos(getRotationX());
		float sinX =(float) Math.sin(getRotationX());
		float[] values = {
				1,0,0,0,
				0,cosX,sinX,0,
				0,-sinX,cosX,0,
				0,0,0,1
		};
		Matrix4 matrix = new Matrix4(values);
		return matrix;
	}
	public Matrix4 getRy(){
		float cosY =(float) Math.cos(getRotationY());
		float sinY =(float) Math.sin(getRotationY());
//		System.out.println("COS:" + cosY);
//		System.out.println("COS:" + sinY);
		float[] values = {
				cosY,0,-sinY,0,
				0,1,0,0,
				sinY,0,cosY,0,
				0,0,0,1
		};
		Matrix4 matrix = new Matrix4(values);
		return matrix;
	}

	public void setFowardSpeed(float fowardSpeed) {
		this.fowardSpeed = fowardSpeed;
	}

	public void setHorizontalSpeed(float horizontalSpeed) {
		this.horizontalSpeed = horizontalSpeed;
	}

	public void move() {
		position.add(getFowardDirection().mul(getRy().mul(getRx())).nor().scl(
				fowardSpeed * Gdx.graphics.getDeltaTime()));
		position.add(getLeftDirection().mul(getRy().mul(getRx())).nor().scl(
				horizontalSpeed * Gdx.graphics.getDeltaTime()));
	}
	
	

    // Ver pagina 91 del libro
    public abstract void setProjection(float l, float r, float b, float t, float n, float f);

    // Projection matrix P
    public abstract Matrix4 getProjectionMatrix();

}
