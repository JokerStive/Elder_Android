package lilun.com.pensionlife.module.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationReply;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 展示互助列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class AidReplyAdapter extends QuickAdapter<OrganizationReply> {
    private BaseFragment fragment;
    private boolean isOwmCreated;
    private String answerId;
    private OnAgreeClickListener listener;


    public AidReplyAdapter(BaseFragment fragment, List<OrganizationReply> data, boolean isOwmCreated) {
        super(R.layout.item_aid_asker, data);
        this.fragment = fragment;
        this.isOwmCreated = isOwmCreated;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrganizationReply reply) {
        TextView tvAgree = helper.getView(R.id.tv_agree);
        if (TextUtils.isEmpty(answerId)) {
            if (isOwmCreated) {
                tvAgree.setVisibility(View.VISIBLE);
            } else {
                tvAgree.setVisibility(View.GONE);

            }
        }else if (answerId.equals(reply.getId())){
            tvAgree.setVisibility(View.VISIBLE);
            tvAgree.setText("已采纳");
        }else {
            tvAgree.setVisibility(View.GONE);
        }



        helper.setText(R.id.tv_sophisticated, reply.getCreatorName())
                .setText(R.id.tv_creator, StringUtils.timeFormat(reply.getCreatedAt()))
                .setText(R.id.tv_content, StringUtils.filterNull(reply.getContent()))
                .setOnClickListener(R.id.tv_agree, v -> {
                    if (answerId == null && listener != null) {
                        listener.agree(reply.getId());
                    }
                });


//        Glide.with(fragment).load(IconUrl.account(reply.getId(),null))
//                .error(R.drawable.avatar)
//                .into((ImageView) helper.getView(R.id.iv_avatar));
        ImageLoaderUtil.instance().loadAvatar(reply.getCreatorId(),helper.getView(R.id.iv_icon));
    }


    public void setAnswerId(String answerId) {
        this.answerId = answerId;
//        notifyDataSetChanged();
    }


    public void setOnAgreeClickListenerr(OnAgreeClickListener listener) {
        this.listener = listener;
    }


    public interface OnAgreeClickListener {
        void agree(String id);
    }
}
