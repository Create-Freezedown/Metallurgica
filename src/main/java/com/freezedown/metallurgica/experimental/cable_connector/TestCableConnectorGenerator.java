package com.freezedown.metallurgica.experimental.cable_connector;

import com.drmangotea.tfmg.blocks.electricity.base.WallMountBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class TestCableConnectorGenerator extends SpecialBlockStateGen {
    public TestCableConnectorGenerator() {
    }

    protected int getXRotation(BlockState state) {
        short value;
        switch ((Direction)state.getValue(WallMountBlock.FACING)) {
            case NORTH -> value = 90;
            case SOUTH -> value = 90;
            case WEST -> value = 90;
            case EAST -> value = 90;
            case DOWN -> value = 180;
            case UP -> value = 0;
            default -> throw new IncompatibleClassChangeError();
        }

        return value;
    }

    protected int getYRotation(BlockState state) {
        short value;
        switch ((Direction)state.getValue(WallMountBlock.FACING)) {
            case NORTH -> value = 0;
            case SOUTH -> value = 180;
            case WEST -> value = 270;
            case EAST -> value = 90;
            case DOWN -> value = 0;
            case UP -> value = 0;
            default -> throw new IncompatibleClassChangeError();
        }

        return value;
    }

    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return (Boolean)state.getValue(TestCableConnectorBlock.EXTENSION) ? AssetLookup.partialBaseModel(ctx, prov, new String[]{"extension"}) : AssetLookup.partialBaseModel(ctx, prov, new String[0]);
    }
}
