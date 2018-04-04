package lilun.com.pensionlife.widget.chatView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.widget.InputSendView;
import lilun.com.pensionlife.widget.NormalItemDecoration;

/**
 * 聊天室组件
 * 互动式支持， 通过Adapter添加header的方式，完成在列表中显示产品基础信息内容；
 * Created by Administrator on 2018/3/19.
 */

public class ChatView extends RelativeLayout {

    private RecyclerView recyclerView;
    private InputSendView inputSendView;

    private TextView unReadView;
    private InputSendView.OnSendListener onSendListener;
    private QuickAdapter adapter;//聊天消息的adapter


    public ChatView(Context context) {
        super(context);
        init(context);
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_layout_chat, this, false);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view);
        inputSendView = (InputSendView) inflate.findViewById(R.id.isv_input_send);
        unReadView = (TextView) inflate.findViewById(R.id.tv_smooth_last_read);
        addView(inflate);
        initRecycleView();
        inputSendView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }


    /**
     * 初始化  未读消息按钮显示界面
     */
    public void setUnreadCount(int unReadCount) {
        if (unReadCount != 0) {
            unReadView.setVisibility(View.VISIBLE);
            unReadView.startAnimation(startShowAnimotion());
        } else {
            unReadView.setVisibility(View.GONE);
        }
        unReadView.setText(getContext().getString(R.string.num_unread, unReadCount > 99 ? ("...") : (unReadCount + "")));
        unReadView.setOnClickListener((v) -> {
            if (adapter != null && adapter.getData() != null && adapter.getData().size() >= unReadCount) {
                recyclerView.smoothScrollToPosition(adapter.getData().size() - unReadCount);
                unReadView.startAnimation(startHideAnimotion());
                unReadView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 初始化 列表显示内容
     */
    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new NormalItemDecoration(getContext(), 5));
    }

    /**
     * 设置聊天adapter
     *
     * @param adapter
     */
    public void setChatAdapter(QuickAdapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(this.adapter);
        if (adapter.getItemCount() > 0)
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);

    }


    /**
     * 设置发送监听事件
     *
     * @param onSendListener
     */
    public void setOnSendListener(InputSendView.OnSendListener onSendListener) {
        inputSendView.setOnSendListener(onSendListener);
    }

    /**
     * 显示按钮动画
     *
     * @return
     */
    public Animation startShowAnimotion() {
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1f);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        set.setDuration(500);
        set.addAnimation(alphaAnimation);
        set.addAnimation(translateAnimation);
        return set;
    }

    /**
     * 隐藏按钮动画
     *
     * @return
     */
    public Animation startHideAnimotion() {
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        set.setDuration(500);
        set.addAnimation(alphaAnimation);
        set.addAnimation(translateAnimation);
        return set;
    }


}
