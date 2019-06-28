
package com.automobile.service.model.Login;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDetailsModel implements Parcelable
{

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("user_wallet_amount")
    @Expose
    private String userWalletAmount;
    @SerializedName("user_created")
    @Expose
    private String userCreated;
    @SerializedName("user_profile")
    @Expose
    private String userProfile;
    public final static Creator<LoginDetailsModel> CREATOR = new Creator<LoginDetailsModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LoginDetailsModel createFromParcel(Parcel in) {
            return new LoginDetailsModel(in);
        }

        public LoginDetailsModel[] newArray(int size) {
            return (new LoginDetailsModel[size]);
        }

    }
    ;

    protected LoginDetailsModel(Parcel in) {
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.userEmail = ((String) in.readValue((String.class.getClassLoader())));
        this.userPhone = ((String) in.readValue((String.class.getClassLoader())));
        this.userWalletAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.userCreated = ((String) in.readValue((String.class.getClassLoader())));
        this.userProfile = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LoginDetailsModel() {
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserWalletAmount() {
        return userWalletAmount;
    }

    public void setUserWalletAmount(String userWalletAmount) {
        this.userWalletAmount = userWalletAmount;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(userName);
        dest.writeValue(userEmail);
        dest.writeValue(userPhone);
        dest.writeValue(userWalletAmount);
        dest.writeValue(userCreated);
        dest.writeValue(userProfile);
    }

    public int describeContents() {
        return  0;
    }

}
