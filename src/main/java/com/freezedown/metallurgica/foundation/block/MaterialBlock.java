package com.freezedown.metallurgica.foundation.block;

import com.freezedown.metallurgica.foundation.client.renderer.MaterialBlockRenderer;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class MaterialBlock extends Block {
    public final Material material;
    public final BlockFlag blockFlag;

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public MaterialBlock(Properties properties, Material material, BlockFlag blockFlag, boolean registerModel) {
        super(properties);
        this.material = material;
        this.blockFlag = blockFlag;
        if (registerModel && ClientUtil.isClientSide()) {
            MaterialBlockRenderer.create(this, material);
        }
    }

    @Override
    public String getDescriptionId() {
        return blockFlag.getUnlocalizedName(material);
    }

    @Override
    public MutableComponent getName() {
        return blockFlag.getLocalizedName(material);
    }

    public MaterialBlock(Properties properties, Material material, BlockFlag blockFlag) {
        this(properties, material, blockFlag, true);
    }
}
