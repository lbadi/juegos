package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.cam.Cam;
import com.mygdx.game.light.Light;
import com.mygdx.game.light.SpotLight;
import com.mygdx.game.networking.Input;
import com.mygdx.game.networking.Inputs;
import com.mygdx.game.objects.GenericObject;
import com.mygdx.game.objects.Scene;

public class SimpleInputController extends InputAdapter{

	Scene enviroment;

	private float mouseSensibility = 1f;

	private Inputs currentInputs;

	public  SimpleInputController(Inputs currentInputs) {
		super();
		this.enviroment = Scene.getCurrentScene();
		this.currentInputs = currentInputs;
	}

	public float getMouseSensibility() {
		return mouseSensibility;
	}
	public void setMouseSensibility(float mouseSensibility) {
		this.mouseSensibility = mouseSensibility;
	}
	@Override
	public boolean keyDown(int keycode) {
		GenericObject player = enviroment.getObject("MainShip");
		Light light = enviroment.getDefaultLight();
		 switch (keycode)
	        {
	        case Keys.LEFT:
				player.yawingLeft = true;
	            break;
	        case Keys.RIGHT:
				player.yawingRight = true;
	            break;
	        case Keys.UP:
				player.movingForward = true;
	            break;
	        case Keys.DOWN:
				player.movingBackward = true;
	            break;
			case Keys.A:
				player.rollingLeft = true;
				break;
			case Keys.D:
				player.rollingRight = true;
				break;
			case Keys.W:
				player.pitchingDown = true;
				break;
			case Keys.S:
				player.pitchingUp = true;
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
		GenericObject player = enviroment.getObject("MainShip");

		switch (keycode)
        {
        case Keys.LEFT:
			player.yawingLeft = false;
            break;
        case Keys.RIGHT:
			player.yawingRight = false;
            break;
        case Keys.UP:
			player.movingForward = false;
            break;
        case Keys.DOWN:
			player.movingBackward = false;
            break;
		case Keys.A:
			player.rollingLeft = false;
			break;
		case Keys.D:
			player.rollingRight = false;
			break;
		case Keys.W:
			player.pitchingDown = false;
			break;
		case Keys.S:
			player.pitchingUp = false;
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
//		Cam cam = enviroment.getCurrentCam();
//		float xMovement = (float)screenX  / Gdx.graphics.getWidth();
//		float yMovement = (float)screenY  / Gdx.graphics.getHeight();
//		//TODO HACER QUE NO SEA DISCRETO EL MOVIMIENTO; QUE SEA CONTINUO
//		float quantityMovement = 0.05f * getMouseSensibility();
//		float yQuantityMovement = yMovement * quantityMovement;
//		float xQuantityMovement = xMovement * quantityMovement;
//		if(xMovement < 0.499){
//			cam.setRotationY(cam.getRotationY() +  xQuantityMovement );
//		}
//		else if(xMovement > 0.501){
//			cam.setRotationY(cam.getRotationY() - xQuantityMovement);
//		}
//
//		if(yMovement < 0.499){
//			cam.setRotationX(cam.getRotationX() + yQuantityMovement);
//		}
//		else if(yMovement > 0.501){
//			cam.setRotationX(cam.getRotationX() - yQuantityMovement);
//		}
//		Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
//
//
//
////		cam.lookAt(target);
//		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
