package com.demo.telenorhealthassignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo.telenorhealthassignment.R;
import com.demo.telenorhealthassignment.model.local.book.BookDataDetails;
import com.demo.telenorhealthassignment.util.Constants;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.GenericViewHolder> {
    private List<BookDataDetails> bookDataDetails;
    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public static class GenericViewHolder extends RecyclerView.ViewHolder {
        public GenericViewHolder(View v) {
            super(v);
        }
    }

    public static class HomeViewHolder extends GenericViewHolder {
        public TextView tvTitle;
        public TextView tvSubTitle;
        public ImageView ivPreview;
        public ImageView ivAddToCart;
        public ImageView ivWishList;

        public HomeViewHolder(View v) {
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
    public HomeAdapter(Context context, Fragment fragment) {
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
            vh = new HomeViewHolder(v);
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
            HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
            Glide.with(context)
                    .load(bookDataDetails.get(position).getPreview())
                    .error(R.drawable.bg_list_item_place_holder_or_error)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(homeViewHolder.ivPreview);
            homeViewHolder.tvTitle.setText(bookDataDetails.get(position).getTitle());
            homeViewHolder.tvSubTitle.setText(bookDataDetails.get(position).getSubTitle());
            if (bookDataDetails.get(position).getType() == Constants.CART_LIST_TYPE) {
                homeViewHolder.ivAddToCart.setImageResource(R.drawable.ic_cart_checked);
                homeViewHolder.ivWishList.setImageResource(R.drawable.ic_wish_list_unchecked);
            } else if (bookDataDetails.get(position).getType() == Constants.WISH_LIST_TYPE) {
                homeViewHolder.ivAddToCart.setImageResource(R.drawable.ic_cart_unchecked);
                homeViewHolder.ivWishList.setImageResource(R.drawable.ic_wish_list_checked);
            } else {
                homeViewHolder.ivAddToCart.setImageResource(R.drawable.ic_cart_unchecked);
                homeViewHolder.ivWishList.setImageResource(R.drawable.ic_wish_list_unchecked);
            }
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
