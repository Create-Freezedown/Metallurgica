package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;

public class FlagKey<T extends IMaterialFlag> {
    public static final FlagKey<EmptyFlag> EMPTY = new FlagKey<>("empty", EmptyFlag.class);
    public static final FlagKey<IngotFlag> INGOT = new FlagKey<>("ingot", IngotFlag.class);
    public static final FlagKey<FluidFlag> FLUID = new FlagKey<>("fluid", FluidFlag.class);
    public static final FlagKey<DustFlag> DUST = new FlagKey<>("dust", DustFlag.class);
    public static final FlagKey<GemFlag> GEM = new FlagKey<>("gem", GemFlag.class);


    public static final FlagKey<SheetFlag> SHEET = new FlagKey<>("sheet", SheetFlag.class);

    public static final FlagKey<WiringFlag> WIRING = new FlagKey<>("wiring", WiringFlag.class);

    private final String key;
    private final Class<T> type;

    public FlagKey(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    protected String getKey() {
        return key;
    }

    public T constructDefault() {
        try {
            return type.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public T cast(IMaterialFlag flag) {
        return this.type.cast(flag);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FlagKey) {
            return ((FlagKey<?>) o).getKey().equals(key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key;
    }

    public static class EmptyFlag implements IMaterialFlag {
        @Override
        public void verifyFlag(MaterialFlags flags) {
            // no-op
        }
    }
}
