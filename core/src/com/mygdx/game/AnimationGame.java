package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;


public class AnimationGame extends ApplicationAdapter {

    private static final String MODEL_PATH = "Dave.g3db";
    private static final String TEXTURE_PATH = "dave_uv.jpg";
    private static final String VS_PATH = "character_vs.glsl";
    private static final String FS_PATH = "character_fs.glsl";

    private AnimationController animationController;
    private ModelInstance characterInstance;

    private ShaderProgram shader;

    private Camera camera;

    @Override
    public void create() {
        AssetManager assets = new AssetManager();
        assets.load(MODEL_PATH, Model.class);
        assets.finishLoading();
        Model character = assets.get(MODEL_PATH);
        characterInstance = new ModelInstance(character);
        animationController = new AnimationController(characterInstance);
        animationController.animate(characterInstance.animations.get(0).id, -1, 1f, null, 0.2f);

        String vs = Gdx.files.internal(VS_PATH).readString();
        String fs = Gdx.files.internal(FS_PATH).readString();
        shader = new ShaderProgram(vs, fs);
        System.out.println(shader.getLog());

        camera = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 10f, 20f);
        camera.lookAt(0f, 7f, 0f);
        camera.near = 0.1f;
        camera.far = 300.0f;
        camera.update();

        characterInstance.transform.translate(new Vector3(0.0f, 0.0f, 0f));
        Texture img = new Texture(TEXTURE_PATH);
        img.bind(0);

    }

    @Override
    public void render() {
        camera.rotateAround(new Vector3(0.0f, 0.0f, 0f), Vector3.Y, 0.4f);
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        animationController.update(Gdx.graphics.getDeltaTime());
        shader.begin(); {
            Array<Renderable> renderables = new Array<Renderable>();
            Pool<Renderable> pool = new Pool<Renderable>() {
                @Override
                protected Renderable newObject() {
                    return new Renderable();
                }
                @Override
                public Renderable obtain () {
                    Renderable renderable = super.obtain();
                    renderable.material = null;
                    renderable.mesh = null;
                    renderable.shader = null;
                    return renderable;
                }
            };
            characterInstance.getRenderables(renderables, pool);
            Matrix4 identity = new Matrix4().idt();
            float[] bones = new float[12 * 16];
            for (int i = 0; i < bones.length; i++)
                bones[i] = identity.val[i % 16];
            Matrix4 mvp = new Matrix4();
            Matrix4 mv = new Matrix4();
            for(Renderable renderable: renderables) {
                /** the combined projection and view matrix **/
                mvp.set(camera.combined);
                mvp.mul(renderable.worldTransform);
                shader.setUniformMatrix("u_mvp", mvp);
                mv.set(camera.view);
                mv.mul(renderable.worldTransform);
                shader.setUniformMatrix("u_mv", mv);
                mv.inv();
                mv.tra();
                shader.setUniformMatrix("u_normal", mv);
                for (int i = 0; i < bones.length; i++) {
                    final int idx = i/16;
                    bones[i] = (renderable.bones == null ||
                            idx >= renderable.bones.length ||
                            renderable.bones[idx] == null) ?
                            identity.val[i%16] : renderable.bones[idx].val[i%16];
                }
                shader.setUniformMatrix4fv("u_bones", bones, 0, bones.length);
                shader.setUniformi("u_texture", 0);
                renderable.mesh.render(shader, renderable.primitiveType,
                        renderable.meshPartOffset, renderable.meshPartSize);
            }
        }
        shader.end();
    }

}
