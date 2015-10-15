package cz.cvut.uhlirad1.homemyo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cz.cvut.uhlirad1.homemyo.R;
import cz.cvut.uhlirad1.homemyo.service.tree.MyoPose;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created on 25.12.14
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
public class GestureAdapter extends ArrayAdapter<MyoPose> {

    LayoutInflater inflater;


    public GestureAdapter(Context context, int resource, List<MyoPose> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.gesture, null);

        TextView text = (TextView) convertView.findViewById(R.id.text);
        ImageView icon = (ImageView)convertView.findViewById(R.id.icon);

        MyoPose pose = getItem(position);

        if (pose.getType() == null) {
            text.setText("No gesture");
            icon.setImageResource(android.R.color.transparent);
            icon.setTag(android.R.color.transparent);
        }else{
            text.setText(pose.toNiceString());
            icon.setImageResource(pose.getIconResource());
            icon.setTag(pose.getIconResource());

        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
