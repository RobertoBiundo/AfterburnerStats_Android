package com.bugbinc.afterburnerstats.models;

import com.google.gson.annotations.SerializedName;

public class Stat {

    // VARIABLES
    @SerializedName("Name")
    private String Name;

    @SerializedName("Units")
    private String Units;

    @SerializedName("Value")
    private float Value;

    @SerializedName("MaxValue")
    private float MaxValue;

    @SerializedName("MinValue")
    private float MinValue;

    @SerializedName("Gpu")
    private float Gpu;


    // GETTERS AND SETTERS
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUnits() {
        return Units;
    }

    public void setUnits(String units) {
        Units = units;
    }

    public float getValue() {
        return Value;
    }

    public void setValue(float value) {
        Value = value;
    }

    public float getMaxValue() {
        return MaxValue;
    }

    public void setMaxValue(float maxValue) {
        MaxValue = maxValue;
    }

    public float getMinValue() {
        return MinValue;
    }

    public void setMinValue(float minValue) {
        MinValue = minValue;
    }

    public float getGpu() {
        return Gpu;
    }

    public void setGpu(float gpu) {
        Gpu = gpu;
    }
}
