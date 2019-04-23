package com.demo.telenorhealthassignment.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.demo.telenorhealthassignment.R;
import com.demo.telenorhealthassignment.adapter.HomeAdapter;
import com.demo.telenorhealthassignment.model.local.book.BookDataDetails;
import com.demo.telenorhealthassignment.model.repository.BookDataRepository;
import com.demo.telenorhealthassignment.util.Constants;
import com.demo.telenorhealthassignment.util.Helper;
import com.demo.telenorhealthassignment.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentSelectedListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeAdapter.OnRecyclerViewItemClickListener {

    private ProgressDialog progressDialog;
    private OnFragmentSelectedListener mListener;
    private HomeViewModel homeViewModel;
    private BookDataRepository bookDataRepository;
    private HomeAdapter mAdapter;
    private boolean isFirstTime = true;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setFragmentTitle();
        initHomeViewModel();
        initView(view);
        bookDataRepository = new BookDataRepository(getActivity().getApplication());
        homeViewModel.getAllBookDataFromDatabase(bookDataRepository);
        if (isFirstTime) {
            sendBookListRequest();
            isFirstTime = false;
        }
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_list_item));
        recyclerView.addItemDecoration(itemDecorator);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new HomeAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);
    }

    public HomeViewModel getHomeViewModel() {
        return homeViewModel;
    }

    private void initHomeViewModel() {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getHomeLiveData().observe(this, new Observer<List<BookDataDetails>>() {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart:
                mListener.onFragmentSelected(Constants.CART_FRAGMENT_ID);
                return true;
            case R.id.menu_wish_list:
                mListener.onFragmentSelected(Constants.WISH_LIST_FRAGMENT_ID);
                return true;
            case R.id.menu_log_out:
                mListener.onFragmentSelected(Constants.LOG_OUT_ID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setFragmentTitle() {
        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.home));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSelectedListener) {
            mListener = (OnFragmentSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRecyclerViewItemClicked(BookDataDetails bookDataDetails) {
        BookDetailsFragment bookDetailsFragment = BookDetailsFragment.newInstance(bookDataDetails);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, bookDetailsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentSelectedListener {
        // TODO: Update argument type and name
        void onFragmentSelected(int fragmentId);
    }

    private void sendBookListRequest() {
        if (Helper.isConnectionAvailable(getActivity())) {
            showProgressDialog();
            homeViewModel.sendAllBookDetailsRequest(bookDataRepository);
        } else {
            showErrorDialog(getString(R.string.internet_connection_unavailable));
        }
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
}
