package navkon.maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityMenu extends Activity {
	public static int officeId;
	public static int modeId;
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.introduction);
	    officeId = 0;
	    modeId = 0;
	    Spinner spinner = (Spinner) findViewById(R.id.spinner1);
	    TextView usaInfo = (TextView) findViewById(R.id.americaText);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	            R.array.offices_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    String infoText = getResources().getString(R.string.usa_info);
	    spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(this, usaInfo, infoText, true));
	    Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
	    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
	            R.array.modes_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner2.setAdapter(adapter2);
	    spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener(this, null, null, false));
	    Button dalej = (Button)findViewById(R.id.button1);
        dalej.setOnClickListener(new View.OnClickListener() 
      {
        	@Override
            public void onClick(View v) 
            {
        		Intent myIntent = new Intent(ActivityMenu.this, ActivityMain.class);
        		myIntent.putExtra("officeId", ActivityMenu.officeId);
        		myIntent.putExtra("modeId", ActivityMenu.modeId);
                ActivityMenu.this.startActivity(myIntent);   
            }
      	});
	}
	
}
