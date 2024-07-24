package com.freezedown.metallurgica.foundation.item;

import net.minecraft.nbt.*;

import java.io.DataOutput;
import java.io.IOException;

public class ImpurityTag implements Tag {
    @Override
    public void write(DataOutput dataOutput) throws IOException {
    
    }
    
    @Override
    public byte getId() {
        return 69;
    }
    
    @Override
    public TagType<?> getType() {
        return null;
    }
    
    @Override
    public Tag copy() {
        return null;
    }
    
    @Override
    public void accept(TagVisitor tagVisitor) {
    
    }
    
    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor streamTagVisitor) {
        return null;
    }
}
