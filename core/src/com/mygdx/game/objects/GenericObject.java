package com.mygdx.game.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class GenericObject implements Serializable {

	private int id;
	private Vector3 position = new Vector3();
	private Vector3 scaleVector = new Vector3(1,1,1);
	private float rotationX = 0;
	private float rotationY = 0;
	private float rotationZ = 0;
	private Mesh mesh;
	private Texture img;
	public GenericObject father;
	public List<GenericObject> childs = new ArrayList<GenericObject>();

	private static int lastId = 0;
	
	//Movimiento
	private Vector3 fowardDirection = new Vector3(0,0,-1);
	private Vector3 leftDirection = new Vector3(1,0,0);
	private float fowardSpeed = 0;
	private float horizontalSpeed = 0;
	
	public GenericObject() {

	}
	
	public GenericObject(Vector3 position, Mesh mesh, Texture img){
		id = ++lastId;
		setPosition(position);
		setMesh(mesh);
		setImg(img);
	}

	public GenericObject(int id, Vector3 position, Mesh mesh, Texture img){
		this.id = id;
		setPosition(position);
		setMesh(mesh);
		setImg(img);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GenericObject getFather() {
		return father;
	}
	public void setFather(GenericObject father) {
		this.father = father;
	}
	public List<GenericObject> getChilds() {
		return childs;
	}
	public void addChild(GenericObject child) {
		childs.add(child);
	}
	public Texture getImg() {
		return img;
	}
	public void setImg(Texture img) {
		this.img = img;
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
	
	public Vector3 getScaleVector() {
		return new Vector3(scaleVector);
	}
	public void setScaleVector(float x, float y, float z){
		scaleVector = new Vector3(x,y,z);
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
	public Vector3 getDirection() {
		return getFowardDirection().mul(getRy().mul(getRx()));
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
	
	public Matrix4 getScaleMatrix(){
		float[] values = { scaleVector.x,0,0,0,
							0,scaleVector.y,0,0,
							0,0,scaleVector.z,0,
							0,0,0,1
		};
		Matrix4 scaleMatrix = new Matrix4(values);
		return scaleMatrix;
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
		float[] values = {
				cosY,0,-sinY,0,
				0,1,0,0,
				sinY,0,cosY,0,
				0,0,0,1
		};
		Matrix4 matrix = new Matrix4(values);
		return matrix;
	}

	public Matrix4 getRz() {
		float cosZ =(float) Math.cos(getRotationZ());
		float sinZ =(float) Math.sin(getRotationZ());
		float[] values = {
				cosZ,sinZ,0,0,
				-sinZ,cosZ,0,0,
				0,0,1,0,
				0,0,0,1
		};
		Matrix4 matrix = new Matrix4(values);
		return matrix;
	}
	
	public Matrix4 getTRS() {
		Matrix4 rot = getRz().mul(getRy().mul(getRx()));
		Matrix4 fatherTRS = new Matrix4();
		if(father != null){
			fatherTRS = father.getTRS();
		}
		return fatherTRS.mul(getTranslationMatrix().mul(rot).mul(getScaleMatrix()));
//		return getTranslationMatrix().inv();
//		return getFpsView();
	}
	
	public Matrix4 getRS(){
		Matrix4 rot = getRy().mul(getRx());
		Matrix4 fatherRS = new Matrix4();
		if(father != null){
			fatherRS = father.getRS();
		}
		return fatherRS.mul(rot);
	}
	//TODO hacer el Rz()
	
	public void setFowardSpeed(float fowardSpeed) {
		this.fowardSpeed = fowardSpeed;
	}

	public void setHorizontalSpeed(float horizontalSpeed) {
		this.horizontalSpeed = horizontalSpeed;
	}

	private float rotationXSpeed;

	public void setRotationXSpeed(float rotationXSpeed) {
		this.rotationXSpeed = rotationXSpeed;
	}

	private float rotationYSpeed;

	public void setRotationYSpeed(float rotationYSpeed) {
		this.rotationYSpeed = rotationYSpeed;
	}

	private float rotationZSpeed;

	public void setRotationZSpeed(float rotationZSpeed) {
		this.rotationZSpeed = rotationZSpeed;
	}

	public Vector3 getFowardDirection() {
		return new Vector3(fowardDirection);
	}



	public Vector3 getLeftDirection() {
		return new Vector3(leftDirection);
	}

	public void move() {
		//TODO arreglar cuando el padre esta rotado
		position.add(getFowardDirection().mul(getRy().mul(getRx())).nor().scl(
				fowardSpeed * Gdx.graphics.getDeltaTime()));
		position.add(getLeftDirection().mul(getRy().mul(getRx())).nor().scl(
				horizontalSpeed * Gdx.graphics.getDeltaTime()));
		setRotationX(rotationX + rotationXSpeed);
		setRotationY(rotationY + rotationYSpeed);
		setRotationZ(rotationZ + rotationZSpeed);
	}
	
}
