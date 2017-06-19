package lilun.com.pensionlife.widget.recycler_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import lilun.com.pensionlife.R;

/**
 * 模块
 *
 * @author yk
 *         create at 2017/2/7 13:03
 *         email : yk_developer@163.com
 */

public class NormalModule extends RelativeLayout {

    private  int moduleBackgroundResId;
    private  String moduleDesc;
    private  int moduleIconResId;
    private ImageView ivModuleIcon;
    private TextView tvModuleDesc;
    private LinearLayout llModuleBackground;

    public NormalModule(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NormalModule);
        moduleDesc = array.getString(R.styleable.NormalModule_module_desc);
        moduleIconResId = array.getResourceId(R.styleable.NormalModule_module_icon,0);
        moduleBackgroundResId = array.getResourceId(R.styleable.NormalModule_module_background,0);
        array.recycle();
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_elder_module, this);
        llModuleBackground = (LinearLayout) view.findViewById(R.id.ll_module_background);
        ivModuleIcon = (ImageView) view.findViewById(R.id.iv_module_icon);
        tvModuleDesc = (TextView) view.findViewById(R.id.tv_module_name);


        setModuleDesc(moduleDesc);
        if (moduleIconResId!=0){
            Logger.d("设置icon");
            setModuleIconResId(moduleIconResId);
        }

        if (moduleBackgroundResId!=0){
            Logger.d("设置背景");
            llModuleBackground.setBackgroundResource(moduleBackgroundResId);
        }
    }

    public void setModuleDesc(String title) {
        tvModuleDesc.setText(title);
    }

    public void setModuleIconResId(int moduleIconResId) {
        ivModuleIcon.setBackgroundResource(moduleIconResId);
    }
}
