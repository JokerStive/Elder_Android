package lilun.com.pensionlife.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Event;

/**
 * 激活账号
 *
 * @author yk
 *         create at 2018/1/23 11:06
 *         email : yk_developer@163.com
 */
public class ActivateAccountManager {


    private static ActivateAccountManager instance;
    private MaterialDialog show;

    private ActivateAccountManager() {
    }

    public static ActivateAccountManager newInstance() {
        if (instance == null) {
            synchronized (ActivateAccountManager.class) {
                if (instance == null) {
                    instance = new ActivateAccountManager();
                }
            }
        }

        return instance;
    }


    public void activate(Activity activity, String mobile) {
        View view = activity.getLayoutInflater().inflate(R.layout.activate, null);
        view.findViewById(R.id.tv_activate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                Intent intent = new Intent(activity, RegisterActivity.class);
                activity.startActivity(intent);
                EventBus.getDefault().postSticky(new Event.ActivateEvent(mobile));
            }
        });

        show = new MaterialDialog.Builder(activity)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .customView(view, false)
                .show();

    }

//    public void dismissShow(){
//        if (show!=null && show.isShowing()){
//            show.dismiss();
//        }
//    }
}
