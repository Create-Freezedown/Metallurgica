package com.freezedown.metallurgica.content.machines.electolizer;

import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.base.TFMGTools;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.blocks.electricity.base.TFMGForgeEnergyStorage;
import com.drmangotea.tfmg.blocks.electricity.base.VoltageAlteringBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.base.cables.*;
import com.drmangotea.tfmg.blocks.electricity.cable_blocks.CableHubBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.energy_components.resistors.ResistorBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

public class ElectrolyzerBlockEntity extends BasinOperatingBlockEntity implements IElectric {
    public long network = this.getId();
    public Player player = null;
    public int voltage = 0;
    boolean destroyed = false;
    public boolean networkUpdate = false;
    public boolean breakNextTick = false;
    public boolean needsVoltageUpdate = false;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    public final TFMGForgeEnergyStorage energy = this.createEnergyStorage();
    public ArrayList<WireConnection> wireConnections = new ArrayList();
    private static final Object electrolysisRecipesKey = new Object();
    public int runningTicks;
    public int processingTicks;
    public boolean running;
    
    public ElectrolyzerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.setLazyTickRate(1);
    }

    public void lazyTick() {
        super.lazyTick();
        this.getOrCreateElectricNetwork().requestEnergy(this);
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        running = compound.getBoolean("Running");
        runningTicks = compound.getInt("Ticks");
        this.voltage = compound.getInt("Voltage");
        this.addConnection(WireManager.Conductor.COPPER, this.getBlockPos().above(2).north(), true, true);
        if (!this.wireConnections.isEmpty()) {
            this.wireConnections = new ArrayList();

            for(int i = 0; i < compound.getInt("WireCount"); ++i) {
                BlockPos pos = new BlockPos(compound.getInt("X1" + i), compound.getInt("Y1" + i), compound.getInt("Z1" + i));
                if (pos == this.getBlockPos()) {
                    pos = new BlockPos(compound.getInt("X2" + i), compound.getInt("Y2" + i), compound.getInt("Z2" + i));
                }

                this.addConnection(WireManager.Conductor.COPPER, pos, compound.getBoolean("ShouldRender" + i), compound.getBoolean("NeighborConnection" + i));
            }

            this.network = compound.getLong("Network");
            this.setNetwork(this.network, false);
            this.energy.setEnergy(compound.getInt("ForgeEnergy"));
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
        compound.putInt("Voltage", this.getVoltage());
        compound.putLong("Network", this.network);
        int value = 0;

        for(WireConnection connection : this.wireConnections) {
            ++value;
            connection.saveConnection(compound, value - 1);
        }

        compound.putInt("WireCount", this.wireConnections.toArray().length);
        compound.putInt("ForgeEnergy", this.getForgeEnergy().getEnergyStored());
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.breakNextTick) {
            this.explode();
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

    public void onLoad() {
        super.onLoad();
        this.lazyEnergyHandler = LazyOptional.of(() -> this.energy);
    }

    @Override
    public long getNetwork() {
        return this.network;
    }

    @Override
    public long getId() {
        return this.getBlockPos().asLong();
    }

    @Override
    public void setNetwork(long newNetwork, boolean removeNetwork) {
        if (this.network != newNetwork) {
            if (removeNetwork) {
                ((Map)ElectricNetworkManager.networks.get(this.getLevel())).remove(this.network);
            } else {
                this.getOrCreateElectricNetwork().remove(this);
            }

            long oldNetwork = this.network;
            this.network = newNetwork;
            ElectricalNetwork network1 = CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor((IElectric)this.level.getBlockEntity(BlockPos.of(this.network)));
            if (network1.members.contains(this)) {
                this.network = oldNetwork;
            } else {
                network1.add(this);
            }
        }
    }

    @Override
    public ElectricalNetwork getOrCreateElectricNetwork() {
        return this.level.getBlockEntity(BlockPos.of(this.network)) instanceof IElectric ? CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor((IElectric)this.level.getBlockEntity(BlockPos.of(this.network))) : CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
    }

    @Override
    public void setNetworkClient(long value) {
        this.network = value;
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        List<Direction> allowedSlots = List.of(this.getBlockState().getValue(DirectionalKineticBlock.FACING).getOpposite(), this.getBlockState().getValue(DirectionalKineticBlock.FACING));
        return allowedSlots.contains(direction);
    }

    @Override
    public void onConnected() {
        this.needsVoltageUpdate = true;
        if (!this.level.isClientSide) {
            Iterator var1 = this.wireConnections.iterator();

            while(true) {
                while(true) {
                    IElectric be;
                    CableHubBlockEntity be1;
                    do {
                        BlockEntity var5;
                        do {
                            if (!var1.hasNext()) {
                                return;
                            }

                            WireConnection connection = (WireConnection)var1.next();
                            BlockPos pos = connection.point1 == this.getBlockPos() ? connection.point2 : connection.point1;
                            var5 = this.level.getBlockEntity(pos);
                        } while(!(var5 instanceof IElectric));

                        be = (IElectric)var5;
                        if (!(be instanceof CableHubBlockEntity)) {
                            break;
                        }

                        be1 = (CableHubBlockEntity)be;
                    } while(be1.hasSignal);

                    if (be.getNetwork() != this.network && !be.destroyed()) {
                        be.setNetwork(this.network, true);
                        be.onConnected();
                    } else if (be.destroyed()) {
                        this.getOrCreateElectricNetwork().remove(be);
                    }
                }
            }
        }
    }

    @Override
    public int FECapacity() {
        return 10000;
    }

    public int maxVoltage() {
        return 3500;
    }

    public boolean outputAllowed() {
        return false;
    }

    @Override
    public int FEProduction() {
        return 0;
    }

    @Override
    public int FETransferSpeed() {
        return 2500;
    }

    @Override
    public int getVoltage() {
        return this.voltage;
    }

    @Override
    public int voltageGeneration() {
        int voltageGeneration = 0;

        for(Direction direction : Direction.values()) {
            if (this.hasElectricitySlot(direction)) {
                BlockEntity var7 = this.level.getBlockEntity(this.getBlockPos().relative(direction));
                if (var7 instanceof VoltageAlteringBlockEntity) {
                    VoltageAlteringBlockEntity be = (VoltageAlteringBlockEntity)var7;
                    if (be.voltage != 0 && be.hasElectricitySlot(direction)) {
                        voltageGeneration = Math.max(voltageGeneration, be.getOutputVoltage());
                    }
                }
            }
        }

        return voltageGeneration;
    }

    public void setVoltage(int value) {
        this.setVoltage(value, true);
    }

    public void setVoltage(int value, boolean update) {
        this.voltage = value;
        if (update) {
            this.onVoltageChanged();
        }

    }

    public void voltageFailure() {
        if (!this.getOrCreateElectricNetwork().blowFuse()) {
            this.voltage = 0;
            this.wireConnections = new ArrayList();
            this.breakNextTick = true;
        }
    }

    @Override
    public void connectNeighbors() {
        for(Direction direction : Direction.values()) {
            if (this.hasElectricitySlot(direction)) {
                BlockPos pos = this.getBlockPos().relative(direction);
                BlockEntity be1 = this.level.getBlockEntity(pos);
                if (be1 instanceof IElectric) {
                    IElectric be = (IElectric)be1;

                    if (be.hasElectricitySlot(direction.getOpposite())) {
                        be.addConnection(WireManager.Conductor.COPPER, this.getBlockPos(), false, true);
                        be.sendStuff();
                        this.addConnection(WireManager.Conductor.COPPER, pos, false, true);
                        this.sendData();
                        this.setChanged();
                        if (this.level.isClientSide) {
                            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new EnergyNetworkUpdatePacket(this.getBlockPos(), this.network));
                            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new VoltagePacket(this.getBlockPos()));
                        }

                        be.makeControllerAndSpread();
                    }
                }
            }
        }
    }

    public void explode() {
        TFMGUtils.createFireExplosion(this.level, (Entity)null, this.getBlockPos(), 10, 0.1F);
        this.level.destroyBlock(this.getBlockPos(), false);
    }

    @Override
    public void needsVoltageUpdate() {
        this.needsVoltageUpdate = true;
    }

    @Override
    public boolean destroyed() {
        return this.destroyed;
    }

    @Override
    public void needsNetworkUpdate() {
        this.networkUpdate = true;
    }

    @Override
    public boolean addConnection(WireManager.Conductor material, BlockPos pos, boolean shouldRender, boolean neighborConnection) {
        float lenght = TFMGTools.getDistance(this.getBlockPos(), pos, false);
        if (lenght < 25.0F) {
            this.wireConnections.add(new WireConnection(material, lenght, pos, this.getBlockPos(), shouldRender, neighborConnection));
            this.sendData();
            this.setChanged();
            return true;
        } else {
            this.sendData();
            this.setChanged();
            return false;
        }
    }

    @Override
    public void makeControllerAndSpread() {
        if (!this.level.isClientSide) {
            this.getOrCreateElectricNetwork().members.remove(this);
            CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
            this.setNetwork(this.getId(), false);
            this.network = this.getId();
            this.onConnected();
        }
    }

    @Override
    public void setVoltageFromNetwork() {
        this.setVoltage(Math.max(this.getOrCreateElectricNetwork().voltage, this.voltageGeneration()), false);
    }

    @Override
    public TFMGForgeEnergyStorage getForgeEnergy() {
        return this.energy;
    }

    public void onVoltageChanged() {
        this.getOrCreateElectricNetwork().updateNetworkVoltage();
    }

    @Override
    public void sendStuff() {
        this.sendData();
        this.setChanged();
    }
}
