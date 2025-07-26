package dev.metallurgists.metallurgica.experimental.shader;

import dev.metallurgists.metallurgica.Metallurgica;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.io.IOException;

public class Blur implements AutoCloseable {
    
    private static final Logger logger = LogUtils.getLogger();
    
    private static final ResourceLocation BLUR_SHADER =
            new ResourceLocation(Metallurgica.ID, "shaders/post/blur.json");
    
    private final Minecraft minecraft = Minecraft.getInstance();
    
    private PostChain postChain;
    
    private float lastWidth;
    private float lastHeight;
    
    public Blur() {
        this(-1);
    }
    
    public Blur(float radius) {
        try {
            this.postChain = new PostChain(this.minecraft.getTextureManager(),
                    this.minecraft.getResourceManager(), this.minecraft.getMainRenderTarget(), BLUR_SHADER);
            this.setRadius(radius);
            this.postChain.resize(this.minecraft.getWindow().getWidth(),
                    this.minecraft.getWindow().getHeight());
        } catch (JsonSyntaxException | IOException ioexception) {
            logger.warn("Failed to load shader: {}", BLUR_SHADER, ioexception);
            this.postChain = null;
        }
    }
    
    public void setRadius(float radius) {
        if (radius > -1 && this.postChain != null) {
            RenderUtil.updateUniform("Radius", radius, this.postChain);
        }
    }
    
    public void tick() {
        var width = this.minecraft.getMainRenderTarget().width;
        var height = this.minecraft.getMainRenderTarget().height;
        // Can't use #resized as it's called before the framebuffer is resized.
        if (width != this.lastWidth || height != this.lastHeight) {
            if (this.postChain != null) {
                this.postChain.resize(this.minecraft.getWindow().getWidth(),
                        this.minecraft.getWindow().getHeight());
            }
            this.lastWidth = width;
            this.lastHeight = height;
        }
    }
    
    public void process(float partialTick) {
        if (this.postChain != null) {
            this.postChain.process(partialTick);
            this.minecraft.getMainRenderTarget().bindWrite(false);
        }
    }
    
    public void render(PoseStack poseStack, float x, float y, float width, float height) {
        if (this.postChain != null) {
            var renderTarget = this.postChain.getTempTarget("output");
            RenderSystem.setShaderTexture(0, renderTarget.getColorTextureId());
            var textureWidth = (float) (renderTarget.width
                    / this.minecraft.getWindow().getGuiScale());
            var textureHeight = (float) (renderTarget.height
                    / this.minecraft.getWindow().getGuiScale());
            var textureX = x;
            var textureY = (textureHeight - height) - y;
            RenderUtil.blit(poseStack, x, y, x + width, y + height, textureX, textureY,
                    textureX + width, textureY + height, textureWidth, textureHeight);
        }
    }
    
    @Override
    public void close() {
        if (this.postChain != null) {
            this.postChain.close();
        }
    }
}
