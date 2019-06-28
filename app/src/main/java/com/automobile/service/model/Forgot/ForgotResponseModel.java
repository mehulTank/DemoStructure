
package com.automobile.service.model.Forgot;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotResponseModel implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public final static Creator<ForgotResponseModel> CREATOR = new Creator<ForgotResponseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ForgotResponseModel createFromParcel(Parcel in) {
            return new ForgotResponseModel(in);
        }

        public ForgotResponseModel[] newArray(int size) {
            return (new ForgotResponseModel[size]);
        }

    }
    ;

    protected ForgotResponseModel(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));

    }

    public ForgotResponseModel() {
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
