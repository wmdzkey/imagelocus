package m.z.imagelocus.adapter.friend;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import m.z.imagelocus.R;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.entity.Chat;
import m.z.imagelocus.entity.convert.UserConvert;
import m.z.util.CalendarUtil;

import java.util.List;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-10-13
 * @Version V1.0
 * Created by Winnid on 13-10-13.
 */
public class ChatAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<Chat> chatList = null;
    private LayoutInflater inflater = null;
    private int COME_MSG = 0;
    private int TO_MSG = 1;

    public ChatAdapter(Context context,List<Chat> chatList){
        this.mContext = context;
        this.chatList = chatList;
        inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        // 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
        Chat entity = chatList.get(position);
        if (entity.isComeMsg())
        {
            return COME_MSG;
        }else{
            return TO_MSG;
        }
    }

    @Override
    public int getViewTypeCount() {
        // 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatHolder chatHolder = null;
        if (convertView == null) {
            chatHolder = new ChatHolder();
            if (chatList.get(position).isComeMsg()) {
                convertView = inflater.inflate(R.layout.friend_chat_from_item, null);
            }else {
                convertView = inflater.inflate(R.layout.friend_chat_to_item, null);
            }
            chatHolder.timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
            chatHolder.contentTextView = (TextView) convertView.findViewById(R.id.tv_content);
            chatHolder.userImageView = (ImageView) convertView.findViewById(R.id.iv_user_image);
            convertView.setTag(chatHolder);
        }else {
            chatHolder = (ChatHolder)convertView.getTag();
        }

        chatHolder.timeTextView.setText(CalendarUtil.showSimpleTime(chatList.get(position).getChatTime()));
        chatHolder.contentTextView.setText(chatList.get(position).getContent());
        if (chatList.get(position).isComeMsg()) {
            chatHolder.userImageView.setImageDrawable(UserConvert.getUserHead(mContext, (chatList.get(position).getUserhead())));
        }else {
            chatHolder.userImageView.setImageDrawable(UserConvert.getUserHead(mContext, (SystemAdapter.currentUser.getUserhead())));
        }

        return convertView;
    }

    private class ChatHolder{
        private TextView timeTextView;
        private ImageView userImageView;
        private TextView contentTextView;
    }

}
