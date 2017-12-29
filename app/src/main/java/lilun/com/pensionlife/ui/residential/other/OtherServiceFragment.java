package lilun.com.pensionlife.ui.residential.other;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.ui.WebActivity;
import lilun.com.pensionlife.widget.DividerGridItemDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Administrator on 2017/12/29.
 */

public class OtherServiceFragment extends BaseFragment {
    @Bind(R.id.title_bar)
    NormalTitleBar normalTitleBar;
    @Bind(R.id.rv_content)
    RecyclerView recyclerView;

    List<LifeServiceBean> data;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_other_service;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        normalTitleBar.setTitle(getString(R.string.life_service));
        normalTitleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                pop();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
        LifeServiceAdapter lifeServiceAdapter = new LifeServiceAdapter(data);
        recyclerView.setAdapter(lifeServiceAdapter);
        lifeServiceAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getContext(), WebActivity.class);
            intent.putExtra("url",data.get(position).url);
            intent.putExtra("title",data.get(position).title);
            startActivity(intent);
        });

    }

    @Override
    protected void initData() {
        data = new ArrayList<>();
        data.add(new LifeServiceBean("一键出行", "方便您的出行，让出行更加便捷", R.drawable.icon_trip, "https://m.zuche.com/"));
        data.add(new LifeServiceBean("本地生活", "让您保持永不断电的生活状态", R.drawable.icon_local_life, "http://m.58.com/cq/"));
    }

    public static SupportFragment newInstance() {
        return new OtherServiceFragment();
    }

    class LifeServiceBean {
        String title;
        String desp;
        int iconRes;
        String url;

        public LifeServiceBean(String title, String desp, int iconRes, String url) {
            this.title = title;
            this.desp = desp;
            this.iconRes = iconRes;
            this.url = url;
        }
    }

    class LifeServiceAdapter extends QuickAdapter<LifeServiceBean> {

        public LifeServiceAdapter(List<LifeServiceBean> data) {
            super(R.layout.item_life_service, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, LifeServiceBean item) {
            helper.setText(R.id.tv_title, item.title)
                    .setText(R.id.tv_desp, item.desp);
            ImageLoaderUtil.instance().loadImage(item.iconRes, helper.getView(R.id.iv_icon));
        }
    }
}
