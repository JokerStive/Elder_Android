package lilun.com.pensionlife.ui.protocol;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Information;

/**
 * 协议view
 */
public class ProtocolView extends RelativeLayout {

    private TextView tvProtocol;
    private BaseFragment fragment;
    private Information protocol;
    private TextView tvAgree;


    public ProtocolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    //
    public ProtocolView(Context context) {
        super(context);
        initView(context);
    }

    public void showProtocol(BaseFragment fragment, Information protocol) {
        if (fragment == null || protocol == null) {
            throw new IllegalStateException("fragment  and protocol can't be null");
        }

        this.fragment = fragment;
        this.protocol = protocol;

        show();
    }

    private void show() {
        String title = protocol.getTitle();
        tvProtocol.setText(title);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.protocol_view, this);

        tvAgree = (TextView) view.findViewById(R.id.tv_agree);
        tvProtocol = (TextView) view.findViewById(R.id.tv_protocol_title);
        tvAgree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIsFocus();

            }
        });
        tvProtocol.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.start(ProtocolDetailFragment.newInstance(protocol.getId(),false));
            }
        });
    }

    private void checkIsFocus() {
        boolean isFocus = false;
        JSONObject extend = protocol.getExtend();
        if (extend != null) {
            isFocus = extend.getBoolean("isFocus");
        }
        if (isFocus) {
            fragment.start(ProtocolDetailFragment.newInstance(protocol.getId(),true));
        } else {
            tvAgree.setSelected(!tvAgree.isSelected());
        }
    }

    public void agreeProtocol(boolean isAgree) {
        tvAgree.setSelected(isAgree);
    }

    public boolean isAgreeProtocol() {
        return tvAgree.isSelected();
    }


    public static class IsAgreeProtocol {
        public IsAgreeProtocol(boolean isAgreeProtocol) {
            this.isAgreeProtocol = isAgreeProtocol;
        }

        public boolean isAgreeProtocol;
    }

}
