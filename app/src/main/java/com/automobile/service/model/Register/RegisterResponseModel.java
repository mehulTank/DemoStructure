
package com.automobile.service.model.Register;

import android.os.Parcel;
import android.os.Parcelable;

import com.automobile.service.model.Login.LoginDetailsModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponseModel implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public final static Creator<RegisterResponseModel> CREATOR = new Creator<RegisterResponseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RegisterResponseModel createFromParcel(Parcel in) {
            return new RegisterResponseModel(in);
        }

        public RegisterResponseModel[] newArray(int size) {
            return (new RegisterResponseModel[size]);
        }

    }
    ;

    protected RegisterResponseModel(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));

    }

    public RegisterResponseModel() {
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



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
    }

    public int describeContents() {
        return  0;
    }

}
