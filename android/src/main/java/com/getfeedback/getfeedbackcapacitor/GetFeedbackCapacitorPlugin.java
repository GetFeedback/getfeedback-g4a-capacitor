package com.getfeedback.getfeedbackcapacitor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.usabilla.sdk.ubform.UbConstants;
import com.usabilla.sdk.ubform.Usabilla;
import com.usabilla.sdk.ubform.UsabillaFormCallback;
import com.usabilla.sdk.ubform.sdk.entity.FeedbackResult;
import com.usabilla.sdk.ubform.sdk.form.FormClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "GetFeedbackCapacitor")
public class GetFeedbackCapacitorPlugin extends Plugin implements UsabillaFormCallback {

    private Usabilla getfeedback = Usabilla.INSTANCE;
    private static final String LOG_TAG = "Usabilla Ionic Bridge";
    private Fragment form;
    public static final String FRAGMENT_TAG = "passive form";
    private static final String DEFAULT_DATA_MASKS = "DEFAULT_DATA_MASKS";
    private static final String KEY_ABANDONED_PAGE_INDEX = "abandonedPageIndex";
    private static final String KEY_RATING = "rating";
    private static final String KEY_SENT = "sent";
    private static final JSArray defaultDataMasks = new JSArray(UbConstants.getDefaultDataMasks());
    private static final String defaultMaskCharacter = "X";

    private String passiveCallbackId;
    private String campaignCallbackId;
    private String standardEventsCallID;

