package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;

/**
 * 热门目的地
 *
 * @author yk
 *         create at 2017/4/13 10:02
 *         email : yk_developer@163.com
 */
public class PopularDestinationAdapter extends QuickAdapter<String> {
    public PopularDestinationAdapter(List<String> data) {
        super(R.layout.item_popular_destination, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String destination) {
        holder.setText(R.id.tv_destination, destination);
    }
}
