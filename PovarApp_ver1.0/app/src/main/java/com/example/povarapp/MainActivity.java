package com.example.povarapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.Enums.*;
import com.example.povarapp.MenuFragments.BuyListFragment.BuyListFragment;
import com.example.povarapp.MenuFragments.FavFragment.FavFragment;
import com.example.povarapp.MenuFragments.MainFragment.MainFragment;
import com.example.povarapp.MenuFragments.MyRecipesFragment.MyRecipesFragment;
import com.example.povarapp.MenuFragments.ProfileFragments.HostProfileFragment;
import com.example.povarapp.MenuFragments.SearchFragment.SearchFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Drawer drawer;
    private MaterialSearchView mSearchView;
    private MenuItem mSearch;
    FragmentManager fragmentManager;
    private Fragment fragment;
    private Toolbar toolbar;
    private Bundle args;
    private AppUserVM user;

    private void ChangeToolBarState(boolean state) {
        if (state) {
            mSearchView.setVisibility(View.VISIBLE);
            mSearch.setVisible(true);
        }
        else {
            mSearchView.setVisibility(View.GONE);
            mSearch.setVisible(false);
        }
    }

    public void ChangeUser(AppUserVM user) {
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        if (user == null) {
            user = new AppUserVM();
            user.SetId(0);
        }

        //создание тулбара
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        args = new Bundle();

        //получение менеджера фрагментов
        fragmentManager = getSupportFragmentManager();
    }

    private void InitializeDrawer() {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Профиль").withTag(MainState.SIGNIN),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Главная").withTag(MainState.HOME),
                        new PrimaryDrawerItem().withIdentifier(3).withName("Мои рецепты").withTag(MainState.MY_RCPS),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Избранное").withTag(MainState.FAV),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Список покупок").withTag(MainState.BUY)
                )
                .withOnDrawerItemClickListener(OnMainMenuItemClickListener)
                .build();
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        drawer.setSelection(2);
    }


    Drawer.OnDrawerItemClickListener OnMainMenuItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            fragment = null;
            Class fragmentClass = null;
            switch((MainState)drawerItem.getTag()) {
                case SIGNIN:
                    fragmentClass = HostProfileFragment.class;
                    ChangeToolBarState(false);
                    break;
                case HOME:
                    fragmentClass = MainFragment.class;
                    ChangeToolBarState(true);
                    break;
                case MY_RCPS:
                    fragmentClass = MyRecipesFragment.class;
                    ChangeToolBarState(false);
                    break;
                case BUY:
                    fragmentClass = BuyListFragment.class;
                    ChangeToolBarState(false);
                    break;
                case FAV:
                    fragmentClass = FavFragment.class;
                    ChangeToolBarState(false);
                    break;
                default:
                    fragmentClass = MainFragment.class;
                    ChangeToolBarState(false);
            }
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                args.putSerializable("user", user);
                fragment.setArguments(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentManager.beginTransaction().replace(R.id.fl_content, fragment).commit();
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        mSearch = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(mSearch);

        //init drawer
        InitializeDrawer();

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (fragment instanceof SearchFragment) {
                    ((SearchFragment) fragment).OnSearchTextChanged(newText);
                }
                return true;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                Bundle args = new Bundle();
                args.putSerializable("mainState", MainState.HOME);
                args.putSerializable("user", user);
                fragment = new SearchFragment();
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.fl_content, fragment).commit();
            }

            @Override
            public void onSearchViewClosed() {
                Bundle args = new Bundle();
                args.putSerializable("mainState", MainState.HOME);
                args.putSerializable("user", user);
                fragment = new MainFragment();
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.fl_content, fragment).commit();
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
