package com.mygdx.game;

import com.mygdx.game.light.Light;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private static Environment instance;

    private Cam currentCam;
    private Light defaultLight;
	private Map<String, Light> lights;
	private Map<String, Cam> cams;

	public static Environment getInstance() {
		if(instance == null) {
			instance = new Environment();
		}
		return instance;
	}

    private Environment() {
        lights = new HashMap<String, Light>();
        cams = new HashMap<String, Cam>();
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

}
