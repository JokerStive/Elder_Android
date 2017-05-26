package lilun.com.pension.ui.push_info;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Constants;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.adapter.CacheInfoAdapter;
import lilun.com.pension.module.bean.CacheInfo;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.PushMessage;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 消息列表
 *
 * @author yk
 *         create at 2017/4/27 17:02
 *         email : yk_developer@163.com
 */
public class CacheInfoListActivity extends BaseActivity {
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private String title;
    private String model;
    private List<PushMessage> pushMessages;
    private CacheInfoAdapter cacheInfoAdapter;


    @Override
    protected void getTransferData() {
        super.getTransferData();
        title = getIntent().getStringExtra("title");
        model = getIntent().getStringExtra("model");
        Preconditions.checkNull(title);
        Preconditions.checkNull(model);
    }


    @Override
    protected void initPresenter() {
        if(model.equals(Constants.organizationInfo))
            pushMessages = DataSupport.where("model = ?", model).find(PushMessage.class);
        else
            pushMessages = DataSupport.where("verb = ?", PushMessage.VERB_HELP).find(PushMessage.class);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void initView() {
        titleBar.setTitle(title);
        titleBar.setOnBackClickListener(this::finish);
        titleBar.setRightText("清空");
        titleBar.setOnRightClickListener(new NormalTitleBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                clearCacheInfo();
            }


        });

        recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new NormalItemDecoration(10));
        cacheInfoAdapter = new CacheInfoAdapter(getCacheInfos());
        recyclerView.setAdapter(cacheInfoAdapter);
        swipeLayout.setEnabled(false);

    }

    private void clearCacheInfo() {
        new MaterialDialog.Builder(this)
                .content("确定清空" + title + "?")
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    cacheInfoAdapter.clear();
                    DataSupport.deleteAll(PushMessage.class, "model = ?", model);
                })
                .onNegative((dialog1, which1) -> dialog1.dismiss())
                .show();
    }


    private List<CacheInfo> getCacheInfos() {
        List<CacheInfo> cacheInfos = new ArrayList<>();
        if (pushMessages != null && model.equals(Constants.organizationInfo)) {
            Logger.d("数据库--" + model + "---缓存的条数--" + pushMessages.size());
            for (int i = 0; i < pushMessages.size(); i++) {
                PushMessage pushMessage = pushMessages.get(i);
                String data = pushMessage.getData();
                CacheInfo cacheInfo = getCacheInfoFromModel(data);
                if (cacheInfo != null) {
                    cacheInfos.add(cacheInfo);
                }
            }
        }
        else {
            Logger.d("数据库--" + PushMessage.VERB_HELP + "---缓存的条数--" + pushMessages.size());
            for (int i = 0; i < pushMessages.size(); i++) {
                PushMessage pushMessage = pushMessages.get(i);
                CacheInfo cacheInfo = new CacheInfo("姓名：" + pushMessage.getTitle(),
                        "位置：" + pushMessage.getAddress(),
                        "发生时间：" + pushMessage.getTime(),
                        "联系电话：" + pushMessage.getMobile());

                if (cacheInfo != null) {
                    cacheInfos.add(cacheInfo);
                }
            }
        }
        return cacheInfos;
    }

    private CacheInfo getCacheInfoFromModel(String data) {
        CacheInfo cacheInfo = null;
        Gson gson = new Gson();
        switch (model) {
            case Constants.organizationAid:
                OrganizationAid aid = gson.fromJson(data, OrganizationAid.class);
                if (title.equals("紧急求助") && aid.getKind() == 2) {
                    String createdAt = aid.getCreatedAt();
                    cacheInfo = new CacheInfo("姓名：" + aid.getCreatorName(), "位置：" + aid.getAddress(), "发生时间：" + StringUtils.IOS2ToUTC(createdAt, 6), "联系电话：" + aid.getMobile());
                }
                break;

            case Constants.organizationInfo:
                Information Information = gson.fromJson(data, Information.class);
                cacheInfo = new CacheInfo(Information.getCreatorName(), Information.getTitle(), StringUtils.IOS2ToUTC(Information.getCreatedAt(), 6), Information.getContext());
                break;

        }
        return cacheInfo;
    }


}
