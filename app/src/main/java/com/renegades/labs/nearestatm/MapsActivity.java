package com.renegades.labs.nearestatm;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.renegades.labs.nearestatm.api.ATM;
import com.renegades.labs.nearestatm.api.Device;
import com.renegades.labs.nearestatm.api.Tw;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Spinner spinner;
    private DBHelper dbHelper;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        int granted = PackageManager.PERMISSION_GRANTED;

        while (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != granted) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Location loc = getLocation();
        if (loc != null) {
            lat = loc.getLatitude();
            lng = loc.getLongitude();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final int[] checkFirstCall = {0};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++checkFirstCall[0] > 1) {
                    getCurrentCity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        dbHelper = new DBHelper(this);
    }

    public Location getLocation() {
        Location location = null;

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            Log.d("MapsActivity", "getLocation: no provider is enabled");
        } else {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

            if (isGpsEnabled) {
                if (location == null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            }
        }

        return location;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this));

        LatLng current = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 10));

        Geocoder geocoder = new Geocoder(this, new Locale("ru", "RU"));
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            String currentCity = addresses.get(0).getLocality();
            drawMarkers(currentCity);
        }
    }

    public void getCurrentCity() {
        String currentCity = spinner.getSelectedItem().toString();

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(currentCity, 1);

            if (address != null) {
                Address location = address.get(0);

                lat = location.getLatitude();
                lng = location.getLongitude();
                LatLng current = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 10));

                drawMarkers(currentCity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawMarkers(final String currentCity) {
        mMap.clear();
        if (isCityInDatabase(currentCity)) {
            drawMarkersFromDb(currentCity);
        } else {
            drawMarkersFromNetwork(currentCity);
        }
    }

    public boolean isCityInDatabase(String city) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuery = "SELECT city FROM atms ;";
        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String dbCity = c.getString(0);
                    if (dbCity.equals(city)) {
                        c.close();
                        db.close();
                        return true;
                    }
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return false;
    }

    public void drawMarkersFromDb(String city) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuery = "SELECT lat, lng, place, snippet FROM atms WHERE city = ?";
        Cursor c = db.rawQuery(sqlQuery, new String[]{city});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    double lat = c.getDouble(0);
                    double lng = c.getDouble(1);
                    String place = c.getString(2);
                    String snippet = c.getString(3);

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .title(place)
                            .snippet(snippet));
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
    }

    public void drawMarkersFromNetwork(final String city) {
        Map<String, String> params = new HashMap<>();
        params.put("json", "");
        params.put("atm", "");
        params.put("city", city);

        Call<ATM> call = MyApp.getAtmApi().getAtms(params);
        call.enqueue(new Callback<ATM>() {
            @Override
            public void onResponse(Call<ATM> call, Response<ATM> response) {
                if (response.code() == 200) {
                    ATM atm = response.body();
                    if (atm != null) {
                        Device[] devices = atm.getDevices();

                        for (Device device : devices) {
                            double lat = Double.valueOf(device.getLatitude());
                            double lng = Double.valueOf(device.getLongitude());
                            String address = device.getFullAddressRu();
                            Tw tw = device.getTw();

                            String snippet = address + "\n"
                                    + tw.getMon() + " Пн\n"
                                    + tw.getTue() + " Вт\n"
                                    + tw.getWed() + " Ср\n"
                                    + tw.getThu() + " Чт\n"
                                    + tw.getFri() + " Пт\n"
                                    + tw.getSat() + " Сб\n"
                                    + tw.getSun() + " Нд\n"
                                    + tw.getHol() + " Св";

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                    .title(device.getPlaceRu())
                                    .snippet(snippet));

                            putInDatabase(city, lat, lng, device.getPlaceRu(), snippet);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ATM> call, Throwable t) {
                Log.d("MapsActivity", "onFailure: " + t.toString());
            }
        });
    }

    public void putInDatabase(String city, double lat, double lng, String place, String snippet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put("city", city);
        cv.put("lat", lat);
        cv.put("lng", lng);
        cv.put("place", place);
        cv.put("snippet", snippet);

        db.insert("atms", null, cv);
        db.close();
    }

    @Override
    public void onLocationChanged(Location location) {
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

}
