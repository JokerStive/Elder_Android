package lilun.com.pension.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import lilun.com.pension.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * 天时分秒倒计时器
 * Created by zp on 2017/4/20.
 */

public class CountDownView extends LinearLayout {
    TextView tvDays, tvHours, tvMinutes, tvSenconds;

    private Date targetTime;
    CompositeSubscription cntDownRx;

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context);
    }

    private void initUI(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_count_down, this);
        tvDays = (TextView) view.findViewById(R.id.tv_count_down_days);
        tvHours = (TextView) view.findViewById(R.id.tv_count_down_hours);
        tvMinutes = (TextView) view.findViewById(R.id.tv_count_down_minutes);
        tvSenconds = (TextView) view.findViewById(R.id.tv_count_down_seconds);
    }

    public CountDownView setTime(Date targetTime) {
        this.targetTime = targetTime;
        return this;
    }

    public Date getTime() {
        return targetTime;
    }

    /**
     * 开始计时
     */
    public void start() {
        if (targetTime == null) return;
        if (cntDownRx == null)
            cntDownRx = new CompositeSubscription();
        Date now = new Date();
        long time = targetTime.getTime() - now.getTime();
        if (time <= 0) return;


        try {
            final long finalTotalSecs = time;
            cntDownRx.add(
                    Observable.timer(0, 1000, TimeUnit.MILLISECONDS)
                            //  .observeOn(Schedulers.io())
                            .map(new Func1<Long, Long>() {
                                @Override
                                public Long call(Long aLong) {
                                    return finalTotalSecs - aLong.intValue() * 1000L;
                                }
                            })
                            .take((int) (finalTotalSecs + 1))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(res -> {

                                long day = res / (24 * 60 * 60 * 1000);
                                long hour = (res / (60 * 60 * 1000) - day * 24);
                                long min = ((res / (60 * 1000)) - day * 24 * 60 - hour * 60);
                                long s = (res / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);


                                tvDays.setText(String.format("%02d", day));
                                tvHours.setText(String.format("%02d", hour));
                                tvMinutes.setText(String.format("%02d", min));
                                tvSenconds.setText(String.format("%02d", s));
                                if(res/1000 ==0){
                                    stop();
                                }
                            })
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (cntDownRx != null && !cntDownRx.isUnsubscribed()) {
            cntDownRx.unsubscribe();
        }
    }
}
