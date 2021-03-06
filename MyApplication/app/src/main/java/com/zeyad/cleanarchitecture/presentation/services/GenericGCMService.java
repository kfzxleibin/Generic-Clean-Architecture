package com.zeyad.cleanarchitecture.presentation.services;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.zeyad.cleanarchitecture.utilities.Utils;

public class GenericGCMService extends GcmTaskService {

    public static final String TAG = GenericNetworkQueueIntentService.class.getSimpleName(),
            TAG_TASK_ONE_OFF_LOG = "one_off_task",
            TAG_TASK_PERIODIC_LOG = "periodic_task";

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        // Reschedule removed tasks here
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        switch (taskParams.getTag()) {
            case TAG_TASK_ONE_OFF_LOG:
                Log.i(TAG, TAG_TASK_ONE_OFF_LOG);
                switch (taskParams.getExtras().getString(GenericNetworkQueueIntentService.JOB_TYPE)) {
                    case GenericNetworkQueueIntentService.DOWNLOAD_IMAGE:
                        String url = taskParams.getExtras().getString(GenericNetworkQueueIntentService.EXTRA_REMOTE_PATH);
                        startService(new Intent(getApplicationContext(), GenericNetworkQueueIntentService.class)
                                .putExtra(GenericNetworkQueueIntentService.EXTRA_REMOTE_PATH, url)
                                .putExtra(GenericNetworkQueueIntentService.EXTRA_REMOTE_NAME,
                                        Utils.getFileNameFromUrl(url))
                                .putExtra(GenericNetworkQueueIntentService.WIDTH, -1)
                                .putExtra(GenericNetworkQueueIntentService.HEIGHT, -1));
                        break;
                    case GenericNetworkQueueIntentService.UPLOAD_IMAGE:
                        // not yet
                        break;
                    case GenericNetworkQueueIntentService.POST_OBJECT:
                        startService(new Intent(this, GenericNetworkQueueIntentService.class)
                                .putExtra(GenericNetworkQueueIntentService.JOB_TYPE, GenericNetworkQueueIntentService.POST)
                                .putExtra(GenericNetworkQueueIntentService.POST_OBJECT,
                                        (Parcelable) taskParams.getExtras()
                                                .getParcelable(GenericNetworkQueueIntentService.POST_OBJECT)));
                        break;
                    case GenericNetworkQueueIntentService.DELETE_COLLECTION:
                        startService(new Intent(this, GenericNetworkQueueIntentService.class)
                                .putExtra(GenericNetworkQueueIntentService.JOB_TYPE, GenericNetworkQueueIntentService.DELETE_COLLECTION)
                                .putExtra(GenericNetworkQueueIntentService.DELETE_COLLECTION,
                                        (Parcelable) taskParams.getExtras()
                                                .getParcelable(GenericNetworkQueueIntentService.DELETE_COLLECTION)));
                        break;
                    default:
                        break;
                }
                return GcmNetworkManager.RESULT_SUCCESS;
            case TAG_TASK_PERIODIC_LOG:
                Log.i(TAG, TAG_TASK_PERIODIC_LOG);
                // This is where useful work would go
                return GcmNetworkManager.RESULT_SUCCESS;
            default:
                return GcmNetworkManager.RESULT_FAILURE;
        }
    }
}