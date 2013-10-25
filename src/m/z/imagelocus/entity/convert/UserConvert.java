package m.z.imagelocus.entity.convert;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.baidu.location.BDLocation;
import m.z.imagelocus.R;
import m.z.imagelocus.entity.yun.UserLocYun;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-21
 * @Version V1.0
 * Created by Winnid on 13-9-21.
 */
public class UserConvert {

    public static Drawable getUserHead(Context mContext, Object userhead) {

        Drawable drawable = null;

        int i = 0;
        if(userhead != null && userhead instanceof String && !userhead.equals("")) {
            i = Integer.parseInt(userhead.toString());
        }
        switch (i) {
            case 1:
                drawable = mContext.getResources().getDrawable(R.drawable.user_head_system_1);
                break;
            case 2:
                drawable = mContext.getResources().getDrawable(R.drawable.user_head_system_2);
                break;
            case 3:
                drawable = mContext.getResources().getDrawable(R.drawable.user_head_system_3);
                break;
            case 4:
                drawable = mContext.getResources().getDrawable(R.drawable.user_head_system_4);
                break;
            case 5:
                drawable = mContext.getResources().getDrawable(R.drawable.user_head_system_5);
                break;
            case 6:
                drawable = mContext.getResources().getDrawable(R.drawable.user_head_system_6);
                break;
            case 7:
                drawable = mContext.getResources().getDrawable(R.drawable.user_head_system_7);
                break;
            case 8:
                drawable = mContext.getResources().getDrawable(R.drawable.user_head_system_8);
                break;
            case 9:
                drawable = mContext.getResources().getDrawable(R.drawable.user_head_system_9);
                break;
            case 10:
                drawable = mContext.getResources().getDrawable(R.drawable.user_head_system_10);
                break;
        }
        if( drawable == null) {
            drawable = mContext.getResources().getDrawable(R.drawable.default_avatar_shadow);
        }
        return drawable;
    }
}
