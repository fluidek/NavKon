package navkon.maps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Html;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class getDirection extends AsyncTask<String, String, String> {
	private ProgressDialog pDialog;
	private mojaMapa mapa;
	private List<LatLng> polylines = new ArrayList<LatLng>();
	private List<LatLng> waypoints = new ArrayList<LatLng>();
	private List<String> instructions= new ArrayList<String>();
	private Context context;
	private LatLng office;
	private String mode;
	private String language;
	private double startLat;
	private double startLng;
	private String distance;
	private String duration;
	private String startAddress;
	private String endAddress;
	private LatLng waypoint;
	private int numberOfSteps;
	mojaLokacja lokacja;
    
    public getDirection(Context context, double lat, double lng, mojaMapa map, mojaLokacja lok, int officeId, int modeId, LatLng waypoint)
    {
    	this.context = context;
    	this.startLat = lat;
    	this.startLng = lng;
    	this.lokacja = lok;
    	this.waypoint = waypoint;
    	mapa = map;
    	switch (officeId)
    	{
    		case 0: 
    			office = new LatLng(53.137542, 18.127186);
    			break;
    		case 1:
    			office = new LatLng(51.753589 ,19.455633);
    			break;
    		case 2:
    			office = new LatLng(37.385239, -121.924867);
    			break;
    		case 3:
    			office = new LatLng(53.42883, 14.55658);
    			break;
    		case 4:
    			office = new LatLng(52.172165, 21.025509);
    			break;
    		case 5:
    			office = new LatLng(53.327801, -2.228015);
    			break;
    	}
    	switch (modeId)
    	{
    		case 0:
    			mode="driving";
    			break;
    		case 1: 
    			mode="cycling";
    			break;
    		case 2:
    			mode="walking";
    			break;
    	}

    	if (context.getResources().getConfiguration().locale.getDisplayLanguage().equals("polski"))
    	{
    		language="pl";
    	}
    	else
    	{
    		language="en";
    	}
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getResources().getString(R.string.init));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected String doInBackground(String... args) {
    	String stringUrl;
    	if (waypoint!=null)
    	{
    		stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin="+startLat+","+startLng+"&destination="+office.latitude+","+office.longitude+"&sensor=false&mode="+mode+"&waypoints=via:"+waypoint.latitude+","+waypoint.longitude+"&language="+language;
    	}
    	else
    	{
    		 stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin="+startLat+","+startLng+"&destination="+office.latitude+","+office.longitude+"&sensor=false&mode="+mode+"&language="+language;
    	}
    	StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection httpconn = (HttpURLConnection) url
                    .openConnection();
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(httpconn.getInputStream()),
                        8192);
                String strLine = null;

                while ((strLine = input.readLine()) != null) {
                    response.append(strLine);
                }
                input.close();
            }

            String jsonOutput = response.toString();

            JSONObject jsonObject = new JSONObject(jsonOutput);

            
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            JSONObject route = routesArray.getJSONObject(0);
            JSONArray legsArray = route.getJSONArray("legs");
            JSONObject leg = legsArray.getJSONObject(0);
            JSONObject dist = leg.getJSONObject("distance");
            distance = dist.getString("text");
            JSONObject dur = leg.getJSONObject("duration");
            duration = dur.getString("text");
            startAddress = leg.getString("start_address");
            endAddress = leg.getString("end_address");
            JSONArray stepsArray = leg.getJSONArray("steps");
            List<String> stringi = new ArrayList<String>();
            for (int i = 0;i < stepsArray.length(); i++)
            {
            	JSONObject step = stepsArray.getJSONObject(i);
            	JSONObject poly = step.getJSONObject("polyline");
            	JSONObject wp = step.getJSONObject("end_location");
            	waypoints.add(new LatLng(wp.getDouble("lat"),wp.getDouble("lng")));
            	stringi.add(poly.getString("points"));
            	instructions.add(step.getString("html_instructions"));
            }
            numberOfSteps = stepsArray.length();
            polylines = decodePoly(stringi);
           

        } catch (Exception e) {

        }

        return null;

    }

    protected void onPostExecute(String file_url) {
        for (int i = 0; i < polylines.size() - 1; i++) {
            LatLng src = polylines.get(i);
            LatLng dest = polylines.get(i + 1);
            Polyline line = mapa.addLine(new PolylineOptions()
                    .add(new LatLng(src.latitude, src.longitude),
                            new LatLng(dest.latitude,                dest.longitude))
                    .width(10).color(Color.BLUE).geodesic(true));
            if(this.waypoint!=null)
            {
            	mapa.setWypoint(waypoint);
            }
        }
        lokacja.checkPoints = waypoints;
        lokacja.htmlInstructions = instructions;
        lokacja.setInfo(distance, duration, startAddress, endAddress, numberOfSteps);
        pDialog.dismiss();
    }
/* Method to decode polyline points */
private List<LatLng> decodePoly(List<String> encoded)
{
	List<LatLng> poly = new ArrayList<LatLng>();
	for (int h = 0; h<encoded.size(); h++)
	{ 		
		int index = 0, len = encoded.get(h).length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.get(h).charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.get(h).charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng; 
			LatLng p = new LatLng((((double) lat / 1E5)),
				(((double) lng / 1E5)));
			poly.add(p);
		}
	}

    return poly;
}

}