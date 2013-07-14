package navkon.maps;

import android.view.View;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {
	TextView textView;
	ActivityMenu act;
	String info;
	boolean mode;
	public CustomOnItemSelectedListener(ActivityMenu activity, TextView text, String info, boolean officeMode)
	{
		textView = text;
		act = activity;
		this.info = info;
		mode = officeMode;
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (mode)
		{
			act.officeId = (int) arg3;
			if (arg3 == 2)
			{
				textView.setText(info);
			}
			else
			{
				textView.setText("");
			}
		}
		else
		{
			act.modeId = (int) arg3;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
