package dev.metallurgists.metallurgica.infastructure.conductor;

import dev.metallurgists.metallurgica.foundation.config.MetallurgicaConfigs;
import dev.metallurgists.metallurgica.foundation.config.server.MServer;
import dev.metallurgists.metallurgica.foundation.util.MetalLang;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.ChatFormatting.GRAY;

public class ConductorStats implements TooltipModifier {
    protected final CableItem cableItem;

    public ConductorStats(CableItem cableItem) {
        this.cableItem = cableItem;
    }

    @Nullable
    public static ConductorStats create(Item item) {
        if (item instanceof CableItem cableItem) {
            return new ConductorStats(cableItem);
        }
        return null;
    }

    @Override
    public void modify(ItemTooltipEvent context) {
        List<Component> conductorStats = getConductorStats(cableItem, context.getEntity());
        if (!conductorStats.isEmpty()) {
            List<Component> tooltip = context.getToolTip();
            tooltip.add(CommonComponents.EMPTY);
            tooltip.addAll(conductorStats);
        }
    }

    public static List<Component> getConductorStats(CableItem cableItem, Player player) {
        List<Component> list = new ArrayList<>();

        MServer config = MetallurgicaConfigs.server();

        boolean hasGoggles = GogglesItem.isWearingGoggles(player);

        MetalLang.translate("tooltip.resistivity")
                .style(GRAY)
                .addTo(list);
        double resistivity = WireConductorValues.getResistivity(cableItem.conductorEntry.get());
        Resistivity resistivityId = resistivity >= config.highResistivity.get() ? Resistivity.HIGH
                : (resistivity >= config.mediumResistivity.get() ? Resistivity.MEDIUM : Resistivity.LOW);

        LangBuilder builder = CreateLang.builder()
                .add(CreateLang.text(TooltipHelper.makeProgressBar(3, resistivityId.ordinal() + 1))
                        .style(resistivityId.getAbsoluteColor()));

        builder.translate("tooltip.resistivity." + Lang.asId(resistivityId.name()))
                .addTo(list);

        return list;
    }

    enum Resistivity {
        VERY_LOW(ChatFormatting.RED, ChatFormatting.GOLD),
        LOW(ChatFormatting.GOLD, ChatFormatting.YELLOW),
        MEDIUM(ChatFormatting.YELLOW, ChatFormatting.GREEN),
        HIGH(ChatFormatting.GREEN, ChatFormatting.DARK_GREEN)
        ;

        private final ChatFormatting absoluteColor;
        private final ChatFormatting relativeColor;

        Resistivity(ChatFormatting absoluteColor, ChatFormatting relativeColor) {
            this.absoluteColor = absoluteColor;
            this.relativeColor = relativeColor;
        }

        public ChatFormatting getAbsoluteColor() {
            return absoluteColor;
        }

        public ChatFormatting getRelativeColor() {
            return relativeColor;
        }

        public static Resistivity of(double resistivity) {
            if (resistivity > 1)
                return Resistivity.HIGH;
            if (resistivity > .75d)
                return Resistivity.MEDIUM;
            if (resistivity > .5d)
                return Resistivity.LOW;
            return Resistivity.VERY_LOW;
        }
    }
}
