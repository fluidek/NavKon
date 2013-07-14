package navkon.maps;

import com.google.android.gms.maps.model.LatLng;

public class Place {
	private String placeName;
	private String placeAddress;
	private double latitude;
	private double longitude;
	public String getPlaceName()
	{
		return this.placeName;
	}
	public String getPlaceAddress()
	{
		return this.placeAddress;
	}
	public LatLng getLocation()
	{
		return new LatLng(latitude,longitude);
	}
	public void setPlaceAddress(String add)
	{
		this.placeAddress = add;
	}
	public void setPlaceName(String name)
	{
		this.placeName = name;
	}
	public void setGeo(double lat, double lng)
	{
		this.latitude = lat;
		this.longitude = lng;
	}
}
