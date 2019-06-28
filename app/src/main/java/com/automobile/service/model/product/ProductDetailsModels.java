package com.automobile.service.model.product;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductDetailsModels implements Serializable, Parcelable {

    @SerializedName("product_id")
    @Expose
    private String productId;

    @SerializedName("product_name")
    @Expose
    private String productName;

    @SerializedName("product_price")
    @Expose
    private String productPrice;

    @SerializedName("product_desc")
    @Expose
    private String productDesc;

    @SerializedName("product_qnty")
    @Expose
    private String productQnty;

    @SerializedName("product_created")
    @Expose
    private String productCreated;

    @SerializedName("product_image")
    @Expose
    private String productImage;

    @SerializedName("product_image_ar")
    @Expose
    private List<String> productImageAr = null;


    @SerializedName("product_html")
    @Expose
    private String productHtml;

    public final static Parcelable.Creator<ProductDetailsModels> CREATOR = new Creator<ProductDetailsModels>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProductDetailsModels createFromParcel(Parcel in) {
            ProductDetailsModels instance = new ProductDetailsModels();
            instance.productId = ((String) in.readValue((String.class.getClassLoader())));
            instance.productName = ((String) in.readValue((String.class.getClassLoader())));
            instance.productPrice = ((String) in.readValue((String.class.getClassLoader())));
            instance.productDesc = ((String) in.readValue((String.class.getClassLoader())));
            instance.productQnty = ((String) in.readValue((String.class.getClassLoader())));
            instance.productCreated = ((String) in.readValue((String.class.getClassLoader())));
            instance.productHtml = ((String) in.readValue((String.class.getClassLoader())));
            instance.productImage = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.productImageAr, (java.lang.String.class.getClassLoader()));
            return instance;
        }

        public ProductDetailsModels[] newArray(int size) {
            return (new ProductDetailsModels[size]);
        }

    };
    private final static long serialVersionUID = -6036791845974535496L;

    /**
     * No args constructor for use in serialization
     */
    public ProductDetailsModels() {
    }

    /**
     * @param productDesc
     * @param productImage
     * @param productCreated
     * @param productPrice
     * @param productName
     * @param productQnty
     * @param productImageAr
     * @param productId
     */
    public ProductDetailsModels(String productId, String productName, String productPrice, String productDesc, String productQnty, String productCreated, String productImage, List<String> productImageAr) {
        super();
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDesc = productDesc;
        this.productQnty = productQnty;
        this.productCreated = productCreated;
        this.productImage = productImage;
        this.productImageAr = productImageAr;
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

    public String getProductCreated() {
        return productCreated;
    }

    public void setProductCreated(String productCreated) {
        this.productCreated = productCreated;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public List<String> getProductImageAr() {
        return productImageAr;
    }

    public void setProductImageAr(List<String> productImageAr) {
        this.productImageAr = productImageAr;
    }

    public String getProductHtml() {
        return productHtml;
    }

    public void setProductHtml(String productHtml) {
        this.productHtml = productHtml;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(productId);
        dest.writeValue(productName);
        dest.writeValue(productPrice);
        dest.writeValue(productDesc);
        dest.writeValue(productQnty);
        dest.writeValue(productCreated);
        dest.writeValue(productImage);
        dest.writeList(productImageAr);
        dest.writeValue(productHtml);
    }

    public int describeContents() {
        return 0;
    }

}