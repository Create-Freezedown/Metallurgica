package com.freezedown.metallurgica.content.machines.rotary_kiln.heater_segment;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HeaterSegmentBlockEntity extends KineticBlockEntity {
    public HeaterSegmentBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        material = TFMGBlocks.HEAVY_MACHINERY_CASING.getDefaultState();
    }
    
    public BlockState material;
    
    public InteractionResult applyMaterialIfValid(ItemStack stack) {
        if (!(stack.getItem() instanceof BlockItem blockItem))
            return InteractionResult.PASS;
        BlockState material = blockItem.getBlock()
                .defaultBlockState();
        if (material == this.material)
            return InteractionResult.PASS;
        if (!material.is(AllTags.AllBlockTags.CASING.tag))
            return InteractionResult.PASS;
        if (level.isClientSide() && !isVirtual())
            return InteractionResult.SUCCESS;
        this.material = material;
        notifyUpdate();
        level.levelEvent(2001, worldPosition, Block.getId(material));
        return InteractionResult.SUCCESS;
    }
    
    private void redraw() {
        if (!isVirtual())
            requestModelDataUpdate();
        if (hasLevel()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(worldPosition);
        }
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        BlockState prevMaterial = material;
        if (!compound.contains("Material"))
            return;
        
        material = NbtUtils.readBlockState(compound.getCompound("Material"));
        if (material.isAir())
            material = Blocks.SPRUCE_PLANKS.defaultBlockState();
        
        if (clientPacket && prevMaterial != material)
            redraw();
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Material", NbtUtils.writeBlockState(material));
    }
}
