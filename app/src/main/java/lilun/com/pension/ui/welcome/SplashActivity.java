package lilun.com.pension.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import lilun.com.pension.R;
import lilun.com.pension.app.User;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.ui.home.HomeActivity;
import rx.Observable;

/**
 * 启动页面
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Observable.timer(1, TimeUnit.SECONDS)
                .compose(RxUtils.applySchedule())
                .subscribe(aLong -> {
                    start();
                });
    }


    private void start() {
        if (!TextUtils.isEmpty(User.getUserId())) {
            startActivity(new Intent(this, HomeActivity.class));
        }else {
            startActivity(new Intent(this, WelcomeActivity.class));
        }
        finish();
    }
}
