package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class FlagKey<T extends IMaterialFlag> {
    @Getter
    private static final List<FlagKey<?>> allFlags = new ArrayList<>();

    public static final FlagKey<EmptyFlag> EMPTY = create("empty", EmptyFlag.class);
    public static final FlagKey<IngotFlag> INGOT = create("ingot", IngotFlag.class);
    public static final FlagKey<FluidFlag> FLUID = create("fluid", FluidFlag.class);
    public static final FlagKey<DustFlag> DUST = create("dust", DustFlag.class);
    public static final FlagKey<GemFlag> GEM = create("gem", GemFlag.class);


    public static final FlagKey<SheetFlag> SHEET = create("sheet", SheetFlag.class);
    public static final FlagKey<SemiPressedSheetFlag> SEMI_PRESSED_SHEET = create("semi_pressed_sheet", SemiPressedSheetFlag.class);
    public static final FlagKey<WireFlag> WIRE = create("wire", WireFlag.class);
    public static final FlagKey<CableFlag> CABLE = create("cable", CableFlag.class);

    private final String key;
    private final Class<T> type;



    public static <C extends IMaterialFlag> FlagKey<C> create(String key, Class<C> type) {
        FlagKey<C> flag = new  FlagKey<>(key, type);
        allFlags.add(flag);
        return flag;
    }

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
