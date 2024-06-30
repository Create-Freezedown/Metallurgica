package com.freezedown.metallurgica.content.machines.reverbaratory;

import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_furnace.BlastFurnaceOutputBlockEntity;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.freezedown.metallurgica.foundation.util.ClientUtil.createCustomCubeParticles;

public class ReverbaratoryBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    public ReverbaratoryCookingRecipe recipe;
    protected LazyOptional<IFluidHandler> fluidCapability = LazyOptional.of(() -> this.tankInventory);
    public LazyOptional<IItemHandlerModifiable> itemCapability = LazyOptional.of(() -> new InvWrapper(this.inputInventory));
    public SmartInventory inputInventory;
    public FluidTank tankInventory = this.createInventory();
    public boolean isValid = false;
    public int timer;
    public ReverbaratoryChecker checker;
    public float fuelEfficiency;
    public float speedModifier;
    
    public ReverbaratoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.timer = -1;
        this.inputInventory = (new SmartInventory(1, this)).forbidInsertion().forbidExtraction().withMaxStackSize(64);
        this.checker = new ReverbaratoryChecker(this);
        this.fuelEfficiency = 1000.0F;
        this.speedModifier = 1.0F;
    }
    
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(4000, this::onFluidStackChanged) {
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().is(MetallurgicaTags.AllFluidTags.REVERBARATORY_FUELS.tag);
            }
        };
    }
    
    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
    }
    
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        this.sendData();
    }
    public void lazyTick() {
        super.lazyTick();
        if (this.level == null) {
            return;
        }
        checker.createMissingSegmentParticles();
        
    }
    
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        
        isValid = checker.checkReverbaratory();
        if (isValid) {
            this.acceptInsertedItems();
        }
        
        this.fuelEfficiency = 400.0F;
        this.speedModifier = 1.0F;
        
        
        if (this.recipe != null && this.timer == -1 && checker.getOutputBlockEntity().tank1.getPrimaryHandler().getFluidAmount() + this.recipe.getFluidResults().get(0).getAmount() <= checker.getOutputBlockEntity().tank1.getPrimaryHandler().getCapacity() && checker.getOutputBlockEntity().tank2.getPrimaryHandler().getFluidAmount() + this.recipe.getFluidResults().get(1).getAmount() <= checker.getOutputBlockEntity().tank2.getPrimaryHandler().getCapacity() && isValid && !this.tankInventory.isEmpty()) {
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
        
        if (isValid && this.timer > 0 && checker.getOutputBlockEntity().tank1.getPrimaryHandler().getFluidAmount() + ((FluidStack)this.recipe.getFluidResults().get(0)).getAmount() <= checker.getOutputBlockEntity().tank1.getPrimaryHandler().getCapacity() && checker.getOutputBlockEntity().tank2.getPrimaryHandler().getFluidAmount() + ((FluidStack)this.recipe.getFluidResults().get(1)).getAmount() <= checker.getOutputBlockEntity().tank2.getPrimaryHandler().getCapacity()) {
            --this.timer;
            createSmokeVolume();
            for (LivingEntity entity : this.getEntitiesToCremate()) {
                if (level.random.nextDouble() > 0.85) {
                    entity.hurt(getDamageSource(), 0.5F);
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
    
    private void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = this.fluidCapability;
        this.fluidCapability = LazyOptional.of(this::handlerForCapability);
        oldCap.invalidate();
    }
    
    private IFluidHandler handlerForCapability() {
        return this.tankInventory;
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.fluidCapability.isPresent()) {
            this.refreshCapability();
        }
        
        return cap == ForgeCapabilities.FLUID_HANDLER ? this.fluidCapability.cast() : super.getCapability(cap, side);
    }
    
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.inputInventory.deserializeNBT(compound.getCompound("InputItems"));
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("InputItems", this.inputInventory.serializeNBT());
    }
    
    public void invalidate() {
        super.invalidate();
        this.itemCapability.invalidate();
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    public void process() {
        if (this.level == null) {
            return;
        }
        if (!this.level.isClientSide) {
            checker.getOutputBlockEntity().tank1.getPrimaryHandler().setFluid(new FluidStack(this.recipe.getFluidResults().get(0).getFluid(), checker.getOutputBlockEntity().tank1.getPrimaryHandler().getFluidAmount() + this.recipe.getFluidResults().get(0).getAmount()));
            checker.getOutputBlockEntity().tank2.getPrimaryHandler().setFluid(new FluidStack(this.recipe.getFluidResults().get(1).getFluid(), checker.getOutputBlockEntity().tank2.getPrimaryHandler().getFluidAmount() + this.recipe.getFluidResults().get(1).getAmount()));
        }
    }
    
    public void acceptInsertedItems() {
        List<ItemEntity> itemsToPick = this.getItemsToPick();
        Iterator<ItemEntity> var2 = itemsToPick.iterator();
        
        while(true) {
            while(var2.hasNext()) {
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
        int messageType = Mth.randomBetweenInclusive(level.random, 1, 4);
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
            Lang.translate("goggles.reverbaratory.stats", new Object[0]).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
            if (this.timer > 0) {
                Lang.translate("goggles.blast_furnace.status.running", new Object[0]).style(ChatFormatting.YELLOW).forGoggles(tooltip, 1);
            } else {
                Lang.translate("goggles.blast_furnace.status.off", new Object[0]).style(ChatFormatting.YELLOW).forGoggles(tooltip, 1);
            }
            Lang.translate("goggles.misc.storage_info").style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
            Lang.translate("goggles.blast_furnace.item_count", this.inputInventory.getStackInSlot(0).getCount()).style(ChatFormatting.AQUA).forGoggles(tooltip, 1);
            Lang.translate("goggles.blast_furnace.nothing_lol").style(ChatFormatting.AQUA).forGoggles(tooltip, 1);
        }
        
        LazyOptional<IFluidHandler> handler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent()) {
            return false;
        } else {
            IFluidHandler tank = (IFluidHandler)resolve.get();
            if (tank.getTanks() == 0) {
                return false;
            } else {
                LangBuilder mb = Lang.translate("generic.unit.millibuckets");
                boolean isEmpty = true;
                
                for(int i = 0; i < tank.getTanks(); ++i) {
                    FluidStack fluidStack = tank.getFluidInTank(i);
                    if (!fluidStack.isEmpty()) {
                        Lang.translate("goggles.reverbaratory.fuel_info").style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
                        Lang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                        Lang.builder().add(Lang.number((double)fluidStack.getAmount()).add(mb).style(ChatFormatting.DARK_GREEN)).text(ChatFormatting.GRAY, " / ").add(Lang.number((double)tank.getTankCapacity(i)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
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
                    Lang.translate("gui.goggles.fluid_container.capacity", new Object[0]).add(Lang.number((double)tank.getTankCapacity(0)).add(mb).style(ChatFormatting.DARK_GREEN)).style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
                    return true;
                }
            }
        }
    }
    
    int logCounter = 0;
    public void createSmokeVolume() {
        Direction facing = this.getBlockState().getValue(ReverbaratoryBlock.FACING);
        BlockPos firstPos = this.getBlockPos().relative(facing, 1);
        BlockPos middlePos = this.getBlockPos().relative(facing, 2);
        BlockPos endPos = this.getBlockPos().relative(facing, 3);
        
        logCounter++;
        if (logCounter > 200) {
            Metallurgica.LOGGER.info("FirstPos: {}", firstPos);
            Metallurgica.LOGGER.info("MiddlePos: {}", middlePos);
            Metallurgica.LOGGER.info("EndPos: {}", endPos);
            logCounter = 0;
        }
        
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
    
    
    public class ReverbaratoryChecker {
        @Getter
        private final ReverbaratoryBlockEntity blockEntity;
        
        public ReverbaratoryChecker(ReverbaratoryBlockEntity blockEntity) {
            this.blockEntity = blockEntity;
        }
        
        
        public boolean checkReverbaratory() {
            return checkSegments();
        }
        
        ReverbaratoryOutputBlockEntity getOutputBlockEntity() {
            if (level == null) {
                return null;
            }
            return (ReverbaratoryOutputBlockEntity) level.getBlockEntity(getOutputPos());
        }
        
        ReverbaratoryCarbonOutputBlockEntity getCarbonDioxideOutputBlockEntity() {
            if (level == null) {
                return null;
            }
            return (ReverbaratoryCarbonOutputBlockEntity) level.getBlockEntity(getCarbonDioxideOutputPos());
        }
        
        private final TagKey<Block> reverbaratoryWallTag = MetallurgicaTags.AllBlockTags.REVERBARATORY_WALL.tag;
        private final TagKey<Block> reverbaratoryGlassTag = MetallurgicaTags.AllBlockTags.REVERBARATORY_GLASS.tag;
        private final TagKey<Block> reverbaratoryInputTag = MetallurgicaTags.AllBlockTags.REVERBARATORY_INPUT.tag;
        Direction getFacing() {
            return blockEntity.getBlockState().getValue(ReverbaratoryBlock.FACING);
        }
        BlockPos getMainPos() {
            return blockEntity.getBlockPos();
        }
        BlockPos getRightSidePos(Direction facing, BlockPos pos) {
            return pos.relative(facing.getClockWise());
        }
        BlockPos getRightSidePos(Direction facing) {
            return getMainPos().relative(facing.getClockWise());
        }
        BlockPos getRightSidePos(Direction facing, int distance) {
            return getMainPos().relative(facing.getClockWise(), distance);
        }
        BlockPos getLeftSidePos(Direction facing, BlockPos pos) {
            return pos.relative(facing.getCounterClockWise());
        }
        BlockPos getLeftSidePos(Direction facing) {
            return getMainPos().relative(facing.getCounterClockWise());
        }
        BlockPos getLeftSidePos(Direction facing, int distance) {
            return getMainPos().relative(facing.getCounterClockWise(), distance);
        }
        BlockPos getOutputPos() {
            return getMainPos().relative(getFacing(), 3).below();
        }
        BlockPos getCarbonDioxideOutputPos() {
            return getMainPos().relative(getFacing(), 3).above();
        }
        BlockPos getInputPos() {
            return getMainPos().relative(getFacing(), 1).above();
        }
        
        public boolean checkForWall(BlockPos pos) {
            if (level == null) {
                return false;
            }
            return level.getBlockState(pos).is(reverbaratoryWallTag);
        }
        public boolean checkForWallOrGlass(BlockPos pos) {
            if (level == null) {
                return false;
            }
            return level.getBlockState(pos).is(reverbaratoryWallTag) || level.getBlockState(pos).is(reverbaratoryGlassTag);
        }
        
        public boolean checkSegment1() {
            boolean above = checkForWall(getMainPos().above());
            boolean below = checkForWall(getMainPos().below());
            boolean right = checkForWall(getRightSidePos(getFacing()));
            boolean left = checkForWall(getLeftSidePos(getFacing()));
            boolean rightAbove = checkForWall(getRightSidePos(getFacing()).above());
            boolean leftAbove = checkForWall(getLeftSidePos(getFacing()).above());
            boolean rightBelow = checkForWall(getRightSidePos(getFacing()).below());
            boolean leftBelow = checkForWall(getLeftSidePos(getFacing()).below());
            return above && below && right && left && rightAbove && leftAbove && rightBelow && leftBelow;
        }
        public boolean checkSegment2() {
            if (level == null) {
                return false;
            }
            BlockPos segment2Pos = getMainPos().relative(getFacing(), 1);
            boolean above = level.getBlockState(getInputPos()).is(reverbaratoryInputTag);
            boolean below = checkForWall(segment2Pos.below());
            boolean right = checkForWallOrGlass(getRightSidePos(getFacing(), segment2Pos));
            boolean left = checkForWallOrGlass(getLeftSidePos(getFacing(), segment2Pos));
            boolean rightAbove = checkForWall(getRightSidePos(getFacing(), segment2Pos).above());
            boolean leftAbove = checkForWall(getLeftSidePos(getFacing(), segment2Pos).above());
            boolean rightBelow = checkForWall(getRightSidePos(getFacing(), segment2Pos).below());
            boolean leftBelow = checkForWall(getLeftSidePos(getFacing(), segment2Pos).below());
            
            return above && below && right && left && rightAbove && leftAbove && rightBelow && leftBelow;
        }
        
        public boolean checkSegment3() {
            BlockPos segment3Pos = getMainPos().relative(getFacing(), 2);
            boolean above = checkForWall(segment3Pos.above());
            boolean below = checkForWall(segment3Pos.below());
            boolean right = checkForWallOrGlass(getRightSidePos(getFacing(), segment3Pos));
            boolean left = checkForWallOrGlass(getLeftSidePos(getFacing(), segment3Pos));
            boolean rightAbove = checkForWall(getRightSidePos(getFacing(), segment3Pos).above());
            boolean leftAbove = checkForWall(getLeftSidePos(getFacing(), segment3Pos).above());
            boolean rightBelow = checkForWall(getRightSidePos(getFacing(), segment3Pos).below());
            boolean leftBelow = checkForWall(getLeftSidePos(getFacing(), segment3Pos).below());
            
            return above && below && right && left && rightAbove && leftAbove && rightBelow && leftBelow;
        }
        
        public boolean checkSegment4() {
            if (level == null) {
                return false;
            }
            BlockPos segment4Pos = getMainPos().relative(getFacing(), 3);
            boolean above = level.getBlockState(segment4Pos.above()).getBlock() instanceof ReverbaratoryCarbonOutputBlock;
            boolean below = level.getBlockState(segment4Pos.below()).getBlock() instanceof ReverbaratoryOutputBlock;
            boolean right = checkForWallOrGlass(getRightSidePos(getFacing(), segment4Pos));
            boolean left = checkForWallOrGlass(getLeftSidePos(getFacing(), segment4Pos));
            boolean rightAbove = checkForWall(getRightSidePos(getFacing(), segment4Pos).above());
            boolean leftAbove = checkForWall(getLeftSidePos(getFacing(), segment4Pos).above());
            boolean rightBelow = checkForWall(getRightSidePos(getFacing(), segment4Pos).below());
            boolean leftBelow = checkForWall(getLeftSidePos(getFacing(), segment4Pos).below());
            
            return above && below && right && left && rightAbove && leftAbove && rightBelow && leftBelow;
        }
        
        public boolean checkSegment5() {
            BlockPos segment5Pos = getMainPos().relative(getFacing(), 4);
            boolean middle = checkForWall(segment5Pos);
            boolean above = checkForWall(segment5Pos.above());
            boolean below = checkForWall(segment5Pos.below());
            boolean right = checkForWall(getRightSidePos(getFacing(), segment5Pos));
            boolean left = checkForWall(getLeftSidePos(getFacing(), segment5Pos));
            boolean rightAbove = checkForWall(getRightSidePos(getFacing(), segment5Pos).above());
            boolean leftAbove = checkForWall(getLeftSidePos(getFacing(), segment5Pos).above());
            boolean rightBelow = checkForWall(getRightSidePos(getFacing(), segment5Pos).below());
            boolean leftBelow = checkForWall(getLeftSidePos(getFacing(), segment5Pos).below());
            
            return middle && above && below && right && left && rightAbove && leftAbove && rightBelow && leftBelow;
        }
        
        public boolean checkSegments() {
            if (level == null) {
                Metallurgica.LOGGER.error("Level is null, cannot check segments");
                return false;
            }
            return checkSegment1() && checkSegment2() && checkSegment3() && checkSegment4() && checkSegment5();
        }
        
        public void createMissingSegmentParticles() {
            ParticleOptions smokeParticle = ParticleTypes.SMOKE;
            if (level == null) {
                Metallurgica.LOGGER.error("Level is null, cannot create particles");
                return;
            }
            if (!checkSegment1()) {
                createCustomCubeParticles(getMainPos().above(), level, smokeParticle);
                createCustomCubeParticles(getMainPos().below(), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing()), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing()), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing()).above(), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing()).above(), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing()).below(), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing()).below(), level, smokeParticle);
            }
            if (!checkSegment2()) {
                BlockPos segment2Pos = getMainPos().relative(getFacing(), 1);
                createCustomCubeParticles(segment2Pos.above(), level, smokeParticle);
                createCustomCubeParticles(segment2Pos.below(), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment2Pos), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment2Pos), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment2Pos).above(), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment2Pos).above(), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment2Pos).below(), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment2Pos).below(), level, smokeParticle);
            }
            if (!checkSegment3()) {
                BlockPos segment3Pos = getMainPos().relative(getFacing(), 2);
                createCustomCubeParticles(segment3Pos.above(), level, smokeParticle);
                createCustomCubeParticles(segment3Pos.below(), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment3Pos), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment3Pos), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment3Pos).above(), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment3Pos).above(), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment3Pos).below(), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment3Pos).below(), level, smokeParticle);
            }
            if (!checkSegment4()) {
                BlockPos segment4Pos = getMainPos().relative(getFacing(), 3);
                createCustomCubeParticles(segment4Pos.above(), level, smokeParticle);
                createCustomCubeParticles(segment4Pos.below(), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment4Pos), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment4Pos), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment4Pos).above(), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment4Pos).above(), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment4Pos).below(), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment4Pos).below(), level, smokeParticle);
            }
            if (!checkSegment5()) {
                BlockPos segment5Pos = getMainPos().relative(getFacing(), 4);
                createCustomCubeParticles(segment5Pos.above(), level, smokeParticle);
                createCustomCubeParticles(segment5Pos.below(), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment5Pos), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment5Pos), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment5Pos).above(), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment5Pos).above(), level, smokeParticle);
                createCustomCubeParticles(getRightSidePos(getFacing(), segment5Pos).below(), level, smokeParticle);
                createCustomCubeParticles(getLeftSidePos(getFacing(), segment5Pos).below(), level, smokeParticle);
            }
        }
    }
    
}
