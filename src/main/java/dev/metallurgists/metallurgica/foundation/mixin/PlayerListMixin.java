package dev.metallurgists.metallurgica.foundation.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Shadow
    @Final
    private List<ServerPlayer> players;
    
    @Shadow
    @Final
    private MinecraftServer server;
    
    @Inject(
            method = "placeNewPlayer",
            at = @At("TAIL")
    )
    private void afterSyncData(Connection pNetManager, ServerPlayer pPlayer, CallbackInfo ci) {
        //if (FMLEnvironment.dist.isClient()) {
        //    MetallurgicaPackets.sendToPlayer(pPlayer, MetallurgicaPackets.fluidComposition);
        //}
    }
    
    @Inject(
            method = "reloadResources",
            at = @At("TAIL")
    )
    private void afterSyncDataToAll(CallbackInfo ci) {
        //if (FMLEnvironment.dist.isClient()) {
        //    MetallurgicaPackets.sendToAll(MetallurgicaPackets.fluidComposition);
        //}
    }
}
