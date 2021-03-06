package m.z.imagelocus.adapter.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import m.z.imagelocus.R;
import m.z.imagelocus.entity.Lbs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-15
 * @Version V1.0
 * Created by Winnid on 13-9-15.
 */
public class LbsHistoryAdapter extends BaseAdapter {

    private Context mContext;

    //展示的LBS
    private List<Lbs> lbsList = new ArrayList<Lbs>();

    public LbsHistoryAdapter(Context context, List<Lbs> lbsList) {
        this.mContext=context;
        this.lbsList = lbsList;
    }

    /**
     * 元素的个数
     */
    public int getCount() {
        return lbsList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    //用以生成在ListView中展示的一个个元素View
    public View getView(int position, View convertView, ViewGroup parent) {
        //优化ListView
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.adapter_lbshistory_item, null);
            ItemViewCache viewCache=new ItemViewCache();
            viewCache.mTextView=(TextView)convertView.findViewById(R.id.text);
            convertView.setTag(viewCache);
        }
        ItemViewCache cache=(ItemViewCache)convertView.getTag();
        //设置文本，然后返回这个View，用于ListView的Item的展示
        cache.mTextView.setText(lbsList.get(position).getAddrStr());
        return convertView;
    }

    //元素的缓冲类,用于优化ListView
    private static class ItemViewCache{
        public TextView mTextView;
    }
}
