package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class MetallurgicaSpriteShifts {
    
    public static final CTSpriteShiftEntry drillExpansion = vertical("drill_expansion");
    public static final CTSpriteShiftEntry blastProofGlass = omni("blast_proof_glass");
    public static final CTSpriteShiftEntry directionalMetalBlock = horizontal("directional_metal_block");

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }
    
    private static CTSpriteShiftEntry horizontal(String name) {
        return getCT(AllCTTypes.HORIZONTAL, name);
    }
    
    private static CTSpriteShiftEntry vertical(String name) {
        return getCT(AllCTTypes.VERTICAL, name);
    }
    
    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Metallurgica.asResource(originalLocation), Metallurgica.asResource(targetLocation));
    }
    
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, Metallurgica.asResource("block/" + blockTextureName),
                Metallurgica.asResource("block/" + connectedTextureName + "_connected"));
    }
    
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
    
}
