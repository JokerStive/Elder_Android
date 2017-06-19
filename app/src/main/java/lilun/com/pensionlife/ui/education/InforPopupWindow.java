package lilun.com.pensionlife.ui.education;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.widget.CircleImageView;

/**
 * 信息提交框
 * Created by zp on 2017/3/8.
 */

public class InforPopupWindow extends PopupWindow {
    Context context;
    CircleImageView civIcon;
    TextView tvShowStr;
    private Button btConfirm;

    public InforPopupWindow(View view, String uri, String showStr) {
        super(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        context = view.getContext();
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);
        UIUtils.setBackgroundAlpha((Activity) context, 0.6f);
        init(view, uri, showStr);
    }

    public static InforPopupWindow newInstance(Context context, String uri, String showStr) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_info, null);
        InforPopupWindow popupWindow = new InforPopupWindow(view, uri, showStr);
        return popupWindow;
    }


    public void init(View view, String uri, String showStr) {
        civIcon = (CircleImageView) view.findViewById(R.id.civ_icon);
        tvShowStr = (TextView) view.findViewById(R.id.tv_show_str);
        btConfirm = (Button) view.findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener((view1) -> {
            dismiss();
        });
        Glide.with(view.getContext())
                .load(uri)
                //.load(R.drawable.icon_def)
                .placeholder(R.drawable.icon_def)
                .error(R.drawable.icon_def)
                .into(civIcon);
        tvShowStr.setText(showStr);
    }

    @Override
    public void dismiss() {
        UIUtils.setBackgroundAlpha((Activity) context, 1f);
        super.dismiss();
    }
}
