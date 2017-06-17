package unprogramador.com.calloutexpress;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CesarFlores on 16-Jun-17.
 */

public class BlockNumberAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<BlockNumberData> items;

    public BlockNumberAdapter(Activity activity, ArrayList<BlockNumberData> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inf.inflate(R.layout.listview, null);
        BlockNumberData det = items.get(position);
        TextView name = (TextView) v.findViewById(R.id.NameText);
        TextView phone = (TextView) v.findViewById(R.id.NumberText);
        name.setText(det.getName());
        phone.setText(det.getPhone());
        return v;
    }
}
