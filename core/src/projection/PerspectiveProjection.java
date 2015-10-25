package projection;

import com.badlogic.gdx.math.Matrix4;

public class PerspectiveProjection {
	private Matrix4 projection;

	public static final float DEFAULT_FOVX = 60.0f;
	public static final float DEFAULT_FOVY = 60.0f;
	public static final float DEFAULT_NEAR = 0.1f;
	public static final float DEFAULT_FAR = 100.0f;
	
	public PerspectiveProjection() {
		setProjection(DEFAULT_NEAR, DEFAULT_FAR, DEFAULT_FOVX, DEFAULT_FOVY);
	}
	// Matriz que esta en
	// http://ogldev.atspace.co.uk/www/tutorial12/tutorial12.html
	public void setProjection(float near, float far, float fovX, float fovY) {
		float[] values = { (float) Math.atan(fovX / 2), 0, 0, 0, 0,
				(float) Math.atan(fovY / 2), 0, 0, 0, 0,
				-(far + near) / (far - near), -2 * (near * far) / (far - near),
				0, 0, -1, 0 };
		projection = new Matrix4(values).tra();
	}

	// Projection matrix P
	public Matrix4 getProjectionMatrix() {
		return new Matrix4(projection);
	}
}
