package com.daff.cataloguemovieapi.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.view.fragment.MovieFragment;
import com.daff.cataloguemovieapi.view.fragment.SearchFragment;
import com.daff.cataloguemovieapi.view.fragment.TvShowFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView img;
    Fragment pageContent = new MovieFragment();
    String title = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        img = findViewById(R.id.imageView);
         title = getString(R.string.menu_home);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.core_fragment, pageContent)
                    .commit();
            getSupportActionBar().setTitle(title);
        }else {
            pageContent = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            title = savedInstanceState.getString("title");

            getSupportFragmentManager().beginTransaction().replace(R.id.core_fragment, pageContent)
                    .commit();
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingLanguage = new Intent(this, SettingActivity.class);
            startActivity(settingLanguage);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_nowplaying) {
           pageContent = new MovieFragment();
           title = getString(R.string.menu_nowplaying);
        } else if (id == R.id.nav_tvshow) {
           pageContent = new TvShowFragment();
           title = getString(R.string.menu_tvshow);
        }else if (id == R.id.nav_favorite){
             Intent favorite = new Intent(this, FavoriteActivity.class);
             startActivity(favorite);
             title = getString(R.string.favorite);
         }else if (id == R.id.nav_search){
             pageContent = new SearchFragment();
             title = getResources().getString(R.string.search);
         }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.core_fragment, pageContent)
                .commit();
         getSupportActionBar().setTitle(title);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("title",title );
        getSupportFragmentManager().putFragment(outState, "fragment", pageContent);
        super.onSaveInstanceState(outState);

    }
}
