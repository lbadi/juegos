package projection;

import com.badlogic.gdx.math.Matrix4;

public class OrthoProjection {

	private Matrix4 projection;

    public OrthoProjection() {
        setProjection(-5f, 5f, -5f, 5f, -5f, 5f);
    }
	
	// Ver pagina 91 del libro
    //TODO ARREGLAR ESTA CAMARA
    public void setProjection(float l, float r, float b, float t, float n, float f) {
        float[] values = {  2/(r-l), 0, 0, -(r+l)/(r-l),
                            0, 2/(t-b), 0, -(t+b)/(t-b),
                            0, 0, 2/(f-n), -(f+n)/(f-n),
                            0, 0, 0, 1};
        //TODO hay que arreglarlo
        //projection = new Matrix4(values).tra();
        projection = new Matrix4().setToOrtho(l, r, b, t, n, f);
    }

    // Projection matrix P
    public Matrix4 getProjectionMatrix() {
        return new Matrix4(projection);
    }
}