    private BroadcastReceiver closingCampaignReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final JSObject result = prepareResult(intent, FeedbackResult.INTENT_FEEDBACK_RESULT_CAMPAIGN);
                PluginCall call = bridge.getSavedCall(campaignCallbackId);
                if (call != null) {
                    call.resolve(result);
                    bridge.releaseCall(call);
                } else {
                    PluginCall callStandardEvent = bridge.getSavedCall(standardEventsCallID);
                    callStandardEvent.resolve(result);
                }
            }
        }
    };
    private BroadcastReceiver closingFormReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final JSObject result = prepareResult(intent, FeedbackResult.INTENT_FEEDBACK_RESULT);
                PluginCall call = bridge.getSavedCall(passiveCallbackId);
                call.resolve(result);
                bridge.releaseCall(call);
            }
            final Activity activity = getActivity();
            if (activity instanceof FragmentActivity) {
                final FragmentManager supportFragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                final Fragment fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG);
                if (fragment != null) {
                    supportFragmentManager.beginTransaction().remove(fragment).commit();
                }
            }
        }
    };

    @Override
    public void load() {
        super.load();
        LocalBroadcastManager
            .getInstance(getActivity())
            .registerReceiver(closingFormReceiver, new IntentFilter(UbConstants.INTENT_CLOSE_FORM));
        LocalBroadcastManager
            .getInstance(getActivity())
            .registerReceiver(closingCampaignReceiver, new IntentFilter(UbConstants.INTENT_CLOSE_CAMPAIGN));
    }

    @Override
    public void handleOnDestroy() {
        super.handleOnDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(closingFormReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(closingCampaignReceiver);
    }

    @PluginMethod
    public void initialize(@NotNull PluginCall call) {
        String appID = call.getString("appID");
        final Activity activity = getActivity();
        if (activity != null) {
            getfeedback.initialize(activity.getBaseContext(), appID);
            getfeedback.updateFragmentManager(((FragmentActivity) activity).getSupportFragmentManager());
            return;
        }
        Log.e(LOG_TAG, "Initialisation not possible. Android activity is null");
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void standardEvents(@NotNull PluginCall call) {
        bridge.saveCall(call);
        call.setKeepAlive(true);
        standardEventsCallID = call.getCallbackId();
    }

    @PluginMethod
    public void loadFeedbackForm(@NotNull PluginCall call) {
        String formID = call.getString("formID");
        bridge.saveCall(call);
        passiveCallbackId = call.getCallbackId();
        getfeedback.loadFeedbackForm(formID, null, null, this);
    }

    @PluginMethod
    public void loadFeedbackFormWithCurrentViewScreenshot(@NotNull PluginCall call) {
        String formID = call.getString("formID");
        bridge.saveCall(call);
        passiveCallbackId = call.getCallbackId();
        final Activity activity = getActivity();
        if (activity != null) {
            final Bitmap screenshot = getfeedback.takeScreenshot(activity);
            getfeedback.loadFeedbackForm(formID, screenshot, this);
            return;
        }
        Log.e(LOG_TAG, "Loading feedback form not possible. Android activity is null");
    }

    @PluginMethod
    public void preloadFeedbackForms(@NotNull PluginCall call) throws JSONException {
        JSArray formIDs = call.getArray("formIDs");
        List<String> listformIDs = new ArrayList<>();
        for (int i = 0; i < formIDs.length(); i++) {
            listformIDs.add(formIDs.getString(i));
        }
        getfeedback.preloadFeedbackForms(listformIDs);
    }

    @PluginMethod
    public void removeCachedForms(@NotNull PluginCall call) {
        getfeedback.removeCachedForms();
    }

    @PluginMethod
    public void setCustomVariables(@NotNull PluginCall call) {
        JSObject customVars = call.getObject("customVariables");
        final HashMap<String, Object> customVariables = new HashMap<>();
        if (customVars != null) {
            for (Iterator<String> it = customVars.keys(); it.hasNext();) {
                final String key = it.next();
                Object value;
                try {
                    value = customVars.get(key);
                } catch (JSONException e) {
                    value = null;
                    e.printStackTrace();
                }
                customVariables.put(key, value);
            }
        }
        getfeedback.setCustomVariables(customVariables);
    }

    @PluginMethod
    public void sendEvent(@NotNull PluginCall call) {
        String eventName = call.getString("eventName");
        bridge.saveCall(call);
        campaignCallbackId = call.getCallbackId();
        final Activity activity = getActivity();
        if (activity != null) {
            getfeedback.sendEvent(activity.getBaseContext(), eventName);
            return;
        }
        Log.e(LOG_TAG, "Sending event to Usabilla is not possible. Android activity is null");
    }

    @PluginMethod
    public void resetCampaignData(@NotNull PluginCall call) {
        final Activity activity = getActivity();
        if (activity != null) {
            getfeedback.resetCampaignData(activity.getBaseContext());
            return;
        }
        Log.e(LOG_TAG, "Resetting Usabilla campaigns is not possible. Android activity is null");
    }

    @PluginMethod
    public boolean dismiss(@NotNull PluginCall call) {
        final Activity activity = getActivity();
        if (activity != null) {
            return getfeedback.dismiss(activity.getBaseContext());
        }
        Log.e(LOG_TAG, "Dismissing the Usabilla form is not possible. Android activity is null");
        return false;
    }

    @PluginMethod
    public void setDebugEnabled(@NonNull PluginCall call) {
        final boolean debugEnabled = call.getBoolean("debugEnabled");
        getfeedback.setDebugEnabled(debugEnabled);
    }

    @PluginMethod
    public void setDataMasking(@NonNull PluginCall call) {
        JSArray masks = call.getArray("masks", defaultDataMasks);
        String character = call.getString("character", defaultMaskCharacter);
        List<String> listMasks = null;
        try {
            listMasks = masks.toList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getfeedback.setDataMasking(listMasks, character.charAt(0));
    }

    @PluginMethod
    public void getDefaultDataMasks(@NonNull PluginCall call) {
        final JSObject result = new JSObject();
        String[] array = UbConstants.getDefaultDataMasks().toArray(new String[0]);
        try {
            result.put(DEFAULT_DATA_MASKS, new JSArray(array));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        call.resolve(result);
    }

    @Override
    public void formLoadFail() {
        PluginCall call = bridge.getSavedCall(passiveCallbackId);
        call.reject("The form could not be loaded");
        bridge.releaseCall(call);
    }

    @Override
    public void formLoadSuccess(@NotNull FormClient formClient) {
        form = formClient.getFragment();
        final Activity activity = getActivity();
        if (activity instanceof FragmentActivity && form != null) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, form, FRAGMENT_TAG)
                .commit();
            form = null;
        }
    }

    private JSObject prepareResult(Intent intent, String feedbackResultType) {
        final JSObject result = new JSObject();
        final JSObject resultData = new JSObject();
        String res = (feedbackResultType == FeedbackResult.INTENT_FEEDBACK_RESULT) ? "results" : "result";
        resultData.put(res, getResult(intent, feedbackResultType));
        result.put("completed", resultData);
        return result;
    }

    private JSObject getResult(Intent intent, String feedbackResultType) {
        final FeedbackResult res = intent.getParcelableExtra(feedbackResultType);
        final JSObject result = new JSObject();
        result.put(KEY_RATING, res.getRating());
        result.put(KEY_ABANDONED_PAGE_INDEX, res.getAbandonedPageIndex());
        result.put(KEY_SENT, res.isSent());
        return result;
    }

    @Override
    public void mainButtonTextUpdated(@NotNull String s) {
        // To fill with handling required when the main button text changes
    }
}
