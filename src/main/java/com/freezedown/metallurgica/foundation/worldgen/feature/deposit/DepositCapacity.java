package com.freezedown.metallurgica.foundation.worldgen.feature.deposit;

import com.mojang.serialization.Codec;

public enum DepositCapacity {
    TINY(1000, 2000),
    SMALL(3000, 4000),
    MEDIUM(6000, 8000),
    HIGH(12000, 16000),
    VERY_HIGH(18000, 24000);
    
    private final int minPossible;
    private final int maxPossible;
    
    DepositCapacity(int minPossible, int maxPossible) {
        this.minPossible = minPossible;
        this.maxPossible = maxPossible;
    }
    
    public int getMinPossible() {
        return minPossible;
    }
    
    public int getMaxPossible() {
        return maxPossible;
    }
    
    public static int getRandomCapacity(DepositCapacity capacity) {
        return capacity.getMinPossible() + (int) (Math.random() * (capacity.getMaxPossible() - capacity.getMinPossible()));
    }
    
    public static final Codec<DepositCapacity> CODEC = Codec.STRING.xmap(DepositCapacity::valueOf, DepositCapacity::name);
}
