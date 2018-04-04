package lilun.com.pensionlife.ui.activity.activity_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.WindowManager;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseChatFragment;
import lilun.com.pensionlife.module.bean.OrganizationActivity;

/**
 * 社区活动聊天室
 * 分开写的目的是各自逻辑不同
 * Created by zp on 2017/5/3.
 */

public class ActivityChatFragment extends BaseChatFragment {


    static String Arg_Activity = "activity";
    OrganizationActivity activity;

    /**
     * 普通聊天室
     *
     * @return
     */

    public static ActivityChatFragment newInstance(OrganizationActivity activity) {
        ActivityChatFragment fragment = new ActivityChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(Arg_Activity, activity);
        args.putSerializable(Arg_ChatType, ChatType_Normal);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//    }

    /**
     * 获取产品id，订阅产品所在组织的聊天频道
     * 注意topic----eg ：
     * 社区活动主题：: /地球村/中国/重庆/重庆/渝中区/大坪街道/#activity/春季运动会活动ID/.chat
     *
     * @param arguments
     */
    @Override
    protected void getTransferData(Bundle arguments) {
        activity = (OrganizationActivity) arguments.getSerializable(Arg_Activity);
        chatType = (int) arguments.getSerializable(Arg_ChatType);
        if (chatType == ChatType_Normal) {
            if (activity == null) try {
                throw new Throwable("OrganizationActivity must not be null");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return;
            }
            //此两值 在父类里的initData里进行mqtt消息订阅
            objectId = activity.getId();
            orgId = activity.getOrganizationId();
        }
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(activity.getTitle());
        titleBar.setRightIcon(R.drawable.detail);
        titleBar.setOnRightClickListener(() -> {
            ActivityDetailFragment fragment = findFragment(ActivityDetailFragment.class);
            if (fragment == null) {
                hideSoftInput();
                start(ActivityDetailFragment.newInstance(activity));
            } else
                popTo(ActivityDetailFragment.class, false);
        });
        super.initView(inflater);
    }

    @Override
    public void inflateProductView() {

    }

}
