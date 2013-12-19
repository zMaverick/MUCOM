package com.coursework.project;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	
	private LatLng pos_ShootingCentre = new LatLng(56.49314, -2.74544);		//The Latitude and Longitude for the Scooting Centre
	private LatLng pos_BikeTrails = new LatLng(55.794273, -4.219052);		//The Latitude and Longitude for the Bike Trails
	private LatLng pos_CelticPark = new LatLng(55.849734, -4.205625);		//The Latitude and Longitude for Celtic Park
	private LatLng pos_EmiratesArena = new LatLng(55.84694, -4.207384);		//The Latitude and Longitude for the Emirates Arena
	private LatLng pos_HockeyCentre = new LatLng(55.845831, -4.23696);		//The Latitude and Longitude for the Hockey Centre
	private LatLng pos_HampdenPark = new LatLng(55.825515, -4.25197);		//The Latitude and Longitude for Hampden Park
	private LatLng pos_IbroxStadium = new LatLng(55.853372, -4.309069);		//The Latitude and Longitude for Ibrox Stadium
	private LatLng pos_BowlsCentre = new LatLng(55.867895, -4.288194);		//The Latitude and Longitude for the Bowls Centre
	private LatLng pos_RoyalPool = new LatLng(55.939923, -3.173174);		//The Latitude and Longitude for the Royal Pool
	private LatLng pos_SportsCampus = new LatLng(55.880583, -4.340808);		//The Latitude and Longitude for the Sports Campus
	private LatLng pos_SECC = new LatLng(55.860923, -4.287418);				//The Latitude and Longitude for the SECC
	private LatLng pos_CountryPark = new LatLng(55.7971971, -4.0342997);	//The Latitude and Longitude for the Country Park
	private LatLng pos_SwimmingCentre = new LatLng(55.845591, -4.175798);	//The Latitude and Longitude for the Swimming Centre
	
	private LatLng pos_Glasgow = new LatLng(55.872614, -4.247696);			//The Latitude and Longitude for Glasgoe
	private LatLng pos_Delhi = new LatLng(28.6100, 77.2300);				//The Latitude and Longitude for Delhi
	private LatLng pos_Melbourne = new LatLng(-37.814361, 144.963091);		//The Latitude and Longitude for Melbourne
	private LatLng pos_Manchester = new LatLng(53.479329, -2.248486);		//The Latitude and Longitude for Manchester
	private LatLng pos_KualaLumpur = new LatLng(3.138992, 101.686851);		//The Latitude and Longitude for Kuala Lumpur
	private LatLng pos_Victoria = new LatLng(48.428199, -123.365739);		//The Latitude and Longitude for Victoria
	private LatLng pos_Auckland = new LatLng(-36.848487, 174.763286);		//The Latitude and Longitude for Auckland
	private LatLng pos_Edinburgh = new LatLng(55.952372, -3.188764);		//The Latitude and Longitude for Edinburgh
	private LatLng pos_Brisbane = new LatLng(-27.470925, 153.023519);		//The Latitude and Longitude for Brisbane
	private LatLng pos_Edmonton = new LatLng(53.544362, -113.490897);		//The Latitude and Longitude for Edmonton
	private LatLng pos_Christchurch = new LatLng(-43.532006, 172.636268);	//The Latitude and Longitude for Christchurch
			
	private LatLng pos_GlasgowCentre = new LatLng(55.86483666, -4.258317947);	//The Latitude and Longitude for the centre of Glasgow City
	
	private MarkerOptions[] venueList = new MarkerOptions[13];					//Array of Marker options for each of the venues
	private Marker[] markerList =  new Marker[13];								//Array of Markers to store the marker for each venue
	
	/*Array of Strings for the venue URL endings*/
	private String[] urls = {"celtic-park","barry-buddon-shooting-centre-carnoustie",
								"cathkin-braes-mountain-bike-trails","emirates-arena-including-sir-chris-hoy-velodrome",
								"glasgow-national-hockey-centre","hampden-park","ibrox-stadium","kelvingrove-lawn-bowls-centre",
								"royal-commonwealth-pool-edinburgh","scotstoun-sports-campus","secc-precinct","strathclyde-country-park",
								"tollcross-international-swimming-centre"};
	
	private MarkerOptions[] cityList = new MarkerOptions[11];	//Array of Marker options for each of the cities
	private Marker[] cityMarkers =  new Marker[11];				//Array of Markers to store the marker for each City
	
	/*Array of Strings for the city URL endings*/
	private String[] cityURLs = {"2014","2010","‎2006", "2002", "1998", 
									"1994", "1990", "1986", "1982", "1978", "1974"};
	
	private GoogleMap map;		//Google Map variable		
	private int mapCounter = 0;	//Counter to change the map type
	
	private List<Address> addresses;	//List to hold the results passed from the search functions geocoder
	private Geocoder geocoder;			//Geocoder used to parse the string from the search into an address

	private  SearchView searchView;		//SearchView variable
	private MenuItem searchMenuItem;	//Menu Item for the search object
	
	private Marker searchMarker;		//Marker placed for searches
	private Polyline line = null;		//Line created for distances, null be start
	private Marker glasMarker;			//Marker used for the polyLine distances
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 	//Set the layout to activity_main     
        
        geocoder = new Geocoder(this);				//create Geocoder and apply to geocoder variable
        
        SetUpMap();		//Call the Map functions
    	AddMarkers();	//Add all the Markers
    	
    	/* Listener for InfoWindow Clicks */
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
		{
			 @Override
		     public void onInfoWindowClick(Marker marker) 
			 {
				 EventClicked(marker);	//Call the event click function for the Marker with its InfoWindow was clicked
		     }
		});
		
		
		handleIntent(getIntent());	//Call HandleIntent with the current intent
    }
   
    public void SetUpMap()
    {
    	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();		//Create the map and apply to the variable     				
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_Glasgow, 10));					//Set the default position to Glasgow	
        map.setMyLocationEnabled(true);														//Turn on GPS
        map.getUiSettings().setCompassEnabled(true);										//Turn on the Compass
        map.getUiSettings().setMyLocationButtonEnabled(true);								//Turn on the Location Buttons Functionality
        map.getUiSettings().setRotateGesturesEnabled(true);									//Turn on Rotation Gestures
    }
    
    public void AddMarkers()
    {  	
    	/* Set the Array positions to result of the SetMarker function with the variables associating cities */
    	cityList[0] = SetMarker("Glasgow 2014, Scotland", "23 July - 3 August 2014", pos_Glasgow, R.drawable.glasgow, false);
    	cityList[1] = SetMarker("Delhi 2010, India", "26 Medals won by Scotland", pos_Delhi, R.drawable.delhi, false);
    	cityList[2] = SetMarker("Melbourne 2006, Australia", "29 Medals won by Scotland", pos_Melbourne, R.drawable.melbourne, false);
    	cityList[3] = SetMarker("Manchester 2002, England", "30 Medals won by Scotland", pos_Manchester, R.drawable.manchester, true);
    	cityList[4] = SetMarker("Kuala Lumpur 1998, Malaysia", "12 Medals won by Scotland", pos_KualaLumpur, R.drawable.kualalumpur, false);
    	cityList[5] = SetMarker("Victoria 1994, Canada", "20 Medals won by Scotland", pos_Victoria, R.drawable.victoria, false);
    	cityList[6] = SetMarker("Auckland 1990, New Zealand", "22 Medals won by Scotland", pos_Auckland, R.drawable.auckland, true);
    	cityList[7] = SetMarker("Edinburgh 1986, Scotland", "33 Medals won by Scotland", pos_Edinburgh, R.drawable.edinburgh, true);
    	cityList[8] = SetMarker("Brisbane 1982, Australia", "26 Medals won by Scotland", pos_Brisbane, R.drawable.brisbane, false);
    	cityList[9] = SetMarker("Edmonton 1978, Canada", "14 Medals won by Scotland", pos_Edmonton, R.drawable.edmonton, false);
    	cityList[10] = SetMarker("Christchurch 1974, New Zealand", "19 Medals won by Scotland", pos_Christchurch, R.drawable.christchurch, true);
    	
    	/* Set the Array positions to result of the SetMarker function with the variables associating venues */
    	venueList[0] = SetMarker("Celtic Park","Glasgow 2014 Opening Ceremony", pos_CelticPark,R.drawable.logowhite, true);
        venueList[1] = SetMarker("Barry Buddon Shooting Centre", "Carnoustie, Sport: Shooting", pos_ShootingCentre,R.drawable.shooting, true);
        venueList[2] = SetMarker("Cathkind Braes Mountain Bike Trails","Sport: Mountain Bike", pos_BikeTrails,R.drawable.biking, true);
        venueList[3] = SetMarker("Emirates Arena","Sports: Badminton and Cycling", pos_EmiratesArena,R.drawable.badminton, true);
        venueList[4] = SetMarker("Glasgow Natrional Hockey Centre","Sport: Hockey", pos_HockeyCentre,R.drawable.hockey, true);
        venueList[5] = SetMarker("Hampden Park","Sport: Athletics", pos_HampdenPark,R.drawable.athletics, true);
        venueList[6] = SetMarker("Ibrox Stadium", "Sport: Rugby Sevens", pos_IbroxStadium,R.drawable.rugby, true);
    	venueList[7] = SetMarker("Kelvin Grove Lawn Bowls Centre", "Sport: Lawn Bowls", pos_BowlsCentre,R.drawable.bowls, true);
        venueList[8] = SetMarker("Royal Commonwealth Pool", "Edinburgh, Sport: Diving", pos_RoyalPool,R.drawable.swimming, true);
    	venueList[9] = SetMarker("Scotstoun Sports Campus", "Sports: Squash and Table Tennis", pos_SportsCampus,R.drawable.squash, true);
      	venueList[10] = SetMarker("Scottish Exhibition and Conference Centre (SECC) Precinct","Sports: Boxing, Gymnastics, Judo, Netball, Wrestling and Weightlifting", pos_SECC, R.drawable.weightlifting, true);
      	venueList[11] = SetMarker("StrathClyde Country Park","Sport: Triathlon", pos_CountryPark,R.drawable.triathalon, true);
    	venueList[12] = SetMarker("Tollcross International Swimming Centre","Sport: Swimming", pos_SwimmingCentre,R.drawable.swimming, true);
    	
    	/* For all the marker options in city list */
    	for(int i = 0; i < cityList.length; i++)
    	{
    		cityMarkers[i] = map.addMarker(cityList[i]);	//create a maker and add to the city markers list
    	}
    	
    	/* For all the marker options in venue's list */
    	for(int i = 0; i < venueList.length; i++)
    	{
    		markerList[i] = map.addMarker(venueList[i]);	//create a maker and add to the venue markers list
    	}
    }
    
    public MarkerOptions SetMarker(String title, String snippet, LatLng position, int icon, boolean centreAnchor)
    {
    	float anchorX;	//Create anchorX
    	float anchorY;	//Create anchorY
    	
    	/* On the condition the anchor is to be centred */
    	if(centreAnchor)
    	{
    		anchorX = 0.5f;	//Centre X
    		anchorY = 0.5f;	//Centre Y
    	}
    	else
    	{
    		anchorX = 0.5f;	//Centre X
    		anchorY = 1f;	//Bottom Y
    	}
    	
    	/* create marker options from the input variables */
    	MarkerOptions marker = new MarkerOptions().title(title).snippet(snippet).icon(BitmapDescriptorFactory.fromResource(icon)).anchor(anchorX, anchorY).position(position);
    	
    	return marker;	//Return marker
    }
    
    public void EventClicked(Marker marker)
    {
    	/* for each of the marker list items*/
    	for(int i=0;i < markerList.length;i++)
    	{
    		/* if the marker selected is from marker list */
    		if(marker.equals(markerList[i]))
        	{
    			ToWebsite("http://www.glasgow2014.com/games/venues/"+urls[i]);	//Call ToWebsite with the related URL list item
        	}
        	
    	}
    	/* for each of the city marker list items*/
    	for(int i=0;i < cityMarkers.length;i++)
    	{
    		/* if the marker selected is from city marker list */
    		if(marker.equals(cityMarkers[i]))
        	{
    			ToWebsite("http://www.thecgf.com/games/intro.asp?yr="+cityURLs[i]);	//Call ToWebsite with the related URL list item
        	}
        	
    	}
    	/* if the marker is the search maker */
    	if(marker.equals(searchMarker))
		{
			GetDistance(searchMarker.getPosition().latitude,searchMarker.getPosition().longitude, searchMarker.getTitle());	//Call the GetDistance function with the associated values
		}
    	/* if the marker is the GLasgow Centre Marker */
    	if(marker.equals(glasMarker))
    	{
    		if(line != null)
    		{
    			line.remove();		//Remove if it's Active
    		}
			glasMarker.remove();	//Remove the Glasgow Marker
    	}
    	
    }
    
    public void ToWebsite (String url) 
    {
    	Uri uriUrl = Uri.parse(url);									//Parse the URL
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);	//Create an intent for the browser with the URL
        startActivity(launchBrowser);									//Launch the URL
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();				//Create Menu Inflater
        inflater.inflate(R.menu.android_main_actions, menu);	//Add action items to the menu
               
        /* Associate searchable configuration with the SearchView */
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);		//Create the search manager
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();				//set searchView to the action_search item
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));			//Set the search manager component to the searchView item
        searchView.setSubmitButtonEnabled(true);													//Enable the submit button on the search url
        searchMenuItem =  menu.findItem(R.id.action_search);										//create a variable for the search item
        return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		//For each of the action bar items
		switch(item.getItemId())
		{
			/* Perform the Associate Action */
			case android.R.id.home:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Glasgow).zoom(10).build()));
					break;
				}
			case R.id.change_map:
				{
					ChangeMap();	//Call Change Map
					break;
				}
			case R.id.action_search:
				{		
					break;
				}
			case R.id.action_location:
				{		
					/* If GPS is on */
					if(map.isMyLocationEnabled())
					{
						if(searchMarker != null)
						{
							searchMarker.remove();	//Remove Marker if already active
						}
											
						GetDistance(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude(), "Current Location");	//Call GetDistance with relevant variables
					}
					else
					{
						/* Output a toast to the user */
						Context context = getApplicationContext();
						Toast toast = Toast.makeText(context, "Turn on GPS", Toast.LENGTH_SHORT);
						toast.show();
					}
					break;
				}
			case R.id.glasgow:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Glasgow).zoom(10).build()));
					break;
				}
			case R.id.delhi:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Delhi).zoom(10).build()));
					break;
				}
			case R.id.melbourne:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Melbourne).zoom(10).build()));
					break;
				}
			case R.id.manchester:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Manchester).zoom(10).build()));
					break;
				}
			case R.id.kuala_lumpur:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_KualaLumpur).zoom(10).build()));
					break;
				}
			case R.id.victoria:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Victoria).zoom(10).build()));
					break;
				}
			case R.id.auckland:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Auckland).zoom(10).build()));
					break;
				}
			case R.id.edinburgh:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Edinburgh).zoom(10).build()));
					break;
				}
			case R.id.brisbane:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Brisbane).zoom(10).build()));
					break;
				}
			case R.id.edmonton:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Edmonton).zoom(10).build()));
					break;
				}
			case R.id.christchurch:
				{
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_Christchurch).zoom(10).build()));
					break;
				}
			default:
				{
					return super.onOptionsItemSelected(item);
				}
		}
		return true;
	}
	
	public void ChangeMap()
	{
		/* Based on the counter number */
		if(mapCounter == 0)
		{
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);	//Set to Hybrid Map
		}
		else if(mapCounter == 1)
		{
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);	//Set to Terrain Map
		}
		else
		{
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);	//Set to Normal Map
		}
		
		mapCounter++;	//Iterate counter up
		
		/* Clamp variable between 0-2 */
		if(mapCounter > 2)
		{
			mapCounter = 0;
		}
	}

	public void MoveTo(String location)
	{
		/* already active search marker, line and glasgow marker remove*/
		if(searchMarker != null)
		{
			searchMarker.remove();
			if (line != null)
			{
				line.remove();
				glasMarker.remove();
			}
		}
		
		try 
		{
			addresses = geocoder.getFromLocationName(location, 1);	//Fill addresses list with the top location
		} 
		catch (IOException e) 
		{
			// If it cant find any set addresses to null
			addresses = null;
			e.printStackTrace();
		}

		/* address found */
		if (addresses.size() > 0)
		{
			double latitude = addresses.get(0).getLatitude();	//Set Lat
			double longitude = addresses.get(0).getLongitude();	//Set Long
			
			/* Create Marker, Set InfoWindow on, and animate camera towards it */
			searchMarker = map.addMarker(new MarkerOptions().title(addresses.get(0).getFeatureName()+", "+addresses.get(0).getCountryName()).snippet("Tap for distance to Glasgow").position(new LatLng(latitude, longitude)));
			searchMarker.showInfoWindow();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(10).build()));
			
			searchMenuItem.collapseActionView();	//Collapse the search bar

		}
		else
		{
			/* Output a toast to the user */
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "Search Invalid", Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}
	
	public void GetDistance(double latitude, double longitude, String name)
	{
		/* Remove Line and Marker if active */
		if (line != null)
		{
			line.remove();
			glasMarker.remove();
		}
		
		float[] results = new float[1];	//Create a float for the results
		Location.distanceBetween(latitude, longitude, pos_GlasgowCentre.latitude, pos_GlasgowCentre.longitude, results);	//Get the distance
		
		String distance = Float.toString(Math.round(results[0]/1000f));	//Create a String from the rounded, converted (metres to km) float
		
		/* Create Markers, show its infowindow, add line between position and Glasgow centre, animate camera to Glasgow */
		glasMarker = map.addMarker(new MarkerOptions().title(distance+" Kilometres approx.").snippet(name+" to Glasgow").position(pos_GlasgowCentre));
		glasMarker.showInfoWindow();
		line = map.addPolyline(new PolylineOptions().add(pos_GlasgowCentre).add(new LatLng(latitude, longitude)));
		map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(pos_GlasgowCentre).zoom(map.getCameraPosition().zoom).build()));
	}
	
	@Override
    protected void onNewIntent(Intent intent) 
    {        
    	setIntent(intent);		//Set Intent
        handleIntent(intent);	//Call handle intent
    }
	
	private void handleIntent(Intent intent) 
    {
		/* If the new intent is the search intent */
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
	    {
	        String query = intent.getStringExtra(SearchManager.QUERY); //Set the string to its queery
	        MoveTo(query);	//Call MoveTo with the query text
	    }
    }
	
}


