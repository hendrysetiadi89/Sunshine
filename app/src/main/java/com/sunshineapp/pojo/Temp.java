package com.sunshineapp.pojo;

import java.util.HashMap;
import java.util.Map;

public class Temp {

    private Double min;
    private Double max;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The min
     */
    public Double getMin() {
        return min;
    }

    /**
     *
     * @param min
     * The min
     */
    public void setMin(Double min) {
        this.min = min;
    }

    /**
     *
     * @return
     * The max
     */
    public Double getMax() {
        return max;
    }

    /**
     *
     * @param max
     * The max
     */
    public void setMax(Double max) {
        this.max = max;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
