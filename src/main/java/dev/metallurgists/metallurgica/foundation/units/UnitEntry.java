package dev.metallurgists.metallurgica.foundation.units;

import dev.metallurgists.metallurgica.foundation.util.ClientUtil;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.lang.LangBuilder;

import java.util.function.BiConsumer;

public class UnitEntry {
    private final Couple<String> metric;
    private final Couple<String> imperial;
    public final double toImperial;
    
    public UnitEntry(Couple<String> metric, Couple<String> imperial, double toImperial) {
        this.metric = metric;
        this.imperial = imperial;
        this.toImperial = toImperial;
    }
    
    public Couple<String> getMetric() {
        return metric;
    }
    
    public Couple<String> getImperial() {
        return imperial;
    }
    
    public double getToImperial() {
        return toImperial;
    }
    
    public double convertToImperial(double value) {
        return value * toImperial;
    }
    
    public double convertFromImperial(double value) {
        return value / toImperial;
    }
    
    public void provideLang(BiConsumer<String, String> consumer) {
        consumer.accept("metallurgica.generic.unit." + getMetric().getFirst(), getMetric().getSecond());
        consumer.accept("metallurgica.generic.unit." + getImperial().getFirst(), getImperial().getSecond());
    }
    
    public LangBuilder metricLang() {
        return ClientUtil.lang().translate("generic.unit." + metric.getFirst());
    }
    
    public LangBuilder imperialLang() {
        return ClientUtil.lang().translate("generic.unit." + imperial.getFirst());
    }
}
