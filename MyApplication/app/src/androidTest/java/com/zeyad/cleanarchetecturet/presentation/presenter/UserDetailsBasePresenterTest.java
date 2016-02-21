package com.zeyad.cleanarchetecturet.test;

import android.content.Context;
import android.test.AndroidTestCase;

import com.zeyad.cleanarchetecturet.domain.interactor.GetUserDetails;
import com.zeyad.cleanarchetecturet.presentation.model.mapper.UserModelDataMapper;
import com.zeyad.cleanarchetecturet.presentation.presenters.UserDetailsPresenter;
import com.zeyad.cleanarchetecturet.presentation.view.UserDetailsView;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Subscriber;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class UserDetailsBasePresenterTest extends AndroidTestCase {

    private static final int FAKE_USER_ID = 123;

    private UserDetailsPresenter userDetailsPresenter;

    @Mock
    private Context mockContext;
    @Mock
    private UserDetailsView mockUserDetailsView;
    @Mock
    private GetUserDetails mockGetUserDetails;
    @Mock
    private UserModelDataMapper mockUserModelDataMapper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        userDetailsPresenter = new UserDetailsPresenter(mockGetUserDetails,
                mockUserModelDataMapper);
        userDetailsPresenter.setView(mockUserDetailsView);
    }

    public void testUserDetailsPresenterInitialize() {
        given(mockUserDetailsView.getContext()).willReturn(mockContext);

        userDetailsPresenter.initialize(FAKE_USER_ID);

        verify(mockUserDetailsView).hideRetry();
        verify(mockUserDetailsView).showLoading();
        verify(mockGetUserDetails).execute(any(Subscriber.class));
    }
}