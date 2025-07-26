package dev.metallurgists.metallurgica.content.fluids.fluid_shower;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Collection;
import java.util.List;

public class FluidShowerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    SmartFluidTankBehaviour internalTank;
    private boolean isShowering;

    public FluidShowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(internalTank = SmartFluidTankBehaviour.single(this, 2000)
                .allowInsertion()
                .forbidExtraction());
    }

    @Override
    public void tick() {
        super.tick();

        boolean onClient = level.isClientSide && !isVirtual();

        isShowering = internalTank.getPrimaryHandler().getFluidAmount() > 0;

        if (isShowering) {
            int toDrain = Math.min(internalTank.getPrimaryHandler().getFluidAmount(), 10);
            if (onClient) {
                showerParticles(toDrain);
            }
            for (LivingEntity entity : getShoweringEntities()) {
                var activeEffects = entity.getActiveEffects();
                for (MobEffectInstance effect : activeEffects) {
                    MobEffectInstance effectInstance = new MobEffectInstance(effect.getEffect(), effect.getDuration() - 100, effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon(), null, effect.getFactorData());
                    if (effect.getDuration() <= 100) continue;
                    entity.removeEffect(effect.getEffect());
                    entity.addEffect(effectInstance);
                }
            }
            internalTank.getPrimaryHandler().drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void showerParticles(int amount) {
        if (level == Minecraft.getInstance().level)
            if (!isRenderEntityWithinDistance(getBlockPos()))
                return;
        ParticleOptions particle = FluidFX.getFluidParticle(internalTank.getPrimaryHandler().getFluid());
        float rimRadius = 1 / 4f + 1 / 64f;
        for (Direction direction : Direction.values()) {
            if (direction == Direction.UP) continue;
            Vec3 directionVec = Vec3.atLowerCornerOf(direction.getNormal());
            if (direction != Direction.DOWN) {
                directionVec = directionVec.subtract(getParticleSubtractX(direction), 1 / 16d, getParticleSubtractZ(direction));
                amount = (int) (amount * 0.75f);
                rimRadius = 1 / 4f + 1 / 32f;
            } else {
                directionVec = directionVec.subtract(0, 4 / 16d, 0);
            }
            FluidFX.spawnPouringLiquid(level, getBlockPos(), amount, particle, rimRadius, directionVec, false);
        }
    }

    private double getParticleSubtractX(Direction direction) {
        return switch (direction) {
            case WEST -> -3 / 16d;
            case EAST -> 3 / 16d;
            default -> 0;
        };
    }

    private double getParticleSubtractZ(Direction direction) {
        return switch (direction) {
            case NORTH -> -3 / 16d;
            case SOUTH -> 3 / 16d;
            default -> 0;
        };
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isRenderEntityWithinDistance(BlockPos pos) {
        Entity renderViewEntity = Minecraft.getInstance()
                .getCameraEntity();
        if (renderViewEntity == null)
            return false;
        Vec3 center = VecHelper.getCenterOf(pos);
        if (renderViewEntity.position()
                .distanceTo(center) > 20)
            return false;
        return true;
    }

    private Collection<LivingEntity> getShoweringEntities() {
        if (!isShowering || level == null) return List.of();
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(getBlockPos().below(1)).inflate(1), Entity::isAlive);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (side == Direction.UP && isFluidHandlerCap(cap))
            return internalTank.getCapability().cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));
    }
}
