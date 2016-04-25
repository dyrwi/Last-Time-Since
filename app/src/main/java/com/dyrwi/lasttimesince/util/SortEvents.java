package com.dyrwi.lasttimesince.util;

import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.BaseEntity;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;

/**
 * Created by Ben on 17-Mar-16.
 */
public class SortEvents {

    private SortEvents(String request) {
    }

    public static class byMostRecent implements java.util.Comparator<JodaEvent> {

        @Override
        public int compare(JodaEvent lhs, JodaEvent rhs) {
            return 0;
        }
    }
}
