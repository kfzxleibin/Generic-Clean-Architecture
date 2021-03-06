package com.zeyad.cleanarchitecture.presentation.views.activities;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zeyad.cleanarchitecture.domain.eventbus.RxEventBus;
import com.zeyad.cleanarchitecture.presentation.AndroidApplication;
import com.zeyad.cleanarchitecture.presentation.internal.di.components.ApplicationComponent;
import com.zeyad.cleanarchitecture.presentation.internal.di.modules.ActivityModule;
import com.zeyad.cleanarchitecture.presentation.navigation.Navigator;
import com.zeyad.cleanarchitecture.utilities.Utils;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Base {@link Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {
    // FIXME: 3/27/16 Fix DI!
    //    @Inject
    Navigator navigator;
    //    public static final int MSG_SERVICE_OBJ = 37;
    @Inject
    RxEventBus rxEventBus;
    public CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        navigator = new Navigator();
        mCompositeSubscription = Utils.getNewCompositeSubIfUnsubscribed(mCompositeSubscription);
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment        The fragment to be added.
     */
    protected void addFragment(int containerViewId, Fragment fragment, List<Pair<View, String>> sharedElements) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Pair<View, String> pair : sharedElements)
            fragmentTransaction.addSharedElement(pair.first, pair.second);
        fragmentTransaction.add(containerViewId, fragment).commit();
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link com.zeyad.cleanarchitecture.presentation.internal.di.components.ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((AndroidApplication) getApplicationContext()).getApplicationComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     * @return {@link com.zeyad.cleanarchitecture.presentation.internal.di.modules.ActivityModule}
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(getApplicationContext()).clearMemory();
        Glide.get(getApplicationContext()).trimMemory(ComponentCallbacks2.TRIM_MEMORY_COMPLETE);
        AndroidApplication.getRefWatcher(getApplicationContext()).watch(this);
    }

    /**
     * Shows a {@link Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

//    /**
//     * Service object to interact scheduled jobs.
//     */
//    GenericJobService mImageDownloadJobService;

    //    Handler mHandler = new Handler(/* default looper */) {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_SERVICE_OBJ:
//                    mImageDownloadJobService = (GenericJobService) msg.obj;
//                    mImageDownloadJobService.setUiCallback(ProductListActivity.this);
//            }
//        }
//    };
}