package dev.metallurgists.metallurgica.content.primitive.bellows.intake;

import dev.metallurgists.metallurgica.Metallurgica;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class BellowsIntakeGenerator extends SpecialBlockStateGen {

    private String material;
    private ResourceLocation blockTexture;

    public BellowsIntakeGenerator(String material) {
        this.material = material;
        this.blockTexture = Metallurgica.asResource("block/bellows_intake/" + material);
    }

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return horizontalAngle(state.getValue(BellowsIntakeBlock.FACING)) + 180;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> c, RegistrateBlockstateProvider p, BlockState s) {
        String prefix = "block/bellows_intake/";

        BlockModelBuilder model = p.models().withExistingParent("block/bellows_intake/" + material, p.modLoc(prefix + "side"))
                .texture("base", blockTexture);

        return model;
    }
}
