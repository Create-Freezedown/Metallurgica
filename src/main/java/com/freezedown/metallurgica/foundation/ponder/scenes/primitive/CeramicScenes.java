package com.freezedown.metallurgica.foundation.ponder.scenes.primitive;

import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;

public class CeramicScenes {
    
    public static void ceramic_firing(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("ceramic_firing", "");
        scene.configureBasePlate(0, 0, 5);
        scene.setSceneOffsetY(-2.0F);
        scene.idle(10);
        scene.showBasePlate();
    }
}
