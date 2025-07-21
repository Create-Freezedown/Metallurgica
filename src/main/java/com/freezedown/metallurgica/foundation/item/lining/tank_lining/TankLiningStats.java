package com.freezedown.metallurgica.foundation.item.lining.tank_lining;

import com.freezedown.metallurgica.foundation.block_entity.behaviour.TankLiningBehaviour;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class TankLiningStats {
    @Setter
    @Getter
    float damage = 0;

    LiningProperties properties;

    public TankLiningStats(LiningProperties properties) {
        this.properties = properties;
    }


    public int getMaxDurability() {
        return properties.getMaxDamage();
    }

    public CorrosionResistance getCorrosionResistance() {
        return properties.getCorrosionResistance();
    }

    public TemperatureResistance getTemperatureResistance() {
        return properties.getTemperatureResistance();
    }

    public boolean hurtAndBreak(int damage) {
        float corrosionMultiplier = getCorrosionResistance() != null ? getCorrosionResistance().getDamageMultiplier() : 1.0f;
        float temperatureMultiplier = getTemperatureResistance() != null ? getTemperatureResistance().getDamageMultiplier() : 1.0f;
        float newDamage = getDamage() + ((damage * corrosionMultiplier) * temperatureMultiplier);
        if (newDamage >= getMaxDurability()) {
            return true;
        }
        setDamage(newDamage);
        return false;
    }

    public InteractionResult applyToBE(SmartBlockEntity blockEntity, Player player) {
        if (blockEntity.getBehaviour(TankLiningBehaviour.TYPE) != null) {
            ItemStack toApply = player.getItemInHand(player.getUsedItemHand());
            TankLiningBehaviour liningBehaviour = blockEntity.getBehaviour(TankLiningBehaviour.TYPE);
            if (liningBehaviour.hasLining()) {
                ItemStack newLining = liningBehaviour.removeLining();
                ItemHandlerHelper.giveItemToPlayer(player, newLining, player.getInventory().selected);
            }
            liningBehaviour.setLining(toApply.copy());
            toApply.shrink(1);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    public void writeToNBT(CompoundTag tag) {
        CompoundTag liningTag = new CompoundTag();
        liningTag.putFloat("damage", getDamage());
        liningTag.putInt("maxDamage", getMaxDurability());
        if (getCorrosionResistance() != null) {
            CompoundTag corrosionResistanceTag = new CompoundTag();
            getCorrosionResistance().serialize(corrosionResistanceTag);
            liningTag.put("corrosionResistance", corrosionResistanceTag);
        }
        if (getTemperatureResistance() != null) {
            CompoundTag temperatureResistanceTag = new CompoundTag();
            getTemperatureResistance().serialize(temperatureResistanceTag);
            liningTag.put("temperatureResistance", temperatureResistanceTag);
        }
        tag.put("liningStats", liningTag);
    }

    public static TankLiningStats readFromNBT(CompoundTag tag) {
        CompoundTag liningTag = tag.getCompound("liningStats");
        float damage = liningTag.getFloat("damage");
        LiningProperties properties = new LiningProperties(liningTag.getInt("maxDamage"));
        if (liningTag.contains("corrosionResistance")) {
            properties.corrosionResistant(CorrosionResistance.deserialize(liningTag.getCompound("corrosionResistance")));
        }
        if (liningTag.contains("temperatureResistance")) {
            properties.temperatureResistant(TemperatureResistance.deserialize(liningTag.getCompound("temperatureResistance")));
        }
        TankLiningStats stats = new TankLiningStats(properties);
        stats.setDamage(damage);
        return stats;
    }

    public void addToTooltip(List<Component> tooltip) {
        ClientUtil.lang().translate("tooltip.tank_lining.durability").style(ChatFormatting.GRAY).forGoggles(tooltip);
        int currentDurability = (int) (getMaxDurability() - getDamage());
        String durabilityText = currentDurability + "/" + getMaxDurability();
        ClientUtil.lang().text(durabilityText).style(ChatFormatting.DARK_GREEN).forGoggles(tooltip, 1);
        if (getCorrosionResistance() != null) {
            getCorrosionResistance().addToTooltip(tooltip, 0);
        }
        if (getTemperatureResistance() != null) {
            getTemperatureResistance().addToTooltip(tooltip, 0);
        }
    }

    public void addToGoggleTooltip(List<Component> tooltip, ItemStack liningStack) {
        ClientUtil.lang().add(liningStack.getHoverName()).forGoggles(tooltip);
        int currentDurability = (int) (getMaxDurability() - getDamage());
        String durabilityText = currentDurability + "/" + getMaxDurability();
        ClientUtil.lang().translate("tooltip.tank_lining.durability").style(ChatFormatting.GRAY).space().text(durabilityText).style(ChatFormatting.DARK_GREEN).forGoggles(tooltip, 1);
        if (getCorrosionResistance() != null) {
            getCorrosionResistance().addToTooltip(tooltip, 1);
        }
        if (getTemperatureResistance() != null) {
            getTemperatureResistance().addToTooltip(tooltip, 1);
        }
    }

    public static class LiningProperties {
        @Getter
        int maxDamage;
        @Getter
        CorrosionResistance corrosionResistance;
        @Getter
        TemperatureResistance temperatureResistance;

        public LiningProperties(int maxDamage) {
            this.maxDamage = maxDamage;
        }

        public LiningProperties corrosionResistant(float damageMultiplier) {
            this.corrosionResistance = new CorrosionResistance().damageMultiplier(damageMultiplier);
            return this;
        }
        public LiningProperties corrosionResistant(CorrosionResistance corrosionResistance) {
            this.corrosionResistance = corrosionResistance;
            return this;
        }

        public LiningProperties temperatureResistant(float damageMultiplier, double maxTemperature, float damageMultiplierWhenOverheated) {
            this.temperatureResistance = new TemperatureResistance()
                    .damageMultiplier(damageMultiplier)
                    .damageMultiplierWhenOverheated(damageMultiplierWhenOverheated)
                    .maxTemperature(maxTemperature);
            return this;
        }
        public LiningProperties temperatureResistant(TemperatureResistance temperatureResistance) {
            this.temperatureResistance = temperatureResistance;
            return this;
        }
    }

    public static class CorrosionResistance {
        @Getter
        float damageMultiplier = 1.0f;

        public CorrosionResistance damageMultiplier(float damageMultiplier) {
            this.damageMultiplier = damageMultiplier;
            return this;
        }

        public void serialize(CompoundTag tag) {
            tag.putFloat("corrosionDmgMultiplier", getDamageMultiplier());
        }

        public static CorrosionResistance deserialize(CompoundTag tag) {
            CorrosionResistance resistance = new CorrosionResistance();
            resistance.damageMultiplier(tag.getFloat("corrosionDmgMultiplier"));
            return resistance;
        }

        public void addToTooltip(List<Component> tooltip, int indent) {
            ClientUtil.lang().translate("tooltip.tank_lining.corrosion_resistant").style(ChatFormatting.BLUE).forGoggles(tooltip, indent);
        }
    }

    public static class TemperatureResistance {
        @Getter
        float damageMultiplier = 1.0f;
        @Getter
        float damageMultiplierWhenOverheated = 1.0f;
        @Getter
        double maxTemperature = 100.0;

        public TemperatureResistance damageMultiplier(float damageMultiplier) {
            this.damageMultiplier = damageMultiplier;
            return this;
        }

        public TemperatureResistance damageMultiplierWhenOverheated(float damageMultiplierWhenOverheated) {
            this.damageMultiplierWhenOverheated = damageMultiplierWhenOverheated;
            return this;
        }

        public TemperatureResistance maxTemperature(double maxTemperature) {
            this.maxTemperature = maxTemperature;
            return this;
        }

        public void serialize(CompoundTag tag) {
            tag.putFloat("tempDmgMultiplier", getDamageMultiplier());
            tag.putFloat("overheatedTempDmgMultiplier", getDamageMultiplierWhenOverheated());
            tag.putDouble("maxTemp", getMaxTemperature());
        }

        public static TemperatureResistance deserialize(CompoundTag tag) {
            TemperatureResistance resistance = new TemperatureResistance();
            resistance.damageMultiplier(tag.getFloat("tempDmgMultiplier"));
            resistance.damageMultiplierWhenOverheated(tag.getFloat("overheatedTempDmgMultiplier"));
            resistance.maxTemperature(tag.getDouble("maxTemp"));
            return resistance;
        }

        public void addToTooltip(List<Component> tooltip, int indent) {
            ClientUtil.lang().translate("tooltip.tank_lining.temperature_resistant").style(ChatFormatting.GRAY).forGoggles(tooltip, indent);
            ClientUtil.lang().translate("tooltip.tank_lining.temperature_resistant.max").space().style(ChatFormatting.GRAY).add(ClientUtil.temperature(maxTemperature)).style(ChatFormatting.GOLD).forGoggles(tooltip, indent + 1);
        }
    }
}
