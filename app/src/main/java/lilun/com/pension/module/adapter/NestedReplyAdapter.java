package lilun.com.pension.module.adapter;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
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
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class NestedReplyAdapter extends QuickAdapter<NestedReply> {
    private BaseFragment fragment;
    private boolean isMaster;
    private AnswerListener listener;
    private String creatorName;


    public NestedReplyAdapter(BaseFragment fragment, List<NestedReply> data, boolean isMaster, String creatorName) {
        this(fragment, data, isMaster, creatorName, null);
    }

    public NestedReplyAdapter(BaseFragment fragment, List<NestedReply> data, boolean isMaster, String creatorName, AnswerListener listener) {
        super(R.layout.item_nested_reply, data);
        this.fragment = fragment;
        this.isMaster = isMaster;
        this.listener = listener;
        this.creatorName = creatorName;
    }

    @Override
    protected void convert(BaseViewHolder helper, NestedReply nestedReply) {
        OrganizationReply question = nestedReply.getQuestion();
        OrganizationReply answer = nestedReply.getAnswer();
        if (question != null) {
            helper.setText(R.id.tv_name, question.getCreatorName())
                    .setText(R.id.tv_creator, StringUtils.timeFormat(question.getCreatedAt()))
                    .setText(R.id.tv_content, StringUtils.filterNull(question.getContent()));

            ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, question.getCreatorId(), null), R.drawable.icon_def, helper.getView(R.id.iv_avatar));
        }

        TextView tvCrartorReply = helper.getView(R.id.tv_creator_reply);
        if (isMaster) {
            if (answer == null || TextUtils.isEmpty(answer.getContent())) {
                helper.getView(R.id.iv_reply).setVisibility(View.VISIBLE);
                if (listener != null) {
                    helper.setOnClickListener(R.id.iv_reply, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            listener.OnClickAnswer(nestedReply, (helper.getLayoutPosition() - getHeaderLayoutCount()));
                        }
                    });
                }
            } else
                helper.getView(R.id.iv_reply).setVisibility(View.GONE);
        } else
            helper.getView(R.id.iv_reply).setVisibility(View.GONE);
        if (answer != null) {
            Spanned answerContent = Html.fromHtml("<font color ='#008CEE'>" + creatorName + "：</font>" + answer.getContent());
            tvCrartorReply.setVisibility(View.VISIBLE);
            tvCrartorReply.setText(answerContent);
        } else {
            tvCrartorReply.setVisibility(View.GONE);
        }
    }

    public interface AnswerListener {
        void OnClickAnswer(NestedReply nestedReply, int position);
    }


}
