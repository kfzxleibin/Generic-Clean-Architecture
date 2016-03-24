package com.zeyad.cleanarchitecturet.presentation.internal.di.modules;

import com.zeyad.cleanarchitecturet.domain.executors.PostExecutionThread;
import com.zeyad.cleanarchitecturet.domain.executors.ThreadExecutor;
import com.zeyad.cleanarchitecturet.domain.interactor.BaseUseCase;
import com.zeyad.cleanarchitecturet.domain.interactor.GeneralizedUseCase;
import com.zeyad.cleanarchitecturet.domain.interactor.GetUserDetails;
import com.zeyad.cleanarchitecturet.domain.interactor.GetUserList;
import com.zeyad.cleanarchitecturet.domain.repositories.Repository;
import com.zeyad.cleanarchitecturet.domain.repositories.UserRepository;
import com.zeyad.cleanarchitecturet.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related collaborators.
 */
@Module
public class UserModule {

    private int userId = -1;

    public UserModule() {
    }

    public UserModule(int userId) {
        this.userId = userId;
    }

    @Provides
    @PerActivity
    @Named("userEntityList")
    BaseUseCase provideGetUserListUseCase(GetUserList getUserList) {
        return getUserList;
    }

    @Provides
    @PerActivity
    @Named("userDetails")
    BaseUseCase provideGetUserDetailsUseCase(UserRepository userRepository, ThreadExecutor threadExecutor,
                                             PostExecutionThread postExecutionThread) {
        return new GetUserDetails(userId, userRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("generalEntityList")
    GeneralizedUseCase provideGetGeneralListUseCase(Repository repository, ThreadExecutor threadExecutor,
                                                    PostExecutionThread postExecutionThread) {
        return new GeneralizedUseCase(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("generalEntityDetail")
    GeneralizedUseCase provideGetGeneralDetailUseCase(Repository repository, ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutionThread) {
        return new GeneralizedUseCase(repository, threadExecutor, postExecutionThread);
    }
}