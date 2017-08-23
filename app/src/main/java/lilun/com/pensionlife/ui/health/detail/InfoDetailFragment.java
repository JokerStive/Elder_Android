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
        webContentH5.setVisibility(contextType == 2 ? View.VISIBLE : View.GONE);
        switch (contextType) {
            //html
            case 2:
//                webContentH5.loadDataWithBaseURL("http://test.j1home.com/api/OrganizationInformations/%2F%E5%9C%B0%E7%90%83%E6%9D%91%2F%E4%B8%AD%E5%9B%BD%2F%E9%87%8D%E5%BA%86%2F%E9%87%8D%E5%BA%86%E5%B8%82%2F%E6%BC%94%E7%A4%BA%E5%8C%BA%2F%E6%BC%94%E7%A4%BA%E8%A1%97%E9%81%93%2F%E8%80%83%E8%AF%95%E7%A4%BE%E5%8C%BA1%2F%23information%2F%E7%A4%BE%E5%8C%BA%E5%85%AC%E5%91%8A%2F%E5%AF%84%E5%88%B0%E5%B0%B1%E5%A4%A7%E5%AE%B6%E9%83%BD/downloadPic/1503480658505.png");
                webContentH5.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                break;
            //json
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
