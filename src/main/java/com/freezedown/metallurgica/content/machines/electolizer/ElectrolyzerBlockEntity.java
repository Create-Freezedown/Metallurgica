package com.freezedown.metallurgica.content.machines.electolizer;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.base.*;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

public class ElectrolyzerBlockEntity extends BasinOperatingBlockEntity implements IElectric {
    public Player player = null;
    boolean destroyed = false;
    public ElectricBlockValues data = new ElectricBlockValues(this.getPos());
    private static final Object electrolysisRecipesKey = new Object();
    public int runningTicks;
    public int processingTicks;
    public boolean running;
    
    public ElectrolyzerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.setLazyTickRate(1);
        this.data.connectNextTick = true;
        if (!this.canBeInGroups()) {
            this.data.group = new ElectricalGroup(-1);
        }
    }

    public void lazyTick() {
        super.lazyTick();
        if (this.data.failTimer >= 4) {
            this.blockFail();
            this.data.failTimer = 0;
            this.sendStuff();
        } else if (this.data.voltage > this.getMaxVoltage() && this.getMaxVoltage() > 0 || this.getCurrent() > (float)this.getMaxCurrent() && this.getMaxCurrent() > 0) {
            ++this.data.failTimer;
        }
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        running = compound.getBoolean("Running");
        runningTicks = compound.getInt("Ticks");
        this.data.group = new ElectricalGroup(compound.getInt("GroupId"));
        this.data.group.resistance = compound.getFloat("GroupResistance");
        if (!clientPacket) {
            this.data.connectNextTick = true;
        }
        super.read(compound, clientPacket);

        if (clientPacket && hasLevel())
            getBasin().ifPresent(bte -> bte.setAreFluidsMoving(running && runningTicks <= 20));
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putBoolean("Running", running);
        compound.putInt("Ticks", runningTicks);
        compound.putInt("GroupId", this.data.group.id);
        compound.putFloat("GroupResistance", this.data.group.resistance);
    }
    
    @Override
    public void tick() {
        super.tick();

        if (this.data.checkForLoopsNextTick) {
            this.getOrCreateElectricNetwork().checkForLoops(this.getBlockPos());
            this.data.checkForLoopsNextTick = false;
        }

        if (this.data.connectNextTick) {
            this.onPlaced();
            this.data.connectNextTick = false;
        }

        if (this.data.updateNextTick) {
            this.updateNetwork();
            this.data.updateNextTick = false;
        }

        if (this.data.updatePowerNextTick) {
            this.updateUnpowered(new ArrayList());
            this.data.updatePowerNextTick = false;
        }

        if (this.data.setVoltageNextTick) {
            this.setVoltage(this.data.voltageSupply);
            this.data.setVoltageNextTick = false;
        }

        if (runningTicks >= 40) {
            running = false;
            runningTicks = 0;
            basinChecker.scheduleUpdate();
            return;
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
                    
                    Optional<BasinBlockEntity> basin = getBasin();
                    if (basin.isPresent()) {
                        Couple<SmartFluidTankBehaviour> tanks = basin.get()
                                .getTanks();
                        if (!tanks.getFirst()
                                .isEmpty()
                                || !tanks.getSecond()
                                .isEmpty())
                            level.playSound(null, worldPosition, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT,
                                    SoundSource.BLOCKS, .75f, speed < 65 ? .75f : 1.5f);
                    }
                    
                } else {
                    processingTicks--;
                    if (processingTicks == 0) {
                        runningTicks++;
                        processingTicks = -1;
                        applyBasinRecipe();
                        sendData();
                    }
                }
            }
            
            if (runningTicks != 20)
                runningTicks++;
        }
    }

    @Override
    protected Optional<BasinBlockEntity> getBasin() {
        if (this.level == null) {
            return Optional.empty();
        } else {
            BlockEntity basinBE = this.level.getBlockEntity(this.worldPosition.below());
            return !(basinBE instanceof BasinBlockEntity) ? Optional.empty() : Optional.of((BasinBlockEntity)basinBE);
        }
    }

    public void renderParticles() {
        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.isPresent() || level == null)
            return;
        
        for (SmartInventory inv : basin.get()
                .getInvs()) {
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getItem(slot);
                if (stackInSlot.isEmpty())
                    continue;
                ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
                spillParticle(data);
            }
        }
        
        for (SmartFluidTankBehaviour behaviour : basin.get()
                .getTanks()) {
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
        Vec3 target = VecHelper.rotate(offset, getSpeed() > 0 ? 25 : -25, Direction.Axis.Y)
                .add(0, .25f, 0);
        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));
        target = VecHelper.offsetRandomly(target.subtract(offset), level.random, 1 / 128f);
        level.addParticle(data, center.x, center.y - 1.75f, center.z, target.x, target.y, target.z);
    }
    
    @Override
    protected boolean isRunning() {
        return running;
    }
    
    @Override
    public void startProcessingBasin() {
        if (running && runningTicks <= 20)
            return;
        super.startProcessingBasin();
        running = true;
        runningTicks = 0;
    }
    
    @Override
    public boolean continueWithPreviousRecipe() {
        runningTicks = 20;
        return true;
    }
    
    @Override
    protected void onBasinRemoved() {
        if (!running)
            return;
        runningTicks = 40;
        running = false;
    }
    
    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r.getType() == MetallurgicaRecipeTypes.electrolysis.getType();
    }
    
    @Override
    protected Object getRecipeCacheKey() {
        return electrolysisRecipesKey;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();
        
        // SoundEvents.BLOCK_STONE_BREAK
        boolean slow = Math.abs(getSpeed()) < 65;
        if (slow && AnimationTickHolder.getTicks() % 2 == 0)
            return;
        if (runningTicks == 20)
            AllSoundEvents.MIXING.playAt(level, worldPosition, .75f, 1, true);
    }

    @Override
    public void setNetwork(long network) {
        this.data.electricalNetworkId = network;
        if (network != this.getPos()) {
            ((Map)ElectricNetworkManager.networks.get(this.getLevel())).remove(this.getPos());
        }
    }

    @Override
    public ElectricalNetwork getOrCreateElectricNetwork() {
        if (this.level.getBlockEntity(BlockPos.of(this.data.electricalNetworkId)) instanceof IElectric) {
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor((IElectric)this.level.getBlockEntity(BlockPos.of(this.data.electricalNetworkId)));
        } else {
            ((Map)ElectricNetworkManager.networks.get(this.getLevel())).remove(this.data.electricalNetworkId);
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
        }
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        List<Direction> allowedSlots = List.of(this.getBlockState().getValue(DirectionalKineticBlock.FACING).getOpposite(), this.getBlockState().getValue(DirectionalKineticBlock.FACING));
        return allowedSlots.contains(direction);
    }

    @Override
    public ElectricBlockValues getData() {
        return this.data;
    }

    @Override
    public float resistance() {
        return 0;
    }

    @Override
    public int voltageGeneration() {
        int voltageGeneration = 0;

        for(Direction direction : Direction.values()) {
            if (this.hasElectricitySlot(direction)) {
                BlockEntity var7 = this.level.getBlockEntity(this.getBlockPos().relative(direction));
                if (var7 instanceof VoltageAlteringBlockEntity) {
                    VoltageAlteringBlockEntity be = (VoltageAlteringBlockEntity)var7;
                    if (be.getData().getId() != this.getData().getId() && be.getData().getVoltage() != 0 && be.hasElectricitySlot(direction)) {
                        voltageGeneration = Math.max(voltageGeneration, be.getOutputVoltage());
                        this.data.getsOutsidePower = true;
                    }
                }
            }
        }

        if (voltageGeneration == 0) {
            this.data.getsOutsidePower = false;
        }

        return voltageGeneration;
    }

    @Override
    public int powerGeneration() {
        int powerGeneration = 0;

        for(Direction direction : Direction.values()) {
            if (this.hasElectricitySlot(direction)) {
                BlockEntity var7 = this.level.getBlockEntity(this.getBlockPos().relative(direction));
                if (var7 instanceof VoltageAlteringBlockEntity) {
                    VoltageAlteringBlockEntity be = (VoltageAlteringBlockEntity)var7;
                    if (be.canWork() && be.getData().getId() != this.getData().getId() && be.getData().getVoltage() != 0 && be.hasElectricitySlot(direction)) {
                        powerGeneration = Math.max(powerGeneration, be.getPowerUsage()) + 1;
                        if (powerGeneration > be.getNetworkPowerGeneration()) {
                            powerGeneration = 0;
                            be.data.updatePowerNextTick = true;
                        }
                    }
                }
            }
        }

        return powerGeneration;
    }

    @Override
    public int frequencyGeneration() {
        return 0;
    }

    @Override
    public int getNetworkResistance() {
        return this.data.networkResistance;
    }

    @Override
    public void updateNextTick() {
        this.data.updateNextTick = true;
    }

    @Override
    public void updateNetwork() {
        this.getOrCreateElectricNetwork().updateNetwork();
        if (!this.level.isClientSide) {
            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new NetworkUpdatePacket(BlockPos.of(this.getPos())));
        }

        this.sendData();
    }

    public void setVoltage(int newVoltage) {
        if (this.canBeInGroups()) {
            this.data.voltage = (int)(this.resistance() / this.data.group.resistance * (float)this.data.voltageSupply);
        } else {
            this.data.voltage = newVoltage;
        }
    }

    @Override
    public void setFrequency(int newFrequency) {
        this.data.frequency = newFrequency;
    }

    @Override
    public void setNetworkResistance(int newUsage) {
        this.data.networkResistance = newUsage;
    }

    public void explode() {
        TFMGUtils.createFireExplosion(this.level, (Entity)null, this.getBlockPos(), 10, 0.1F);
        this.level.destroyBlock(this.getBlockPos(), false);
    }

    @Override
    public long getPos() {
        return this.getBlockPos().asLong();
    }

    @Override
    public LevelAccessor getLevelAccessor() {
        return this.level;
    }

    @Override
    public boolean destroyed() {
        return this.destroyed;
    }

    @Override
    public void sendStuff() {
        this.sendData();
        this.setChanged();
    }
}
