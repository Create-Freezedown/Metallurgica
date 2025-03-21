package com.freezedown.metallurgica.foundation.data.custom.composition.data;

import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;


public record Element(@Getter String name, @Getter int amount) {
    public static final Codec<Element> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("element").forGetter(Element::getName),
            Codec.INT.optionalFieldOf("amount", 1).forGetter(Element::getAmount)
    ).apply(instance, Element::new));
    
    public static Element create(String name) {
        return new Element(name, 1);
    }
    
    public Element withAmount(int amount) {
        return new Element(name, amount);
    }
    
    public int getAmount() {
        return amount;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("amount", getAmount());
        json.addProperty("element", getName());
        return json;
    }
    
    public static List<SubComposition> createComposition(SubComposition.Builder... subCompositionBuilders) {
        List<SubComposition> subCompositions = new ArrayList<>();
        for (SubComposition.Builder builder : subCompositionBuilders) {
            subCompositions.add(builder.build());
        }
        return subCompositions;
    }
    
    public String getDisplay() {
        StringBuilder display = new StringBuilder(ClientUtil.lang().translate("element." + name.toLowerCase()).string());
        if (amount > 1)
            display.append(amount);
        return ClientUtil.toSmallDownNumbers(display.toString());
    }
    
    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeUtf(name);
        buf.writeInt(amount);
    }
    
    public static Element fromNetwork(FriendlyByteBuf buf) {
        String name = buf.readUtf();
        int amount = buf.readInt();
        return new Element(name, amount);
    }
    
    public static List<Element> listFromNetwork(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            elements.add(fromNetwork(buf));
        }
        return elements;
    }
}
