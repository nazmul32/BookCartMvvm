package com.demo.telenorhealthassignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo.telenorhealthassignment.R;
import com.demo.telenorhealthassignment.model.local.book.BookDataDetails;
import com.demo.telenorhealthassignment.util.Constants;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.GenericViewHolder> {
    private List<BookDataDetails> bookDataDetails;
    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public static class GenericViewHolder extends RecyclerView.ViewHolder {
        public GenericViewHolder(View v) {
            super(v);
        }
    }

    public static class WishListViewHolder extends GenericViewHolder {
        public TextView tvTitle;
        public TextView tvSubTitle;
        public ImageView ivPreview;
        public ImageView ivAddToCart;
        public ImageView ivWishList;

        public WishListViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_title);
            tvSubTitle = v.findViewById(R.id.tv_sub_title);
            ivPreview = v.findViewById(R.id.iv_preview);
            ivAddToCart = v.findViewById(R.id.iv_add_to_cart);
            ivWishList = v.findViewById(R.id.iv_wish_list);
        }
    }

    public static class EmptyViewHolder extends GenericViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WishListAdapter(Context context, Fragment fragment) {
        this.context = context;
        if (fragment instanceof OnRecyclerViewItemClickListener) {
            this.onRecyclerViewItemClickListener = (OnRecyclerViewItemClickListener) fragment;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecyclerViewItemClickListener");
        }
    }

    public void updateAdapter(List<BookDataDetails> bookDataDetails) {
        this.bookDataDetails = bookDataDetails;
        notifyDataSetChanged();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View v;
        final GenericViewHolder vh;
        if (viewType == Constants.EMPTY_VIEW_TYPE) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_type_empty, parent, false);
            vh = new EmptyViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_type_normal, parent, false);
            vh = new WishListViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener
                                .onRecyclerViewItemClicked(bookDataDetails
                                        .get(vh.getAdapterPosition()));
                    }
                }
            });
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        if (holder.getItemViewType() == Constants.NORMAL_VIEW_TYPE) {
            WishListViewHolder wishListViewHolder = (WishListViewHolder) holder;
            Glide.with(context)
                    .load(bookDataDetails.get(position).getPreview())
                    .error(R.drawable.bg_list_item_place_holder_or_error)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(wishListViewHolder.ivPreview);
            wishListViewHolder.tvTitle.setText(bookDataDetails.get(position).getTitle());
            wishListViewHolder.tvSubTitle.setText(bookDataDetails.get(position).getSubTitle());
            wishListViewHolder.ivAddToCart.setImageResource(R.drawable.ic_cart_unchecked);
            wishListViewHolder.ivWishList.setImageResource(R.drawable.ic_wish_list_checked);
        }
    }

    @Override
    public int getItemCount() {
        return (bookDataDetails == null || bookDataDetails.size() == 0) ? 1 : bookDataDetails.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (bookDataDetails == null || bookDataDetails.size() == 0) {
            return Constants.EMPTY_VIEW_TYPE;
        } else {
            return Constants.NORMAL_VIEW_TYPE;
        }
    }

    public interface OnRecyclerViewItemClickListener {
        // TODO: Update argument type and name
        void onRecyclerViewItemClicked(BookDataDetails bookDataDetails);
    }
}
