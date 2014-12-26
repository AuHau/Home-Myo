package cz.cvut.uhlirad1.homemyo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cz.cvut.uhlirad1.homemyo.R;
import cz.cvut.uhlirad1.homemyo.knx.Command;

import java.util.List;

/**
 * Created on 26.12.14
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
public class CommandsAdapter extends ArrayAdapter<Command> {

    LayoutInflater inflater;

    public CommandsAdapter(Context context, int resource, List<Command> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.command, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        ImageView icon = (ImageView)convertView.findViewById(R.id.icon);

        Command command = getItem(position);

        name.setText(command.getName());
        address.setText(command.getAddress());
        icon.setImageResource(command.getElementType().getIconResource());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
