//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package m.z.imagelocus.activity.push.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import m.z.imagelocus.R.id;
import m.z.imagelocus.R.layout;

public final class PushInitActivity_
    extends PushInitActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_push_init);
    }

    private void init_(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void afterSetContentView_() {
        text_init_message = ((TextView) findViewById(id.text_init_message));
        btn_init_getid = ((Button) findViewById(id.btn_init_getid));
        btn_init_go = ((Button) findViewById(id.btn_init_go));
        {
            View view = findViewById(id.btn_init_go);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PushInitActivity_.this.initGoClicked(view);
                    }

                }
                );
            }
        }
        {
            View view = findViewById(id.btn_init_getid);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PushInitActivity_.this.initGetIdClicked(view);
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

    public static PushInitActivity_.IntentBuilder_ intent(Context context) {
        return new PushInitActivity_.IntentBuilder_(context);
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, PushInitActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public PushInitActivity_.IntentBuilder_ flags(int flags) {
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
