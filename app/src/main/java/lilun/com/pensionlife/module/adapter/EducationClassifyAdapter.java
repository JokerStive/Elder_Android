package lilun.com.pensionlife.module.adapter;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.ElderModule;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class EducationClassifyAdapter extends QuickAdapter<ElderModule> {


    private int defaultHeight = (int) App.context.getResources().getDimension(R.dimen.dp_180);
    private int height = defaultHeight;
    //    private int height = 180;


    //    private float textSize = UIUtils.sp2px(App.context, App.context.getResources().getDimension(R.dimen.sp_22));
    private float textSize = 22;

    public EducationClassifyAdapter(List<ElderModule> data) {
        super(R.layout.item_college_classify, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ElderModule elderModule) {
        int adapterPosition = helper.getAdapterPosition();
        if (adapterPosition > 0) {
            height = defaultHeight / 2;
//            textSize = App.context.getResources().getDimension(R.dimen.dp_18);
            textSize = 18;
        }
        View itemView = helper.itemView;
        itemView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        TextView tvElderName = helper.getView(R.id.tv_elder_title);
        tvElderName.setTextSize(textSize);
        tvElderName.setText(elderModule.getName());
        tvElderName.setTextColor(getColor(adapterPosition));


        if (elderModule.getIcon() != null) {
            String iconUrl = elderModule.getIcon();
            ImageLoaderUtil.instance().loadImage(iconUrl, R.drawable.icon_def, helper.getView(R.id.iv_elder_icon));
        }
    }

    private int getColor(int adapterPosition) {
        int color;
        if (adapterPosition == 0) {
            color = Color.parseColor("#F79A24");
        } else if (adapterPosition == 1) {
            color = Color.parseColor("#F31E22");
        } else {
            color = Color.parseColor("#7DCA55");
        }
        return color;
    }


}
