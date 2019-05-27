package ru.homecatering;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ExpandableListView;

import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ExpandableListView expandableListView;
    private LeftMenuAdapter adapter;
    private String activeContent = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FloatingActionButton fab = findViewById(R.id.fab);//Haven't thought if it's needed yet
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        expandableListView = findViewById(R.id.expandableListView);
        adapter = new LeftMenuAdapter(this);
        expandableListView.setAdapter(adapter);
        setListeners();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Fragment contentFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content,contentFragment);
        ft.commit();
    }

    private void setListeners(){
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                LeftMenuModel menuItem = adapter.getGroup(groupPosition);
                String message = "";
                switch(menuItem.getId()){
                    case "nav_home" :
                        message = "Home clicked";
                        if(!activeContent.equals("home")){
                            Fragment contentFragment = new HomeFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content,contentFragment);
                            ft.commit();
                            activeContent = "home";
                        }
                        break;
                    case "nav_menu" :
                        message = "Menu clicked";
                        if(expandableListView.isGroupExpanded(groupPosition)){
                            expandableListView.collapseGroup(groupPosition);
                        }else{
                            expandableListView.expandGroup(groupPosition);
                        }
                        break;
                    case "nav_prepared" :
                        message = "Prepared clicked";
                        if(!activeContent.equals("prepared")){
                            Fragment contentFragment = new PreparedFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content,contentFragment);
                            ft.commit();
                            activeContent = "prepared";
                        }
                        break;
                    case "nav_gallery" :
                        message = "Gallery clicked";
                        if(!activeContent.equals("gallery")){
                            Fragment contentFragment = new GalleryFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content,contentFragment);
                            ft.commit();
                            activeContent = "gallery";
                        }
                        break;
                    case "nav_contacts" :
                        message = "Contacts clicked";
                        if(!activeContent.equals("contacts")){
                            Fragment contentFragment = new ContactsFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content,contentFragment);
                            ft.commit();
                            activeContent = "contacts";
                        }
                        break;

                }
                Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                LeftMenuModel menuItem = adapter.getChild(groupPosition,childPosition);
                String message = "";
                switch(menuItem.getId()){
                    case "nav_stew" :
                        message = "Stew clicked";
                        if(!activeContent.equals("stew")){
                            Fragment contentFragment = new StewFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content,contentFragment);
                            ft.commit();
                            activeContent = "stew";
                        }
                        break;
                    case "nav_hot" :
                        message = "Hot beverages clicked";
                        if(!activeContent.equals("hot")){
                            Fragment contentFragment = new HotFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content,contentFragment);
                            ft.commit();
                            activeContent = "hot";
                        }
                        break;
                    case "nav_cold" :
                        message = "Cold beverages clicked";
                        if(!activeContent.equals("cold")){
                            Fragment contentFragment = new ColdFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content,contentFragment);
                            ft.commit();
                            activeContent = "cold";
                        }
                        break;
                    case "nav_grille" :
                        message = "Grille clicked";
                        if(!activeContent.equals("grille")){
                            Fragment contentFragment = new GrilleFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content,contentFragment);
                            ft.commit();
                            activeContent = "grille";
                        }
                        break;

                }
                Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                return false;
            }
        });

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
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_callback) {
            return true;
        }
        if (id == R.id.action_share) {
            return true;
        }
        if (id == R.id.action_cart) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //NavigationView navigationView = findViewById(R.id.nav_view);
        switch (id) {
            case R.id.nav_home:

                break;
            case R.id.nav_menu:
                break;
            case R.id.nav_prepared:

                break;
            case R.id.nav_gallery:

                break;
            case R.id.nav_contacts:

                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
