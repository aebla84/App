package dms.deideas.zas.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import dms.deideas.zas.Fragments.HistoricalFragment;
import dms.deideas.zas.Fragments.HomeFragment;
import dms.deideas.zas.Fragments.MyOrdersFragment;
import dms.deideas.zas.Fragments.OrdersFragment;
import dms.deideas.zas.Fragments.ProfileFragment;
import dms.deideas.zas.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private String drawerTitle;

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout rl = (DrawerLayout) findViewById(R.id.drawer);
        rl.setBackgroundResource(R.drawable.ic_background);

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        drawerTitle = getResources().getString(R.string.home_item);
        if (savedInstanceState == null) {
            selectItem(drawerTitle);
        }

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_view_headline_white);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        // Marcar item presionado
                        item.setChecked(true);
                        // Crear nuevo fragmento
                        String title = item.getTitle().toString();
                        selectItem(title);
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.drawer, menu);
            return true;
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(String title) {

        // En función que se ha seleccionado vamos a un fragment u otro
        if (title.equals(getResources().getString(R.string.home_item))) {

            HomeFragment fragment = HomeFragment.newInstance(title);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

        } else if (title.equals(getResources().getString(R.string.profile_item))) {

            ProfileFragment fragment = ProfileFragment.newInstance(title);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();
        } else if (title.equals(getResources().getString(R.string.historical_item))) {

            HistoricalFragment fragment = HistoricalFragment.newInstance(title, "4");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        else if (title.equals(getResources().getString(R.string.orders_item))) {

            OrdersFragment fragment = OrdersFragment.newInstance(title);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        else if (title.equals(getResources().getString(R.string.my_orders_item))) {

            final SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            int idUser = prefs.getInt("idUser",0);
            String motodriver = String.valueOf(idUser);

            MyOrdersFragment fragment = MyOrdersFragment.newInstance(title,motodriver);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        else if (title.equals(getResources().getString(R.string.logout_item))) {

            SharedPreferences preferences = getSharedPreferences("MyPreferences", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawers();

        setTitle(title);

    }
}
