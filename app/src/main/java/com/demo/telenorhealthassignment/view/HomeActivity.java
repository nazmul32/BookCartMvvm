package com.demo.telenorhealthassignment.view;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.demo.telenorhealthassignment.R;
import com.demo.telenorhealthassignment.model.repository.BookDataRepository;
import com.demo.telenorhealthassignment.util.Constants;
import com.demo.telenorhealthassignment.util.PreferencesManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentSelectedListener {
    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private WishListFragment wishListFragment;
    private BookDataRepository bookDataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bookDataRepository = new BookDataRepository(getApplication());
        setOnBackStackChangedListener();
        navigateToHomeFragment();
    }

    private void setOnBackStackChangedListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fragment != null) {
                    if (Constants.HOME_FRAGMENT.equals(fragment.getClass().getSimpleName())) {
                        homeFragment.setFragmentTitle();
                        homeFragment.getHomeViewModel().getAllBookDataFromDatabase(bookDataRepository);
                    } else if (Constants.CART_FRAGMENT_TAG.equals(fragment.getClass().getSimpleName())) {
                        cartFragment.setFragmentTitle();
                        cartFragment.getCartViewModel().getAllCartListBookDataFromDatabase(bookDataRepository);
                    } else if (Constants.WISH_LIST_FRAGMENT.equals(fragment.getClass().getSimpleName())) {
                        wishListFragment.setFragmentTitle();
                        wishListFragment.getWishListViewModel().getAllWishListBookDataFromDatabase(bookDataRepository);
                    }
                }
            }
        });
    }

    private void navigateToHomeFragment() {
        homeFragment = HomeFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, homeFragment);
        ft.commit();
    }

    private void navigateToCartFragment() {
        cartFragment = CartFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, cartFragment);
        ft.addToBackStack(Constants.CART_FRAGMENT_TAG);
        ft.commit();
    }

    private void navigateToWishListFragment() {
        wishListFragment = WishListFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, wishListFragment);
        ft.addToBackStack(Constants.WISH_LIST_FRAGMENT_TAG);
        ft.commit();
    }

    @Override
    public void onFragmentSelected(int fragmentId) {
        if (fragmentId == Constants.CART_FRAGMENT_ID) {
            navigateToCartFragment();
        } else if (fragmentId == Constants.WISH_LIST_FRAGMENT_ID) {
            navigateToWishListFragment();
        } else if (fragmentId == Constants.LOG_OUT_ID) {
            showLogoutDialog();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                ActivityCompat.finishAffinity(HomeActivity.this);
            }
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.you_will_be_logged_out))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (PreferencesManager.removeString(Constants.KEY_ACCESS_TOKEN)) {
                            dialog.dismiss();
                            HomeActivity.super.onBackPressed();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }
}
