package navkon.maps;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class mojaMapa {
	private GoogleMap mapa;
	LatLng MOBICA;
	Marker position;
	Marker waypoint;
	
	public mojaMapa(GoogleMap mapa, int officeId) {
		this.mapa = mapa;
		switch (officeId)
    	{
    		case 0: 
    			MOBICA = new LatLng(53.137542, 18.127186);
    			break;
    		case 1:
    			MOBICA = new LatLng(51.753589 ,19.455633);
    			break;
    		case 2:
    			MOBICA = new LatLng(37.38525,-121.924905);
    			break;
    		case 3:
    			MOBICA = new LatLng(53.42883, 14.55658);
    			break;
    		case 4:
    			MOBICA = new LatLng(52.172165, 21.025509);
    			break;
    		case 5:
    			MOBICA = new LatLng(53.327801, -2.228015);
    			break;
    	}
		Marker target = mapa.addMarker(new MarkerOptions()
				.position(MOBICA)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.target))
				);
	    CameraPosition cameraPosition = new CameraPosition.Builder()
    	.target(MOBICA)
    	.zoom(16)
    	.bearing(0)
    	.tilt(25)
    	.build();
    mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    mapa.getUiSettings().setAllGesturesEnabled(false);
    mapa.getUiSettings().setZoomControlsEnabled(false);
    mapa.getUiSettings().setCompassEnabled(false);
	}
	public Polyline addLine(PolylineOptions options)
	{
		Polyline poly = mapa.addPolyline(options);
		return poly;
	}
	public void zoomIn()
	{
		float zoom = mapa.getCameraPosition().zoom;
		float kierunek = mapa.getCameraPosition().bearing;
		float tilit = mapa.getCameraPosition().tilt;
		if (zoom != 17)
		{
			CameraPosition cameraPosition = new CameraPosition.Builder()
	    		.target(mapa.getCameraPosition().target)
	    		.zoom(zoom+1)
	    		.bearing(kierunek)
	    		.tilt(tilit+25)
	    		.build();
			mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}
	public void zoomOut()
	{
		float zoom = mapa.getCameraPosition().zoom;
		float kierunek = mapa.getCameraPosition().bearing;
		float tilt = mapa.getCameraPosition().tilt;
		if (zoom != 15)
		{
			CameraPosition cameraPosition = new CameraPosition.Builder()
	    		.target(mapa.getCameraPosition().target)
	    		.zoom(zoom-1)
	    		.bearing(kierunek)
	    		.tilt(tilt-25)
	    		.build();
			mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}
	public void setPosition(LatLng pos)
	{
		if (this.position!=null)
			this.position.remove();
		
		this.position = mapa.addMarker(new MarkerOptions()
		.position(pos)
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.position)));
	}
	public void setWypoint(LatLng pos)
	{
		this.waypoint= mapa.addMarker(new MarkerOptions()
		.position(pos)
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.target))
		);
	}
	public void setCameraOn(LatLng position, float bear)
	{
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(position)
		.zoom(mapa.getCameraPosition().zoom)
		.bearing(bear)
		.tilt(mapa.getCameraPosition().tilt)
		.build();
		mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
}
