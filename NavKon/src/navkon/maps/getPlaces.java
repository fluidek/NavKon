package navkon.maps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

public class getPlaces extends AsyncTask<String, String, String> {
	private ProgressDialog pDialog;
	private ActivityPlaces res;
	private Intent intent;
	private Context context;
	private static final String API_KEY = "AIzaSyDPP2s0h8xD5TOTyf8IBeY0vw6QvP_a0Ug";
	private LatLng currentLocation;
	private String types;
	private int status;
	private int size;
	private double latitude;
	private double longitude;
	public String[] namesArray;
	public String[] addressArray;
	private double[] latitudesArray;
	private double[] longitudesArray;
	private int officeID;
	private int modeID;
	private List<LatLng> location = new ArrayList<LatLng>();
	private List<String> name = new ArrayList<String>();
	private List<String> address = new ArrayList<String>();
	
	public getPlaces (Context context, double latitude, double longitude, int mode, int offID, int modID)
	{
		this.context = context;
		this.latitude = latitude; 
		this.longitude = longitude;
		this.officeID = offID;
		this.modeID = modID;
		switch (mode)
		{
		case 0: 
			this.types ="gas_station";
			break;
		case 1:
			this.types="lodging";
			break;
		case 2:
			this.types="grocery_or_supermarket";
			break;
		case 3:
			this.types="restaurant|bar";
			break;
		case 4:
			this.types="atm";
			break;
		}
	}
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getResources().getString(R.string.loading));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    protected String doInBackground(String... args) {
        String stringUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&rankby=distance&types="+types+"&sensor=false&key="+API_KEY;
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
           
            	this.status = 2;
            	JSONArray resultsArray = jsonObject.getJSONArray("results");
            	this.size = resultsArray.length();
            	//Przetworz kazdy rezultat
            	for(int i=0; i<resultsArray.length(); i++)
            	{
            		JSONObject res = resultsArray.getJSONObject(i);
            		JSONObject geo = res.getJSONObject("geometry");
            		JSONObject loc = geo.getJSONObject("location");
            		location.add(new LatLng(loc.getDouble("lat"), loc.getDouble("lng")));
            		name.add(res.getString("name"));
            		address.add(res.getString("vicinity"));
            	}

        } catch (Exception e) {

        }

        return null;

    }
    protected void onPostExecute(String file_url) {
    	if(this.status == 2)
    	{
    		setNamesAndAddress(this.name.size());
    		Intent intent = new Intent(context,ActivityPlacesResults.class);
    		intent.putExtra("names",this.namesArray);
    		intent.putExtra("latitudes",latitudesArray);
    		intent.putExtra("longitudes", longitudesArray);
    		intent.putExtra("address",addressArray);
    		intent.putExtra("officeId", officeID);
    		intent.putExtra("modeId", modeID);
    		context.startActivity(intent);
    	}
        pDialog.dismiss();
    }
    public void setNamesAndAddress(int n)
	 {
		 this.namesArray = new String[n];
		 this.addressArray = new String[n];
		 this.latitudesArray = new double[n];
		 this.longitudesArray = new double [n];
		 
		 for (int i=0; i<n; i++)
		 {
			 this.namesArray[i] = this.name.get(i);
			 this.addressArray[i] = this.address.get(i);
			 this.latitudesArray[i] = this.location.get(i).latitude;
			 this.longitudesArray[i] = this.location.get(i).longitude;
		 }
	 }
}