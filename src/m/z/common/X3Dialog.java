package m.z.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import m.z.imagelocus.activity.push.tool.Utils;

/**
 * Created by Winnid on 13-10-24.
 */
public abstract class X3Dialog {
    private Context mContext;
    private String msg;

    public X3Dialog(Context context, String msg) {
        this.mContext = context;
        this.msg = msg;
        initParams();

    }

    private void initParams() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(msg);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                doConfirm();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                doCancel();
            }
        });
        builder.create().show();
    }

    public abstract void doConfirm();
    public abstract void doCancel();
}
