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
public class AidHelperListAdapter extends QuickAdapter<OrganizationReply> {
    private BaseFragment fragment;
    private OnFunctionClickListener listener;
    private String answerId;


    public AidHelperListAdapter(BaseFragment fragment, List<OrganizationReply> data) {
        super(R.layout.item_aid_helper, data);
        this.fragment = fragment;
    }


    @Override
    protected void convert(BaseViewHolder helper, OrganizationReply reply) {
        TextView tvAgree = helper.getView(R.id.tv_agree);
        if (TextUtils.isEmpty(answerId)) {
            tvAgree.setVisibility(View.VISIBLE);
        } else {
            if (answerId.equals(reply.getId())) {
                tvAgree.setVisibility(View.VISIBLE);
                tvAgree.setText(App.context.getString(R.string.evaluation));
            } else {
                tvAgree.setVisibility(View.GONE);
            }
        }


        helper.setText(R.id.tv_name, reply.getCreatorName())
                .setOnClickListener(R.id.tv_agree, v -> {
                    if (listener != null) {
                        if (answerId != null) {
                            listener.evaluation(reply.getId());
                        } else {
                            listener.agree(reply.getId());
                        }
                    }
                });
        ImageLoaderUtil.instance().loadImage(IconUrl.account(reply.getId(), null), helper.getView(R.id.iv_avatar));
    }


    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public void setOnFunctionClickListener(OnFunctionClickListener listener) {
        this.listener = listener;
    }

    public interface OnFunctionClickListener {
        void agree(String id);

        void evaluation(String replyId);
    }


}
