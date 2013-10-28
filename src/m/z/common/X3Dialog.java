package m.z.common;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import m.z.imagelocus.R;

/**
 * Created by Winnid on 13-10-24.
 */
public abstract class X3Dialog {
    private Context mContext;
    private String msg;

    TextView dialog_title;
    TextView dialog_msg;
    Button dialog_btn0;
    Button dialog_btn1;

    public X3Dialog(Context context, String msg) {
        this.mContext = context;
        this.msg = msg;
        initParams();
    }

    private void initParams() {

        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();

        Window window = dialog.getWindow();
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.view_x3dialog);

        dialog_title = (TextView) window.findViewById(R.id.dialog_title);
        dialog_msg = (TextView) window.findViewById(R.id.dialog_msg);
        dialog_title.setText("提示");
        dialog_msg.setText(msg);

        // 为是按钮添加事件,执行退出应用操作
        dialog_btn0 = (Button) window.findViewById(R.id.dialog_btn0);
        dialog_btn0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                doConfirm();
            }
        });

        // 为否按钮添加事件,执行退出应用操作
        dialog_btn1 = (Button) window.findViewById(R.id.dialog_btn1);
        dialog_btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                doCancel();
            }
        });
    }

    public abstract void doConfirm();
    public abstract void doCancel();
}
