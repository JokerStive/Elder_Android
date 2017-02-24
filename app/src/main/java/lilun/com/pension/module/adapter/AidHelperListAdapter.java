package lilun.com.pension.module.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationReply;

/**
 * 展示互助列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class AidHelperListAdapter extends QuickAdapter<OrganizationReply> {
    private BaseFragment fragment;
    private String icon;
    private OnFunctionClickListener listener;
    private OrganizationReply mAgreeReply;
    private boolean showAllAgreeBtn = true;
    private String answerId;


    public AidHelperListAdapter(BaseFragment fragment, List<OrganizationReply> data) {
        super(R.layout.item_aid_helper, data);
        this.fragment = fragment;
//        initData();
    }

//    @Override
//    public void addAll(List<OrganizationReply> elements) {
//        super.addAll(elements);
//        initData();
//    }
//
//    @Override
//    public void replaceAll(List<OrganizationReply> elements) {
//        super.replaceAll(elements);
//        initData();
//    }
//
//    private void initData() {
//        for (OrganizationReply reply : getData()) {
//            if (reply.isEnabled()) {
//                showAllAgreeBtn = false;
//            }
//        }
//    }

    @Override
    protected void convert(BaseViewHolder helper, OrganizationReply reply) {
        TextView tvAgree = helper.getView(R.id.tv_agree);
        tvAgree.setVisibility(showAllAgreeBtn ? View.VISIBLE : View.GONE);

        if (answerId!=null && reply.getId().equals(answerId)) {
            tvAgree.setVisibility(View.VISIBLE);
            tvAgree.setText(App.context.getString(R.string.evaluation));
        }


        helper.setText(R.id.tv_name, reply.getCreatorName())
                .setOnClickListener(R.id.tv_agree, v -> {
                    if (listener != null) {
                        if (answerId!=null){
                            listener.evaluation();
                        }else {
                            listener.agree(reply.getId());
                        }
                    }
                });
    }


    public void setAgree(boolean showAllAgreeBtn,String answerId) {
        this.showAllAgreeBtn = showAllAgreeBtn;
        this.answerId =answerId;
        notifyDataSetChanged();
    }

    public void setOnFunctionClickListener(OnFunctionClickListener listener) {
        this.listener = listener;
    }

    public interface OnFunctionClickListener {
        void agree(String id);
        void evaluation();
    }


}
