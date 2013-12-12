package com.example.coursework;

import java.util.List;
import java.util.Vector;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import static java.lang.System.out;

public class MainActivity extends Activity 
{
	
	private LatLng pos_ShootingCentre = new LatLng(56.49314, -2.74544);
	private LatLng pos_BikeTrails = new LatLng(55.794273, -4.219052);
	private LatLng pos_CelticPark = new LatLng(55.849734, -4.205625);
	private LatLng pos_EmiratesArena = new LatLng(55.84694, -4.207384);
	private LatLng pos_HockeyCentre = new LatLng(55.845831, -4.23696);
	private LatLng pos_HampdenPark = new LatLng(55.825515, -4.25197);
	private LatLng pos_IbroxStadium = new LatLng(55.853372, -4.309069);
	private LatLng pos_BowlsCentre = new LatLng(55.867895, -4.288194);
	private LatLng pos_RoyalPool = new LatLng(55.939923, -3.173174);
	private LatLng pos_SportsCampus = new LatLng(55.880583, -4.340808);
	private LatLng pos_SECC = new LatLng(55.860923, -4.287418);
	private LatLng pos_CountryPark = new LatLng(55.7971971, -4.0342997);
	private LatLng pos_SwimmingCentre = new LatLng(55.845591, -4.175798);
	
	private LatLng glasgow = new LatLng(55.872614, -4.247696);
	private LatLng delhi = new LatLng(28.6100, 77.2300);
	private LatLng melbourne = new LatLng(-37.814361, 144.963091);
	private LatLng manchester = new LatLng(53.479329, -2.248486);
	private LatLng kualaLumpur = new LatLng(3.138992, 101.686851);
	private LatLng victoria = new LatLng(48.428199, -123.365739);
	private LatLng auckland = new LatLng(-36.848487, 174.763286);
	private LatLng edinburgh = new LatLng(55.952372, -3.188764);
	private LatLng brisbane = new LatLng(-27.470925, 153.023519);
	private LatLng edmonton = new LatLng(53.544362, -113.490897);
	private LatLng christchurch = new LatLng(-43.532006, 172.636268);
	
/*
	private MarkerOptions marker_ShootingCentre;
	private MarkerOptions marker_BikeTrails;
	private MarkerOptions marker_CelticPark;
	private MarkerOptions marker_EmirateArena;
	private MarkerOptions marker_HockeyCentre;
	private MarkerOptions marker_HampdenPark;
	private MarkerOptions marker_IbroxStadium;
	private MarkerOptions marker_BowlsCentre;
	private MarkerOptions marker_RoyalPool;
	private MarkerOptions marker_SportsCampus;
	private MarkerOptions marker_SECC;
	private MarkerOptions marker_CountryPark;
	private MarkerOptions marker_SwimmingCentre;
*/
	
	//private BitmapDescriptor seccIcon = BitmapDescriptorFactory.fromResource(R.drawable.weightlifting);
	
	private MarkerOptions[] venueList = new MarkerOptions[13];
	private GoogleMap map;

	boolean mapBool = true;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a handle to the Map Fragment
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        final Button button = (Button) findViewById(R.id.terrain);
        
        button.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	mapBool = !mapBool;
            	
            	if(mapBool)
            	{
            		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            	}
            	else
            	{
            		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            	}
            }
        });
       
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 10));
    	
        venueList[0] = SetMarker("Barry Buddon Shooting Centre", "Carnoustie, Sport: Shooting", pos_ShootingCentre);
        venueList[1] = SetMarker("Cathkind Braes Mountain Bike Trails","Sport: Mountain Bike", pos_BikeTrails);
        venueList[2] = SetMarker("Celtic Park","Glasgow 2014 Opening Ceremony", pos_CelticPark);
        venueList[3] = SetMarker("Emirates Arena","Sports: Badminton and Cycling", pos_EmiratesArena);
        venueList[4] = SetMarker("Glasgow Natrional Hockey Centre","Sport: Hockey", pos_HockeyCentre);
        venueList[5] = SetMarker("Hampden Park","Sport: Athletics", pos_HampdenPark);
        venueList[6] = SetMarker("Ibrox Stadium", "Sport: Rugby Sevens", pos_IbroxStadium);
    	venueList[7] = SetMarker("Kelvin Grove Lawn Bowls Centre", "Sport: Lawn Bowls", pos_BowlsCentre);
        venueList[8] = SetMarker("Royal Commonwealth Pool", "Edinburgh, Sport: Diving", pos_RoyalPool);
    	venueList[9] = SetMarker("Scotstoun Sports Campus", "Sports: Squash and Table Tennis", pos_SportsCampus);
      	venueList[10] = SetMarker("Scottish Exhibition and Conference Centre (SECC) Precinct","Sports: Boxing, Gymnastics, Judo, Netball, Wrestling and Weightlifting", pos_SECC);
      	venueList[11] = SetMarker("StrathClyde Country Park","Sport: Triathlon", pos_CountryPark);
    	venueList[12] = SetMarker("Tollcross International Swimming Centre","Sport: Swimming", pos_SwimmingCentre);
        
    	AddMarkers();
    	

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
		{
			 @Override
		     public void onInfoWindowClick(Marker marker) 
			 {

		            //Log.d("", marker.getTitle());
				 EventClicked(marker.getPosition());
				 out.println(marker.getTitle()+"1");
		     }
		});

    	
        //= new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.weightlifting)).anchor(0.5f, 0.5f).position(secc)
        //new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.weightlifting)).anchor(0.5f, 0.5f).position(ibroxStadium)
        //map.addMarker(new MarkerOptions().title("Glasgow").snippet("The 2014 Host City of the Commonwealth Games").position(glasgow));
        //map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.weightlifting)).anchor(0.0f, 1.0f).position(secc));
        
        
    }
    public void AddMarkers()
    {  	
    	for(int i = 0; i < venueList.length; i++)
    	{
    		map.addMarker(venueList[i]);
    	}
    	
    }
    
    public MarkerOptions SetMarker(String title, String snippet, LatLng position)
    {
    	MarkerOptions marker = new MarkerOptions().title(title).snippet(snippet).icon(BitmapDescriptorFactory.fromResource(R.drawable.weightlifting)).anchor(0.5f, 0.5f).position(position);
    	
    	return marker;
    }
    public void EventClicked(LatLng event)
    {
    		ToWebsite("http://www.glasgow2014.com/games/venues/barry-buddon-shooting-centre-carnoustie");
    		out.println(event+"2");
    }
    
    
    
    public void ToWebsite (String url) 
    {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
        out.println("2");
    }
    
}
