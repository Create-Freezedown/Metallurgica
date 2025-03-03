package com.freezedown.metallurgica.foundation.ponder.scenes.primitive;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import net.minecraft.core.Direction;

public class DrillTowerScenes {
    
    public static void drill_tower(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("drill_tower", "");
        scene.configureBasePlate(0, 0, 9);
        scene.setSceneOffsetY(-2.0F);
        scene.idle(10);
        scene.showBasePlate();
        scene.setSceneOffsetY(-0.1F);
        Selection brassEncasedShaft = util.select.position(3, 0, 5);
        Selection deposit = util.select.position(4, 0, 4);
        Selection tower = util.select.fromTo(4, 1, 4, 4, 3, 4);
        Selection activator = util.select.position(4, 4, 4);
        Selection expansion1 = util.select.position(4, 5, 4);
        Selection expansion2 = util.select.position(4, 6, 4);
        Selection expansion3 = util.select.position(4, 7, 4);
        Selection expansion4 = util.select.position(4, 8, 4);
        Selection expansions = util.select.fromTo(4, 5, 4, 4, 8, 4);
        Selection kinetics = util.select.fromTo(3, 1, 5, 3, 4, 5);
        Selection stand1 = util.select.fromTo(7, 1, 3, 5, 4, 5);
        Selection stand2 = util.select.fromTo(5, 0, 7, 3, 4, 6).add(util.select.position(4, 3, 5));
        scene.overlay.showText(50).text("Deposits can be found in the world").pointAt(util.vector.blockSurface(util.grid.at(4, 0, 4), Direction.UP)).placeNearTarget();
        scene.idle(60);
        scene.overlay.showText(50).text("To extract their minerals, a Drill Tower is needed").pointAt(util.vector.blockSurface(util.grid.at(4, 0, 4), Direction.UP)).placeNearTarget();
        scene.idle(100);
        scene.addKeyframe();
        ElementLink<WorldSectionElement> towerElement = scene.world.showIndependentSection(tower, Direction.DOWN);
        ElementLink<WorldSectionElement> stand1Element = scene.world.showIndependentSection(stand1, Direction.DOWN);
        ElementLink<WorldSectionElement> stand2Element = scene.world.showIndependentSection(stand2, Direction.DOWN);
        scene.idle(10);
        ElementLink<WorldSectionElement> activatorElement = scene.world.showIndependentSection(activator, Direction.DOWN);
        scene.overlay.showText(50).text("The Drill Tower is a multi-block structure").pointAt(util.vector.blockSurface(util.grid.at(4, 4, 4), Direction.UP)).placeNearTarget();
        scene.idle(60);
        scene.overlay.showOutline(PonderPalette.GREEN, towerElement, util.select.fromTo(4, 1, 4, 4, 3, 4), 50);
        scene.overlay.showText(50).text("Drill Towers must be built from the deposit up").pointAt(util.vector.blockSurface(util.grid.at(4, 2, 4), Direction.UP)).placeNearTarget();
        scene.addKeyframe();
        scene.idle(20);
        scene.world.setBlocks(brassEncasedShaft, AllBlocks.BRASS_ENCASED_SHAFT.getDefaultState().setValue(EncasedShaftBlock.AXIS, Direction.Axis.Y), false);
        ElementLink<WorldSectionElement> kineticsElement = scene.world.showIndependentSection(kinetics, Direction.DOWN);
        scene.world.setKineticSpeed(kinetics, 32);
        scene.world.setKineticSpeed(brassEncasedShaft, 32);
        scene.world.setKineticSpeed(activator, 32);
        scene.effects.indicateRedstone(util.grid.at(4, 4, 4));
        scene.idle(20);
        scene.overlay.showOutline(PonderPalette.GREEN, kineticsElement, util.select.position(3, 4, 5), 50);
        scene.overlay.showText(50).text("The Drill Tower is powered by Kinetic Energy").pointAt(util.vector.blockSurface(util.grid.at(3, 4, 5), Direction.WEST)).placeNearTarget();
        scene.idle(60);
        scene.addKeyframe();
        scene.overlay.showText(50).text("Different deposits require specific efficiency to extract").pointAt(util.vector.blockSurface(util.grid.at(4, 4, 4), Direction.WEST)).placeNearTarget();
        scene.idle(60);
        scene.overlay.showText(50).text("Drill Expansion blocks can be added to increase efficiency").pointAt(util.vector.blockSurface(util.grid.at(4, 5, 4), Direction.UP)).placeNearTarget();
        ElementLink<WorldSectionElement> expansionElement = scene.world.showIndependentSection(expansions, Direction.DOWN);
        scene.effects.indicateSuccess(util.grid.at(4, 4, 4));
        scene.idle(20);
        scene.overlay.showOutline(PonderPalette.GREEN, expansionElement, util.select.fromTo(4, 5, 4, 4, 8, 4), 50);
        scene.idle(100);
    }
}
