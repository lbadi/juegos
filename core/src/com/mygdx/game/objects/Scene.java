package com.mygdx.game.objects;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.cam.Cam;
import com.mygdx.game.light.Light;

public class Scene {

    private static Scene currentScene;

    private static Map<String, Scene> scenes = new HashMap<String, Scene>();

    private Cam currentCam;
    private Light defaultLight;
	private Map<String, Light> lights;
	private Map<String, Cam> cams;
    private Map<String, GenericObject> objects;
    CustomCubemap envCubemap;


    private long objectsCount;

	public static Scene getCurrentScene() {
		return currentScene;
	}

    public static void setCurrentScene(String tag) {
        Scene scene = scenes.get(tag);
        if(scene != null) {
            currentScene = scene;
        }
    }

    public static Scene newScene(String tag) {
        Scene scene = new Scene();
        if(currentScene == null)
            currentScene = scene;
        scenes.put(tag, scene);
        return scene;
    }

    private Scene() {
        lights = new HashMap<String, Light>();
        cams = new HashMap<String, Cam>();
        objects = new HashMap<String, GenericObject>();
        objectsCount = 0;
    }
    
    public Collection<Light> getLights() {
		return lights.values();
	}

	public Cam getCurrentCam() {
		return currentCam;
	}

    public void setCurrentCam(String tag) {
        if(cams.containsKey(tag)) {
            currentCam = cams.get(tag);
        }
    }

    public Light getDefaultLight() {
        return defaultLight;
    }

    public void setDefaultLight(String tag) {
        defaultLight = lights.get(tag);
    }

    public Light getLight(String tag) {
		return lights.get(tag);
	}

    public Cam getCam(String tag) {
        return cams.get(tag);
    }

	public void addLight(String tag, Light light) {
        if(!lights.containsKey(tag)) {
            if(lights.isEmpty())
                defaultLight = light;
            lights.put(tag, light);
        }
	}

    public void replaceLight(String tag, Light light) {
        if(lights.containsKey(tag)) {
            lights.put(tag, light);
        }
    }

    public void addCam(String tag, Cam cam) {
        if(!cams.containsKey(tag)) {
            if(cams.isEmpty())
                currentCam = cam;
            cams.put(tag, cam);
        }
    }

    public void replaceCam(String tag, Cam cam) {
        if(cams.containsKey(tag)) {
            cams.put(tag, cam);
        }
    }

    public void addObject(String tag, GenericObject object) {
        objects.put(tag, object);
    }

    public void addObject(GenericObject object) {
        objects.put("OBJECT_KEY_" + objectsCount++, object);
    }

    public GenericObject getObject(String tag) {
        return objects.get(tag);
    }

    public Collection<GenericObject> getAllObjects() {
        return objects.values();
    }

    public Set<GenericObject> getSceneAsObjects() {
        Set<GenericObject> allObjects = new HashSet<GenericObject>();
        allObjects.addAll(lights.values());
        allObjects.addAll(cams.values());
        allObjects.addAll(objects.values());
        return allObjects;
    }

	public void setCubeMap(String path) {
		envCubemap = new CustomCubemap(new Pixmap(Gdx.files.internal(path)));
	}
	public CustomCubemap getCustomCubemap(){
		return envCubemap;
	}
	public void renderCubeMap(){
		envCubemap.cubeMapRender(getCurrentCam());
	}

}
