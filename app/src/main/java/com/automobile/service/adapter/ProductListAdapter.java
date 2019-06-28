package com.automobile.service.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.automobile.service.R;
import com.automobile.service.customecomponent.CustomTextView;
import com.automobile.service.fragment.ProductListFragment;
import com.automobile.service.model.product.ProductModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<ProductModel> productModelList;
    private OnItemClickListener onItemClickListener;
    private Context mContext;
    private ProductListFragment productListFragment;
    private boolean isLoading;
    private int lastPosition = -1;

    public ProductListAdapter(ProductListFragment productListFragment, Context context, List<ProductModel> items) {
        this.productModelList = items;
        this.productListFragment = productListFragment;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_productlist, parent, false);
        v.setOnClickListener(this);
        return new ViewHolderData(v);

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);




    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //final int itemType = getItemViewType(position);
//        if (position > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(mContext,
//                    R.anim.up_from_bottom
//            );
//            holder.itemView.startAnimation(animation);
//        }
        lastPosition = position;
        ((ViewHolderData) holder).bindData(productModelList.get(position), position);

    }

    public void addRecord(ArrayList<ProductModel> sleeptipsModelArrayList) {
        productModelList = sleeptipsModelArrayList;
    }


    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    @Override
    public void onClick(final View v) {
        // Give some time to the ripple to finish the effect
        if (onItemClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onItemClickListener.onItemClick(v, (ProductModel) v.getTag());
                }
            }, 200);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, ProductModel viewModel);

    }

    protected class ViewHolderData extends RecyclerView.ViewHolder {


        private CardView cvContainer;
        private CustomTextView tvTitle;
        private CustomTextView tvPrise;
        private ImageView ivProductImg;


        public ViewHolderData(View itemView) {
            super(itemView);

            cvContainer = (CardView) itemView.findViewById(R.id.row_productlist_cvContainer);
            tvTitle = (CustomTextView) itemView.findViewById(R.id.row_productlist_tvTitle);
            tvPrise = (CustomTextView) itemView.findViewById(R.id.row_productlist_tvPrise);
            ivProductImg = (ImageView) itemView.findViewById(R.id.row_productlist_ivProductImg);


        }

        public void bindData(ProductModel item, int position) {


            tvTitle.setText("" + item.getProductName());
            tvPrise.setText("₹"+item.getProductPrice());
            tvTitle.setTag(item);


            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Toast.makeText(mContext,"Clcik",Toast.LENGTH_LONG).show();
                }
            });


            if (item.getProductImage() != null && !item.getProductImage().isEmpty()) {
                Glide.with(mContext).load(item.getProductImage()).placeholder(R.drawable.ic_placeholder).centerCrop().into(ivProductImg);
            }

        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setLoading() {
        isLoading = true;
    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        //holder.itemView.clearAnimation();
    }
}
