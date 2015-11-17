package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class ReflectiveObject extends GenericObject{

	
	String vs = Gdx.files.internal("shaders/lights/defaultVS.glsl").readString();
    String fs = Gdx.files.internal("shaders/lights/spot/spotLightFS.glsl").readString();
    String shadowVs = Gdx.files.internal("shaders/shadows/shadowMapVS.glsl").readString();
    String shadowFs = Gdx.files.internal("shaders/shadows/shadowMapFS.glsl").readString();
    //FrameBuffer left,top,right,behind,bottom,front
    private FrameBuffer left = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    private FrameBuffer right = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    private FrameBuffer bottom = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    private FrameBuffer top = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    private FrameBuffer front = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    private FrameBuffer behind = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    
	public FrameBuffer generateReflectiveCubeMap(){
		//LLamar al shader para que dibujo los alrededores	
		return left;
	}
}
