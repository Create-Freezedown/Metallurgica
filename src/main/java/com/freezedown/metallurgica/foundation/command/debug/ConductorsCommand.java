package com.freezedown.metallurgica.foundation.command.debug;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.registry.misc.MetallurgicaRegistries;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ConductorsCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("conductors").requires(s -> s.hasPermission(2)).executes(arguments -> listConductors(arguments.getSource()));
    }

    private static int listConductors(CommandSourceStack source) {
        for (RegistryEntry<Conductor> conductorEntry : Metallurgica.registrate().getAll(MetallurgicaRegistries.CONDUCTOR_KEY)) {
            ResourceLocation key = conductorEntry.getId();
            Conductor conductor = conductorEntry.get();
            Component conductorMessage = Component.literal("> (").append(conductor.getDisplayName()).append(")").withStyle(FontHelper.styleFromColor(0x624a95));
            source.sendSuccess(() -> conductorMessage, false);
        }
        return 0;
    }

}
