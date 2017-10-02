package com.renegades.labs.nearestatm;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner spinner;
    private Map<String, String[]> coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner = (Spinner) findViewById(R.id.spinner);

        List<String> cities = Arrays.asList(getResources().getStringArray(R.array.cities_array));
        List<String> cityNames = new ArrayList<>();
        coordinates = new HashMap<>();

        for (int i = 0; i < cities.size(); i++) {
            String[] parts = cities.get(i).split(" ");
            cityNames.add(parts[0]);
            coordinates.put(parts[0], new String[]{parts[1], parts[2]});
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cityNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drawMarkers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this));
    }

    public void drawMarkers() {
        mMap.clear();

        String currentCity = spinner.getSelectedItem().toString();
        Double lat = Double.valueOf(coordinates.get(currentCity)[0]);
        Double lng = Double.valueOf(coordinates.get(currentCity)[1]);
        LatLng current = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 10));

        Map<String, String> params = new HashMap<>();
        params.put("json", "");
        params.put("atm", "");
        params.put("city", currentCity);

        Call<ATM> call = MyApp.getAtmApi().getAtms(params);
        call.enqueue(new Callback<ATM>() {
            @Override
            public void onResponse(Call<ATM> call, Response<ATM> response) {
                Log.d("MapsActivity", "response = " + response);

                if (response.code() == 200) {
                    ATM atm = response.body();
                    if (atm != null) {
                        List<Device> devices = new ArrayList();
                        Collections.addAll(devices, atm.getDevices());

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
}
