package com.freezedown.metallurgica.foundation.data.custom.composition;

import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public record Element(String name, int amount, boolean areNumbersUp, boolean bracketed, boolean forceCloseBracket, boolean appendDash, int groupedAmount) {
    public static final Codec<Element> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("element").forGetter(Element::getName),
            Codec.INT.optionalFieldOf("amount", 1).forGetter(Element::getAmount),
            Codec.BOOL.optionalFieldOf("areNumbersUp", false).forGetter(Element::areNumbersUp),
            Codec.BOOL.optionalFieldOf("bracketed", false).forGetter(Element::bracketed),
            Codec.BOOL.optionalFieldOf("forceCloseBracket", false).forGetter(Element::forceCloseBracket),
            Codec.BOOL.optionalFieldOf("appendDash", false).forGetter(Element::hasDash),
            Codec.INT.optionalFieldOf("groupedAmount", 1).forGetter(Element::getGroupedAmount)
    ).apply(instance, Element::new));
    
    public static Element create(String name) {
        return new Element(name, 1, false, false, false, false, 1);
    }
    
    public Element withAmount(int amount) {
        return new Element(name, amount, areNumbersUp, bracketed, forceCloseBracket, appendDash, groupedAmount);
    }
    
    public Element withNumbersUp() {
        return new Element(name, amount, true, bracketed, forceCloseBracket, appendDash, groupedAmount);
    }
    
    public Element withBrackets() {
        return new Element(name, amount, areNumbersUp, true, forceCloseBracket, appendDash, groupedAmount);
    }
    
    public Element withForceClosedBracket() {
        return new Element(name, amount, areNumbersUp, true, true, appendDash, groupedAmount);
    }
    
    public Element withDash() {
        return new Element(name, amount, areNumbersUp, bracketed, forceCloseBracket, true, groupedAmount);
    }
    
    public Element withGroupedAmount(int groupedAmount) {
        return new Element(name, amount, areNumbersUp, bracketed, forceCloseBracket, appendDash, groupedAmount);
    }
    
    public String getName() {
        return name;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public int getGroupedAmount() {
        return groupedAmount;
    }
    
    public boolean areNumbersUp() {
        return areNumbersUp;
    }
    
    public boolean bracketed() {
        return bracketed;
    }
    
    public boolean isBracketForceClosed() {
        return forceCloseBracket;
    }
    
    public boolean hasDash() {
        return appendDash;
    }
    
    public static List<Element> createComposition(Element... elements) {
        return List.of(elements);
    }
    
    public String getDisplay() {
        String display = ClientUtil.lang().translate("element." + name.toLowerCase()).string();
        if (amount > 1)
            display += amount;
        return areNumbersUp ? ClientUtil.toSmallUpNumbers(display) : ClientUtil.toSmallDownNumbers(display);
    }
    
    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeUtf(name);
        buf.writeInt(amount);
        buf.writeBoolean(areNumbersUp);
        buf.writeBoolean(bracketed);
        buf.writeBoolean(forceCloseBracket);
        buf.writeBoolean(appendDash);
        buf.writeInt(groupedAmount);
    }
    
    public static Element fromNetwork(FriendlyByteBuf buf) {
        String name = buf.readUtf();
        int amount = buf.readInt();
        boolean areNumbersUp = buf.readBoolean();
        boolean bracketed = buf.readBoolean();
        boolean forceCloseBracket = buf.readBoolean();
        boolean appendDash = buf.readBoolean();
        int groupedAmount = buf.readInt();
        return new Element(name, amount, areNumbersUp, bracketed, forceCloseBracket, appendDash, groupedAmount);
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
