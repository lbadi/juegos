package com.mygdx.game.controller;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Cam;
import com.mygdx.game.Enviroment;
import com.mygdx.game.light.DirectionalLight;
import com.mygdx.game.light.Light;

public class SimpleInputController extends InputAdapter{
	
	Enviroment enviroment;
	
	private float mouseLastPositionX = 0.5f;
	private float mouseLastPositionY = 0.5f;
	
	private float mouseSensibility = 1f;
	
	public  SimpleInputController(Enviroment enviroment) {
		super();
		this.enviroment = enviroment;
	}

	public float getMouseSensibility() {
		return mouseSensibility;
	}
	public void setMouseSensibility(float mouseSensibility) {
		this.mouseSensibility = mouseSensibility;
	}
	@Override
	public boolean keyDown(int keycode) {
		Cam cam = enviroment.getCam();
		Light light = enviroment.getLight();
		 switch (keycode)
	        {
	        case Keys.LEFT:
	    		cam.setHorizontalSpeed(-1);
	            break;
	        case Keys.RIGHT:
	        	cam.setHorizontalSpeed(1);
	            break;
	        case Keys.UP:
	        	cam.setFowardSpeed(1);
	            break;
	        case Keys.DOWN:
	        	cam.setFowardSpeed(-1);
	            break;
	        case Keys.R:
	        	light.setLightColor(light.getLightColor().add(0.2f, -0.1f, -0.1f, 0));
	        	enviroment.setLight(light);
	        	break;
	        case Keys.B:
	        	light.setLightColor(light.getLightColor().add(-0.1f, -0.1f, 0.2f, 0));
	        	enviroment.setLight(light);
	        	break;
	        case Keys.G:
	        	light.setLightColor(light.getLightColor().add(-0.1f, 0.2f, -0.1f, 0));
	        	enviroment.setLight(light);
	        	break;
	        case Keys.L:
	        	light.setLightColor(new Color(1,1,1, 1));
	        	enviroment.setLight(light);
	        	break;
	        }
		 
		 
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Cam cam = enviroment.getCam();

		switch (keycode)
        {
        case Keys.LEFT:
    		cam.setHorizontalSpeed(0);
            break;
        case Keys.RIGHT:
        	cam.setHorizontalSpeed(0);
            break;
        case Keys.UP:
        	cam.setFowardSpeed(0);
            break;
        case Keys.DOWN:
        	cam.setFowardSpeed(0);
            break;
        }
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Cam cam = enviroment.getCam();
		float xMovement = (float)screenX  / Gdx.graphics.getWidth();
		float yMovement = (float)screenY  / Gdx.graphics.getHeight();
		//TODO HACER QUE NO SEA DISCRETO EL MOVIMIENTO; QUE SEA CONTINUO
		float quantityMovement = 0.05f * getMouseSensibility();
		float yQuantityMovement = yMovement * quantityMovement;
		float xQuantityMovement = xMovement * quantityMovement;
		if(xMovement < 0.499){
			cam.setRotationY(cam.getRotationY() +  xQuantityMovement );
		}
		else if(xMovement > 0.501){
			cam.setRotationY(cam.getRotationY() - xQuantityMovement);
		}
		
		if(yMovement < 0.499){
			cam.setRotationX(cam.getRotationX() + yQuantityMovement);
		}
		else if(yMovement > 0.501){
			cam.setRotationX(cam.getRotationX() - yQuantityMovement);
		}
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

		
		
//		cam.lookAt(target);
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
