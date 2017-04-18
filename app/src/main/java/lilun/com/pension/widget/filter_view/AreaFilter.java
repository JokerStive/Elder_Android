package lilun.com.pension.widget.filter_view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.module.adapter.NormalStringFilterAdapter;
import lilun.com.pension.module.bean.Area;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 区域选择器
 *
 * @author yk
 *         create at 2017/3/31 14:42
 *         email : yk_developer@163.com
 */
public class AreaFilter extends LinearLayout {
    private String title;
    private List<SwipeRefreshLayout> viewContainer;
    //    private List<LinearLayout> viewContainer;
    private ArrayList<NormalStringFilterAdapter> adapterContainer;
    private OnItemChooseListener listener;

    public AreaFilter(Context context, int level, String title) {
        super(context);
        setOrientation(HORIZONTAL);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dp2px(context, 200)));
        init(context, level);
        this.title = title;
    }


    private void init(Context context, int level) {
        viewContainer = new ArrayList<>();
        adapterContainer = new ArrayList<>();
        List<String> areas = new ArrayList<>();
        areas.add("重庆");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        for (int i = 0; i < level; i++) {
            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) LayoutInflater.from(context).inflate(R.layout.wheel_view, null);
            swipeRefreshLayout.setLayoutParams(params);
            swipeRefreshLayout.setVisibility(GONE);
            RecyclerView recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.recycler_view);

            recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));

            viewContainer.add(swipeRefreshLayout);

            addView(swipeRefreshLayout);

        }
        getLevelData(0, null);

    }

    private void getLevelData(int level, String locationName) {

        SwipeRefreshLayout swipeRefreshLayout = viewContainer.get(level);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(() -> getLevelData(level, locationName));

        NetHelper.getApi()
                .getChildLocation(locationName)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Area>>() {
                    @Override
                    public void _next(List<Area> areas) {
                        if (areas.size() != 0) {
                            String currentLevelId = areas.get(0).getId();
                            String substring = currentLevelId.substring(0, currentLevelId.lastIndexOf("/"));
                            Logger.d("上层id = " + substring);
                            Area area = new Area();
                            area.setName("不限");
                            area.setId(level == 0 ? null : substring);
                            areas.add(0, area);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        showLevelData(level, areas);
                    }
                });


    }

    private void showLevelData(int level, List<Area> areas) {
        SwipeRefreshLayout swipeRefreshLayout = viewContainer.get(level);
        swipeRefreshLayout.setVisibility(VISIBLE);
        RecyclerView childAt = (RecyclerView) swipeRefreshLayout.getChildAt(0);


        NormalStringFilterAdapter adapter;
        if (adapterContainer.size() <= level) {
            adapter = new NormalStringFilterAdapter(areas);
            adapterContainer.add(adapter);
            adapter.setOnItemClickListener((adapterPosition, id) -> {
                if (adapterPosition == 0 || level == viewContainer.size() - 1) {
                    //如果点的是不限或者到了层级的最后一层，就把后面的层级都隐藏掉，并且重置adapter的状态
                    for (int i = level + 1; i < viewContainer.size(); i++) {
                        SwipeRefreshLayout refreshLayout = viewContainer.get(i);
                        if (refreshLayout.getVisibility() == VISIBLE) {
                            refreshLayout.setVisibility(GONE);
                        }
                        if (i < adapterContainer.size()) {
                            adapterContainer.get(i).resetSelectedPosition();
                        }
                    }
                    if (listener != null) {
                        //恢复成本来的id /地球村/中国/***
                        if (TextUtils.isEmpty(id)) {
                            listener.onItemClick(id, title, true);
                        } else {
                            id = getContext().getResources().getString(R.string.common_address) + id;
                            String name = id.substring(id.lastIndexOf("/") + 1);
                            listener.onItemClick(id, name, false);
                        }
                    }
                } else {
                    getLevelData(level + 1, id);
                }
            });
            adapter.setOnLoadMoreListener(() -> {

            });
            childAt.setAdapter(adapter);
        } else {
            adapter = adapterContainer.get(level);
            adapter.replaceAll(areas);
        }


    }

    public interface OnItemChooseListener {
        void onItemClick(String chooseId, String chooseName, boolean idDef);
    }

    public void setOnItemClickListener(OnItemChooseListener listener) {
        this.listener = listener;
    }


}
