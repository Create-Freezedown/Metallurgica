package com.freezedown.metallurgica.experimental.cable_connector;

import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.base.TFMGTools;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.blocks.electricity.base.TFMGForgeEnergyStorage;
import com.drmangotea.tfmg.blocks.electricity.base.VoltageAlteringBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.base.WallMountBlock;
import com.drmangotea.tfmg.blocks.electricity.base.cables.*;
import com.drmangotea.tfmg.blocks.electricity.cable_blocks.CableHubBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.energy_components.resistors.ResistorBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.freezedown.metallurgica.infastructure.conductor.CableConnection;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.registry.misc.MetallurgicaRegistries;
import com.simibubi.create.Create;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.catnip.nbt.NBTProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestCableConnectorBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IElectric {
    public long network = this.getId();
    public Player player = null;
    public int voltage = 0;
    public boolean destroyed = false;
    public boolean networkUpdate = false;
    public boolean needsVoltageUpdate = false;
    public boolean breakNextTick = false;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    public final TFMGForgeEnergyStorage energy = this.createEnergyStorage();
    public List<CableConnection> cableConnections = new ArrayList();

    public TestCableConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(10);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyEnergyHandler.invalidate();
    }

    public void useEnergy(int value) {
        this.energy.extractEnergy(value, false);
    }

    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY && side == null) {
            return this.lazyEnergyHandler.cast();
        } else {
            return cap == ForgeCapabilities.ENERGY && this.hasElectricitySlot(side) ? this.lazyEnergyHandler.cast() : super.getCapability(cap, side);
        }
    }

    public void lazyTick() {
        super.lazyTick();
        this.getOrCreateElectricNetwork().members.removeIf((member) -> !(this.level.getBlockEntity(BlockPos.of(member.getId())) instanceof IElectric));
        this.getOrCreateElectricNetwork().members.removeIf((member) -> member.getNetwork() != this.getNetwork());
        this.getOrCreateElectricNetwork().add(this);

        for(Direction direction : Direction.values()) {
            if (this.hasElectricitySlot(direction)) {
                BlockEntity be = this.level.getBlockEntity(this.getBlockPos().relative(direction));
                if (be == null) {
                    return;
                }

                LazyOptional<IEnergyStorage> capability = be.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite());
                if (capability.isPresent() && ((IEnergyStorage)capability.orElseGet((NonNullSupplier)null)).canReceive() && !(be instanceof IElectric)) {
                    this.getOrCreateElectricNetwork().requestEnergy(this);
                    int maxTransfer1 = this.getForgeEnergy().extractEnergy(this.getForgeEnergy().getEnergyStored(), true);
                    int maxTransfer2 = ((IEnergyStorage)capability.orElseGet((NonNullSupplier)null)).receiveEnergy(this.getForgeEnergy().getEnergyStored(), true);
                    int a = this.getForgeEnergy().extractEnergy(Math.min(maxTransfer1, maxTransfer2), false);
                    ((IEnergyStorage)capability.orElseGet((NonNullSupplier)null)).receiveEnergy(a, false);
                }
            }
        }

    }

    public void onLoad() {
        super.onLoad();
        this.lazyEnergyHandler = LazyOptional.of(() -> this.energy);
    }

    public void tick() {
        super.tick();
        if (this.breakNextTick) {
            this.explode();
        }

        if (this.getVoltage() > this.maxVoltage()) {
            this.voltageFailure();
        }

        if (this.FEProduction() > 0) {
            this.energy.receiveEnergy(this.FEProduction(), false);
        }

        if (this.networkUpdate) {
            this.getOrCreateElectricNetwork().updateNetworkVoltage();
            this.networkUpdate = false;
            this.sendData();
        }

        if (this.needsVoltageUpdate) {
            this.setVoltageFromNetwork();
            this.needsVoltageUpdate = false;
            this.sendData();
        }

        if (Create.RANDOM.nextBoolean()) {
            this.removeInvalidConnections();
        }

    }

    public void remove() {
        super.destroy();
        this.voltage = 0;
        this.destroyed = true;
        this.getOrCreateElectricNetwork().remove(this);
        ((Map) ElectricNetworkManager.networks.get(this.getLevel())).remove(this.getId());
        ((Map)ElectricNetworkManager.networks.get(this.level)).remove(this.getId());

        for(CableConnection connection : this.cableConnections) {
            BlockPos pos = connection.point1 == this.getBlockPos() ? connection.point2 : connection.point1;
            BlockEntity var5 = this.level.getBlockEntity(pos);
            if (var5 instanceof IElectric be) {
                be.makeControllerAndSpread();
                be.getOrCreateElectricNetwork().updateNetworkVoltage();
                be.getOrCreateElectricNetwork().updateVoltageFromNetwork();
                if (be instanceof ResistorBlockEntity) {
                    be.getOrCreateElectricNetwork().voltage = 0;
                }
            }
        }

        List<CableConnection> list = new ArrayList();
        List<ItemEntity> itemsToSpawn = new ArrayList<>();

        for(CableConnection connection : this.cableConnections) {
            if (!connection.neighborConnection) {
                list.add(connection);
                itemsToSpawn.add(new ItemEntity(this.level, (float)this.getBlockPos().getX() + 0.5F, (float)this.getBlockPos().getY() + 0.5F, (float)this.getBlockPos().getZ() + 0.5F, connection.cableItem));
            }
        }

        for (ItemEntity itemToSpawn : itemsToSpawn) {
            if (itemToSpawn.getItem().getCount() != 0) {
                this.level.addFreshEntity(itemToSpawn);
                super.remove();
            }
        }
    }

    public boolean outputAllowed() {
        return true;
    }

    @Override
    public boolean addConnection(WireManager.Conductor conductor, BlockPos blockPos, boolean b, boolean b1) {
        return false;
    }

    public void changeToExtension() {
        BlockState state = this.level.getBlockState(this.getBlockPos().relative((Direction)this.getBlockState().getValue(WallMountBlock.FACING)));
        if (state.getBlock() == this.getBlockState().getBlock()) {
            if (state.getValue(WallMountBlock.FACING) == this.getBlockState().getValue(WallMountBlock.FACING) && !(Boolean)this.getBlockState().getValue(CableConnectorBlock.EXTENSION)) {
                this.level.setBlock(this.getBlockPos(), (BlockState)this.getBlockState().setValue(CableConnectorBlock.EXTENSION, true), 2);
            }
        } else if ((Boolean)this.getBlockState().getValue(CableConnectorBlock.EXTENSION)) {
            this.level.setBlock(this.getBlockPos(), (BlockState)this.getBlockState().setValue(CableConnectorBlock.EXTENSION, false), 2);
        }

    }

    public void removeInvalidConnections() {
        this.cableConnections.removeIf((connection) -> !(this.level.getBlockState(connection.point1).getBlock() instanceof IHaveCables) || !(this.level.getBlockState(connection.point2).getBlock() instanceof IHaveCables));
    }

    public void makeControllerAndSpread() {
        if (!this.level.isClientSide) {
            this.getOrCreateElectricNetwork().members.remove(this);
            CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
            this.setNetwork(this.getId(), false);
            this.network = this.getId();
            this.onConnected();
        }
    }

    public TFMGForgeEnergyStorage getForgeEnergy() {
        return this.energy;
    }

    public void onConnected() {
        this.needsVoltageUpdate = true;
        this.getOrCreateElectricNetwork().updateNetworkVoltage();

        for(CableConnection connection : this.cableConnections) {
            BlockPos pos = connection.point1 == this.getBlockPos() ? connection.point2 : connection.point1;
            BlockEntity blockEntity = this.level.getBlockEntity(pos);
            if (blockEntity instanceof IElectric electric) {
                if (electric instanceof CableHubBlockEntity cableHubBlockEntity) {
                    if (cableHubBlockEntity.hasSignal) {
                        continue;
                    }
                }

                if (electric.getNetwork() != this.network && !electric.destroyed() && electric != this) {
                    electric.setNetwork(this.network, true);
                    electric.onConnected();
                } else if (electric.destroyed()) {
                    this.getOrCreateElectricNetwork().remove(electric);
                }
            }
        }

    }

    public int FECapacity() {
        return 1000;
    }

    public int FEProduction() {
        return 0;
    }

    public int FETransferSpeed() {
        return 2500;
    }

    public int getVoltage() {
        return this.voltage;
    }

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
            this.cableConnections = new ArrayList();
            this.breakNextTick = true;
        }

    }

    public void explode() {
        TFMGUtils.createFireExplosion(this.level, (Entity)null, this.getBlockPos(), 10, 0.1F);
        this.level.destroyBlock(this.getBlockPos(), false);
    }

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

    public void onPlaced() {
        this.voltage = 0;
        this.networkUpdate = true;
        this.connectNeighbors();
        this.setVoltage(this.voltageGeneration());
        if (!this.level.isClientSide) {
            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new VoltagePacket(this.getBlockPos()));
        }

        this.getOrCreateElectricNetwork().updateNetworkVoltage();
        this.needsNetworkUpdate();
    }

    public void setVoltageFromNetwork() {
        this.setVoltage(Math.max(this.getOrCreateElectricNetwork().voltage, this.voltageGeneration()), false);
    }

    public int maxVoltage() {
        return (Integer) MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.CABLE_CONNECTOR.get());
    }

    public void connectNeighbors() {
        for(Direction direction : Direction.values()) {
            if (this.hasElectricitySlot(direction)) {
                BlockPos pos = this.getBlockPos().relative(direction);
                BlockEntity blockEntity = this.level.getBlockEntity(pos);
                if (blockEntity instanceof IElectric electric) {
                    if (electric instanceof CableHubBlockEntity cableHubBlockEntity) {
                        if (cableHubBlockEntity.hasSignal) {
                            continue;
                        }
                    }

                    if (electric.hasElectricitySlot(direction.getOpposite())) {
                        electric.addConnection(WireManager.Conductor.COPPER, this.getBlockPos(), false, true);
                        electric.sendStuff();
                        this.addConnection(WireManager.Conductor.COPPER, pos, false, true);
                        this.sendData();
                        this.setChanged();
                        if (this.level.isClientSide) {
                            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new EnergyNetworkUpdatePacket(this.getBlockPos(), this.network));
                            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new VoltagePacket(this.getBlockPos()));
                        }

                        electric.makeControllerAndSpread();
                    }
                }
            }
        }

    }

    public void needsVoltageUpdate() {
        this.needsVoltageUpdate = true;
    }

    public boolean destroyed() {
        return this.destroyed;
    }

    public void needsNetworkUpdate() {
        this.networkUpdate = true;
    }

    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("Voltage", this.getVoltage());
        compound.putLong("Network", this.network);
        int value = 0;

        for(CableConnection connection : this.cableConnections) {
            ++value;
            connection.saveConnection(compound, value - 1);
        }

        compound.putInt("CableCount", this.cableConnections.toArray().length);
        compound.putInt("ForgeEnergy", this.getForgeEnergy().getEnergyStored());
    }

    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.voltage = compound.getInt("Voltage");
        //this.addConnection(WireManager.Conductor.COPPER, this.getBlockPos().above(2).north(), true, true);
        if (!this.cableConnections.isEmpty()) {
            this.cableConnections = new ArrayList();

            for(int i = 0; i < compound.getInt("WireCount"); ++i) {
                BlockPos pos = new BlockPos(compound.getInt("X1" + i), compound.getInt("Y1" + i), compound.getInt("Z1" + i));
                if (pos == this.getBlockPos()) {
                    pos = new BlockPos(compound.getInt("X2" + i), compound.getInt("Y2" + i), compound.getInt("Z2" + i));
                }
                Conductor conductor = MetallurgicaRegistries.registeredConductors.get(NBTHelper.readResourceLocation(compound, "Conductor" + i));
                CompoundTag itemTag = compound.getCompound("CableItem");
                this.addConnection(conductor, ItemStack.of(itemTag), pos, compound.getBoolean("ShouldRender" + i), compound.getBoolean("NeighborConnection" + i));
            }

            this.network = compound.getLong("Network");
            this.setNetwork(this.network, false);
            this.energy.setEnergy(compound.getInt("ForgeEnergy"));
        }
    }

    public void onVoltageChanged() {
        this.getOrCreateElectricNetwork().updateNetworkVoltage();
        this.getOrCreateElectricNetwork().updateVoltageFromNetwork();
        if (!this.level.isClientSide) {
            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new VoltagePacket(this.getBlockPos()));
        }

    }

    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    protected AABB createRenderBoundingBox() {
        return (new AABB(this.getBlockPos())).inflate((double)30.0F);
    }

    public boolean addConnection(Conductor conductor, ItemStack cableItem, BlockPos pos, boolean shouldRender, boolean neighborConnection) {
        float lenght = TFMGTools.getDistance(this.getBlockPos(), pos, false);
        if (lenght < 25.0F) {
            this.cableConnections.add(new CableConnection(conductor, cableItem, lenght, pos, this.getBlockPos(), shouldRender, neighborConnection));
            this.sendData();
            this.setChanged();
            return true;
        } else {
            this.sendData();
            this.setChanged();
            return false;
        }
    }

    public boolean hasElectricitySlot(Direction direction) {
        return direction == ((Direction)this.getBlockState().getValue(WallMountBlock.FACING)).getOpposite();
    }

    public long getNetwork() {
        return this.network;
    }

    public long getId() {
        return this.getBlockPos().asLong();
    }

    public ElectricalNetwork getOrCreateElectricNetwork() {
        return this.level.getBlockEntity(BlockPos.of(this.network)) instanceof IElectric ? CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor((IElectric)this.level.getBlockEntity(BlockPos.of(this.network))) : CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
    }

    public void setNetworkClient(long value) {
        this.network = value;
    }

    public void sendStuff() {
        this.sendData();
        this.setChanged();
    }
}
