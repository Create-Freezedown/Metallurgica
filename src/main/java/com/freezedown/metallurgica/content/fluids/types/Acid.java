package com.freezedown.metallurgica.content.fluids.types;

import com.freezedown.metallurgica.content.fluids.effects.corrosion.Corrosive;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import lombok.Getter;
import net.minecraft.world.level.material.Fluid;

public class Acid extends VirtualFluid implements Corrosive {

    public static Acid createSource(Properties properties) {
        return new Acid(properties, true);
    }

    public static Acid createFlowing(Properties properties) {
        return new Acid(properties, false);
    }

    @Getter
    private float acidity = 7.0f;
    private boolean dousesFire = false;
    
    public Acid(Properties properties, boolean source) {
        super(properties, source);
    }
    
    public Acid acidity(float acidity) {
        this.acidity = acidity;
        return this;
    }
    
    public Acid douseFire() {
        this.dousesFire = true;
        return this;
    }

    public boolean dousesFire() {
        return dousesFire;
    }
    
    public boolean isBase() {
        return getAcidity() > 7;
    }
    
    public boolean isAcid() {
        return getAcidity() < 7;
    }
    
    public boolean isNeutral() {
        return getAcidity() == 7;
    }
    
}
