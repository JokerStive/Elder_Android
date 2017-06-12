package lilun.com.pension.ui.push_info;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.adapter.CacheInfoAdapter;
import lilun.com.pension.module.bean.CacheInfo;
import lilun.com.pension.module.bean.CacheMsg;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.utils.CacheMsgClassify;
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
    private int classify;
    private List<CacheMsg> cacheMsgs;
    private CacheInfoAdapter cacheInfoAdapter;


    @Override
    protected void getTransferData() {
        super.getTransferData();
        title = getIntent().getStringExtra("title");
        classify = getIntent().getIntExtra("classify", -1);
        Preconditions.checkNull(title);
        if (classify == -1) {
            throw new NullPointerException();
        }
    }


    @Override
    protected void initPresenter() {
        cacheMsgs = DataSupport.where("classify = ? and userId=?", classify + "", User.getUserId()).find(CacheMsg.class);
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
                clearCacheMsgs();
            }


        });

        recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new NormalItemDecoration(10));
        cacheInfoAdapter = new CacheInfoAdapter(getCacheInfos());
        recyclerView.setAdapter(cacheInfoAdapter);
        swipeLayout.setEnabled(false);

    }

    private void clearCacheMsgs() {
        new MaterialDialog.Builder(this)
                .content("确定清空" + title + "?")
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    cacheInfoAdapter.clear();
                    DataSupport.deleteAll(CacheMsg.class, "classify = ? and userId= ? ", classify + "", User.getUserId());
                })
                .onNegative((dialog1, which1) -> dialog1.dismiss())
                .show();
    }


    private List<CacheInfo> getCacheInfos() {
        List<CacheInfo> cacheInfos = new ArrayList<>();
        if (cacheMsgs != null) {

            Logger.d("数据库--" + classify + "---缓存的条数--" + cacheMsgs.size());

            for (int i = 0; i < cacheMsgs.size(); i++) {
                CacheMsg cacheMsg = cacheMsgs.get(i);
                CacheInfo cacheInfo = getCacheInfoFromModel(cacheMsg.getData());
                if (cacheInfo != null) {
                    cacheInfos.add(cacheInfo);
                }
            }
        }

        return cacheInfos;
    }

    private CacheInfo getCacheInfoFromModel(String data) {
        CacheInfo cacheInfo = null;
        JSONObject dataJson = JSON.parseObject(data);
        CacheMsgClassify msgClassify = new CacheMsgClassify();

        //紧急求助
        if (classify == msgClassify.urgent_help) {
            cacheInfo = new CacheInfo("姓名：" + dataJson.getString("title"),
                    "位置：" + dataJson.getString("address"),
                    "发生时间：" + dataJson.getString("time"),
                    "联系电话：" + dataJson.getString("mobile"));
        }


        //公告
        if (classify == msgClassify.announce) {
            String infoJson = dataJson.getString("data");
            if (infoJson != null) {
                Information information = JSON.parseObject(infoJson, Information.class);
                if (information != null) {
                    String content = null;
                    if (information.getContextType() == 0) {
                        content = information.getContext();
                    }
                    cacheInfo = new CacheInfo(information.getCreatorName(), information.getTitle(), StringUtils.IOS2ToUTC(information.getCreatedAt(), 6), content);
                }
            }


            //邻居互助
            if (classify == msgClassify.normal_help) {
                String aidJson = dataJson.getString("data");
                if (aidJson != null) {
                    OrganizationAid aid = JSON.parseObject(infoJson, OrganizationAid.class);
                    if (aid != null) {
                        cacheInfo = new CacheInfo(aid.getCreatorName(), aid.getTitle(), StringUtils.IOS2ToUTC(aid.getCreatedAt(), 6), aid.getMemo());
                    }
                }
            }
        }

        return cacheInfo;
    }


}
