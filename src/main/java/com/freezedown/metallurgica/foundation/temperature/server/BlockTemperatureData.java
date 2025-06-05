package com.freezedown.metallurgica.foundation.temperature.server;

public class BlockTemperatureData {
    private double temperature;
    private final double diffusivity;

    public BlockTemperatureData(double v, double d) {
        temperature = v;
        diffusivity = d;
    }

    public double getDiffusivity() {
        return diffusivity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double t) {
        temperature = t;
    }
}
