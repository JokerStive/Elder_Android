package lilun.com.pensionlife.module.adapter;

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
public class RankAdapter extends QuickAdapter<Rank> {


    public RankAdapter(List<Rank> data) {
        super(R.layout.item_aid_asker, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Rank rank) {


        helper.setText(R.id.tv_sophisticated, rank.getCreatorName())
                .setText(R.id.tv_creator, StringUtils.timeFormat(rank.getCreatedAt()))
                .setText(R.id.tv_content, StringUtils.filterNull(rank.getDescription()));


        ImageLoaderUtil.instance().loadAvatar( rank.getCreatorId() , helper.getView(R.id.iv_icon));
    }


}
