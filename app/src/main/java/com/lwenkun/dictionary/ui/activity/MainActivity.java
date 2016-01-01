package com.lwenkun.dictionary.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lwenkun.dictionary.R;
import com.lwenkun.dictionary.model.AlbumManager;
import com.lwenkun.dictionary.model.TranslateResultSet;
import com.lwenkun.dictionary.ui.Interface.UIUpdater;
import com.lwenkun.dictionary.ui.fragment.ResultDisplayFragment;
import com.lwenkun.dictionary.util.NetworkSearchTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,UIUpdater {

    public static String KEY_SER = "com.lwenkun.dictionary.key.ser";
    public static String KEY_TYPE = "com.lwenkun.dictionary.key.type";

    private ProgressBar progressBar;
    private AlbumManager albumManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        progressBar = (ProgressBar) findViewById(R.id.pb_load_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final EditText ed_search = (EditText) findViewById(R.id.et_query);
        ed_search.getCompoundDrawables();
        ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    doSearchWork(ed_search);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_add :
                showAddNewDialog();
                break;
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_about :
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
        }
        return true;
    }

    public void doSearchWork(EditText editText) {
        String key = editText.getText().toString();
        NetworkSearchTask searchTask = new NetworkSearchTask(this);
        searchTask.execute(key);
    }

    @Override
    public void onShowProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDataUpdate(Object object, int type) {
        TranslateResultSet resultSet = (TranslateResultSet) object;
        ResultDisplayFragment fragment = new ResultDisplayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        bundle.putSerializable(KEY_SER, resultSet);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fg_container, fragment).commit();
    }

    public void showAddNewDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_new_album, null);
        final EditText et_addNewAlbum = (EditText) view.findViewById(R.id.et_add_new_album);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_note_add_white_24dp)
                .setTitle("新建单词本")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewAlbum(et_addNewAlbum.getText().toString());
                    }
                }).show();
    }

    public void addNewAlbum(final String albumName) {
        new Thread() {
            @Override
            public void run() {
                if (albumManager == null) {
                    albumManager = AlbumManager.from(MainActivity.this);
                } else {
                    albumManager.addNewAlbum(albumName);
                }
            }
        }.start();
    }

}
