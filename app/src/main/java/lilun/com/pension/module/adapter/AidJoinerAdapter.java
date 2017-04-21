package lilun.com.pension.module.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 展示互助列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class AidJoinerAdapter extends QuickAdapter<OrganizationReply> {
    private BaseFragment fragment;
    private OnFunctionClickListener listener;
    private String answerId;
    private String rankId;


    public AidJoinerAdapter(BaseFragment fragment, List<OrganizationReply> data) {
        super(R.layout.item_aid_helper, data);
        this.fragment = fragment;
    }


    @Override
    protected void convert(BaseViewHolder helper, OrganizationReply reply) {
        TextView tvAgree = helper.getView(R.id.tv_agree);
        if (TextUtils.isEmpty(answerId)) {
            tvAgree.setVisibility(View.VISIBLE);
        } else {
            if (answerId.equals(reply.getId()) && TextUtils.isEmpty(rankId)) {
                tvAgree.setVisibility(View.VISIBLE);
                tvAgree.setText(App.context.getString(R.string.evaluation));
            } else {
                tvAgree.setVisibility(View.GONE);
            }
        }


        helper.setText(R.id.tv_product_name, reply.getCreatorName())
                .setText(R.id.tv_mobile,"13202011223")
                .setOnClickListener(R.id.tv_agree, v -> {
                    if (listener != null) {
                        if (answerId != null) {
                            listener.evaluation(reply.getId());
                        } else {
                            listener.agree(reply.getId());
                        }
                    }
                });
        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts,reply.getCreatorId(), null),R.drawable.icon_def, helper.getView(R.id.iv_icon));
    }


    public void setAnswerId(String answerId, String rankId) {
        this.answerId = answerId;
        this.rankId = rankId;
    }

    public void setOnFunctionClickListener(OnFunctionClickListener listener) {
        this.listener = listener;
    }

    public interface OnFunctionClickListener {
        void agree(String id);

        void evaluation(String replyId);
    }


}
