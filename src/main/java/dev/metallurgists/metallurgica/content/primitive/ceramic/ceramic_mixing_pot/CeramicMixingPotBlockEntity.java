package dev.metallurgists.metallurgica.content.primitive.ceramic.ceramic_mixing_pot;

import dev.metallurgists.metallurgica.foundation.util.MetalLang;
import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.fluids.particle.FluidParticleData;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.simple.DeferralBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.*;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.IntAttached;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CeramicMixingPotBlockEntity extends GeneratingKineticBlockEntity implements IHaveGoggleInformation {
    public int inUse;
    
    private boolean areFluidsMoving;
    LerpedFloat ingredientRotationSpeed;
    LerpedFloat ingredientRotation;
    
    public CeramicMixingPotInventory inputInventory;
    public SmartFluidTankBehaviour inputTank;
    protected SmartInventory outputInventory;
    protected SmartFluidTankBehaviour outputTank;
    public DeferralBehaviour checker;
    
    private FilteringBehaviour filtering;
    
    public boolean shouldReset;
    
    private boolean contentsChanged;
    
    private final Couple<SmartInventory> invs;
    private final Couple<SmartFluidTankBehaviour> tanks;
    
    protected LazyOptional<IItemHandlerModifiable> itemCapability;
    protected LazyOptional<IFluidHandler> fluidCapability;
    
    public static final int OUTPUT_ANIMATION_TIME = 10;
    List<IntAttached<ItemStack>> visualizedOutputItems;
    List<IntAttached<FluidStack>> visualizedOutputFluids;
    
    public int runningTicks;
    public int processingTicks;
    public boolean running;
    
    public boolean backwards;
    public float independentAngle;
    public float chasingVelocity;
    
    private static final Object ceramicMixingRecipeKey = new Object();
    protected Recipe<?> currentRecipe;
    
    public CeramicMixingPotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInventory = new CeramicMixingPotInventory(3, this);
        inputInventory.whenContentsChanged($ -> contentsChanged = true);
        outputInventory = new CeramicMixingPotInventory(3, this).forbidInsertion()
                .withMaxStackSize(64);
        areFluidsMoving = false;
        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inputInventory, outputInventory));
        contentsChanged = true;
        ingredientRotation = LerpedFloat.angular()
                .startWithValue(0);
        ingredientRotationSpeed = LerpedFloat.linear()
                .startWithValue(0);
        
        invs = Couple.create(inputInventory, outputInventory);
        tanks = Couple.create(inputTank, outputTank);
        visualizedOutputItems = Collections.synchronizedList(new ArrayList<>());
        visualizedOutputFluids = Collections.synchronizedList(new ArrayList<>());
    }
    
    public float getIndependentAngle(float partialTicks) {
        return (independentAngle + partialTicks * chasingVelocity) / 360;
    }
    
    public void stir(boolean back) {
        boolean update = false;
        
        if (getGeneratedSpeed() == 0 || back != backwards)
            update = true;
        
        inUse = 10;
        this.backwards = back;
        if (update && !level.isClientSide)
            updateGeneratedRotation();
    }
    
    @Override
    public float getGeneratedSpeed() {
        Block block = getBlockState().getBlock();
        if (!(block instanceof CeramicMixingPotBlock))
            return 0;
        CeramicMixingPotBlock crank = (CeramicMixingPotBlock) block;
        int speed = (inUse == 0 ? 0 : clockwise() ? -1 : 1) * crank.getRotationSpeed();
        return convertToDirection(speed, Direction.UP);
    }
    
    protected boolean clockwise() {
        return backwards;
    }
    
    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        if (getSpeed() == 0)
            shouldReset = true;
        shouldReset = false;
        checker.scheduleUpdate();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (level == null)
            return;
        if (inUse > 0) {
            inUse--;
            
            if (inUse == 0 && !level.isClientSide) {
                sequenceContext = null;
                updateGeneratedRotation();
            }
        }
        if (level.isClientSide) {
            createFluidParticles();
            tickVisualizedOutputs();
            ingredientRotationSpeed.tickChaser();
            ingredientRotation.setValue(ingredientRotation.getValue() + ingredientRotationSpeed.getValue());
        }
        
        if (shouldReset) {
            shouldReset = false;
            reset();
            sendData();
            return;
        }
        
        float actualSpeed = getSpeed();
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;
        
        if (runningTicks >= 40) {
            running = false;
            runningTicks = 0;
            checker.scheduleUpdate();
        }
        
        
        float speed = Math.abs(getSpeed());
        if (running && level != null) {
            if (level.isClientSide && runningTicks == 20)
                renderParticles();
            if ((!level.isClientSide || isVirtual()) && runningTicks == 20) {
                if (processingTicks < 0) {
                    float recipeSpeed = 1;
                    if (currentRecipe instanceof ProcessingRecipe) {
                        int t = ((ProcessingRecipe<?>) currentRecipe).getProcessingDuration();
                        if (t != 0)
                            recipeSpeed = t / 100f;
                    }
                    
                    processingTicks = Mth.clamp((Mth.log2((int) (512 / speed))) * Mth.ceil(recipeSpeed * 15) + 1, 1, 512);
                    
                    if (!tanks.getFirst().isEmpty() || !tanks.getSecond().isEmpty())
                        level.playSound(null, worldPosition, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT,
                                SoundSource.BLOCKS, .75f, .75f);
                } else {
                    processingTicks--;
                    if (processingTicks == 0) {
                        runningTicks++;
                        processingTicks = -1;
                        applyRecipe();
                        sendData();
                    }
                }
            }
            
            if (runningTicks != 20)
                runningTicks++;
        }
    }
    
    protected void reset() {
        if (!running)
            return;
        runningTicks = 40;
        running = false;
    }
    
    protected boolean update() {
        if (isRunning())
            return true;
        if (getSpeed() == 0)
            return true;
        if (level == null || level.isClientSide)
            return true;
        List<Recipe<?>> recipes = getMatchingRecipes();
        if (recipes.isEmpty())
            return true;
        currentRecipe = recipes.get(0);
        startProcessing();
        sendData();
        return true;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public void startProcessing() {
        if (running && runningTicks <= 20)
            return;
        running = true;
        runningTicks = 0;
    }
    
    protected void applyRecipe() {
        if (currentRecipe == null)
            return;
        
        if (!CeramicMixingRecipe.apply(this, currentRecipe))
            return;
        inputTank.sendDataImmediately();
        
        // Continue mixing
        if (matchRecipe(currentRecipe)) {
            continueWithPreviousRecipe();
            sendData();
        }
        
        notifyChangeOfContents();
    }
    
    public void continueWithPreviousRecipe() {
        runningTicks = 20;
    }
    
    protected List<Recipe<?>> getMatchingRecipes() {
        if (this.isEmpty())
            return new ArrayList<>();
        
        List<Recipe<?>> list = RecipeFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters);
        return list.stream()
                .filter(this::matchRecipe)
                .sorted((r1, r2) -> r2.getIngredients().size() - r1.getIngredients().size())
                .collect(Collectors.toList());
    }
    
    
    protected <C extends Container> boolean matchRecipe(Recipe<C> recipe) {
        if (recipe == null)
            return false;
        return CeramicMixingRecipe.match(this, recipe);
    }
    
    
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r.getType() == MetallurgicaRecipeTypes.ceramic_mixing.getType();
    }
    
    protected Object getRecipeCacheKey() {
        return ceramicMixingRecipeKey;
    }
    
    public boolean acceptOutputs(List<ItemStack> outputItems, List<FluidStack> outputFluids, boolean simulate) {
        outputInventory.allowInsertion();
        outputTank.allowInsertion();
        boolean acceptOutputsInner = acceptOutputsInner(outputItems, outputFluids, simulate);
        outputInventory.forbidInsertion();
        outputTank.forbidInsertion();
        return acceptOutputsInner;
    }
    
    private boolean acceptOutputsInner(List<ItemStack> outputItems, List<FluidStack> outputFluids, boolean simulate) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof BasinBlock))
            return false;
        
        Direction direction = blockState.getValue(BasinBlock.FACING);
        if (direction != Direction.DOWN) {
            
            BlockEntity be = level.getBlockEntity(worldPosition.below().relative(direction));
            
            InvManipulationBehaviour inserter = be == null ? null : BlockEntityBehaviour.get(level, be.getBlockPos(), InvManipulationBehaviour.TYPE);
            IItemHandler targetInv = be == null ? null : be.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).orElse(inserter == null ? null : inserter.getInventory());
            IFluidHandler targetTank = be == null ? null : be.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite()).orElse(null);
            boolean externalTankNotPresent = targetTank == null;
            
            if (!outputItems.isEmpty() && targetInv == null)
                return false;
            if (!outputFluids.isEmpty() && externalTankNotPresent) {
                // Special case - fluid outputs but output only accepts items
                targetTank = outputTank.getCapability()
                        .orElse(null);
                if (targetTank == null)
                    return false;
                if (!acceptFluidOutputsIntoBasin(outputFluids, simulate, targetTank))
                    return false;
            }
            
            if (simulate)
                return true;
            return true;
        }
        
        IItemHandler targetInv = outputInventory;
        IFluidHandler targetTank = outputTank.getCapability()
                .orElse(null);
        
        if (targetInv == null && !outputItems.isEmpty())
            return false;
        if (!acceptItemOutputsIntoBasin(outputItems, simulate, targetInv))
            return false;
        if (outputFluids.isEmpty())
            return true;
        if (targetTank == null)
            return false;
        if (!acceptFluidOutputsIntoBasin(outputFluids, simulate, targetTank))
            return false;
        
        return true;
    }
    
    public FilteringBehaviour getFilter() {
        return filtering;
    }
    
    
    
    private boolean acceptFluidOutputsIntoBasin(List<FluidStack> outputFluids, boolean simulate,
                                                IFluidHandler targetTank) {
        for (FluidStack fluidStack : outputFluids) {
            IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
            int fill = targetTank instanceof SmartFluidTankBehaviour.InternalFluidHandler
                    ? ((SmartFluidTankBehaviour.InternalFluidHandler) targetTank).forceFill(fluidStack.copy(), action)
                    : targetTank.fill(fluidStack.copy(), action);
            if (fill != fluidStack.getAmount())
                return false;
        }
        return true;
    }
    
    private boolean acceptItemOutputsIntoBasin(List<ItemStack> outputItems, boolean simulate, IItemHandler targetInv) {
        for (ItemStack itemStack : outputItems) {
            if (!ItemHandlerHelper.insertItemStacked(targetInv, itemStack.copy(), simulate)
                    .isEmpty())
                return false;
        }
        return true;
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));
        filtering = new FilteringBehaviour(this, new MixingValueBox()).withCallback(newFilter -> contentsChanged = true)
                .forRecipes();
        behaviours.add(filtering);
        
        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 2, 1000, true)
                .whenFluidUpdates(() -> contentsChanged = true);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 2, 1000, true)
                .whenFluidUpdates(() -> contentsChanged = true)
                .forbidInsertion();
        behaviours.add(inputTank);
        behaviours.add(outputTank);
        
        fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = inputTank.getCapability();
            LazyOptional<? extends IFluidHandler> outputCap = outputTank.getCapability();
            return new CombinedTankWrapper(outputCap.orElse(null), inputCap.orElse(null));
        });
        
        checker = new DeferralBehaviour(this, this::update);
        behaviours.add(checker);
    }
    
    static class MixingValueBox extends ValueBoxTransform.Sided {
        
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 10, 14.05);
        }
        
        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction.getAxis()
                    .isHorizontal();
        }
        
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inUse = compound.getInt("InUse");
        backwards = compound.getBoolean("Backwards");
        running = compound.getBoolean("Running");
        runningTicks = compound.getInt("Ticks");
        inputInventory.deserializeNBT(compound.getCompound("InputItems"));
        outputInventory.deserializeNBT(compound.getCompound("OutputItems"));
        
        if (!clientPacket)
            return;
        
        NBTHelper.iterateCompoundList(compound.getList("VisualizedItems", Tag.TAG_COMPOUND),
                c -> visualizedOutputItems.add(IntAttached.with(OUTPUT_ANIMATION_TIME, ItemStack.of(c))));
        NBTHelper.iterateCompoundList(compound.getList("VisualizedFluids", Tag.TAG_COMPOUND),
                c -> visualizedOutputFluids
                        .add(IntAttached.with(OUTPUT_ANIMATION_TIME, FluidStack.loadFluidStackFromNBT(c))));
        
        setAreFluidsMoving(running && runningTicks <= 20);
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("InUse", inUse);
        compound.putBoolean("Backwards", backwards);
        compound.putBoolean("Running", running);
        compound.putInt("Ticks", runningTicks);
        compound.put("InputItems", inputInventory.serializeNBT());
        compound.put("OutputItems", outputInventory.serializeNBT());
        
        if (!clientPacket)
            return;
        
        compound.put("VisualizedItems", NBTHelper.writeCompoundList(visualizedOutputItems, ia -> ia.getValue()
                .serializeNBT()));
        compound.put("VisualizedFluids", NBTHelper.writeCompoundList(visualizedOutputFluids, ia -> ia.getValue()
                .writeToNBT(new CompoundTag())));
        visualizedOutputItems.clear();
        visualizedOutputFluids.clear();
    }
    
    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
        ItemHelper.dropContents(level, worldPosition, outputInventory);
    }
    
    @Override
    public void remove() {
        super.remove();
        onEmptied();
    }
    
    public void onEmptied() {
    
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
        fluidCapability.invalidate();
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    
    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }
    
    @Override
    public void lazyTick() {
        super.lazyTick();
        
        if (!level.isClientSide) {
            if (isEmpty())
                return;
            notifyChangeOfContents();
            return;
        }
        
        setAreFluidsMoving(running && runningTicks <= 20);
    }
    
    public boolean isEmpty() {
        return inputInventory.isEmpty() && outputInventory.isEmpty() && inputTank.isEmpty() && outputTank.isEmpty();
    }
    
    public float getTotalFluidUnits(float partialTicks) {
        int renderedFluids = 0;
        float totalUnits = 0;
        
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.getRenderedFluid()
                        .isEmpty())
                    continue;
                float units = tankSegment.getTotalUnits(partialTicks);
                if (units < 1)
                    continue;
                totalUnits += units;
                renderedFluids++;
            }
        }
        
        if (renderedFluids == 0)
            return 0;
        if (totalUnits < 1)
            return 0;
        return totalUnits;
    }
    
    public void notifyChangeOfContents() {
        contentsChanged = true;
    }
    
    public SmartInventory getInputInventory() {
        return inputInventory;
    }
    
    public SmartInventory getOutputInventory() {
        return outputInventory;
    }
    
    public static BlazeBurnerBlock.HeatLevel getHeatLevelOf(BlockState state) {
        if (state.hasProperty(BlazeBurnerBlock.HEAT_LEVEL))
            return state.getValue(BlazeBurnerBlock.HEAT_LEVEL);
        return AllTags.AllBlockTags.PASSIVE_BOILER_HEATERS.matches(state) && BlockHelper.isNotUnheated(state) ? BlazeBurnerBlock.HeatLevel.SMOULDERING : BlazeBurnerBlock.HeatLevel.NONE;
    }
    
    public Couple<SmartFluidTankBehaviour> getTanks() {
        return tanks;
    }
    
    public Couple<SmartInventory> getInvs() {
        return invs;
    }
    
    // client things
    
    private void tickVisualizedOutputs() {
        visualizedOutputFluids.forEach(IntAttached::decrement);
        visualizedOutputItems.forEach(IntAttached::decrement);
        visualizedOutputFluids.removeIf(IntAttached::isOrBelowZero);
        visualizedOutputItems.removeIf(IntAttached::isOrBelowZero);
    }
    
    private void createFluidParticles() {
        RandomSource r = level.random;
        
        if (!visualizedOutputFluids.isEmpty())
            createOutputFluidParticles(r);
        
        if (!areFluidsMoving && r.nextFloat() > 1 / 8f)
            return;
        
        int segments = 0;
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks())
                if (!tankSegment.isEmpty(0))
                    segments++;
        }
        if (segments < 2)
            return;
        
        float totalUnits = getTotalFluidUnits(0);
        if (totalUnits == 0)
            return;
        float fluidLevel = Mth.clamp(totalUnits / 2000, 0, 1);
        float rim = 4 / 16f;
        float space = 11 / 16f;
        float surface = worldPosition.getY() + rim + space * fluidLevel + 1 / 32f;
        
        if (areFluidsMoving) {
            createMovingFluidParticles(surface, segments);
            return;
        }
        
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                float x = worldPosition.getX() + rim + space * r.nextFloat();
                float z = worldPosition.getZ() + rim + space * r.nextFloat();
                level.addAlwaysVisibleParticle(
                        new FluidParticleData(AllParticleTypes.BASIN_FLUID.get(), tankSegment.getRenderedFluid()), x,
                        surface, z, 0, 0, 0);
            }
        }
    }
    
    private void createOutputFluidParticles(RandomSource r) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof BasinBlock))
            return;
        Direction direction = blockState.getValue(BasinBlock.FACING);
        if (direction == Direction.DOWN)
            return;
        Vec3 directionVec = Vec3.atLowerCornerOf(direction.getNormal());
        Vec3 outVec = VecHelper.getCenterOf(worldPosition)
                .add(directionVec.scale(.65)
                        .subtract(0, 1 / 4f, 0));
        Vec3 outMotion = directionVec.scale(1 / 16f)
                .add(0, -1 / 16f, 0);
        
        for (int i = 0; i < 2; i++) {
            visualizedOutputFluids.forEach(ia -> {
                FluidStack fluidStack = ia.getValue();
                ParticleOptions fluidParticle = FluidFX.getFluidParticle(fluidStack);
                Vec3 m = VecHelper.offsetRandomly(outMotion, r, 1 / 16f);
                level.addAlwaysVisibleParticle(fluidParticle, outVec.x, outVec.y, outVec.z, m.x, m.y, m.z);
            });
        }
    }
    
    private void createMovingFluidParticles(float surface, int segments) {
        Vec3 pointer = new Vec3(1, 0, 0).scale(1 / 16f);
        float interval = 360f / segments;
        Vec3 centerOf = VecHelper.getCenterOf(worldPosition);
        float intervalOffset = (AnimationTickHolder.getTicks() * 18) % 360;
        
        int currentSegment = 0;
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                float angle = interval * (1 + currentSegment) + intervalOffset;
                Vec3 vec = centerOf.add(VecHelper.rotate(pointer, angle, Direction.Axis.Y));
                level.addAlwaysVisibleParticle(
                        new FluidParticleData(AllParticleTypes.BASIN_FLUID.get(), tankSegment.getRenderedFluid()), vec.x(),
                        surface, vec.z(), 1, 0, 0);
                currentSegment++;
            }
        }
    }
    
    public boolean areFluidsMoving() {
        return areFluidsMoving;
    }
    
    public boolean setAreFluidsMoving(boolean areFluidsMoving) {
        this.areFluidsMoving = areFluidsMoving;
        ingredientRotationSpeed.chase(areFluidsMoving ? 20 : 0, .1f, LerpedFloat.Chaser.EXP);
        return areFluidsMoving;
    }
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        MetalLang.translate("gui.goggles.basin_contents")
                .forGoggles(tooltip);
        
        IItemHandlerModifiable items = itemCapability.orElse(new ItemStackHandler());
        IFluidHandler fluids = fluidCapability.orElse(new FluidTank(0));
        boolean isEmpty = true;
        
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stackInSlot = items.getStackInSlot(i);
            if (stackInSlot.isEmpty())
                continue;
            MetalLang.text("")
                    .add(Component.translatable(stackInSlot.getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(MetalLang.text(" x" + stackInSlot.getCount())
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }
        
        LangBuilder mb = MetalLang.translate("generic.unit.millibuckets");
        for (int i = 0; i < fluids.getTanks(); i++) {
            FluidStack fluidStack = fluids.getFluidInTank(i);
            if (fluidStack.isEmpty())
                continue;
            MetalLang.text("")
                    .add(MetalLang.fluidName(fluidStack)
                            .add(MetalLang.text(" "))
                            .style(ChatFormatting.GRAY)
                            .add(MetalLang.number(fluidStack.getAmount())
                                    .add(mb)
                                    .style(ChatFormatting.BLUE)))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }
        
        if (currentRecipe != null) {
            tooltip.add(Component.literal(currentRecipe.getId().toString()));
        } else tooltip.add(Component.literal("No recipe"));
        
        if (isEmpty)
            tooltip.remove(0);
        
        return true;
    }
    
    public void renderParticles() {
        if (level == null)
            return;
        
        for (SmartInventory inv : getInvs()) {
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getItem(slot);
                if (stackInSlot.isEmpty())
                    continue;
                ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
                spillParticle(data);
            }
        }
        
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                spillParticle(FluidFX.getFluidParticle(tankSegment.getRenderedFluid()));
            }
        }
    }
    
    protected void spillParticle(ParticleOptions data) {
        float angle = level.random.nextFloat() * 360;
        Vec3 offset = new Vec3(0, 0, 0.25f);
        offset = VecHelper.rotate(offset, angle, Direction.Axis.Y);
        Vec3 target = VecHelper.rotate(offset, 25, Direction.Axis.Y)
                .add(0, .25f, 0);
        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));
        target = VecHelper.offsetRandomly(target.subtract(offset), level.random, 1 / 128f);
        level.addParticle(data, center.x, center.y - 1.75f, center.z, target.x, target.y, target.z);
    }
}
