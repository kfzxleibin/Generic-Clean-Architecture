package com.zeyad.cleanarchitecture.presentation.view_models;

/**
 * Class that represents a user in the presentation layer.
 */
public class UserViewModel {

    private int userId;

    public UserViewModel(int userId) {
        this.userId = userId;
    }

    public UserViewModel() {
    }

    private String coverUrl;
    private String full_name;
    private String email;
    private String description;
    private int followers;
    private boolean checked;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String fullName) {
        this.full_name = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "";
    }
}
