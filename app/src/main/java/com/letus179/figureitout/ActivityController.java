package com.letus179.figureitout;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xfyin on 2017/9/24.
 */

public class ActivityController {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static  void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }

        }
    }
}
