//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package m.z.imagelocus.activity.push.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import m.z.imagelocus.R.id;
import m.z.imagelocus.R.layout;

public final class ChatActivity_
    extends ChatActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_push_chat);
    }

    private void init_(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void afterSetContentView_() {
        spn_push_userlist = ((Spinner) findViewById(id.spn_push_userlist));
        et_chat_content = ((EditText) findViewById(id.et_chat_content));
        lv_chat_view = ((ListView) findViewById(id.lv_chat_view));
        btn_chat_push = ((Button) findViewById(id.btn_chat_push));
        {
            View view = findViewById(id.btn_chat_push);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ChatActivity_.this.chatPushClicked(view);
                    }

                }
                );
            }
        }
        init();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView_();
    }

    public static ChatActivity_.IntentBuilder_ intent(Context context) {
        return new ChatActivity_.IntentBuilder_(context);
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, ChatActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public ChatActivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (context_ instanceof Activity) {
                ((Activity) context_).startActivityForResult(intent_, requestCode);
            } else {
                context_.startActivity(intent_);
            }
        }

    }

}
