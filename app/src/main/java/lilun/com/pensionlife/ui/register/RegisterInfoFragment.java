package lilun.com.pensionlife.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vanzh.library.BaseBean;
import com.vanzh.library.BottomDialog;
import com.vanzh.library.DataInterface;
import com.vanzh.library.OnAddressSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.Area;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.ui.welcome.LoginModule;

/**
 * 完善注册信息（地区、街道/社区/小区 、详情地址）
 * Created by zp on 2017/7/18.
 */

public class RegisterInfoFragment extends BaseFragment<RegisterContract.PresenterInfo>
        implements RegisterContract.ViewInfo, DataInterface<BaseBean>, OnAddressSelectedListener {
    final int RECYCLERLEVEL = 3;
    String password;
    String belongOrganizationId;
    String detailAddress;
    BottomDialog dialog;
    int curLevel = -1;

    BaseBean area, distrect;

    @Bind(R.id.rl_belong_stress)
    RelativeLayout rlBelongStress;
    @Bind(R.id.rl_belong_area)
    RelativeLayout rlBelongArea;
    @Bind(R.id.tv_belong_area)
    TextView tvBelongArea;
    @Bind(R.id.tv_belong_stress)
    TextView tvBelongSteress;

    @Bind(R.id.et_belong_communite)
    EditText etBelongCommunite;
    @Bind(R.id.bt_commit)
    Button btCommit;

    int[] skipArray = new int[6];
    int limitSkip = 20;

    @OnClick({R.id.tv_belong_area, R.id.tv_belong_stress, R.id.bt_commit})
    public void onClick(View view) {
        if (view.getId() == R.id.tv_belong_area) {
            dialog = new BottomDialog(this, -1, limitSkip);
            dialog.setOnAddressSelectedListener(this);
            dialog.show();
        } else if (view.getId() == R.id.tv_belong_stress) {
            if (curLevel < RECYCLERLEVEL - 1) {
                ToastHelper.get(getContext()).showWareShort("该地区未开通服务，请重新选择");
                return;
            }
            dialog = new BottomDialog(this, RECYCLERLEVEL, limitSkip);
            dialog.setOnAddressSelectedListener(this);
            dialog.setButtonVisiableLevels(new int[]{2}, View.VISIBLE);
            dialog.show();
        } else if (view.getId() == R.id.bt_commit) {
            if (curLevel < RECYCLERLEVEL) {
                ToastHelper.get(getContext()).showWareShort("该地区未开通服务，请重新选择");
                return;
            }
            belongOrganizationId = getBelongOrganizationId();
            detailAddress = getDetailAddress();
            mPresenter.putAccountLocation(belongOrganizationId, detailAddress);
        }
    }

    public static RegisterInfoFragment newInstance(String password) {

        Bundle args = new Bundle();
        args.putString("password", password);
        RegisterInfoFragment fragment = new RegisterInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        password = arguments.getString("password");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterInfoPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_info;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        rlBelongStress.setVisibility(View.GONE);
        etBelongCommunite.setOnKeyListener(editOnKeyListener);
    }

    @Override
    public void editViewEnterButton() {
        onClick(btCommit);
    }

    @Override
    public void successOfAccountLocation(Account account) {

        LoginModule loginModule = new LoginModule();
        account.setPassword(password);
        loginModule.putAccountInfo(account);
        startWithPop(RegisterAvatorFragment.newInstance(account));
    }

    @Override
    public void successOfChildLocation(List<Area> areas, Response<BaseBean> response, int level, int recyclerIndex) {
        skipArray[level] += areas.size();
        ArrayList<BaseBean> data = new ArrayList<>();
        for (int i = 0; i < areas.size(); i++) {
            data.add(new BaseBean(areas.get(i).getId(), areas.get(i).getName()));
        }
        if (level == recyclerIndex || level == RECYCLERLEVEL) {
            response.send(level, null, false);
            return;
        }

        response.send(level, data, data != null && data.size() == limitSkip);
    }

    public String getBelongOrganizationId() {
        return distrect == null ? "" : distrect.getId();
    }

    public String getDetailAddress() {
        return etBelongCommunite.getText().toString().trim();
    }


    @Override
    public void onAddressSelected(int recyclerIndex, BaseBean... baseBeen) {
        if (baseBeen.length == 0) {
            ToastHelper.get(getContext()).showWareShort("该地区未开通服务，请重新选择");
            return;
        }
        if (recyclerIndex != RECYCLERLEVEL) {
            area = baseBeen[baseBeen.length - 1];
            tvBelongArea.setText(area.getName());
            rlBelongStress.setVisibility(View.VISIBLE);
            tvBelongSteress.setText("");
            distrect = null;
            curLevel = baseBeen.length - 1;
        } else {
            distrect = baseBeen[baseBeen.length - 1];
            tvBelongSteress.setText(distrect.getName());
            curLevel = baseBeen.length - 1 + RECYCLERLEVEL;
        }
        dialog.dismiss();

    }

    @Override
    public void onConfirm(BaseBean baseBean) {
        distrect = baseBean;
        tvBelongSteress.setText(distrect.getName());
        dialog.dismiss();
    }

    @Override
    public void requestData(BaseBean baseBean, Response<BaseBean> response, int level, int recyclerIndex, int startIndex, int reqCount) {

        if (baseBean == null) {
            if (recyclerIndex == -1) {
                mPresenter.getChildLocation("", response, level, recyclerIndex, startIndex, reqCount);
            } else {
                mPresenter.getChildLocation(area.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex, startIndex, reqCount);
            }
        } else {
            mPresenter.getChildLocation(baseBean.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex, startIndex, reqCount);
        }
    }
}
