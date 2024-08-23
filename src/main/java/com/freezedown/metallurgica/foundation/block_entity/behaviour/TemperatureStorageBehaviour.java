package com.freezedown.metallurgica.foundation.block_entity.behaviour;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TemperatureStorageBehaviour extends BlockEntityBehaviour {
    
    public static final BehaviourType<TemperatureStorageBehaviour>
            
            TYPE = new BehaviourType<>();
    
    private BehaviourType<TemperatureStorageBehaviour> behaviourType;
    
    //If the tank should have input/output disabled
    protected boolean tankBricked;
    
    protected double internalTemperature;
    protected double internalTemperatureResistance;
    
    protected double heaterTemperature;
    
    protected int destroyProgress;
    
    private long previousTime;
    
    public TemperatureStorageBehaviour(BehaviourType<TemperatureStorageBehaviour> type, SmartBlockEntity be) {
        super(be);
        this.behaviourType = type;
        this.previousTime = System.nanoTime();
    }
    
    public TemperatureStorageBehaviour withResistance(double resistance) {
        internalTemperatureResistance = resistance;
        return this;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        setHeaterTemperature(getHeat());
        
        if (internalTemperature > internalTemperatureResistance) {
            if (getWorld().getGameTime() % 20 == 0) {
                if (getWorld().random.nextInt(10) == 0) {
                    destroyProgress++;
                }
            }
            startBreakingBlock();
        }
        
        if (blockEntity.getAllBehaviours().stream().noneMatch(b -> b.getType() == SmartFluidTankBehaviour.TYPE)) {
            SmartFluidTankBehaviour tank = blockEntity.getBehaviour(SmartFluidTankBehaviour.TYPE);
            if (tankBricked) {
                tank.forbidInsertion();
                tank.forbidExtraction();
            } else {
                tank.allowInsertion();
                tank.allowExtraction();
            }
        }
        
        if (heaterTemperature > internalTemperature) {
            updateTemperature();
        }
        
        
    }
    
    private void startBreakingBlock() {
        Level world = blockEntity.getLevel();
        if (world == null) {
            return;
        }
        Player player = world.getNearestPlayer(blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ(), 256, true);
        if (player == null) {
            return;
        }
        world.destroyBlock(blockEntity.getBlockPos(), false, world.getNearestPlayer(blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ(), 256, true), destroyProgress + 1);
    }
    
    private double getHeat() {
        Level world = blockEntity.getLevel();
        if (world == null) {
            return 0;
        }
        BlockState state = world.getBlockState(blockEntity.getBlockPos().below());
        if (state.hasProperty(BlazeBurnerBlock.HEAT_LEVEL)) {
            return switch (state.getValue(BlazeBurnerBlock.HEAT_LEVEL).getSerializedName()) {
                case "smouldering" ->  500;
                case "kindled" ->  1000;
                case "seething" ->  1500;
                default -> 0;
            };
        }
        return 0;
    }
    
    private double calculateDuration(double initialTemp, double targetTemp) {
        double temperatureDifference = Math.abs(targetTemp - initialTemp);
        // Example: duration is proportional to the temperature difference
        return temperatureDifference * 10; // Adjust the multiplier as needed
    }
    
    public void updateTemperature() {
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - previousTime) / 1e9;
        
        double elapsedTime = 0;
        elapsedTime += deltaTime;
        double duration = calculateDuration(internalTemperature, heaterTemperature);
        double t = Math.min(elapsedTime / duration, 1.0); // Ensure t does not exceed 1
        double easedT = easeInCubic(t);
        setInternalTemperature(internalTemperature + (heaterTemperature - internalTemperature) * easedT);
    }
    
    private double easeInCubic(double t) {
        return t * t * t;
    }
    
    public void increaseTemperature(double amount) {
        internalTemperature += amount;
    }
    
    public double getInternalTemperature() {
        return internalTemperature;
    }
    
    public void setInternalTemperature(double internalTemperature) {
        this.internalTemperature = internalTemperature;
    }
    
    public void decreaseTemperature(double amount) {
        internalTemperature -= amount;
    }
    
    public double getInternalTemperatureResistance() {
        return internalTemperatureResistance;
    }
    
    public double getHeaterTemperature() {
        return heaterTemperature;
    }
    
    public void setHeaterTemperature(double heaterTemperature) {
        this.heaterTemperature = heaterTemperature;
    }
    
    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        nbt.putBoolean("tankBricked", tankBricked);
        nbt.putDouble("internalTemperature", internalTemperature);
        nbt.putDouble("internalTemperatureResistance", internalTemperatureResistance);
        nbt.putDouble("heaterTemperature", heaterTemperature);
    }
    
    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        tankBricked = nbt.getBoolean("tankBricked");
        internalTemperature = nbt.getDouble("internalTemperature");
        internalTemperatureResistance = nbt.getDouble("internalTemperatureResistance");
        heaterTemperature = nbt.getDouble("heaterTemperature");
    }
    
    @Override
    public BehaviourType<?> getType() {
        return behaviourType;
    }
}
