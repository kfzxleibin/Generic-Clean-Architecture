package com.zeyad.cleanarchitecture.presentation.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.grability.rappitendero.RappiApplication;
import com.grability.rappitendero.domain.eventbus.RxEventBus;
import com.grability.rappitendero.presentation.di.HasComponent;
import com.grability.rappitendero.presentation.di.components.ApplicationComponent;
import com.grability.rappitendero.presentation.factories.SnackBarFactory;
import com.grability.rappitendero.presentation.navigation.Navigator;
import com.grability.rappitendero.utils.Utils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Base {@link Fragment} class for every fragment in this application.
 */
public abstract class BaseFragment extends Fragment {

    @Inject
    public Navigator navigator;
    @Inject
    public RxEventBus rxEventBus;
    public CompositeSubscription mCompositeSubscription;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        setRetainInstance(true);
        mCompositeSubscription = Utils.getNewCompositeSubIfUnsubscribed(mCompositeSubscription);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    public abstract void initialize();

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link com.grability.rappitendero.presentation.di.components.ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((RappiApplication) getContext().getApplicationContext()).getApplicationComponent();
    }

    /**
     * Shows a {@link Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showSnackBarMessage(@SnackBarFactory.SnackBarType String typeSnackBar, View view, String message, int duration) {
        SnackBarFactory.getSnackBar(typeSnackBar, view, message, duration)
                .show();
    }

    protected void showSnackBarMessage(@SnackBarFactory.SnackBarType String typeSnackBar, View view, int messageId, int duration) {
        SnackBarFactory.getSnackBar(typeSnackBar, view, getString(messageId), duration)
                .show();
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        Utils.unsubscribeIfNotNull(mCompositeSubscription);
        super.onDestroyView();
    }
}