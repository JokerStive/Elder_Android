package lilun.com.pension.module.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.NestedReply;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 嵌套replay的adapter
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class NestedReplyAdapter extends QuickAdapter<NestedReply> {
    private BaseFragment fragment;


    public NestedReplyAdapter(BaseFragment fragment, List<NestedReply> data) {
        super(R.layout.item_nested_reply, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, NestedReply nestedReply) {
        OrganizationReply question = nestedReply.getQuestion();
        OrganizationReply answer = nestedReply.getAnswer();
        if (question!=null){
            helper.setText(R.id.tv_provider_name, question.getCreatorName())
                    .setText(R.id.tv_creator, StringUtils.timeFormat(question.getCreatedAt()))
                    .setText(R.id.tv_content, StringUtils.filterNull(question.getContent()));

            ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts,question.getCreatorId(),null),R.drawable.icon_def,helper.getView(R.id.iv_avatar));
        }

        TextView tvCrartorReply = helper.getView(R.id.tv_creator_reply);
        if (answer!=null){
            String answerContent = "主持人："+answer.getContent();
            tvCrartorReply.setVisibility(View.VISIBLE);
            tvCrartorReply.setText(answerContent);
        }else {
            tvCrartorReply.setVisibility(View.GONE);
        }


    }


}
