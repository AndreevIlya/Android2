package ru.homecatering;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ExpandableListView expandableListView;
    private LeftMenuAdapter adapter;
    private String activeContent = "";
    private Map<String, FragmentCreator> fragments = initFragments();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLeftMenu();
        setFAB();
        setDrawer();

        startFragment("home");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 26) {
            getSupportFragmentManager().findFragmentByTag("home").onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setLeftMenu() {
        expandableListView = findViewById(R.id.expandableListView);
        adapter = new LeftMenuAdapter(this);
        expandableListView.setAdapter(adapter);
        setListeners();
    }

    private void setDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);//Haven't thought if it's needed yet
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setListeners(){
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                LeftMenuModel menuItem = adapter.getGroup(groupPosition);
                String itemId = menuItem.getId();
                if (itemId.equals("nav_menu")) {
                    if (expandableListView.isGroupExpanded(groupPosition)) {
                        expandableListView.collapseGroup(groupPosition);
                    } else {
                        expandableListView.expandGroup(groupPosition);
                    }
                    return true;
                }
                startFragment(itemId.substring(4));
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                LeftMenuModel menuItem = adapter.getChild(groupPosition,childPosition);
                startFragment(menuItem.getId().substring(4));
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private interface FragmentCreator {
        Fragment createFragment();
    }

    private Map<String, FragmentCreator> initFragments() {
        fragments = new HashMap<>();
        fragments.put("home", new FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return new HomeFragment();
            }
        });
        fragments.put("prepared", new FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return new PreparedFragment();
            }
        });
        fragments.put("gallery", new FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return new GalleryFragment();
            }
        });
        fragments.put("contacts", new FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return new ContactsFragment();
            }
        });
        fragments.put("stew", new FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return new StewFragment();
            }
        });
        fragments.put("hot", new FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return new HotFragment();
            }
        });
        fragments.put("cold", new FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return new ColdFragment();
            }
        });
        fragments.put("grille", new FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return new GrilleFragment();
            }
        });
        return fragments;
    }

    private void startFragment(String name) {
        if (!activeContent.equals(name)) {
            Fragment fragment = fragments.get(name).createFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content, fragment, name);
            ft.commit();
            activeContent = name;
        }
    }
}
