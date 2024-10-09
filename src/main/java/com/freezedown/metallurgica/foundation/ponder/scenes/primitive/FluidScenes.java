package com.freezedown.metallurgica.foundation.ponder.scenes.primitive;

import com.freezedown.metallurgica.content.fluids.faucet.FaucetBlock;
import com.freezedown.metallurgica.content.fluids.faucet.FaucetBlockEntity;
import com.freezedown.metallurgica.content.primitive.ceramic.ceramic_pot.CeramicPotBlockEntity;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidScenes {
    
    public static void faucet(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("faucet", "");
        scene.configureBasePlate(0, 0, 5);
        scene.idle(10);
        scene.showBasePlate();
        Selection faucet = util.select.position(2, 2, 2);
        Selection ceramicPot = util.select.position(2, 1, 2);
        Selection besideTank = util.select.fromTo(2, 1, 3, 2, 3, 3);
        Selection aboveTank = util.select.position(2, 3, 2);
        Vec3 faucetCenter = util.vector.centerOf(util.grid.at(2, 2, 2));
        scene.world.showSection(ceramicPot, Direction.EAST);
        scene.world.showSection(besideTank, Direction.EAST);
        scene.world.showSection(faucet, Direction.EAST);
        scene.idle(20);
        scene.overlay.showText(50).text("Faucets can be used to extract fluids from containers").pointAt(faucetCenter).placeNearTarget();
        scene.idle(100);
        scene.overlay.showControls(new InputWindowElement(faucetCenter, Pointing.RIGHT).rightClick(), 60);
        scene.overlay.showText(60).text("Simply right-click the faucet").pointAt(faucetCenter).placeNearTarget();
        scene.idle(70);
        scene.overlay.showText(50).text("And the fluid will be inserted into the fluid container below it").pointAt(faucetCenter).placeNearTarget();
        scene.idle(60);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                scene.addInstruction(builder -> FaucetBlockEntity.createOutputFluidParticles(builder.getWorld(), MetallurgicaBlocks.faucet.getDefaultState().setValue(FaucetBlock.FACING, Direction.NORTH), stack(MetallurgicaFluids.ironChloride.get()), util.grid.at(2, 2, 2)));
            }
            scene.world.modifyBlockEntity(util.grid.at(2, 1, 3), FluidTankBlockEntity.class, be -> be.getTankInventory()
                    .drain(250, IFluidHandler.FluidAction.EXECUTE));
            scene.world.modifyBlockEntity(util.grid.at(2, 1, 2), CeramicPotBlockEntity.class, be -> be.getTankInventory()
                    .fill(stack(MetallurgicaFluids.ironChloride.get()), IFluidHandler.FluidAction.EXECUTE));
            scene.idle(5);
        }
        scene.idle(80);
        scene.addKeyframe();
        scene.world.modifyBlockEntity(util.grid.at(2, 1, 2), CeramicPotBlockEntity.class, be -> be.getTankInventory()
                .drain(1000, IFluidHandler.FluidAction.EXECUTE));
        scene.overlay.showText(50).text("Faucets can also be placed on the bottom of fluid containers").pointAt(faucetCenter).placeNearTarget();
        scene.idle(60);
        scene.world.hideSection(besideTank, Direction.WEST);
        scene.world.showSection(aboveTank, Direction.EAST);
        scene.world.modifyBlock(util.grid.at(2, 2, 2), state -> state.setValue(FaucetBlock.FACING, Direction.DOWN), false);
        scene.idle(20);
        scene.addKeyframe();
        scene.overlay.showText(50).text("This also inserts the fluid into the container below").pointAt(util.vector.blockSurface(util.grid.at(2, 2, 2), Direction.EAST)).placeNearTarget();
        scene.idle(60);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                scene.addInstruction(builder -> FaucetBlockEntity.createOutputFluidParticles(builder.getWorld(), MetallurgicaBlocks.faucet.getDefaultState().setValue(FaucetBlock.FACING, Direction.DOWN), stack(MetallurgicaFluids.hydrochloricAcid.get()), util.grid.at(2, 2, 2)));
            }
            scene.world.modifyBlockEntity(util.grid.at(2, 3, 2), FluidTankBlockEntity.class, be -> be.getTankInventory()
                    .drain(250, IFluidHandler.FluidAction.EXECUTE));
            scene.world.modifyBlockEntity(util.grid.at(2, 1, 2), CeramicPotBlockEntity.class, be -> be.getTankInventory()
                    .fill(stack(MetallurgicaFluids.hydrochloricAcid.get()), IFluidHandler.FluidAction.EXECUTE));
            scene.idle(5);
        }
        scene.idle(80);
    }
    
    private static FluidStack stack(Fluid fluid) {
        return new FluidStack(fluid, 250);
    }
}