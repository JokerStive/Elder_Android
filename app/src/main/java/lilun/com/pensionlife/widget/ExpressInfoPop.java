package lilun.com.pensionlife.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.adapter.UrgentInfoAdapter;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.utils.ScreenUtils;

/**
 * 紧急求助弹窗
 *
 * @author yk
 *         create at 2017/4/26 9:59
 *         email : yk_developer@163.com
 */
public class ExpressInfoPop extends PopupWindow implements View.OnClickListener {
    private View parent;
    private Context context;
    private CheckAllListener listener;
    private AnimationDrawable anim;
    private RecyclerView rvExpressInfo;
    private UrgentInfoAdapter adapter;

    public ExpressInfoPop(Context context, View parent) {
        super(context);
        this.context = context;
        this.parent = parent;
        init();
    }

    private void init() {
        if (isShowing()) {
            return;
        }

        int with = ScreenUtils.getScreenWith(context) / 5 * 4;
        int height = ScreenUtils.getScreenHeight(context) / 5 * 3;
        setHeight(height);
        setWidth(with);
        setFocusable(true);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_rect_black));
        backgroundAlpha(0.3f);


        View view = LayoutInflater.from(App.context).inflate(R.layout.pop_express_help, null);

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(with, height);
//        view.setLayoutParams(layoutParams);

        ImageView ivExpressIcon = (ImageView) view.findViewById(R.id.iv_express_icon);
        ImageView ivExpressDelete = (ImageView) view.findViewById(R.id.express_delete);
        TextView tvExpressDesc = (TextView) view.findViewById(R.id.tv_express_desc);
        rvExpressInfo = (RecyclerView) view.findViewById(R.id.rv_express_info);
        Button btnAll = (Button) view.findViewById(R.id.btn_all);

        rvExpressInfo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        List<OrganizationAid> pushMessages = new ArrayList<>();
        adapter = new UrgentInfoAdapter(pushMessages);
        rvExpressInfo.setAdapter(adapter);


        btnAll.setOnClickListener(this);
        ivExpressDelete.setOnClickListener(this);


        anim = (AnimationDrawable) context.getResources().getDrawable(R.drawable.anim_express);
        ivExpressIcon.setBackground(anim);
        start();



        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(view);


        setOnDismissListener(this::resetPop);
    }

    private void resetPop() {
        backgroundAlpha(1f);
        stop();
        anim = null;
    }

    public void setData(OrganizationAid aid) {
        if (adapter != null && adapter.getItemCount() < 2) {
            adapter.add(aid);
        }
        showAtLocation(parent, Gravity.CENTER, 0, 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.express_delete:
                resetPop();
                dismiss();
                break;

            case R.id.btn_all:
                if (listener != null) {
                    listener.checkAll();
                }
                break;
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        if (context instanceof Activity) {
            Logger.d("背景透明度" + bgAlpha);
            WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
            lp.alpha = bgAlpha; // 0.0~1.0
            ((Activity) context).getWindow().setAttributes(lp);
        }
    }


    public interface CheckAllListener {
        void checkAll();
    }


    public void setCheckAllListener(CheckAllListener listener) {
        this.listener = listener;
    }


    private void start() {
        if (anim != null && !anim.isRunning()) {
            anim.start();
        }
    }

    private void stop() {
        if (anim != null && anim.isRunning()) {
            anim.stop();
        }
    }
}
