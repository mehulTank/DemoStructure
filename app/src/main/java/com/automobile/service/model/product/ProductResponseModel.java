
package com.automobile.service.model.product;

import android.os.Parcel;
import android.os.Parcelable;

import com.automobile.service.model.Login.LoginDetailsModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductResponseModel implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("total_page")
    @Expose
    private String total_page;
    @SerializedName("details")
    @Expose
    private ArrayList<ProductModel> details;


    public final static Creator<ProductResponseModel> CREATOR = new Creator<ProductResponseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ProductResponseModel createFromParcel(Parcel in) {
            return new ProductResponseModel(in);
        }

        public ProductResponseModel[] newArray(int size) {
            return (new ProductResponseModel[size]);
        }

    }
    ;

    protected ProductResponseModel(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        this.total_page = ((String) in.readValue((String.class.getClassLoader())));
        this.details = ((ArrayList<ProductModel>) in.readValue((ProductModel.class.getClassLoader())));
    }

    public ProductResponseModel() {
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

    public ArrayList<ProductModel> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<ProductModel> details) {
        this.details = details;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeValue(details);
        dest.writeValue(total_page);
    }

    public int describeContents() {
        return  0;
    }

    public String getTotal_page() {
        return total_page;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

}
