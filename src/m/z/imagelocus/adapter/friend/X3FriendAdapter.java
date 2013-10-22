package m.z.imagelocus.adapter.friend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-10-11
 * @Version V1.0
 * Created by Winnid on 13-10-11.
 */
public class X3FriendAdapter extends SimpleAdapter {

    //需要配置的信息
    private static int mResource =  R.layout.friend_item;
    private static String[] mFrom = new String[]{"name","img"};
    private static int[] mTo = new int[]{R.id.tv_name,R.id.iv_img};


    private Context mContext;
    private List<? extends Map<String, ?>> mData;
    private LayoutInflater mInflater;
    private ViewBinder mViewBinder;

    public X3FriendAdapter(Context context, List<? extends Object> data) {
        this(addItems(data), context);
    }
    public X3FriendAdapter(List<? extends Map<String, ?>> data, Context context) {
        super(context, data, mResource, mFrom, mTo);
        this.mContext = context;
        this.mData = data;
    }

    //数据和布局自定义绑定
    private void bindView(int position, View view) {
        final Map dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }
        final ViewBinder binder = mViewBinder;
        final View[] holder = (View[]) view.getTag();
        final String[] from = mFrom;
        final int[] to = mTo;
        final int count = to.length;

        for (int i = 0; i < count; i++) {
            final View v = holder[i];
            if (v != null) {
                final Object data = dataSet.get(from[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, data, text);
                }

                if (!bound) {
                    if (v instanceof Checkable) {
                        if (data instanceof Boolean) {
                            ((Checkable) v).setChecked((Boolean) data);
                        } else {
                            throw new IllegalStateException(v.getClass().getName() +
                                    " should be bound to a Boolean, not a " + data.getClass());
                        }

                    } else if (v instanceof TextView) {
                        ((TextView) v).setText(text);

                    } else if (v instanceof RatingBar) {
                        if (data instanceof Number) {
                            final RatingBar rtb = (RatingBar) v;
                            float score = Float.parseFloat(data.toString());
                            rtb.setVisibility(View.VISIBLE);
                            rtb.setRating(score);
                        } else {
                            final RatingBar rtb = (RatingBar) v;
                            rtb.setVisibility(View.GONE);
                        }

                    } else if (v instanceof ImageView) {

//                        if ( (v.getId() == R.id.iv_img) ) {
//                            //使用默认的
//                        } else
                        if (data instanceof Integer) {
                            setViewImage((ImageView) v, (Integer) data);
                        } else if (data instanceof byte[]) {
                            byte[] image = (byte[]) data;
                            if (image.length != 0) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                                ((ImageView) v).setImageBitmap(bmp);
                            }
                        }

                    } else if (v instanceof Button) {
                        final Button button = (Button) v;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CommonView.displayShort(mContext, "我是按钮哦");
                            }
                        });

                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a" +
                                " view that can be bounds by this SimpleAdapter");
                    }
                }
            }
        }
    }


    //加入数据*(Feed)
    public static List<Map<String, Object>> addItems(List<? extends Object> list_object) {
        List<Map<String, Object>> mapTemp = new ArrayList<Map<String, Object>>();
        Map<String, Object> m = null;
        for (Object o : list_object) {
            m = getMapFromObject(o);
            if (m != null) {
                mapTemp.add(m);
            }
        }
        return mapTemp;
    }


    //针对每个对象设置不同的转换方法
    public static Map<String, Object> getMapFromObject(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof User) {
            Map<String, Object> map = new HashMap<String, Object>();
            User u = (User) o;
            map.put("app_user_id", u.getApp_user_id());
            map.put("name", u.getUsername());
            map.put("img", R.drawable.default_avatar_shadow);
            return map;
        } else {
            return null;
        }
    }
}
