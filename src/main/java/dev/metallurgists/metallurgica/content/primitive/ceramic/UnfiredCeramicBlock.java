package dev.metallurgists.metallurgica.content.primitive.ceramic;

import dev.metallurgists.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

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
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return onBlockEntityUse(world, pos, be -> be.addFuel(player));
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (getBlockEntity(pLevel, pPos).isCooking()) {
            if (pRandom.nextInt(24) == 0) {
                pLevel.playLocalSound((double) pPos.getX() + 0.5, (double) pPos.getY() + 0.5, (double) pPos.getZ() + 0.5, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.3F, false);
            }

            int j1;
            double d7;
            double d12;
            double d17;
            for (j1 = 0; j1 < 3; ++j1) {
                d7 = (double) pPos.getX() + pRandom.nextDouble();
                d12 = (double) pPos.getY() + pRandom.nextDouble() * 0.5 + 1;
                d17 = (double) pPos.getZ() + pRandom.nextDouble();
                pLevel.addParticle(ParticleTypes.LARGE_SMOKE, d7, d12, d17, 0.0, 0.0, 0.0);
            }
        }
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
