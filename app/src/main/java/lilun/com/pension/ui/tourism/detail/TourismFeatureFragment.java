package lilun.com.pension.ui.tourism.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;

/**
 * 旅游注意事项等。。。。
 *
 * @author yk
 *         create at 2017/4/17 8:51
 *         email : yk_developer@163.com
 */
public class TourismFeatureFragment extends BaseFragment {


    @Bind(R.id.tv_feature)
    TextView tvFeature;
    private String feature;

    public static TourismFeatureFragment newInstance(String feature) {
        TourismFeatureFragment fragment = new TourismFeatureFragment();
        Bundle bundle = new Bundle();
        bundle.putString("feature", feature);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        feature = arguments.getString("feature");
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tourism_feature;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        tvFeature.setText(feature);
    }


}
