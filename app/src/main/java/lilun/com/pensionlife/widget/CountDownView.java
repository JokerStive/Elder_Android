package lilun.com.pensionlife.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import lilun.com.pensionlife.R;
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
    private LinearLayout llCountDown;
    private TextView tvText;
    Context mContent;

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContent = context;
        initUI(context);
    }

    private void initUI(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_count_down, this);
        llCountDown = (LinearLayout) view.findViewById(R.id.ll_count_down);
        tvDays = (TextView) view.findViewById(R.id.tv_count_down_days);
        tvHours = (TextView) view.findViewById(R.id.tv_count_down_hours);
        tvMinutes = (TextView) view.findViewById(R.id.tv_count_down_minutes);
        tvSenconds = (TextView) view.findViewById(R.id.tv_count_down_seconds);
        tvText = (TextView) view.findViewById(R.id.tv_text);
        llCountDown.setVisibility(GONE);
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
        llCountDown.setVisibility(VISIBLE);
        tvText.setVisibility(GONE);

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
                            .take((finalTotalSecs + 1), TimeUnit.MILLISECONDS)
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
                                if (res / 1000 == 0) {
                                    stop();
                                    tvText.setText(mContent.getString(R.string.activity_has_started));
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
            llCountDown.setVisibility(GONE);
            tvText.setVisibility(VISIBLE);
        }
    }

    public void setText(String text) {
        tvText.setText(text);
    }

}
