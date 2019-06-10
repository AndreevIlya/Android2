package ru.homecatering;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_REQUEST_CODE = 26;
    private ExpandableListView expandableListView;
    private LeftMenuAdapter adapter;
    private String activeContent = "";
    private Map<String, FragmentCreator> fragments = initFragments();
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLeftMenu();
        setFAB();
        setDrawer();

        initToken();

        startFragment("home");

        getLocation();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            Log.i("GEO", provider);
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String latitude = Double.toString(location.getLatitude());
                    String longitude = Double.toString(location.getLongitude());
                    String accuracy = Float.toString(location.getAccuracy());
                    Log.i("GEO", latitude);
                    Log.i("GEO", longitude);
                    Log.i("GEO", accuracy);

                    /*I/GEO: 55.94221769
                    37.61245531
                    46.0*/
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }


    private void initToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("INFO", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("INFO", "Token: " + token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
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
            ft.replace(R.id.content, fragment);
            ft.commit();
            activeContent = name;
        }
    }
}
