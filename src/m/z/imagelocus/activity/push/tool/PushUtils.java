package m.z.imagelocus.activity.push.tool;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import m.z.imagelocus.config.SystemConfig;

public class PushUtils {
	public static final String TAG = "ImageLocus - BaiduPush";

	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_ERRCODE = "errcode";
    public static final String RESPONSE_CONTENT = "content";

	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    public static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";

	// 获取AppKey
    public static String getMetaValue(Context context, String metaKey) {
        //本来存放在Mainfest.xml中，已经移植到SystemConfig.java中
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    // 获取AppKey
    public static String getMetaValue() {
        return SystemConfig.BDPushKey;
    }

}
