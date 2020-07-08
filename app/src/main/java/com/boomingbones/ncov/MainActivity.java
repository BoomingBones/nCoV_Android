package com.boomingbones.ncov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    private static final int FRAGMENT_UPDATE_START = 10001;
    private static final int FRAGMENT_UPDATE_FINISH = 10002;

    private DrawerLayout drawerLayout;
    private NavigationView navView;
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

        findViewById(R.id.sourceCode_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/BoomingBones/nCoV_Android"));
                startActivity(intent);
            }
        });

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case FRAGMENT_UPDATE_START:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case FRAGMENT_UPDATE_FINISH:
                        progressBar.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        };

        replaceFragment(new OverviewFragment(handler), R.string.overview);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(final Fragment newFragment, int titleId) {
        Message message = new Message();
        message.what = FRAGMENT_UPDATE_START;
        handler.sendMessage(message);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
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
                    replaceFragment(new OverviewFragment(handler), R.string.overview);
                    break;
                case R.id.nav_news:
                    replaceFragment(new NewsFragment(handler), R.string.news);
                    break;
                case R.id.nav_rumors:
                    replaceFragment(new RumorsFragment(handler), R.string.rumors);
                    break;
                case R.id.nav_information:
                    replaceFragment(new InformationFragment(handler), R.string.information);
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        }
    }
}
