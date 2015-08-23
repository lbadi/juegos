package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.Cam;

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
	    		System.out.println("LEFT");
	    		cam.setHorizontalSpeed(-1);
	            break;
	        case Keys.RIGHT:
	        	System.out.println("RIGHT");
	        	cam.setHorizontalSpeed(1);
	            break;
	        case Keys.UP:
	    		System.out.println("up");
	        	cam.setFowardSpeed(1);
	            break;
	        case Keys.DOWN:
	        	System.out.println("down");
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
    		System.out.println("LEFT");
    		cam.setHorizontalSpeed(0);
            break;
        case Keys.RIGHT:
        	System.out.println("RIGHT");
        	cam.setHorizontalSpeed(0);
            break;
        case Keys.UP:
    		System.out.println("up");
        	cam.setFowardSpeed(0);
            break;
        case Keys.DOWN:
        	System.out.println("down");
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
