
package com.automobile.service.model.Login;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseModel implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("details")
    @Expose
    private LoginDetailsModel details;
    public final static Creator<LoginResponseModel> CREATOR = new Creator<LoginResponseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LoginResponseModel createFromParcel(Parcel in) {
            return new LoginResponseModel(in);
        }

        public LoginResponseModel[] newArray(int size) {
            return (new LoginResponseModel[size]);
        }

    }
    ;

    protected LoginResponseModel(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        this.details = ((LoginDetailsModel) in.readValue((LoginDetailsModel.class.getClassLoader())));
    }

    public LoginResponseModel() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LoginDetailsModel getDetails() {
        return details;
    }

    public void setDetails(LoginDetailsModel details) {
        this.details = details;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeValue(details);
    }

    public int describeContents() {
        return  0;
    }

}
