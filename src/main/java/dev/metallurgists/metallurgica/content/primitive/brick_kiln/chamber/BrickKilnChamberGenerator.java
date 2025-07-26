package dev.metallurgists.metallurgica.content.primitive.brick_kiln.chamber;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.*;

public class BrickKilnChamberGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return horizontalAngle(state.getValue(BrickKilnChamberBlock.FACING)) + 180;
    }

    public final <T extends Block> void generateState(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov) {
        prov.getVariantBuilder(ctx.getEntry()).forAllStatesExcept(state -> {
            ConfiguredModel[] variants = new ConfiguredModel[4];
            for (int i = 0; i < variants.length; i ++) {
                variants[i] = getModel(ctx, prov, state, i);
            }
            return variants;
        });
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return null;
    }

    public <T extends Block> ConfiguredModel getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state, int textureIndex) {
        String prefix = "block/multiblock/brick_kiln/";
        String name = "brick_kiln_chamber";
        DoubleBlockHalf half = state.getValue(BrickKilnChamberBlock.HALF);
        String halfName = "_" + half.getSerializedName();

        String blockTexture = "block/decoration/bricks/refactory_bricks_" + textureIndex;
        String innerTexture = "block/multiblock/brick_kiln/chamber_inner_lower_" + textureIndex;
        BlockModelBuilder model = prov.models()
                .withExistingParent(prefix + name + halfName + textureIndex, prov.modLoc(prefix + name + halfName))
                .texture("outer", blockTexture);
        if (half == DoubleBlockHalf.LOWER) model.texture("inside", innerTexture);
        return ConfiguredModel.builder().modelFile(model).rotationY((getYRotation(state) + 360) % 360).buildLast();
    }
}
