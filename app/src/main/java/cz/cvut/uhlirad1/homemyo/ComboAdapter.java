package cz.cvut.uhlirad1.homemyo;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hb.views.PinnedSectionListView;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.CommandParserFactory;
import cz.cvut.uhlirad1.homemyo.knx.KnxElementTypes;
import cz.cvut.uhlirad1.homemyo.localization.Room;
import cz.cvut.uhlirad1.homemyo.localization.RoomParserFactory;
import cz.cvut.uhlirad1.homemyo.service.tree.Combo;
import cz.cvut.uhlirad1.homemyo.service.tree.MyoPose;
import cz.cvut.uhlirad1.homemyo.service.tree.Rooms;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created on 23.12.14
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
@EBean
public class ComboAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private static LayoutInflater inflater = null;

    private List<Item> combos;

    private float density;

    @RootContext
    protected Context context;

    @Bean
    AppData data;

    private static int COMBO = 1;
    private static int ROOM = 2;
    private static int[] gestureViews = {R.id.gesture1, R.id.gesture2, R.id.gesture3};


    @AfterInject
    public void init() {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        density = context.getResources().getDisplayMetrics().density;

        combos = convertTree(data.getRootRooms(), data.getRooms(), data.getCommands());
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return combos.size();
    }

    @Override
    public Object getItem(int position) {
        return combos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean isNew = convertView == null;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.row, null);


        Item combo = combos.get(position);

        TextView text = (TextView) convertView.findViewById(R.id.text);

        if (!combo.isRoom()) {
            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            icon.setImageResource(KnxElementTypes.getIconResource(combo.getType()));

            int i = 0;
            ImageView gestureView;
            ViewGroup.LayoutParams params;
            for (MyoPose pose : combo.getPoses()) {
                if (i > 2) break;

                if(isNew){
                    params = text.getLayoutParams();
                    if (i == 0) params.width = Math.round(((params.width / density) - 55) * density);
                    else params.width = Math.round(((params.width / density) - 50) * density);
                    text.setLayoutParams(params);
                }

                gestureView = (ImageView) convertView.findViewById(gestureViews[i++]);
                gestureView.setImageResource(pose.getIconResource());
            }
        } else {
            convertView.setBackgroundColor(Color.argb(255, 80, 80, 80));
        }

        text.setText(combo.getName());

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return (combos.get(position).isRoom ? this.ROOM : this.COMBO);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        return combos.isEmpty();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == 2;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return !combos.get(position).isRoom;
    }

    private List<Item> convertTree(List<Room> tree, Map<Integer, Room> rooms, Map<Integer, Command> commands) {
        LinkedList<Item> list = new LinkedList<Item>();
        Command tmpCommand;

        for (Room treeRoom : tree) {
            if (treeRoom.getCombo() == null || treeRoom.getCombo().isEmpty()) continue;

            list.add(new Item(rooms.get(treeRoom.getId()).getName()));

            for (Combo treeCombo : treeRoom.getCombo()) {
                tmpCommand = commands.get(treeCombo.getCommandId());

                if (tmpCommand.getUserName() == null || tmpCommand.getUserName().isEmpty())
                    list.add(new Item(treeCombo.getId(), tmpCommand.getName(), tmpCommand.getElementType(), treeCombo.getMyoPose()));
                else
                    list.add(new Item(treeCombo.getId(), tmpCommand.getUserName(), tmpCommand.getElementType(), treeCombo.getMyoPose()));
            }
        }

        return list;
    }

    public class Item {

        private KnxElementTypes type;
        private String name;
        private List<MyoPose> poses;
        private int comboId;
        private boolean isRoom;

        public Item(int comboId, String name, KnxElementTypes type, List<MyoPose> poses) {
            this.type = type;
            this.name = name;
            this.poses = poses;
            this.comboId = comboId;
            this.isRoom = false;
        }

        public Item(String name) {
            this.name = name;
            this.isRoom = true;
        }

        public int getComboId() {
            return comboId;
        }

        public List<MyoPose> getPoses() {
            return poses;
        }

        public KnxElementTypes getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public boolean isRoom() {
            return isRoom;
        }
    }
}
