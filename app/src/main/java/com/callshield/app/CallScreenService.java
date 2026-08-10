package com.callshield.app;

import android.net.Uri;
import android.telecom.CallScreeningService;

public class CallScreenService extends CallScreeningService {

    @Override
    public void onScreenCall(android.telecom.Call.Details details) {
        Uri handle = details.getHandle();
        String number = handle == null ? "" : handle.getSchemeSpecificPart();
        StorageManager storage = StorageManager.getInstance(this);

        boolean blocked = storage.isBlocked(number);
        if (!blocked && storage.isSmartBlockEnabled() && storage.getAttemptCount(number) >= 3) {
            storage.addBlocked(number, "Spam");
            blocked = true;
        }

        if (blocked) {
            storage.logAttempt(number, "call");
            NotificationHelper.sendBlockNotification(this, number);
        }

        CallResponse response = new CallResponse.Builder()
                .setDisallowCall(blocked)
                .setSkipCallLog(false)
                .setSkipNotification(false)
                .build();
        respondToCall(details, response);
    }
}
