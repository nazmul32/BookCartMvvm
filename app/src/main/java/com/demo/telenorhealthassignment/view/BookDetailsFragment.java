package com.demo.telenorhealthassignment.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo.telenorhealthassignment.R;
import com.demo.telenorhealthassignment.model.local.book.BookDataDetails;
import com.demo.telenorhealthassignment.model.repository.BookDataRepository;
import com.demo.telenorhealthassignment.util.Constants;
import com.demo.telenorhealthassignment.viewmodel.BookDetailsViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_BOOK_DETAILS = "key_book_details";

    // TODO: Rename and change types of parameters
    private BookDataDetails bookDataDetails;
    private BookDetailsViewModel bookDetailsViewModel;
    private BookDataRepository bookDataRepository;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bookDataDetails Parameter 1.
     * @return A new instance of fragment BookDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookDetailsFragment newInstance(BookDataDetails bookDataDetails) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_BOOK_DETAILS, bookDataDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookDataDetails = getArguments().getParcelable(KEY_BOOK_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);
        bookDataRepository = new BookDataRepository(getActivity().getApplication());
        setFragmentTitle();
        initBookDetailsViewModel();
        initView(view);
        return view;
    }

    public void setFragmentTitle() {
        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.book_details));
        }
    }

    private void initBookDetailsViewModel() {
        bookDetailsViewModel = ViewModelProviders.of(this).get(BookDetailsViewModel.class);
        bookDetailsViewModel.getIntegerMutableLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

            }
        });
    }

    private void initView(View view) {
        TextView tvId = view.findViewById(R.id.tv_details_id);
        TextView tvTitle = view.findViewById(R.id.tv_details_title);
        TextView tvSubTitle = view.findViewById(R.id.tv_details_sub_title);
        TextView tvDescription = view.findViewById(R.id.tv_details_description);
        TextView tvCreateAt = view.findViewById(R.id.tv_details_created_at);
        TextView tvUpdatedAt = view.findViewById(R.id.tv_details_updated_at);
        ImageView ivPreview = view.findViewById(R.id.iv_details_preview);
        final ImageView ivCart = view.findViewById(R.id.iv_details_cart);
        final ImageView ivWishList = view.findViewById(R.id.iv_details_wish_list);

        if (bookDataDetails.getType() == Constants.CART_LIST_TYPE) {
            ivCart.setImageResource(R.drawable.ic_cart_checked);
            ivWishList.setImageResource(R.drawable.ic_wish_list_unchecked);
        } else if (bookDataDetails.getType() == Constants.WISH_LIST_TYPE) {
            ivCart.setImageResource(R.drawable.ic_cart_unchecked);
            ivWishList.setImageResource(R.drawable.ic_wish_list_checked);
        } else {
            ivCart.setImageResource(R.drawable.ic_cart_unchecked);
            ivWishList.setImageResource(R.drawable.ic_wish_list_unchecked);
        }

        tvId.setText(bookDataDetails.getId() + "");
        tvTitle.setText(bookDataDetails.getTitle());
        tvSubTitle.setText(bookDataDetails.getSubTitle());
        tvDescription.setText(bookDataDetails.getDescription());
        tvCreateAt.setText(bookDataDetails.getCreatedAt());
        tvUpdatedAt.setText(bookDataDetails.getUpdatedAt());

        Glide.with(getActivity())
                .load(bookDataDetails.getPreview())
                .error(R.drawable.bg_list_item_place_holder_or_error)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(ivPreview);

        ivCart.setTag(R.drawable.ic_cart_unchecked);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) ivCart.getTag() == R.drawable.ic_cart_unchecked) {
                    bookDetailsViewModel.updateBookType(bookDataRepository, bookDataDetails.getId(), Constants.CART_LIST_TYPE);
                    ivCart.setTag(R.drawable.ic_cart_checked);
                    ivCart.setImageResource(R.drawable.ic_cart_checked);
                    ivWishList.setImageResource(R.drawable.ic_wish_list_unchecked);
                } else if ((int) ivCart.getTag() == R.drawable.ic_cart_checked) {
                    bookDetailsViewModel.updateBookType(bookDataRepository, bookDataDetails.getId(), Constants.DEFAULT_TYPE);
                    ivCart.setTag(R.drawable.ic_cart_unchecked);
                    ivCart.setImageResource(R.drawable.ic_cart_unchecked);
                }
            }
        });

        ivWishList.setTag(R.drawable.ic_wish_list_unchecked);
        ivWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) ivWishList.getTag() == R.drawable.ic_wish_list_unchecked) {
                    bookDetailsViewModel.updateBookType(bookDataRepository, bookDataDetails.getId(), Constants.WISH_LIST_TYPE);
                    ivWishList.setTag(R.drawable.ic_wish_list_checked);
                    ivWishList.setImageResource(R.drawable.ic_wish_list_checked);
                    ivCart.setImageResource(R.drawable.ic_cart_unchecked);
                } else if ((int) ivWishList.getTag() == R.drawable.ic_wish_list_checked) {
                    bookDetailsViewModel.updateBookType(bookDataRepository, bookDataDetails.getId(), Constants.DEFAULT_TYPE);
                    ivWishList.setTag(R.drawable.ic_wish_list_unchecked);
                    ivWishList.setImageResource(R.drawable.ic_wish_list_unchecked);
                }
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
