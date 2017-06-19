package lilun.com.pensionlife.module.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Tourism;
import lilun.com.pensionlife.module.utils.UIUtils;

/**
 * 旅游
 *
 * @author yk
 *         create at 2017/4/13 16:10
 *         email : yk_developer@163.com
 */
public class TourismSmallAdapter extends QuickAdapter<Tourism> {

    public TourismSmallAdapter(List<Tourism> data) {
        super(R.layout.item_tourism_small, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Tourism tourism) {
        holder.setText(R.id.tv_context, tourism.getName())
                .setText(R.id.tv_price, tourism.getPrice() + "");


        Tourism.ExtendBean extend = tourism.getExtend();
        holder.setText(R.id.tv_duration_traffic, extend.getDuration() + " / " + extend.getTraffic())
                .setText(R.id.tv_satisfaction, extend.getSatisfaction());

        LinearLayout tagContainer = holder.getView(R.id.ll_tag_container);
        tagContainer.removeAllViews();
        List<String> tags = extend.getTag();
        for (String tag : tags) {
            TextView tagView = (TextView) LayoutInflater.from(App.context).inflate(R.layout.tourism_tag_red, null);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.rightMargin = UIUtils.dp2px(App.context, 5);
            tagView.setLayoutParams(p);
            tagView.setText(tag);
            tagContainer.addView(tagView);
        }
    }
}
