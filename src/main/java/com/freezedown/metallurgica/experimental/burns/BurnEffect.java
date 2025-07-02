package com.freezedown.metallurgica.experimental.burns;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.fixes.EntityHealthFix;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class BurnEffect {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        //LiteralCommandNode<CommandSourceStack> command =
        //        dispatcher.register(Commands.literal("test").executes((source) -> {
        //
        //        }));

    }
}
