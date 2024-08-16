package com.freezedown.metallurgica.experimental.exposure_effects;

import com.freezedown.metallurgica.registry.misc.MetallurgicaShaders;
import com.mojang.blaze3d.shaders.AbstractUniform;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class BlurShaderPacket extends SimplePacketBase {
    private final boolean showBlur;
    
    public BlurShaderPacket(boolean showBlur) {
        this.showBlur = showBlur;
    }
    
    public BlurShaderPacket(FriendlyByteBuf buffer) {
        this.showBlur = buffer.readBoolean();
    }
    
    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(showBlur);
    }
    
    private static final AbstractUniform blurProgress = MetallurgicaShaders.blurShader.safeGetUniform("Progress");
    private static final AbstractUniform blurRadius = MetallurgicaShaders.blurShader.safeGetUniform("Radius");
    @Override
    public boolean handle(NetworkEvent.Context context) {
        assert Minecraft.getInstance().level != null;
        blurProgress.set(0.2f);
        blurRadius.set(8f);
        MetallurgicaShaders.blurShader.apply();
        return true;
    }
}
