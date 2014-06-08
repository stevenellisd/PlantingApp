package org.openatk.plantingapp;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private enum State {MAP_VIEW, LIST_VIEW};
    State uiState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();
        uiState = State.MAP_VIEW;

        //setup action bar
        ActionBar actionBar = getActionBar();
        // Specify that a dropdown list should be displayed in the action bar.
        // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Hide the title
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,ActionBar.DISPLAY_SHOW_CUSTOM);

        LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.action_bar_main, null);

        LinearLayout item = (LinearLayout) view.findViewById(R.id.action_bar_layout);

        Spinner spinnerMenu = (Spinner) view.findViewById(R.id.action_bar_operation_spinner);
        actionBar.setCustomView(item, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.LEFT));

        String plants[] = {"Corn", "Beans", "Add Category"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.operation_list_item, plants);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.operation_list_item); // The drop down view
        spinnerMenu.setAdapter(spinnerArrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch(uiState) {
            case MAP_VIEW:
                //remove map button
                menu.findItem(R.id.menu_map_view).setVisible(false);
                //show list button
                menu.findItem(R.id.menu_list_view).setVisible(true);
                break;
            case LIST_VIEW:
                //show map button
                menu.findItem(R.id.menu_map_view).setVisible(true);
                //remove list button
                menu.findItem(R.id.menu_list_view).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_map_view:
                setState(State.MAP_VIEW);
                return true;
            case R.id.menu_list_view:
                setState(State.LIST_VIEW);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setState(State state) {
        uiState = state;
        invalidateOptionsMenu();
    }
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.setMyLocationEnabled(true);
        UiSettings ui = mMap.getUiSettings();
        ui.setMyLocationButtonEnabled(true);
        ui.setZoomControlsEnabled(false);
    }
}
