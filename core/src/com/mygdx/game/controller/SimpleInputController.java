package com.mygdx.game.controller;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Cam;
import com.badlogic.gdx.Gdx;

public class SimpleInputController extends InputAdapter{
	
	Cam cam;
	
	public  SimpleInputController(Cam cam) {
		super();
		this.cam = cam;
	}

	@Override
	public boolean keyDown(int keycode) {
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
	        }
		 
		 
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
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
		float xMovement = (float)screenX  / Gdx.graphics.getWidth();
		float yMovement = (float)screenY  / Gdx.graphics.getHeight();
		
		//TODO HACER QUE NO SEA DISCRETO EL MOVIMIENTO; QUE SEA CONTINUO
		if(xMovement < 0.35){
			cam.setRotationY(cam.getRotationY() + 0.01f);
		}
		else if(xMovement > 0.65){
			cam.setRotationY(cam.getRotationY() - 0.01f);
		}
		
		if(yMovement < 0.35){
			cam.setRotationX(cam.getRotationX() + 0.01f);
		}
		else if(yMovement > 0.65){
			cam.setRotationX(cam.getRotationX() - 0.01f);
		}

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
