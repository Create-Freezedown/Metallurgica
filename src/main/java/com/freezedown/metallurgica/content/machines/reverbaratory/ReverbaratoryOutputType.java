package com.freezedown.metallurgica.content.machines.reverbaratory;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum ReverbaratoryOutputType implements StringRepresentable {
    MAIN_OUTPUT(2),
    CARBON_DIOXIDE(1),
    ;
    private final String name;
    private final int tanks;
    
    private ReverbaratoryOutputType(int tanks) {
        name = name().toLowerCase(Locale.ROOT);
        this.tanks = tanks;
    }
    
    @Override
    public String getSerializedName() {
        return name;
    }
    
}
