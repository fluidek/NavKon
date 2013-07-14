package navkon.maps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
public class ActivityMain extends Activity {
    private int officeId;
    private int modeId;
    private LatLng waypoint;
    public static ActivityMain off;
    public LocationManager manLok;
    mojaLokacja mojaLok;
    private boolean wasWaypoint = false;
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
    	off = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        isGPSEnable();
        try {
    		MapsInitializer.initialize(this);
    	} catch (GooglePlayServicesNotAvailableException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        wasWaypoint = getIntent().getBooleanExtra("wasWP", false);
        if (wasWaypoint)
        {
        	waypoint = new LatLng(getIntent().getDoubleExtra("waypointLatitude", 0.0),getIntent().getDoubleExtra("waypointLongitude", 0.0));
        }
        else
        {
        	waypoint = null;
        }
        this.updateSpeed((float) 0.0);
        this.updateDistance((float) 0.0);
        this.officeId = getIntent().getIntExtra("officeId",0);
        this.modeId = getIntent().getIntExtra("modeId", 0);
        
        LayoutInflater inflater = getLayoutInflater();
        manLok = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    final mojaMapa mapa = new mojaMapa(((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap(), officeId);
	    mojaLok = new mojaLokacja(mapa, ActivityMain.this, officeId, modeId,this,waypoint);
	    manLok.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mojaLok);
	    final Button findPlaces = (Button) findViewById(R.id.findPlaces);
	    findPlaces.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(ActivityMain.this, ActivityPlaces.class);
				LatLng lok = mojaLok.pobierzLokacje();
				myIntent.putExtra("latitude",lok.latitude);
				myIntent.putExtra("longitude", lok.longitude);
				myIntent.putExtra("officeId",officeId);
				myIntent.putExtra("modeId",modeId);
				ActivityMain.this.startActivity(myIntent);
			}
		});
	    final Button zoom_in = (Button) findViewById(R.id.zoom_in);
        zoom_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               mapa.zoomIn();
            }
        });
        final Button zoom_out = (Button) findViewById(R.id.zoom_out);
        zoom_out.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               mapa.zoomOut();
            }
        });
        final Button reset = (Button) findViewById(R.id.resetRouteButton);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = getIntent();
            		intent.putExtra("wasWP", false);
            	finish();
            	startActivity(intent);
            }
        });
        final Button info = (Button) findViewById(R.id.routeInfo);
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(ActivityMain.this, ActivityInfo.class);
            	intent.putExtra("saddr", mojaLok.pobierzAdrPocz());
            	intent.putExtra("eaddr", mojaLok.pobierzAdrKonc());
            	intent.putExtra("czas", mojaLok.pobierzCzas());
            	intent.putExtra("odl", mojaLok.pobierzDystans());
            	ActivityMain.this.startActivity(intent);
            }
        });
    }
    public void onPause()
    {
    	super.onPause();
    	manLok.removeUpdates(mojaLok);
    }
    public void onResume()
    {
    	super.onResume();
    	manLok.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mojaLok);
    }
    public void showInfo(String text)
    {
    	final TextView info = (TextView) findViewById(R.id.info);
    	info.setVisibility(View.VISIBLE);
    	info.setText(Html.fromHtml(text));
    	Runnable mRunnable;
    	Handler mHandler=new Handler();

    	mRunnable=new Runnable() {

    	            @Override
    	            public void run() {
    	            	info.setVisibility(View.INVISIBLE);   
    	            }
    	        };
    	mHandler.postDelayed(mRunnable, 10000);
    }
    public void updateSpeed(float speed)
    {
    	final TextView speedText= (TextView) findViewById(R.id.speed);
    	speedText.setText(Html.fromHtml(speed*3.6+" <sup>km</sup>&frasl;<sub>h</sub>"));
    }
    public void updateDistance(float dist)
    {
    	final TextView distanceText= (TextView) findViewById(R.id.distance);
    	distanceText.setText(String.format("%.2f", dist/1000)+" km");
    }
    public void isGPSEnable(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
          .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          startActivity(intent);
        } 
}
}
