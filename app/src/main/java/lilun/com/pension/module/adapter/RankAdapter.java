package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.Rank;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

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


        helper.setText(R.id.tv_name, rank.getCreatorName())
                .setText(R.id.tv_time, StringUtils.timeFormat(rank.getCreatedAt()))
                .setText(R.id.tv_content, StringUtils.filterNull(rank.getDescription()));


        ImageLoaderUtil.instance().loadImage(IconUrl.account(User.getUserId(),null),R.drawable.avatar,helper.getView(R.id.iv_avatar));
    }


}
