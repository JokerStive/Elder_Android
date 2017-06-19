package lilun.com.pensionlife.module.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Tourism;

/**
 * 旅游
 *
 * @author yk
 *         create at 2017/4/13 16:10
 *         email : yk_developer@163.com
 */
public class TourismBigAdapter extends QuickAdapter<Tourism> {

    public TourismBigAdapter(List<Tourism> data) {
        super(R.layout.item_tourism_big, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Tourism tourism) {
        holder.setText(R.id.tv_title, tourism.getTitle())
                .setText(R.id.tv_content, tourism.getContext())
                .setText(R.id.tv_price, tourism.getPrice() + "");

        Tourism.ExtendBean extend = tourism.getExtend();
        List<String> tags = extend.getTag();
        String duration = extend.getDuration();
        String tagDuration = "";
        if (tags != null && tags.size() > 0) {
            tagDuration = tags.get(0);
        }
        if (!TextUtils.isEmpty(tagDuration) && !TextUtils.isEmpty(duration)) {
            tagDuration = tagDuration + "." + duration;
        } else if (!TextUtils.isEmpty(tagDuration)) {

        } else if (!TextUtils.isEmpty(duration)) {
            tagDuration = duration;
        } else {
            holder.setVisible(R.id.tv_tag_duration, false);
        }

        holder.setText(R.id.tv_tag_duration, tagDuration);

        holder.setText(R.id.tv_departure_satisfaction, extend.getDeparture() + " " + extend.getSatisfaction());


    }
}
