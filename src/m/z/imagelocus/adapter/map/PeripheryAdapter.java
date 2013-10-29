package m.z.imagelocus.adapter.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.BitmapUtils;
import m.z.imagelocus.R;
import m.z.imagelocus.entity.Periphery;

import java.util.List;

/**
 * Created by Winnid on 13-10-29.
 */
public class PeripheryAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Periphery> items;
    private Context mContext;

    private ViewHolder holder;

    public PeripheryAdapter(Context context, List<Periphery> list) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        items = list;
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_periphery_item, null);
            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.addr = (TextView) convertView.findViewById(R.id.addr);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.leaseType = (TextView) convertView.findViewById(R.id.leaseType);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder.index.setText((String) items.get(position).getIndex());
        holder.addr.setText((String) items.get(position).getAddr() + "");
        holder.name.setText((String) items.get(position).getName() + "");
        holder.distance.setText((String) items.get(position).getDistance()
                + "");
        holder.price.setText((String) items.get(position).getPrice());
        holder.leaseType.setText((String) items.get(position).getLeaseType());
        BitmapUtils bitmapUtils = new BitmapUtils(mContext);
        bitmapUtils.display(holder.icon, (String) items.get(position).getImageurl());
        return convertView;
    }

    /* class ViewHolder */
    private class ViewHolder {
        TextView addr;
        TextView name;
        TextView distance;
        TextView price;
        TextView leaseType;
        ImageView icon;
    }
}