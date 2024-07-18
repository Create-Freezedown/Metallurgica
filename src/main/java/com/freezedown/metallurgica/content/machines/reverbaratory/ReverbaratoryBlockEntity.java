package com.freezedown.metallurgica.content.machines.reverbaratory;

import com.drmangotea.createindustry.registry.TFMGFluids;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.multiblock.FluidOutputBlockEntity;
import com.freezedown.metallurgica.foundation.multiblock.MultiblockStructure;
import com.freezedown.metallurgica.foundation.multiblock.PositionUtil.PositionRange;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.freezedown.metallurgica.foundation.multiblock.PositionUtil.*;

public class ReverbaratoryBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    public ReverbaratoryCookingRecipe recipe;
    protected LazyOptional<IFluidHandler> fluidCapability = LazyOptional.of(() -> {
        return this.tankInventory;
    });
    public LazyOptional<IItemHandlerModifiable> itemCapability = LazyOptional.of(() -> new InvWrapper(this.inputInventory));
    public SmartInventory inputInventory;
    public FluidTank tankInventory = this.createInventory();
    public boolean isValid = false;
    public int timer;
    public float fuelEfficiency;
    public float speedModifier;
    public MultiblockStructure multiblockStructure;
    
    public BlockState mainWall = MetallurgicaBlocks.carbonBrick.get().defaultBlockState();
    public BlockState mainGlass = MetallurgicaBlocks.blastProofGlass.get().defaultBlockState();
    
    public Direction getMasterDirection() {
        return this.getBlockState().getValue(ReverbaratoryBlock.FACING);
    }
    
    public ReverbaratoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.timer = -1;
        this.inputInventory = (new SmartInventory(1, this)).forbidInsertion().forbidExtraction().withMaxStackSize(64);
        this.fuelEfficiency = 1000.0F;
        this.speedModifier = 1.0F;
        this.multiblockStructure = MultiblockStructure.cuboidBuilder(this)
                .directional(getMasterDirection())
                .withBlockAt(new PositionRange(zero(), generateSequence(-1, 1, 1), generateSequence(-1, 1, 1)), mainWall)
                .withBlockAt(new PositionRange(generateSequence(1, 3, 1), List.of(0), List.of(1)), mainGlass)
                .withBlockAt(new PositionRange(generateSequence(1, 3, 1), List.of(0), List.of(-1)), mainGlass)
                .withBlockAt(new PositionRange(generateSequence(1, 2, 1), List.of(-1), generateSequence(-1, 1, 1)), mainWall)
                .withFluidOutputAt(3, -1, 0, "reverbaratory.primary_fluid_output")
                .withFluidOutputAt(4, 1, 0, "reverbaratory.carbon_dioxide_output")
                .withFluidOutputAt(4, 0, 0, "reverbaratory.slag_output")
                .build();
    }
    
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(1000, this::onFluidStackChanged) {
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().is(MetallurgicaTags.AllFluidTags.REVERBARATORY_FUELS.tag);
            }
        };
    }
    
    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
        multiblockStructure.setFluidOutputCapacity(getOutputBlockEntity(), 0);
        multiblockStructure.setFluidOutputCapacity(getCarbonDioxideOutputBlockEntity(), 0);
        multiblockStructure.setFluidOutputCapacity(getSlagOutputBlockEntity(), 0);
    }
    
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        this.sendData();
    }
    public void lazyTick() {
        super.lazyTick();
        if (this.level == null) {
            return;
        }
        
    }
    
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        multiblockStructure.createMissingParticles();
        
        multiblockStructure.setFluidOutputCapacity(getOutputBlockEntity(), MetallurgicaConfigs.server().machineConfig.reverbaratoryPrimaryOutputCapacity.get());
        multiblockStructure.setFluidOutputCapacity(getCarbonDioxideOutputBlockEntity(), MetallurgicaConfigs.server().machineConfig.genericCarbonDioxideOutputCapacity.get());
        multiblockStructure.setFluidOutputCapacity(getSlagOutputBlockEntity(), MetallurgicaConfigs.server().machineConfig.reverbaratorySlagOutputCapacity.get());
        
        isValid = multiblockStructure.isStructureCorrect();
        if (isValid) {
            this.acceptInsertedItems();
        }
        
        this.fuelEfficiency = 400.0F;
        this.speedModifier = 1.0F;
        
        
        if (this.recipe != null && this.timer == -1 && getOutputBlockEntity().tankInventory.getFluidAmount() + this.recipe.getFluidResults().get(0).getAmount() <= getOutputBlockEntity().tankInventory.getCapacity() && getSlagOutputBlockEntity().tankInventory.getFluidAmount() + this.recipe.getFluidResults().get(1).getAmount() <= getSlagOutputBlockEntity().tankInventory.getCapacity() && isValid && !this.tankInventory.isEmpty()) {
            this.timer = (int)((float)this.recipe.getProcessingDuration() / this.speedModifier);
            this.inputInventory.getStackInSlot(0).setCount(this.inputInventory.getStackInSlot(0).getCount() - 1);
        }
        
        RecipeWrapper inventoryIn = new RecipeWrapper(this.inputInventory);
        if (this.recipe == null || !this.recipe.matches(inventoryIn, this.level) && isValid) {
            Optional<ReverbaratoryCookingRecipe> recipe = MetallurgicaRecipeTypes.reverbaratory_cooking.find(inventoryIn, this.level);
            if (recipe.isEmpty()) {
                this.timer = -1;
                this.sendData();
            } else {
                this.recipe = recipe.get();
                this.sendData();
            }
        }
        if (isValid && this.timer > 0) {
            createSmokeVolume();
        }
        
        if (isValid && this.timer > 0 && getOutputBlockEntity().tankInventory.getFluidAmount() + this.recipe.getFluidResults().get(0).getAmount() <= getOutputBlockEntity().tankInventory.getCapacity() && getSlagOutputBlockEntity().tankInventory.getFluidAmount() + this.recipe.getFluidResults().get(1).getAmount() <= getSlagOutputBlockEntity().tankInventory.getCapacity()) {
            --this.timer;
           
            for (LivingEntity entity : this.getEntitiesToCremate()) {
                if (level.random.nextDouble() > 0.85) {
                    entity.hurt(getDamageSource(), 2.5F);
                    SoundSource soundSource = SoundSource.NEUTRAL;
                    if (entity.getType().getCategory() == MobCategory.MONSTER) {
                        soundSource = SoundSource.HOSTILE;
                    }
                    if (entity instanceof Player) {
                        soundSource = SoundSource.PLAYERS;
                    }
                    level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIRE_EXTINGUISH, soundSource, 1.0F, 1.0F, false);
                }
            }
            if (level.random.nextFloat() < 0.05) {
                getCarbonDioxideOutputBlockEntity().tankInventory.setFluid(new FluidStack(TFMGFluids.CARBON_DIOXIDE.get(), getCarbonDioxideOutputBlockEntity().tankInventory.getFluidAmount() + 10));
            }
            int random = level.random.nextInt((int)Math.abs(this.fuelEfficiency) + 1);
            if (random == 69) {
                this.tankInventory.getFluid().shrink(100);
            }
        }
        
        if (this.timer == 0) {
            
            this.process();
            this.timer = -1;
        }
    }
    
    public void invalidate() {
        super.invalidate();
        this.fluidCapability.invalidate();
        this.itemCapability.invalidate();
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.fluidCapability.isPresent()) {
            this.refreshCapability();
        }
        
        return cap == ForgeCapabilities.FLUID_HANDLER ? this.fluidCapability.cast() : super.getCapability(cap, side);
    }
    
    private IFluidHandler handlerForCapability() {
        return this.tankInventory;
    }
    
    public IFluidTank getTankInventory() {
        return this.tankInventory;
    }
    
    private void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = this.fluidCapability;
        this.fluidCapability = LazyOptional.of(this::handlerForCapability);
        oldCap.invalidate();
    }
    
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.inputInventory.deserializeNBT(compound.getCompound("InputItems"));
        this.tankInventory.setCapacity(4000);
        this.tankInventory.readFromNBT(compound.getCompound("TankContent"));
        timer = compound.getInt("Timer");
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("InputItems", this.inputInventory.serializeNBT());
        compound.put("TankContent", this.tankInventory.writeToNBT(new CompoundTag()));
        compound.putInt("Timer", timer);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    public void process() {
        if (this.level == null) {
            return;
        }
        if (!this.level.isClientSide) {
            getOutputBlockEntity().tankInventory.setFluid(new FluidStack(this.recipe.getFluidResults().get(0).getFluid(), getOutputBlockEntity().tankInventory.getFluidAmount() + this.recipe.getFluidResults().get(0).getAmount()));
            getSlagOutputBlockEntity().tankInventory.setFluid(new FluidStack(this.recipe.getFluidResults().get(1).getFluid(), getSlagOutputBlockEntity().tankInventory.getFluidAmount() + this.recipe.getFluidResults().get(1).getAmount()));
        }
    }
    
    public void acceptInsertedItems() {
        List<ItemEntity> itemsToPick = this.getItemsToPick();
        Iterator<ItemEntity> var2 = itemsToPick.iterator();
        
        while (true) {
            while (var2.hasNext()) {
                ItemEntity itemEntity = var2.next();
                ItemStack itemStack = itemEntity.getItem();
                int freeSpace;
                int count;
                freeSpace = this.inputInventory.getStackInSlot(0).getMaxStackSize() - this.inputInventory.getStackInSlot(0).getCount();
                count = itemStack.getCount();
                if (this.inputInventory.isEmpty() || this.inputInventory.getItem(0).is(itemStack.getItem())) {
                    if (count > freeSpace) {
                        itemStack.setCount(itemStack.getCount() - freeSpace);
                        this.inputInventory.setItem(0, new ItemStack(itemStack.getItem(), this.inputInventory.getStackInSlot(0).getCount() + freeSpace));
                    } else {
                        this.inputInventory.setItem(0, new ItemStack(itemStack.getItem(), this.inputInventory.getStackInSlot(0).getCount() + itemStack.getCount()));
                        itemEntity.discard();
                    }
                }
            }
            
            return;
        }
    }
    
    public CremationDamageSource getDamageSource() {
        int messageType = Mth.randomBetweenInclusive(getLevel().random, 1, 4);
        return new CremationDamageSource("reverbaratory_" + messageType, new Vec3(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()));
    }
    
    public List<LivingEntity> getEntitiesToCremate() {
        Direction facing = this.getBlockState().getValue(ReverbaratoryBlock.FACING);
        AABB searchArea = new AABB(this.getBlockPos().relative(facing, 1));
        AABB searchArea1 = new AABB(this.getBlockPos().relative(facing, 2));
        AABB searchArea2 = new AABB(this.getBlockPos().relative(facing, 3));
        if (this.level == null) {
            return new ArrayList<>();
        }
        List<LivingEntity> entityList = new ArrayList<>();
        entityList.addAll(this.level.getEntitiesOfClass(LivingEntity.class, searchArea));
        entityList.addAll(this.level.getEntitiesOfClass(LivingEntity.class, searchArea1));
        entityList.addAll(this.level.getEntitiesOfClass(LivingEntity.class, searchArea2));
        
        return entityList;
    }
    
    
    public List<ItemEntity> getItemsToPick() {
        Direction facing = this.getBlockState().getValue(ReverbaratoryBlock.FACING);
        AABB searchArea = new AABB(this.getBlockPos().relative(facing, 1));
        AABB searchArea1 = new AABB(this.getBlockPos().relative(facing, 2));
        AABB searchArea2 = new AABB(this.getBlockPos().relative(facing, 3));
        if (this.level == null) {
            return new ArrayList<>();
        }
        List<ItemEntity> itemList = new ArrayList<>();
        itemList.addAll(this.level.getEntitiesOfClass(ItemEntity.class, searchArea));
        itemList.addAll(this.level.getEntitiesOfClass(ItemEntity.class, searchArea1));
        itemList.addAll(this.level.getEntitiesOfClass(ItemEntity.class, searchArea2));
        
        return itemList;
    }
    
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!isValid) {
            Lang.translate("goggles.reverbaratory.invalid").style(ChatFormatting.RED).forGoggles(tooltip, 1);
        }   else {
            if (!isPlayerSneaking) {
                Lang.translate("goggles.reverbaratory.stats").style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                if (this.timer > 0) {
                    Lang.translate("goggles.blast_furnace.status.running").style(ChatFormatting.YELLOW).forGoggles(tooltip, 1);
                } else {
                    Lang.translate("goggles.blast_furnace.status.off").style(ChatFormatting.YELLOW).forGoggles(tooltip, 1);
                }
                Lang.translate("goggles.misc.storage_info").style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
                Lang.translate("goggles.blast_furnace.item_count", this.inputInventory.getStackInSlot(0).getCount()).style(ChatFormatting.AQUA).forGoggles(tooltip, 1);
                Lang.translate("goggles.blast_furnace.nothing_lol").style(ChatFormatting.AQUA).forGoggles(tooltip, 1);
            }
        }
        LazyOptional<IFluidHandler> handler = this.getCapability(ForgeCapabilities.FLUID_HANDLER);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent()) {
            return false;
        } else {
            if (isPlayerSneaking) {
                multiblockStructure.addToGoggleTooltip(tooltip);
                return false;
            }
            IFluidHandler tank = resolve.get();
            if (tank.getTanks() == 0) {
                return false;
            } else {
                LangBuilder mb = Lang.translate("generic.unit.millibuckets");
                boolean isEmpty = true;
                
                for(int i = 0; i < tank.getTanks(); ++i) {
                    FluidStack fluidStack = tank.getFluidInTank(i);
                    if (!fluidStack.isEmpty()) {
                        Lang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                        Lang.builder().add(Lang.number((double)fluidStack.getAmount()).add(mb).style(ChatFormatting.GOLD)).text(ChatFormatting.GRAY, " / ").add(Lang.number((double)tank.getTankCapacity(i)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                        isEmpty = false;
                    }
                }
                
                if (tank.getTanks() > 1) {
                    if (isEmpty) {
                        tooltip.remove(tooltip.size() - 1);
                    }
                    
                    return true;
                } else if (!isEmpty) {
                    return true;
                } else {
                    Lang.translate("gui.goggles.fluid_container.capacity", new Object[0]).add(Lang.number(tank.getTankCapacity(0)).add(mb).style(ChatFormatting.DARK_GREEN)).style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
                    return true;
                }
            }
        }
        
    }
    
    public void createSmokeVolume() {
        Direction facing = this.getBlockState().getValue(ReverbaratoryBlock.FACING);
        BlockPos firstPos = this.getBlockPos().relative(facing, 1);
        BlockPos middlePos = this.getBlockPos().relative(facing, 2);
        BlockPos endPos = this.getBlockPos().relative(facing, 3);
        
        ParticleOptions smallSmoke = ParticleTypes.SMOKE;
        ParticleOptions smallFlame = ParticleTypes.FLAME;
        if (this.level == null) {
            return;
        }
        if (!this.level.isClientSide) {
            return;
        }
        
        ClientUtil.createCustomCubeParticles(firstPos, this.level, smallSmoke, smallFlame);
        ClientUtil.createCustomCubeParticles(middlePos, this.level, smallSmoke, smallFlame);
        ClientUtil.createCustomCubeParticles(endPos, this.level, smallSmoke, smallFlame);
    }
    
    FluidOutputBlockEntity getOutputBlockEntity() {
        if (level == null) {
            return null;
        }
        return (FluidOutputBlockEntity) level.getBlockEntity(multiblockStructure.getFluidOutputPosition("reverbaratory.primary_fluid_output"));
    }
    
    FluidOutputBlockEntity getCarbonDioxideOutputBlockEntity() {
        if (level == null) {
            return null;
        }
        return (FluidOutputBlockEntity) level.getBlockEntity(multiblockStructure.getFluidOutputPosition("reverbaratory.carbon_dioxide_output"));
    }
    
    FluidOutputBlockEntity getSlagOutputBlockEntity() {
        if (level == null) {
            return null;
        }
        return (FluidOutputBlockEntity) level.getBlockEntity(multiblockStructure.getFluidOutputPosition("reverbaratory.slag_output"));
    }
    
}
