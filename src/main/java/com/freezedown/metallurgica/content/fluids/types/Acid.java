package com.freezedown.metallurgica.content.fluids.types;

import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import net.minecraft.world.level.material.Fluid;

public class Acid extends VirtualFluid {

    public static Acid createSource(Properties properties) {
        return new Acid(properties, true);
    }

    public static Acid createFlowing(Properties properties) {
        return new Acid(properties, false);
    }

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
    
    public float getAcidity() {
        return acidity;
    }
    
    public boolean dousesFire() {
        return dousesFire;
    }
    
    public boolean isBase() {
        return acidity > 7;
    }
    
    public boolean isAcid() {
        return acidity < 7;
    }
    
    public boolean isNeutral() {
        return acidity == 7;
    }
    
    public float getAcidity(Fluid fluid) {
        if (fluid instanceof Acid acid) {
            return acid.getAcidity();
        }
        return 7.0f;
    }
    
}
