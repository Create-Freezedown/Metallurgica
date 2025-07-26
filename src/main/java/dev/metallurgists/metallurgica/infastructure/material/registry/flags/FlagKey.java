package dev.metallurgists.metallurgica.infastructure.material.registry.flags;

import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.IMaterialFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.block.*;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.fluid.LiquidFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.fluid.MoltenFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.item.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class FlagKey<T extends IMaterialFlag> {
    @Getter
    private static final List<FlagKey<?>> allFlags = new ArrayList<>();

    public static final FlagKey<EmptyFlag> EMPTY = create("empty", EmptyFlag.class);
    public static final FlagKey<IngotFlag> INGOT = create("ingot", IngotFlag.class);
    public static final FlagKey<LiquidFlag> LIQUID = create("liquid", LiquidFlag.class);
    public static final FlagKey<DustFlag> DUST = create("dust", DustFlag.class);
    public static final FlagKey<GemFlag> GEM = create("gem", GemFlag.class);
    public static final FlagKey<MineralFlag> MINERAL = create("mineral", MineralFlag.class);

    public static final FlagKey<NuggetFlag> NUGGET = create("nugget", NuggetFlag.class);
    public static final FlagKey<SheetFlag> SHEET = create("sheet", SheetFlag.class);
    public static final FlagKey<SemiPressedSheetFlag> SEMI_PRESSED_SHEET = create("semi_pressed_sheet", SemiPressedSheetFlag.class);
    public static final FlagKey<WireFlag> WIRE = create("wire", WireFlag.class);
    public static final FlagKey<SpoolFlag> SPOOL = create("spool", SpoolFlag.class);
    public static final FlagKey<RubbleFlag> RUBBLE = create("rubble", RubbleFlag.class);
    public static final FlagKey<MoltenFlag> MOLTEN = create("molten", MoltenFlag.class);


    //Blocks
    public static final FlagKey<StorageBlockFlag> STORAGE_BLOCK = create("storage_block", StorageBlockFlag.class);
    public static final FlagKey<CasingFlag> CASING = create("casing", CasingFlag.class);
    public static final FlagKey<SheetmetalFlag> SHEETMETAL = create("sheetmetal", SheetmetalFlag.class);
    public static final FlagKey<CogWheelFlag> COG_WHEEL = create("cog_wheel", CogWheelFlag.class);
    public static final FlagKey<LargeCogWheelFlag> LARGE_COG_WHEEL = create("large_cog_wheel", LargeCogWheelFlag.class);

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

        @Override
        public FlagKey<?> getKey() {
            return FlagKey.EMPTY;
        }
    }
}
