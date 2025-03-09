package com.freezedown.metallurgica.foundation.command.debug;

import com.drmangotea.tfmg.blocks.electricity.base.cables.WireItem;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.registry.misc.MetallurgicaRegistries;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ConductorsCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("conductors").requires(s -> s.hasPermission(2)).executes(arguments -> listConductors(arguments.getSource()));
    }

    private static int listConductors(CommandSourceStack source) {
        for (Map.Entry<ResourceLocation, Conductor> conductorEntry : MetallurgicaRegistries.CONDUCTOR.entries()) {
            ResourceLocation key = conductorEntry.getKey();
            Conductor conductor = conductorEntry.getValue();
            Component conductorMessage = Component.literal("> (").append(conductor.getDisplayName()).append(") {" + conductor.getResistivity() + "}").withStyle(FontHelper.styleFromColor(0x624a95));
            source.sendSuccess(() -> conductorMessage, false);
        }
        return 0;
    }

}
