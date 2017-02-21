package lilun.com.pension.ui.help.help_detail;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AidJoinerAdapter;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.utils.Preconditions;

/**
 * 互助详情V
 *
 * @author yk
 *         create at 2017/2/20 9:34
 *         email : yk_developer@163.com
 */
public class HelpDetailFragment extends BaseFragment<HelpDetailContract.Presenter> implements HelpDetailContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private OrganizationAid mAid;
    private ImageView ivIcon;
    private ImageView ivBack;
    private TextView tvChangeStatus;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvCreator;
    private TextView tvJonerTitle;
    private LinearLayout llTypeControl;
    private TextView tvPhone;
    private TextView tvAddress;

    private String status_new="新建";
    private String status_answered="已回答";
    private String status_solved="已解决";
    private int kind_ask=0;
    private int kind_help=1;
    private boolean mCreatorIsOwn;
    private String mStatus;
    private int mKind;
    private List<OrganizationActivity> mDetailData = new ArrayList<>();
    private View mHeadView;

    public static HelpDetailFragment newInstance(OrganizationAid aid) {
        HelpDetailFragment fragment = new HelpDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("OrganizationAid", aid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mAid = (OrganizationAid) arguments.getSerializable("OrganizationAid");
        Preconditions.checkNull(mAid);
        mCreatorIsOwn = User.creatorIsOwn(mAid.getCreatorId());
        mStatus = mAid.getStatus();
        mKind = mAid.getKind();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new HelpDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler_view;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        mHeadView = inflater.inflate(R.layout.head_help_detail, null);

        //不管是问还是帮都具有的
        ivIcon = (ImageView) mHeadView.findViewById(R.id.iv_aid_icon);
        ivBack = (ImageView) mHeadView.findViewById(R.id.iv_back);


        tvChangeStatus = (TextView) mHeadView.findViewById(R.id.tv_change_status);
        tvTitle = (TextView) mHeadView.findViewById(R.id.tv_item_title);
        tvTime = (TextView) mHeadView.findViewById(R.id.tv_aid_time);
        tvCreator = (TextView) mHeadView.findViewById(R.id.tv_aid_creator);

        //参与者或者回答者列表的title
        tvJonerTitle = (TextView) mHeadView.findViewById(R.id.tv_joiner_title);


        //  只有 "帮" 才具有的=============================
        llTypeControl = (LinearLayout) mHeadView.findViewById(R.id.ll_type_help);
        tvPhone = (TextView) mHeadView.findViewById(R.id.tv_aid_phone);
        tvAddress = (TextView) mHeadView.findViewById(R.id.tv_aid_address);


        //类型--问
        if (mKind==kind_ask){
            llTypeControl.setVisibility(View.GONE);
            //是自己创建的
            if (mCreatorIsOwn){
                if (mStatus.equals(status_new)){
                    setChangeStatus(getString(R.string.cancel));
                }
            }
            //不是自己创建的
            else {
                if (mStatus.equals(status_new) || mStatus.equals(status_answered)){
                    setChangeStatus(getString(R.string.answer));
                }
            }
        }

        //类型--帮
        if (mKind==kind_help){
            //是自己创建的
            if (mCreatorIsOwn){
                if (mStatus.equals(status_new)){
                    setChangeStatus(getString(R.string.cancel));
                }
            }
            //不是自己创建的
            else {
                if (mStatus.equals(status_new) || mStatus.equals(status_answered)){
                    setChangeStatus(getString(R.string.help));
                }
            }
        }

        setJoinerAdapter();

    }

    private void setJoinerAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(App.context,LinearLayoutManager.VERTICAL,false));
        AidJoinerAdapter adapter = new AidJoinerAdapter(this,mDetailData);
        adapter.addHeaderView(mHeadView);
        recyclerView.setAdapter(adapter);
    }

    private void setChangeStatus(String string) {
        if (tvChangeStatus.getVisibility()==View.GONE){
            tvChangeStatus.setVisibility(View.VISIBLE);
        }
        tvChangeStatus.setText(string);
    }

    @Override
    public void showHelpDetail() {

    }


}
