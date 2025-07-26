package dev.metallurgists.metallurgica.content.machines.kiln;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class KilnBlock extends Block implements IBE<KilnBlockEntity> {
    public KilnBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<KilnBlockEntity> getBlockEntityClass() {
        return null;
    }

    @Override
    public BlockEntityType<? extends KilnBlockEntity> getBlockEntityType() {
        return null;
    }
}
