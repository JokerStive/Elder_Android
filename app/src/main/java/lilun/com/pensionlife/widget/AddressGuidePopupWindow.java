package lilun.com.pensionlife.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import lilun.com.pensionlife.R;

/**
 * Created by zp on 2017/7/25.
 */

public class AddressGuidePopupWindow extends PopupWindow {
    TextView tvHeader, tvMid;

    public AddressGuidePopupWindow(Context context) {
        this(LayoutInflater.from(context).inflate(R.layout.window_address_guide, null, false));
    }

    public AddressGuidePopupWindow(View contentView) {
        this(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public AddressGuidePopupWindow(View contentView, int width, int height) {
        super(contentView, width, height, true);
        Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
        setBackgroundDrawable(drawable);
        setOutsideTouchable(true);
        initView(contentView);
    }


    public void initView(View inflate) {
        ImageView know = (ImageView) inflate.findViewById(R.id.iv_setaddress_guide_know);
        know.setOnClickListener((v) -> {
            dismiss();
        });

        tvHeader = (TextView) inflate.findViewById(R.id.tv_addres_header);
        tvMid = (TextView) inflate.findViewById(R.id.tv_addres_mid);

    }

    public void setHeaderXYHeight(int x, int y, int headerHeight) {
        tvHeader.getLayoutParams().height = headerHeight;
    }

    public void setMidXYHeight(int x, int y, int midHeight) {
        tvMid.getLayoutParams().height = midHeight;
    }
}
