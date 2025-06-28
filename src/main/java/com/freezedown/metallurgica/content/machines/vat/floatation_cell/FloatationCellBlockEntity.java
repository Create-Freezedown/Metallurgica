package com.freezedown.metallurgica.content.machines.vat.floatation_cell;

import com.drmangotea.tfmg.content.machinery.vat.base.IVatMachine;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public class FloatationCellBlockEntity extends SmartBlockEntity implements IVatMachine, IHaveGoggleInformation {
    SmartFluidTankBehaviour internalTank;

    public int vatSize;
    public int vatHeight;
    public BlockPos vatPos;
    LerpedFloat vatFluidHeight;
    VatBlockEntity vat;

    public FloatationCellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.vatSize = 1;
        this.vatHeight = 1;
        this.vatPos = null;
    }

    public FluidStack getInternalFluid() {
        return internalTank.getPrimaryHandler().getFluid();
    }

    @Override
    public void tick() {

    }

    //@OnlyIn(Dist.CLIENT)
    //private void bubbleParticles() {
    //    if (level == Minecraft.getInstance().level)
    //        if (!FluidShowerBlockEntity.isRenderEntityWithinDistance(getBlockPos()))
    //            return;
    //    ParticleOptions particle = ParticleTypes.BUBBLE;
    //}

    @Override
    public void vatUpdated(VatBlockEntity be) {
        this.vatSize = be.getWidth();
        this.vatHeight = be.getHeight();
        this.vatPos = be.getBlockPos();
        this.vatFluidHeight = be.getFluidLevel()[0];
        this.vat = be;
    }

    public IVatMachine.PositionRequirement getPositionRequirement() {
        return PositionRequirement.BOTTOM_CENTER;
    }

    @Override
    public String getOperationId() {
        if (getLevel() == null) return "";
        List<FloatationCatalyst> recipes = getLevel().getRecipeManager().getAllRecipesFor(MetallurgicaRecipeTypes.floatation_catalyst.getType());
        Optional<FloatationCatalyst> firstCatalyst = recipes.stream().filter(r -> r.matches(getInternalFluid())).findFirst();
        return firstCatalyst.isPresent() ? firstCatalyst.get().operationId : "";
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(internalTank = SmartFluidTankBehaviour.single(this, 2000)
                .allowInsertion()
                .forbidExtraction().whenFluidUpdates(this::updateAttachment));
    }

    private void updateAttachment() {
        if (this.hasLevel()) {
            VatBlock.updateVatState(this.getBlockState(), this.getLevel(), this.getBlockPos().relative(Direction.UP));
        }
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
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (side == Direction.DOWN && isFluidHandlerCap(cap))
            return internalTank.getCapability().cast();

        return super.getCapability(cap, side);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));
    }
}
