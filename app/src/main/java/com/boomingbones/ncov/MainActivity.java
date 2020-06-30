package com.boomingbones.ncov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int START_LOADING = 1;
    private static final int FINISH_LOADING = 2;

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ScrollView fragmentContainer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler;

    @SuppressLint("HandlerLeak")
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

        fragmentContainer = findViewById(R.id.fragment_container);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case START_LOADING:
                        Log.w("Thread", "START");
                        swipeRefreshLayout.setRefreshing(true);
                        fragmentContainer.setVisibility(View.INVISIBLE);
                        break;
                    case FINISH_LOADING:
                        Log.w("Thread", "FINISH");
                        fragmentContainer.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                }
            }
        };

        replaceFragment(new OverviewFragment(), R.string.overview);
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

    private void replaceFragment(final Fragment newFragment, int titleId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message messageStart = new Message();
                messageStart.what = START_LOADING;
                handler.sendMessage(messageStart);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.commit();

                Message messageFinish = new Message();
                messageFinish.what = FINISH_LOADING;
                handler.sendMessage(messageFinish);
            }
        }).start();

        ((Toolbar) findViewById(R.id.toolbar)).setTitle(titleId);
    }

    class NavItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (navView.getCheckedItem() == item) {
                return true;
            }
            switch (item.getItemId()) {
                case R.id.nav_overview:
                    replaceFragment(new OverviewFragment(), R.string.overview);
                    fragmentContainer.scrollTo(0, 0);
                    break;
                case R.id.nav_news:
                    replaceFragment(new NewsFragment(), R.string.news);
                    fragmentContainer.scrollTo(0, 0);
                    break;
                case R.id.nav_rumors:
                    replaceFragment(new RumorsFragment(), R.string.rumors);
                    fragmentContainer.scrollTo(0, 0);
                    break;
                case R.id.nav_information:
                    replaceFragment(new InformationFragment(), R.string.information);
                    fragmentContainer.scrollTo(0, 0);
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        }
    }
}
