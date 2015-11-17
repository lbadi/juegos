package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class CustomCubeMap {

	
	private FrameBuffer left = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    private FrameBuffer right = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    private FrameBuffer bottom = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    private FrameBuffer top = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    private FrameBuffer front = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    private FrameBuffer behind = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, true);
    
    public CustomCubeMap() {
    	
	}
}
