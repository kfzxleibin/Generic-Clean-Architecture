package com.zeyad.cleanarchitecture.presentation.presenters;

import android.content.Context;
import android.test.AndroidTestCase;

import com.zeyad.cleanarchitecture.domain.interactors.GetUserList;
import com.zeyad.cleanarchitecture.presentation.view_models.mapper.UserViewModelDataMapper;
import com.zeyad.cleanarchitecture.presentation.views.UserListView;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Subscriber;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class UserListBasePresenterTest extends AndroidTestCase {

    private UserListPresenter userListPresenter;

    @Mock
    private Context mockContext;
    @Mock
    private UserListView mockUserListView;
    @Mock
    private GetUserList mockGetUserList;
    @Mock
    private UserViewModelDataMapper mockUserViewModelDataMapper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        userListPresenter = new UserListPresenter(mockGetUserList, mockUserViewModelDataMapper);
        userListPresenter.setView(mockUserListView);
    }

    public void testUserListPresenterInitialize() {
        given(mockUserListView.getContext()).willReturn(mockContext);

        userListPresenter.initialize();

        verify(mockUserListView).hideRetry();
        verify(mockUserListView).showLoading();
        verify(mockGetUserList).execute(any(Subscriber.class));
    }
}
