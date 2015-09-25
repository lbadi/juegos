package com.mygdx.game.light;

import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.environment.ShadowMap;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.math.Matrix4;

public class ShadowMapImpl implements ShadowMap{
	
	private TextureDescriptor<GLTexture> depthMap = new TextureDescriptor<GLTexture>(new Texture(1024, 1024, Format.Alpha));
	@Override
	public Matrix4 getProjViewTrans() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextureDescriptor<GLTexture> getDepthMap() {
		return depthMap;
	}

}
