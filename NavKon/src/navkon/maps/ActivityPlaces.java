package navkon.maps;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

public class ActivityPlaces extends ListActivity {
	public String[] names;
	public int numberOfPlaces;
	getPlaces getPlace;
	int modeId;
	int officeId;
	public ActivityPlaces thisAct = this;
	Context context = ActivityPlaces.this;
	 public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			final double latitude = getIntent().getDoubleExtra("latitude", 0.0);
			final double longitude = getIntent().getDoubleExtra("longitude", 0.0);
			officeId= getIntent().getIntExtra("officeId", 0);
			modeId = getIntent().getIntExtra("modeId", 0);
			String[] places = getResources().getStringArray(R.array.places_modes);
			setListAdapter(new ArrayAdapter<String>(this, R.layout.places_list,places));
			ListView listView = getListView();
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				   LatLng pos = getIntent().getParcelableExtra("navkom.maps.position");
				   getPlace = new getPlaces(context, latitude, longitude, (int) id, officeId,modeId);
				   getPlace.execute();
				}
			});
	 }
	 
}
