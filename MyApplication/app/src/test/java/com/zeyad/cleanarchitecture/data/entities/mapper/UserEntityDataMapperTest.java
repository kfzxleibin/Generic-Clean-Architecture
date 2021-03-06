package com.zeyad.cleanarchitecture.data.entities.mapper;

import com.zeyad.cleanarchitecture.data.ApplicationTestCase;
import com.zeyad.cleanarchitecture.data.entities.UserEntity;
import com.zeyad.cleanarchitecture.domain.models.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class UserEntityDataMapperTest extends ApplicationTestCase {

    private static final int FAKE_USER_ID = 123;
    private static final String FAKE_FULLNAME = "Tony Stark";

    private UserEntityDataMapper userEntityDataMapper;

    @Before
    public void setUp() throws Exception {
        userEntityDataMapper = new UserEntityDataMapper();
    }

    @Test
    public void testTransformUserEntity() {
        UserEntity userEntity = createFakeUserEntity();
        User user = userEntityDataMapper.transform(userEntity);
        assertThat(user, is(instanceOf(User.class)));
        assertThat(user.getUserId(), is(FAKE_USER_ID));
        assertThat(user.getFullName(), is(FAKE_FULLNAME));
    }

    @Test
    public void testTransformUserEntityCollection() {
        UserEntity mockUserEntityOne = mock(UserEntity.class);
        UserEntity mockUserEntityTwo = mock(UserEntity.class);

        List<UserEntity> userEntityList = new ArrayList<>(5);
        userEntityList.add(mockUserEntityOne);
        userEntityList.add(mockUserEntityTwo);

        Collection<User> userCollection = userEntityDataMapper.transform(userEntityList);
        assertThat(userCollection.toArray()[0], is(instanceOf(User.class)));
        assertThat(userCollection.toArray()[1], is(instanceOf(User.class)));
        assertThat(userCollection.size(), is(2));
    }

    private UserEntity createFakeUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(FAKE_USER_ID);
        userEntity.setFullName(FAKE_FULLNAME);
        return userEntity;
    }
}