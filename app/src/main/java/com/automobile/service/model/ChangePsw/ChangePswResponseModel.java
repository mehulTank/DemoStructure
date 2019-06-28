
package com.automobile.service.model.ChangePsw;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePswResponseModel implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public final static Creator<ChangePswResponseModel> CREATOR = new Creator<ChangePswResponseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ChangePswResponseModel createFromParcel(Parcel in) {
            return new ChangePswResponseModel(in);
        }

        public ChangePswResponseModel[] newArray(int size) {
            return (new ChangePswResponseModel[size]);
        }

    }
    ;

    protected ChangePswResponseModel(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));

    }

    public ChangePswResponseModel() {
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
