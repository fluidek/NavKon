package navkon.maps;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final String[] nameArray;
  private final String[] addressArray;
  public CustomAdapter(Context context, String[] values, String[] address) {
	super(context, R.layout.list_item, values);
    this.context = context;
    this.nameArray = values;
    this.addressArray = address;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.list_item, parent, false);
    TextView name = (TextView) rowView.findViewById(R.id.place_name);
    TextView address = (TextView) rowView.findViewById(R.id.place_address);
    name.setText(nameArray[position]);
    address.setText(addressArray[position]);
    // 
    return rowView;
  }
}