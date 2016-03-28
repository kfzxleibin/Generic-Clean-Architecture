package com.zeyad.cleanarchitecture.presentation.presenters;

import android.support.annotation.NonNull;

import com.zeyad.cleanarchitecture.data.entities.UserRealmModel;
import com.zeyad.cleanarchitecture.domain.exceptions.DefaultErrorBundle;
import com.zeyad.cleanarchitecture.domain.exceptions.ErrorBundle;
import com.zeyad.cleanarchitecture.domain.interactor.DefaultSubscriber;
import com.zeyad.cleanarchitecture.domain.interactor.GeneralizedUseCase;
import com.zeyad.cleanarchitecture.domain.models.User;
import com.zeyad.cleanarchitecture.presentation.exception.ErrorMessageFactory;
import com.zeyad.cleanarchitecture.presentation.internal.di.PerActivity;
import com.zeyad.cleanarchitecture.presentation.model.UserModel;
import com.zeyad.cleanarchitecture.presentation.views.UserDetailsView;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class GeneralDetailPresenter implements BasePresenter {

    /**
     * id used to retrieve user details
     */
    private int userId;
    private UserDetailsView viewDetailsView;
    private final GeneralizedUseCase getUserDetailsBaseUseCase;

    @Inject
    public GeneralDetailPresenter(@Named("generalizedDetailUseCase") GeneralizedUseCase generalizedUseCase) {
        this.getUserDetailsBaseUseCase = generalizedUseCase;
    }

    public void setView(@NonNull UserDetailsView view) {
        this.viewDetailsView = view;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        getUserDetailsBaseUseCase.unsubscribe();
    }

    /**
     * Initializes the presenter by start retrieving user details.
     */
    public void initialize(int userId) {
        this.userId = userId;
        loadUserDetails();
    }

    /**
     * Loads user details.
     */
    private void loadUserDetails() {
        hideViewRetry();
        showViewLoading();
        getUserDetails();
    }

    private void showViewLoading() {
        viewDetailsView.showLoading();
    }

    private void hideViewLoading() {
        viewDetailsView.hideLoading();
    }

    private void showViewRetry() {
        viewDetailsView.showRetry();
    }

    private void hideViewRetry() {
        viewDetailsView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        viewDetailsView.showError(ErrorMessageFactory.create(viewDetailsView.getContext(),
                errorBundle.getException()));
    }

    private void showUserDetailsInView(UserModel userModel) {
        viewDetailsView.renderUser(userModel);
    }

    private void getUserDetails() {
        getUserDetailsBaseUseCase.execute(new UserDetailsSubscriber(), UserModel.class, User.class,
                UserRealmModel.class, userId);
    }

    private final class UserDetailsSubscriber extends DefaultSubscriber<UserModel> {
        @Override
        public void onCompleted() {
            hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            hideViewLoading();
            showErrorMessage(new DefaultErrorBundle((Exception) e));
            showViewRetry();
            e.printStackTrace();
        }

        @Override
        public void onNext(UserModel userModel) {
            showUserDetailsInView(userModel);
        }
    }
}