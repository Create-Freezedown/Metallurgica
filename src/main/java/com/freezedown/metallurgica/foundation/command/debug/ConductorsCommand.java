package com.freezedown.metallurgica.foundation.command.debug;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.infastructure.element.Element;
import com.freezedown.metallurgica.registry.misc.MetallurgicaRegistries;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.AdvancementCommands;
import net.minecraft.server.commands.LootCommand;

public class ConductorsCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return ((LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("registries").requires(s -> s.hasPermission(2))
                .then(Commands.literal("conductors")).executes(arguments -> listConductors(arguments.getSource())))
                .then(Commands.literal("elements")).executes(arguments -> listElements((CommandSourceStack) arguments.getSource())))
                ;
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

    private static int listElements(CommandSourceStack source) {
        for (RegistryEntry<Element> elementEntry : Metallurgica.registrate().getAll(MetallurgicaRegistries.ELEMENT_KEY)) {
            ResourceLocation key = elementEntry.getId();
            Element element = elementEntry.get();
            Component conductorMessage = Component.literal("> (").append(element.getSymbol()).append(")").append(" (").append(element.getDisplayName()).append(")").withStyle(FontHelper.styleFromColor(0x624a95));
            source.sendSuccess(() -> conductorMessage, false);
        }
        return 0;
    }

}
