package lilun.com.pensionlife.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.module.utils.RxUtils;
import rx.Observable;
import rx.Subscription;

/**
* 紧急求助计数器
*@author yk
*create at 2017/1/23 13:53
*email : yk_developer@163.com
*/
public class AlarmView extends RelativeLayout {

    private int time = 10;
    private int currentTime = time;
    private Subscription subscribe;
    private OnStopTimingListener listener;
    private CountDown countDown;

    public AlarmView(Context context) {
        super(context);
        init(context);
    }


    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AlarmView);
        time = array.getInt(R.styleable.AlarmView_time, this.time);
        array.recycle();
        init(context);
    }

    public int getTime(){
        return time;
    }


    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_alarm_view, this);
        TextView tvCount = (TextView) view.findViewById(R.id.tv_count);

        //开始动画
        countDown = (CountDown) view.findViewById(R.id.cd_count);
        countDown.startAnimator(time);

        //开启定时计数器
        subscribe = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applySchedule())
                .subscribe(aLong -> {
                    currentTime = (int) (time - aLong);
                    tvCount.setText(currentTime + "");
                    if (currentTime == 0) {
                        if (listener!=null){
                            listener.onStopTiming();
                        }
                    }
                });
    }


    /**
     * 停止计时
     */
    public void stopTiming() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }


        countDown.stopAnimator();
    }

    /**
     * 设置停止计时监听
     */
    public void setOnStopTimingListener(OnStopTimingListener listener) {
        this.listener = listener;
    }

    public interface OnStopTimingListener {
        void onStopTiming();
    }
}
