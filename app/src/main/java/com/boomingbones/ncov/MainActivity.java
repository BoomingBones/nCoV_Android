package com.boomingbones.ncov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navView = findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.nav_overview);
        navView.setNavigationItemSelectedListener(new NavItemSelectedListener());

        replaceFragment(new OverviewFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return true;
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }

    class NavItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (navView.getCheckedItem() == item) {
                return true;
            }
            switch (item.getItemId()) {
                case R.id.nav_overview:
                    replaceFragment(new OverviewFragment());
                    break;
                case R.id.nav_news:
                    replaceFragment(new NewsFragment());
                    break;
                case R.id.nav_rumors:
                    replaceFragment(new RumorsFragment());
                    break;
                case R.id.nav_information:
                    replaceFragment(new InformationFragment());
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        }
    }
}