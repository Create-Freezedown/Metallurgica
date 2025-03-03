package com.freezedown.metallurgica.content.fluids.channel.channel;

import com.drmangotea.tfmg.blocks.machines.TFMGMachineBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ChannelBlockEntity extends TFMGMachineBlockEntity {
    public boolean east = true;
    public boolean west = true;
    public boolean north = true;
    public boolean south = true;
    protected FluidTank tankInventory = this.createInventory();
    public LerpedFloat fluidLevel = LerpedFloat.linear();
    
    public ChannelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.fluidCapability = LazyOptional.of(() -> {
            return this.tankInventory;
        });
        this.tank1.forbidInsertion();
        this.tank2.forbidExtraction();
    }
    
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(1000, this::onFluidStackChanged) {
            public boolean isFluidValid(FluidStack stack) {
                return /*stack.getFluid() instanceof MoltenMetal*/true;
            }
            
            public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
                return FluidStack.EMPTY;
            }
        };
    }
    public void tick() {
        super.tick();
        this.fluidLevel.chase(this.tankInventory.getFluidAmount(), 0.30000001192092896, LerpedFloat.Chaser.EXP);
        this.fluidLevel.tickChaser();
        BlockEntity blockEntityNorth = this.level.getBlockEntity(this.getBlockPos().north());
        BlockEntity blockEntityWest = this.level.getBlockEntity(this.getBlockPos().west());
        BlockEntity blockEntityEast = this.level.getBlockEntity(this.getBlockPos().east());
        BlockEntity blockEntitySouth = this.level.getBlockEntity(this.getBlockPos().south());
        this.east = !(blockEntityEast instanceof ChannelBlockEntity);
        this.west = !(blockEntityWest instanceof ChannelBlockEntity);
        this.north = !(blockEntityNorth instanceof ChannelBlockEntity);
        this.south = !(blockEntitySouth instanceof ChannelBlockEntity);
        
        for(int x = 0; x < 30; ++x) {
            for(int i = 0; i < 5; ++i) {
                BlockEntity CheckedBE = null;
                if (i == 0) {
                    CheckedBE = this.level.getBlockEntity(this.getBlockPos().below());
                }
                
                if (i == 1) {
                    CheckedBE = this.level.getBlockEntity(this.getBlockPos().east());
                }
                
                if (i == 2) {
                    CheckedBE = this.level.getBlockEntity(this.getBlockPos().west());
                }
                
                if (i == 3) {
                    CheckedBE = this.level.getBlockEntity(this.getBlockPos().north());
                }
                
                if (i == 4) {
                    CheckedBE = this.level.getBlockEntity(this.getBlockPos().south());
                }
                
                if (CheckedBE instanceof ChannelBlockEntity && (((ChannelBlockEntity)CheckedBE).tankInventory.getFluidAmount() <= this.tankInventory.getFluidAmount() || i == 0)) {
                    FluidTank checkedTank = ((ChannelBlockEntity)CheckedBE).tankInventory;
                    if (checkedTank.getFluidAmount() < 1000) {
                        if (checkedTank.getFluidAmount() >= 995 && this.tankInventory.getFluidAmount() > 0) {
                            checkedTank.setFluid(new FluidStack(tankInventory.getFluid(), checkedTank.getFluidAmount() + 1));
                        }
                        int reducedAmount = this.tankInventory.getFluidAmount() / 8;
                        if (this.tankInventory.getFluidAmount() != 0) {
                            reducedAmount = 1;
                        }
                        int newFluidAmount = checkedTank.getFluidAmount() + reducedAmount;
                        if (newFluidAmount <= 1000) {
                            checkedTank.setFluid(new FluidStack(tankInventory.getFluid(), newFluidAmount));
                            this.tankInventory.drain(1, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        }
        
    }
    
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (this.hasLevel()) {
            if (!this.level.isClientSide) {
                this.setChanged();
                this.sendData();
            }
            
        }
    }
    
    public float getFillState() {
        return (float)this.tankInventory.getFluidAmount() / (float)this.tankInventory.getCapacity();
    }
    
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.east = compound.getBoolean("East");
        this.west = compound.getBoolean("West");
        this.north = compound.getBoolean("North");
        this.south = compound.getBoolean("South");
        this.tankInventory.readFromNBT(compound.getCompound("TankContent"));
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putBoolean("East", this.east);
        compound.putBoolean("West", this.west);
        compound.putBoolean("North", this.north);
        compound.putBoolean("South", this.south);
        compound.put("TankContent", this.tankInventory.writeToNBT(new CompoundTag()));
    }
    
    public LerpedFloat getFluidLevel() {
        return this.fluidLevel;
    }
}
