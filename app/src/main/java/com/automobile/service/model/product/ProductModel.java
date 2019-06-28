
package com.automobile.service.model.product;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductModel implements Parcelable
{

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_desc")
    @Expose
    private String productDesc;
    @SerializedName("product_qnty")
    @Expose
    private String productQnty;
    @SerializedName("product_status")
    @Expose
    private String productStatus;
    @SerializedName("product_created")
    @Expose
    private String productCreated;
    @SerializedName("product_qty")
    @Expose
    private Integer productQty;

    public Integer getProductQty() {
        return productQty;
    }

    public void setProductQty(Integer productQty) {
        this.productQty = productQty;
    }



    public final static Creator<ProductModel> CREATOR = new Creator<ProductModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ProductModel createFromParcel(Parcel in) {
            ProductModel instance = new ProductModel();
            instance.productId = ((String) in.readValue((String.class.getClassLoader())));
            instance.productName = ((String) in.readValue((String.class.getClassLoader())));
            instance.productPrice = ((String) in.readValue((String.class.getClassLoader())));
            instance.productImage = ((String) in.readValue((String.class.getClassLoader())));
            instance.productDesc = ((String) in.readValue((String.class.getClassLoader())));
            instance.productQnty = ((String) in.readValue((String.class.getClassLoader())));
            instance.productStatus = ((String) in.readValue((String.class.getClassLoader())));
            instance.productCreated = ((String) in.readValue((String.class.getClassLoader())));
            instance.productQty = in.readByte() == 0x00 ? null : in.readInt();
            return instance;
        }

        public ProductModel[] newArray(int size) {
            return (new ProductModel[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ProductModel() {
    }

    /**
     * 
     * @param productDesc
     * @param productStatus
     * @param productImage
     * @param productCreated
     * @param productPrice
     * @param productName
     * @param productQnty
     * @param productId
     */
    public ProductModel(String productId, String productName, String productPrice, String productImage, String productDesc, String productQnty, String productStatus, String productCreated) {
        super();
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productDesc = productDesc;
        this.productQnty = productQnty;
        this.productStatus = productStatus;
        this.productCreated = productCreated;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductQnty() {
        return productQnty;
    }

    public void setProductQnty(String productQnty) {
        this.productQnty = productQnty;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductCreated() {
        return productCreated;
    }

    public void setProductCreated(String productCreated) {
        this.productCreated = productCreated;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(productId);
        dest.writeValue(productName);
        dest.writeValue(productPrice);
        dest.writeValue(productImage);
        dest.writeValue(productDesc);
        dest.writeValue(productQnty);
        dest.writeValue(productStatus);
        dest.writeValue(productCreated);

        if (productQty == null) {
            dest.writeByte((byte) (0x01));
        }
    }

    public int describeContents() {
        return  0;
    }

}
