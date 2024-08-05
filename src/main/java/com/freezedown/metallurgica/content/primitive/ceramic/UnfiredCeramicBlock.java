package com.freezedown.metallurgica.content.primitive.ceramic;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;

public class UnfiredCeramicBlock extends Block implements IBE<UnfiredCeramicBlockEntity> {
    private final VoxelShape shape;
    public UnfiredCeramicBlock(Properties pProperties, VoxelShape shape) {
        super(pProperties);
        this.shape = shape;
    }
    
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shape;
    }
    
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shape;
    }
    
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return shape;
    }
    
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return shape;
    }
    
    
    @Override
    public Class<UnfiredCeramicBlockEntity> getBlockEntityClass() {
        return UnfiredCeramicBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends UnfiredCeramicBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.unfiredCeramic.get();
    }
}
