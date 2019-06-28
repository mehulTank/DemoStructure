package com.automobile.service.model.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("user_access_token")
    @Expose
    private String userAccessToken;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_contact_number")
    @Expose
    private String userContactNumber;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("user_school_id")
    @Expose
    private String userSchoolId;
    @SerializedName("user_school_name")
    @Expose
    private String userSchoolName;
    @SerializedName("user_manager_access")
    @Expose
    private Integer userManagerAccess;
    @SerializedName("user_icon_image")
    @Expose
    private String userIconImage;
    @SerializedName("user_medium_image")
    @Expose
    private String userMediumImage;
    @SerializedName("user_has_active_trips")
    @Expose
    private Integer userHasActiveTrips;

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public void setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserSchoolId() {
        return userSchoolId;
    }

    public void setUserSchoolId(String userSchoolId) {
        this.userSchoolId = userSchoolId;
    }

    public String getUserSchoolName() {
        return userSchoolName;
    }

    public void setUserSchoolName(String userSchoolName) {
        this.userSchoolName = userSchoolName;
    }

    public Integer getUserManagerAccess() {
        return userManagerAccess;
    }

    public void setUserManagerAccess(Integer userManagerAccess) {
        this.userManagerAccess = userManagerAccess;
    }

    public String getUserIconImage() {
        return userIconImage;
    }

    public void setUserIconImage(String userIconImage) {
        this.userIconImage = userIconImage;
    }

    public String getUserMediumImage() {
        return userMediumImage;
    }

    public void setUserMediumImage(String userMediumImage) {
        this.userMediumImage = userMediumImage;
    }

    public Integer getUserHasActiveTrips() {
        return userHasActiveTrips;
    }

    public void setUserHasActiveTrips(Integer userHasActiveTrips) {
        this.userHasActiveTrips = userHasActiveTrips;
    }

}