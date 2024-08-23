package com.freezedown.metallurgica.content.temperature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

public record BlockStateTemperature(List<Map<Temperature, State>> blockStateTemperatures) {
    public static final Codec<BlockStateTemperature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Temperature.CODEC, State.CODEC).listOf().fieldOf("blockStateTemperatures").forGetter(BlockStateTemperature::temperatureStateMap)
    ).apply(instance, BlockStateTemperature::new));
    
    public List<Map<Temperature, State>> temperatureStateMap() {
        return blockStateTemperatures;
    }
    
    public static record Temperature(double temperature) {
        public static final Codec<Temperature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.DOUBLE.fieldOf("temperature").forGetter(Temperature::temperature)
        ).apply(instance, Temperature::new));
        
        public double get() {
            return temperature;
        }
    }
    
    public static record State(BlockState blockState) {
        public static final Codec<State> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockState.CODEC.fieldOf("block_state").forGetter(State::blockState)
        ).apply(instance, State::new));
        
        public BlockState get() {
            return blockState;
        }
    }
}
