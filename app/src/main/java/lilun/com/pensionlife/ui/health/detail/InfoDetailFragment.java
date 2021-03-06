package lilun.com.pensionlife.ui.health.detail;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.ProgressWebView;
import lilun.com.pensionlife.widget.slider.BannerPager;

/**
 * @author yk
 *         create at 2017/5/11 10:45
 *         email : yk_developer@163.com
 */
public class InfoDetailFragment extends BaseFragment {

    @Bind(R.id.web_content_h5)
    ProgressWebView webContentH5;
    @Bind(R.id.iv_icon)
    BannerPager ivIcon;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_mobile)
    TextView tvMobile;
    @Bind(R.id.tv_content_title)
    TextView tvContentTitle;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_address_title)
    TextView tvAddressTitle;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.ns_content_json)
    NestedScrollView nsContentJson;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    private Information information;

    public static InfoDetailFragment newInstance(Information information) {
        InfoDetailFragment fragment = new InfoDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("information", information);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        information = (Information) arguments.getSerializable("information");
        Preconditions.checkNull(information);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_info_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
//        String parentId = information.getParentId();
//        String substring = parentId.substring(parentId.lastIndexOf("/") + 1);
        titleBar.setTitle(information.getTitle());

        UIUtils.setBold(tvAddressTitle);
        UIUtils.setBold(tvContentTitle);
        UIUtils.setBold(tvTitle);

        showContentWithType();
    }

    private void showContentWithType() {
        int contextType = information.getContextType();
        String content = information.getContext();
        nsContentJson.setVisibility(contextType == 5 ? View.VISIBLE : View.GONE);
        webContentH5.setVisibility(contextType == 2 ||contextType == 3 ? View.VISIBLE : View.GONE);
        switch (contextType) {
            //html
            case 2:
                webContentH5.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                break;
            //url
            case 3:
                webContentH5.loadUrl(content);
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String address = jsonObject.getString("address");
                    String mobile = jsonObject.getString("mobile");
                    String description = jsonObject.getString("description");
                    tvTitle.setText(information.getTitle());
                    tvAddress.setText(address);
                    if (!TextUtils.isEmpty(mobile)) {
                        tvMobile.setText(String.format("联系电话:%1$s", mobile));
                    } else {
                        tvMobile.setVisibility(View.GONE);
                    }
                    tvContent.setText(description);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if (webContentH5.getVisibility() == View.VISIBLE) {
            if (webContentH5.canGoBack()) {
                webContentH5.goBack();
                return true;
            }
        }
        return super.onBackPressedSupport();

    }
}
