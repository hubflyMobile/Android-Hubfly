package com.hubfly.ctq.util;

import android.content.pm.PackageManager;

/**
 * Created by vis-1597 on 8/25/2016.
 */
public abstract class PermissionUtil {
    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
