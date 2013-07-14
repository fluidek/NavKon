package navkon.maps;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

public class ActivityPlacesResults extends ListActivity {
	private String[] names;
	private String[] addresses;
	private double[] latitudes;
	private double[] longitudes;
	private LatLng location;
	int officeId;
	int modeId;
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.places_results);
	        names = getIntent().getStringArrayExtra("names");
	        addresses = getIntent().getStringArrayExtra("address");
	        latitudes = getIntent().getDoubleArrayExtra("latitudes");
	        longitudes = getIntent().getDoubleArrayExtra("longitudes");
	        officeId = getIntent().getIntExtra("officeId", 0);
	        modeId = getIntent().getIntExtra("modeId", 0);
	        ListView list=getListView();
	        list.setAdapter(new CustomAdapter(this, names, addresses));
	        list.setOnItemClickListener(new OnItemClickListener() {

	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {
	                Intent intent = new Intent(ActivityPlacesResults.this,ActivityMain.class);
	                intent.putExtra("wasWP", true);
	                intent.putExtra("waypointLatitude",latitudes[(int)id]);
	                intent.putExtra("waypointLongitude",longitudes[(int)id]);
	                intent.putExtra("officeId",officeId);
	                intent.putExtra("modeId",modeId);
	                ActivityMain.off.finish();
	                finish();
	                ActivityPlacesResults.this.startActivity(intent);
	            }

	        });
	 }

}
