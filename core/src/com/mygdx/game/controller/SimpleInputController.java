package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Cam;
import com.mygdx.game.Scene;
import com.mygdx.game.light.Light;
import com.mygdx.game.light.SpotLight;

public class SimpleInputController extends InputAdapter{

	Scene enviroment;
	
	
	private float mouseSensibility = 1f;
	
	public  SimpleInputController() {
		super();
		this.enviroment = Scene.getInstance();
	}

	public float getMouseSensibility() {
		return mouseSensibility;
	}
	public void setMouseSensibility(float mouseSensibility) {
		this.mouseSensibility = mouseSensibility;
	}
	@Override
	public boolean keyDown(int keycode) {
		Cam cam = enviroment.getCurrentCam();
		Light light = enviroment.getDefaultLight();
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
	        	break;
	        case Keys.B:
	        	light.setLightColor(light.getLightColor().add(-0.1f, -0.1f, 0.2f, 0));
	        	break;
	        case Keys.G:
	        	light.setLightColor(light.getLightColor().add(-0.1f, 0.2f, -0.1f, 0));
	        	break;
	        case Keys.L:
	        	light.setLightColor(new Color(1,1,1, 1));
	        	break;
	        case Keys.F1:
	        	if(light instanceof SpotLight){
	        		((SpotLight)light).setInnerAngle(((SpotLight) light).getInnerAngle() + 3);
	        	}
	        	break;
	        case Keys.F2:
	        	if(light instanceof SpotLight){
	        		((SpotLight)light).setInnerAngle(((SpotLight) light).getInnerAngle() - 3);
	        	}
	        	break;
	        case Keys.F3:
	        	if(light instanceof SpotLight){
	        		((SpotLight)light).setOutterAngle(((SpotLight) light).getOutterAngle() + 3);
	        	}
	        	break;
	        case Keys.F4:
	        	if(light instanceof SpotLight){
	        		((SpotLight)light).setOutterAngle(((SpotLight) light).getOutterAngle() - 3);
	        	}
	        	break;
	        }
		 
		 
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Cam cam = enviroment.getCurrentCam();

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
		Cam cam = enviroment.getCurrentCam();
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
