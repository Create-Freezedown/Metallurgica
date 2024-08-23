package com.freezedown.metallurgica.foundation.block_entity.behaviour;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;

public enum Temperatures {
    BLAZE_BURNER_SMOULDERING(BlazeBurnerBlock.HeatLevel.SMOULDERING, 1500),
    ;
    
    private final BlazeBurnerBlock.HeatLevel heatLevel;
    
    private final double temperature;
    
    Temperatures(BlazeBurnerBlock.HeatLevel heatLevel, double temperature) {
        this.heatLevel = heatLevel;
        this.temperature = temperature;
    }
}
