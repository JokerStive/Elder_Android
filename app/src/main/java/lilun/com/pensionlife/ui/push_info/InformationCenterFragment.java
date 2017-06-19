package lilun.com.pensionlife.ui.push_info;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 消息中心
 *
 * @author yk
 *         create at 2017/3/29 14:18
 *         email : yk_developer@163.com
 */
public class InformationCenterFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.tv_system)
    TextView tvSystem;

    @Bind(R.id.tv_personal)
    TextView tvPersonal;
    @Bind(R.id.fl_system)
    FrameLayout flSystem;
    @Bind(R.id.fl_personal)
    FrameLayout flPersonal;

    //    @Bind(R.id.rv_system)
//    RecyclerView rvSystem;
//
//    @Bind(R.id.rv_personal)
//    RecyclerView rvPersonal;
    private List<TextView> textViews;
    private List<View> views;

    public static InformationCenterFragment newInstance() {
        InformationCenterFragment fragment = new InformationCenterFragment();
        return fragment;
    }

    @Override
    protected void initPresenter() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_information_center;
    }


    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        tvSystem.setSelected(true);
        tvPersonal.setSelected(false);

        tvPersonal.setOnClickListener(this);
        tvSystem.setOnClickListener(this);

        textViews = new ArrayList<>();
        textViews.add(tvSystem);
        textViews.add(tvPersonal);

        views = new ArrayList<>();
        views.add(flSystem);
        views.add(flPersonal);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_system:
                showWhichView((TextView) v);
                break;

            case R.id.tv_personal:
                showWhichView((TextView) v);
                break;
        }
    }

    private void showWhichView(TextView v) {
        if (!v.isSelected()) {
            for (TextView textView : textViews) {
                boolean selected = textView.isSelected();
                textView.setSelected(!selected);
            }

            for (View view : views) {
                int visibility = view.getVisibility();
                view.setVisibility(visibility == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }


}
