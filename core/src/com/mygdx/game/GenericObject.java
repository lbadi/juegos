package com.mygdx.game;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class GenericObject {

	private Vector3 position = new Vector3();
	private float rotationX = 0;
	private float rotationY = 0;
	private float rotationZ = 0;
	private Mesh mesh;
	public GenericObject() {}
	
	public GenericObject(Vector3 position, Mesh mesh){
		setPosition(position);
		setMesh(mesh);
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	public Mesh getMesh() {
		return mesh;
	}
	public Vector3 getPosition() {
		return new Vector3(position);
	}
	public void setPosition(Vector3 position) {
		this.position = new Vector3(position);
	}
	public float getRotationX() {
		return rotationX;
	}
	public float getRotationY() {
		return rotationY;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}
	public float getRotationZ() {
		return rotationZ;
	}
	public void setRotationZ(float rotationZ) {
		this.rotationZ = rotationZ;
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
		System.out.println("COS:" + cosY);
		System.out.println("COS:" + sinY);
		float[] values = {
				cosY,0,-sinY,0,
				0,1,0,0,
				sinY,0,cosY,0,
				0,0,0,1
		};
		Matrix4 matrix = new Matrix4(values);
		return matrix;
	}
	
	public Matrix4 getTRS() {
		Matrix4 rot = getRy().mul(getRx());
		return getTranslationMatrix().mul(rot);
//		return getTranslationMatrix().inv();
//		return getFpsView();
	}
	
	//TODO hacer el Rz()
	
	
}
