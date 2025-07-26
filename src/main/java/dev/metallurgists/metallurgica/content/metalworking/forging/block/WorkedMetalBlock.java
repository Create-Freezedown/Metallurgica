package dev.metallurgists.metallurgica.content.metalworking.forging.block;

import dev.metallurgists.metallurgica.content.metalworking.forging.hammer.RadialHammerMenu;
import dev.metallurgists.metallurgica.foundation.util.CommonUtil;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class WorkedMetalBlock extends Block implements IBE<WorkedMetalBlockEntity> {
    private static final Vec3 PLANE_NORMAL = new Vec3(0.0, 1.0, 0.0);

    private static Vec3 calculatePoint(Vec3 rayVector, Vec3 rayPoint) {
        return rayPoint.subtract(rayVector.scale(rayPoint.dot(PLANE_NORMAL) / rayVector.dot(PLANE_NORMAL)));
    }

    public WorkedMetalBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        final ItemStack stack = player.getItemInHand(hand);
        if (stack.getOrCreateTag().contains("HammerMode")) {
            RadialHammerMenu.HammerMode hammerMode = RadialHammerMenu.HammerMode.get(stack.getOrCreateTag().getString("HammerMode"));
            if (level.getBlockEntity(pos) instanceof WorkedMetalBlockEntity workedMetal) {
                final Vec3 point = calculatePoint(player.getLookAngle(), hit.getLocation().subtract(new Vec3(pos.getX(), pos.getY(), pos.getZ())));
                workedMetal.onClicked((float) point.x, (float) point.z);
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                doParticles(level, pos, workedMetal, point);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    private static void doParticles(Level level, BlockPos pos, WorkedMetalBlockEntity workedMetal, Vec3 point)
    {
        if (level instanceof ServerLevel server)
        {
            workedMetal.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(cap -> {
                server.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, cap.getStackInSlot(0)), pos.getX() + point.x, pos.getY() + 0.0625, pos.getZ() + point.z, 2, CommonUtil.triangle(level.random) / 2.0D, level.random.nextDouble() / 4.0D, CommonUtil.triangle(level.random) / 2.0D, 0.15f);
            });
        }
    }

    @Override
    public Class<WorkedMetalBlockEntity> getBlockEntityClass() {
        return WorkedMetalBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends WorkedMetalBlockEntity> getBlockEntityType() {
        return null;
    }
}
