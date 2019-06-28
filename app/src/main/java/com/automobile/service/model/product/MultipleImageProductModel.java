package com.automobile.service.model.product;

public class MultipleImageProductModel
{
    private String mattress_products_id;

    private String image;

    public String getMattress_products_id ()
    {
        return mattress_products_id;
    }

    public void setMattress_products_id (String mattress_products_id)
    {
        this.mattress_products_id = mattress_products_id;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [mattress_products_id = "+mattress_products_id+", image = "+image+"]";
    }
}