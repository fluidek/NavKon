package navkon.maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityInfo extends Activity {
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_layout);
		TextView saddr = (TextView) findViewById(R.id.saddress);
		TextView eaddr = (TextView) findViewById(R.id.eaddress);
		TextView odl = (TextView) findViewById(R.id.odl);
		TextView czas = (TextView) findViewById(R.id.time);
		saddr.setText(getIntent().getStringExtra("saddr"));
		eaddr.setText(getIntent().getStringExtra("eaddr"));
		odl.setText(getIntent().getStringExtra("odl"));
		czas.setText(getIntent().getStringExtra("czas"));
		final Button back = (Button) findViewById(R.id.info_powrot);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
	}
}
