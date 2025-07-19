package com.freezedown.metallurgica.foundation.material.block;

import com.freezedown.metallurgica.foundation.client.renderer.MaterialBlockRenderer;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IBlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class MaterialBlock extends Block implements IMaterialBlock {
    public final Material material;
    public final IBlockRegistry blockFlag;

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public MaterialBlock(Properties properties, Material material, IBlockRegistry blockFlag, boolean registerModel) {
        super(properties);
        this.material = material;
        this.blockFlag = blockFlag;
        if (registerModel && ClientUtil.isClientSide()) {
            MaterialBlockRenderer.create(this, material, blockFlag.getKey());
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

    public MaterialBlock(Properties properties, Material material, IBlockRegistry blockFlag) {
        this(properties, material, blockFlag, true);
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public IBlockRegistry getFlag() {
        return this.blockFlag;
    }
}
