package com.freezedown.metallurgica.foundation.ponder.scenes.primitive;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

public class CeramicScenes {
    
    public static void ceramic_firing(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("ceramic_firing", "");
        scene.configureBasePlate(0, 0, 5);
        scene.setSceneOffsetY(-2.0F);
        scene.idle(10);
        scene.showBasePlate();
        scene.setSceneOffsetY(0.4F);
        Selection heater = util.select.position(2, 1, 2);
        Selection ceramic_stand = util.select.fromTo(0, 1, 2, 0, 4, 2).add(util.select.fromTo(1, 4, 2, 2 ,3, 2));
        Selection ceramic = util.select.position(2, 2, 2);
        ElementLink<WorldSectionElement> heaterElement = scene.world.showIndependentSectionImmediately(heater);
        ElementLink<WorldSectionElement> ceramicStandElement = scene.world.showIndependentSection(ceramic_stand, Direction.DOWN);
        scene.idle(20);
        ElementLink<WorldSectionElement> ceramicElement = scene.world.showIndependentSection(ceramic, Direction.EAST);
        scene.overlay.showText(50).attachKeyFrame().text("Unfired Ceramics need to be placed over a heat source").pointAt(util.vector.blockSurface(util.grid.at(2, 2, 2), Direction.WEST)).placeNearTarget();
        scene.idle(100);
        scene.world.setBlocks(ceramic, MetallurgicaBlocks.ceramicPot.getDefaultState(), false);
        scene.idle(20);
        scene.overlay.showText(50).text("After a while, the ceramic will be fired").pointAt(util.vector.blockSurface(util.grid.at(2, 2, 2), Direction.WEST)).placeNearTarget();
        scene.idle(100);
        scene.addKeyframe();
        scene.overlay.showText(50).text("Different types of heat sources can affect the time it takes to fire ceramics").pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST)).placeNearTarget();
        scene.idle(100);
        scene.world.hideIndependentSection(heaterElement, Direction.EAST);
        scene.idle(20);
        scene.world.setBlocks(heater, Blocks.MAGMA_BLOCK.defaultBlockState(), false);
        scene.addKeyframe();
        scene.idle(20);
        scene.world.showIndependentSection(heater, Direction.EAST);
        scene.overlay.showText(50).text("For example, a Magma Block will fire ceramics slower").pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST)).placeNearTarget();
        scene.idle(100);
        scene.world.hideIndependentSection(heaterElement, Direction.EAST);
        scene.idle(20);
        scene.world.setBlocks(heater, AllBlocks.BLAZE_BURNER.getDefaultState().setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SEETHING), false);
        scene.addKeyframe();
        scene.idle(20);
        scene.world.showIndependentSection(heater, Direction.EAST);
        scene.overlay.showText(50).text("But a Superheated Blaze Burner will fire ceramics faster").pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST)).placeNearTarget();
        scene.idle(100);
    }
}
