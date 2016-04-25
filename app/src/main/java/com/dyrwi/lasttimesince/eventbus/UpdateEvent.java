package com.dyrwi.lasttimesince.eventbus;

/**
 * Created by Ben on 16-Mar-16.
 *
 * A class that is used with EventBus. This is just used for updating
 * or refreshing content in the app. This class does nothing at all except carry a string
 * that the listener will identify with.
 *
 * The corresponding "target" string will actually target the class's TAG.
 * E.G: ListViewActivity.TAG
 */
public class UpdateEvent extends BaseEvent {
    public static final String CREATE_NEW_ACTIVITY ="com.dyrwi.lasttimesince.create_new_activity";

    public final String target;
    private String[] strings;

    public UpdateEvent() {
        this.target = "";
    }
    public UpdateEvent(String target) {
        this.target = target;
    }

    public String getTag() {
        return target;
    }

    public String[] getStrings() {
        return strings;
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
    }
}
