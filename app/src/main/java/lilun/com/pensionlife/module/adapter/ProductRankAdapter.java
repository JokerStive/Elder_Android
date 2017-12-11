package lilun.com.pensionlife.module.adapter;

import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Rank;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 展示互助列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class ProductRankAdapter extends QuickAdapter<Rank> {


    public ProductRankAdapter(List<Rank> data) {
        super(R.layout.item_product_rank, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Rank rank) {

        RatingBar ratingBar = helper.getView(R.id.rb_score);
        ratingBar.setRating(rank.getRanking());

        helper.setText(R.id.tv_rank_name, rank.getCreatorName())
                .setText(R.id.tv_rank_time, StringUtils.IOS2ToUTC(rank.getCreatedAt(), 5))
                .setText(R.id.tv_rank_title, StringUtils.filterNull(rank.getDescription()));


        ImageLoaderUtil.instance().loadAvatar(rank.getCreatorId(), helper.getView(R.id.iv_icon));
    }


}
