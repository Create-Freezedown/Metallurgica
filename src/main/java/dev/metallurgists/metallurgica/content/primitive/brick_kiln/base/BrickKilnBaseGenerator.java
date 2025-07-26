package dev.metallurgists.metallurgica.content.primitive.brick_kiln.base;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class BrickKilnBaseGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return horizontalAngle(state.getValue(BrickKilnBaseBlock.FACING)) + 180;
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
        String name = "brick_kiln_base";
        BrickKilnBaseBlock.OpenSide openSide = state.getValue(BrickKilnBaseBlock.OPEN_SIDE);
        boolean isOpen = openSide != BrickKilnBaseBlock.OpenSide.NONE;
        String sideName = "_" + openSide.getSerializedName();

        String blockTexture = "block/decoration/bricks/refactory_bricks_" + textureIndex;
        String openTexture = "block/multiblock/brick_kiln/base" + sideName + "_" + textureIndex;
        String parentPath = isOpen ? prefix + name + "_open" + sideName : prefix + name;
        BlockModelBuilder model = prov.models()
                .withExistingParent(parentPath + "_" + textureIndex, prov.modLoc(parentPath))
                .texture("bricks", blockTexture);
        if (isOpen) model.texture("open", openTexture);
        return ConfiguredModel.builder().modelFile(model).rotationY((getYRotation(state) + 360) % 360).buildLast();
    }
}
