package cn.uc.uccustomtabs.service;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsService;
import android.support.customtabs.CustomTabsSessionToken;
import android.util.Log;

import java.util.List;

/**
 * @author andrewlu
 *         用于处理CustomTab需要的远程服务处理类.
 *         实际任务交给WebViewProvider类来处理.
 */

public class ALCustomTabService extends CustomTabsService {

    @Override
    protected boolean warmup(long flags) {
        Log.i("ALCustomTabService", "warmup");
        WebViewProvider.getInstance().warmup(1);
        return true;
    }

    @Override
    protected boolean newSession(CustomTabsSessionToken sessionToken) {
        Log.i("ALCustomTabService", "newSession");

        return true;
    }

    @Override
    protected boolean mayLaunchUrl(CustomTabsSessionToken sessionToken, Uri url, Bundle extras, List<Bundle> otherLikelyBundles) {
        Log.i("ALCustomTabService", "mayLaunchUrl");
        WebViewProvider.getInstance().mayLaunchUrl(url.toString());
        return true;
    }

    @Override
    protected Bundle extraCommand(String commandName, Bundle args) {
        Log.i("ALCustomTabService", "extraCommand");
        return null;
    }

    @Override
    protected boolean updateVisuals(CustomTabsSessionToken sessionToken, Bundle bundle) {
        Log.i("ALCustomTabService", "updateVisuals");
        return false;
    }
}
