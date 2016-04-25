package com.dyrwi.lasttimesince.eventbus;

/**
 * Created by Ben on 22-Mar-16.
 */
public abstract class BaseEvent {
    private String tag;
    private Class<? extends Object> targetClass;

    public Class<? extends Object> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<? extends Object> targetClass) {
        this.targetClass = targetClass;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
