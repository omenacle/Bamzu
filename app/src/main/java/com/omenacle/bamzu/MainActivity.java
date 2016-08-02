package com.omenacle.bamzu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.omenacle.bamzu.fragment.FavoritesFragment;
import com.omenacle.bamzu.fragment.HelpFragment;
import com.omenacle.bamzu.fragment.HomeFragment;
import com.omenacle.bamzu.fragment.SearchFragment;
import com.omenacle.bamzu.fragment.SettingsFragment;
import com.omenacle.bamzu.models.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String VISIBLE_FRAGMENT = "visible_fragment";
    private static final String TAG = ".MainActivity";
    private NavigationView mNavigationView;
    private Menu mNavigationMenu;

    //Buttons
    Button mBtnSignIn;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    //[START declare_init_auth]
    private FirebaseAuth.AuthStateListener mAuthListener;
    //[END declare_init_auth]

    //[START declare_current_usr]
    private User mCurrentUser;
    //[END declare_current_user]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This will post ad", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //set navigation bar
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        //getting menu
        mNavigationMenu = mNavigationView.getMenu();


        //[start initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        //[end initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in " + user.getUid());
                    mCurrentUser = new User(user);
                    updateNavBar(mCurrentUser);
                } else {
                    //user is logged out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    updateNavBar(null);
                }

            }
        };
        //[END auth_state_listener]


        //if search was sent from @Link SearchView
        displayView(R.id.nav_home);


    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    private void updateNavBar(User mCurrentUser) {
        //Buttons
        mBtnSignIn = (Button) mNavigationView.getHeaderView(0).findViewById(R.id.btn_sign_in);

        mNavigationMenu = mNavigationView.getMenu();
        MenuItem mNavSignOut = mNavigationMenu.findItem(R.id.nav_sign_out);

        if (mCurrentUser == null) {
            Log.d(TAG, "Not Signed In: Update Navbar");
            //Configure sign in button visiblity
            mBtnSignIn.setVisibility(View.VISIBLE);
            mNavSignOut.setVisible(false);
            //User is not logged In
            mBtnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            });
        } else {//user is signed in
            Log.d(TAG, "UpdateNavbar() + Logged In " + mCurrentUser.toString());
            mBtnSignIn.setVisibility(View.GONE);
            mNavSignOut.setVisible(true);
        }
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(MainActivity.this, getString(R.string.succesful_sign_out),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        FragmentManager fragmentManager = getFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentByTag(VISIBLE_FRAGMENT);
        String mActionBarTitle = getString(R.string.app_name);
        int mCurrentFragmentId = R.id.nav_home;
        if (currentFragment instanceof HomeFragment) {
            mActionBarTitle = getString(R.string.app_name);
            mCurrentFragmentId = R.id.nav_home;
        }
        if (currentFragment instanceof FavoritesFragment) {
            mActionBarTitle = getString(R.string.title_favorites_fragment);
            mCurrentFragmentId = R.id.nav_favorites;

        }
        if (currentFragment instanceof SearchFragment) {
            mActionBarTitle = getString(R.string.title_search_fragment);
            mCurrentFragmentId = R.id.nav_search;
        }
        if (currentFragment instanceof SettingsFragment) {
            mActionBarTitle = getString(R.string.title_settings_fragment);
            mCurrentFragmentId = R.id.nav_settings;

        }
        if (currentFragment instanceof HelpFragment) {
            mActionBarTitle = getString(R.string.title_help_fragment);
            mCurrentFragmentId = R.id.nav_help;

        }
        setActionBarTitle(mActionBarTitle);
        mNavigationView.setCheckedItem(mCurrentFragmentId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager mSearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        MenuItemCompat.collapseActionView(searchItem);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSearchableInfo(mSearchManager.getSearchableInfo(getComponentName()));
//        mSearchView.setSearchableInfo(mSearchManager.getSearchableInfo(
//                new ComponentName(getApplicationContext(), SearchResultsActivity.class)));
        // Associate searchable configuration with the SearchView

        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displayView(item.getItemId());
        return true;
    }

    public void displayView(int viewId) {
        Fragment mNewFragment;
        String mActionBarTitle;
        switch (viewId) {
            case R.id.nav_home:
                mNewFragment = new HomeFragment();
                mActionBarTitle = getString(R.string.app_name);
                break;
            case R.id.nav_search:
                //@TODO add search capability
                mNewFragment = new SearchFragment();
                mActionBarTitle = getString(R.string.title_search_fragment);
                break;
            case R.id.nav_post_ad:
                //@TODO add search capability
                mNewFragment = new HomeFragment();
                mActionBarTitle = getString(R.string.title_my_services);
                break;
            case R.id.nav_messages:
                //@TODO add search capability
                mNewFragment = new HomeFragment();
                mActionBarTitle = getString(R.string.title_messages_fragment);
                break;
            case R.id.nav_favorites:
                mNewFragment = new FavoritesFragment();
                mActionBarTitle = getString(R.string.title_favorites_fragment);
                break;
            case R.id.nav_settings:
                mNewFragment = new SettingsFragment();
                mActionBarTitle = getString(R.string.title_settings_fragment);
                break;
            case R.id.nav_help:
                mNewFragment = new HelpFragment();
                mActionBarTitle = getString(R.string.title_help_fragment);
                break;
            case R.id.nav_sign_out:
                mNewFragment = new HomeFragment();
                mActionBarTitle = getString(R.string.title_help_fragment);
                signOut();
            default:
                mNewFragment = new HomeFragment();
                mActionBarTitle = getString(R.string.app_name);

        }


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, mNewFragment, VISIBLE_FRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
        //setToolbar title
        setActionBarTitle(mActionBarTitle);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);

    }

    private void setActionBarTitle(String mActionBarTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mActionBarTitle);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleSearchIntent(intent);
    }


    private void handleSearchIntent(Intent intent) {

        String query = intent.getStringExtra(SearchManager.QUERY);
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        //use the query to search your data somehow
        displayView(R.id.nav_search);

    }


}
