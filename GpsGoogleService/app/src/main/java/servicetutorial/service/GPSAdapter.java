package servicetutorial.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by koteswara on 19/06/17.
 */

public class GPSAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;
    public ArrayList<GPSModel> gpsModelArrayList;
    Context gpsContext;
    public GPSAdapter(Context context,ArrayList<GPSModel> gpsModelArrayList ){
        gpsContext=context;
        this.gpsModelArrayList=gpsModelArrayList;
    }
    @Override
    public int getCount() {
        return gpsModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return gpsModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) gpsContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.custom_row, null);
            viewHolder = new ViewHolder();


            viewHolder.latitude=(TextView)convertView.findViewById(R.id.latitude_text_display);
            viewHolder.langitude=(TextView)convertView.findViewById(R.id.langitude_text_display);
            viewHolder.locality=(TextView)convertView.findViewById(R.id.locality_text_display);
            viewHolder.area=(TextView)convertView.findViewById(R.id.area_text_display);
            viewHolder.address=(TextView)convertView.findViewById(R.id.address_text_display);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final GPSModel gpsModel = (GPSModel) gpsModelArrayList.get(position);


        viewHolder.latitude.setText(gpsModel.getLatitude());
        viewHolder.langitude.setText(gpsModel.getLangitude());
        viewHolder.locality.setText(gpsModel.getLocality());
        viewHolder.area.setText(gpsModel.getArea());
        viewHolder.address.setText(gpsModel.getAddress());

        notifyDataSetChanged();
        return convertView;
    }
    static class ViewHolder{

        TextView latitude;
        TextView langitude;
        TextView locality;
        TextView area;
        TextView address;


    }
}
