package com.demo.telenorhealthassignment.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.demo.telenorhealthassignment.R;
import com.demo.telenorhealthassignment.adapter.CartAdapter;
import com.demo.telenorhealthassignment.adapter.HomeAdapter;
import com.demo.telenorhealthassignment.model.local.book.BookDataDetails;
import com.demo.telenorhealthassignment.model.repository.BookDataRepository;
import com.demo.telenorhealthassignment.util.Constants;
import com.demo.telenorhealthassignment.viewmodel.CartViewModel;
import com.demo.telenorhealthassignment.viewmodel.HomeViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements CartAdapter.OnRecyclerViewItemClickListener {
    private ProgressDialog progressDialog;
    private CartViewModel cartViewModel;
    private CartAdapter mAdapter;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the list_type_empty for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        setFragmentTitle();
        initCartViewModel();
        initView(view);
        BookDataRepository bookDataRepository = new BookDataRepository(getActivity().getApplication());
        showProgressDialog();
        cartViewModel.getAllCartListBookDataFromDatabase(bookDataRepository);
        return view;
    }

    public void setFragmentTitle() {
        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.cart_list));
        }
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.cart_recycler_view);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_list_item));
        recyclerView.addItemDecoration(itemDecorator);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CartAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);
    }

    public CartViewModel getCartViewModel() {
        return cartViewModel;
    }

    private void initCartViewModel() {
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        cartViewModel.getCartLiveData().observe(this, new Observer<List<BookDataDetails>>() {
            @Override
            public void onChanged(List<BookDataDetails> bookDataDetails) {
                hideProgressDialog();
                mAdapter.updateAdapter(bookDataDetails);
                if (bookDataDetails == null) {
                    showErrorDialog(getString(R.string.data_loading_error));
                }
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem itemCart = menu.findItem(R.id.menu_cart);
        MenuItem itemWishList = menu.findItem(R.id.menu_wish_list);
        MenuItem itemLogOut = menu.findItem(R.id.menu_log_out);
        itemCart.setVisible(false);
        itemWishList.setVisible(false);
        itemLogOut.setVisible(false);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
            }
        }, Constants.READ_TIMEOUT * 1000);
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(errorMessage)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRecyclerViewItemClicked(BookDataDetails bookDataDetails) {
        BookDetailsFragment bookDetailsFragment = BookDetailsFragment.newInstance(bookDataDetails);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, bookDetailsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
