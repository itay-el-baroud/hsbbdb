package com.callshield.app;

import android.telecom.Call;
import android.telecom.InCallService;
import android.util.Log;

public class CallScreenService extends InCallService {
    @Override
    public void onCallAdded(Call call) {
        String number = call.getDetails().getHandle().getSchemeSpecificPart();
        if (StorageManager.getInstance(this).isBlocked(number)) {
            call.disconnect();
            Log.d("CallShield", "Blocked incoming call from: " + number);
            NotificationHelper.sendBlockNotification(this, number);
        } else {
            super.onCallAdded(call);
        }
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
    }
}
