package lilun.com.pensionlife.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.ui.home.HomeActivity;
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
        String belongsOrganizationId = User.getBelongsOrganizationId();
        if (!TextUtils.isEmpty(belongsOrganizationId)) {
            startActivity(new Intent(this, HomeActivity.class));
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
