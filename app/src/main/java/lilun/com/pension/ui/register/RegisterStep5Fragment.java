package lilun.com.pension.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vanzh.library.BaseBean;
import com.vanzh.library.BottomDialog;
import com.vanzh.library.DataInterface;
import com.vanzh.library.OnAddressSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.Area;
import lilun.com.pension.module.bean.Register;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.ui.welcome.LoginModule;
import lilun.com.pension.widget.NormalDialog;

/**
 * Created by zp on 2017/4/13.
 */

public class RegisterStep5Fragment extends BaseFragment<RegisterContract.PresenterStep5>
        implements RegisterContract.ViewStep5, DataInterface<BaseBean>, OnAddressSelectedListener {
    final int RECYCLERLEVEL = 3;
    RegisterStep6Fragment fragmentStep6 = new RegisterStep6Fragment();
    Account account;
    String IDCode;
    String belongOrganizationId;
    String detailAddress;
    BottomDialog dialog;
    int curLevel = -1;

    BaseBean area, distrect;
    @Bind(R.id.ll_belong_stress)
    LinearLayout llBelongStress;
    @Bind(R.id.ll_belong_area)
    LinearLayout llBelongArea;
    @Bind(R.id.tv_belong_area)
    TextView tvBelongArea;
    @Bind(R.id.tv_belong_stress)
    TextView tvBelongSteress;

    @Bind(R.id.et_belong_communite)
    EditText etBelongCommunite;

    @OnClick({R.id.tv_belong_area, R.id.tv_belong_stress, R.id.fab_go_next})
    public void onClick(View view) {
        if (view.getId() == R.id.tv_belong_area) {
            dialog = new BottomDialog(this, -1);
            dialog.setOnAddressSelectedListener(this);
            dialog.show();
        } else if (view.getId() == R.id.tv_belong_stress) {
            if (curLevel < RECYCLERLEVEL - 1) {
                ToastHelper.get(getContext()).showWareShort("该地区未开通服务，请重新选择");
                return;
            }
            dialog = new BottomDialog(this, RECYCLERLEVEL);
            dialog.setOnAddressSelectedListener(this);
            dialog.show();
        } else if (view.getId() == R.id.fab_go_next) {
            if (curLevel < RECYCLERLEVEL) {
                ToastHelper.get(getContext()).showWareShort("该地区未开通服务，请重新选择");
                return;
            }
            belongOrganizationId = getBelongOrganizationId();
            detailAddress = belongOrganizationId.replace(getString(R.string.common_address), "") + getDetailAddress();
            detailAddress.replace("/", "");
            account.setDefaultOrganizationId(belongOrganizationId);
            mPresenter.commitRegister(_mActivity, belongOrganizationId, IDCode, detailAddress, account);
        }
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        account = (Account) arguments.getSerializable("account");
        IDCode = arguments.getString("IDCode");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterStep5Presenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_step4;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        llBelongStress.setVisibility(View.GONE);
    }


    @Override
    public void successOfCommitRegister(Register register) {
        Account retAccount = register.getUser();
        retAccount.setPassword(account.getPassword());
        account = retAccount;
        LoginModule loginModule = new LoginModule();
        loginModule.putToken(register.getId());
        loginModule.putAccountInfo(account);
        new NormalDialog().createShowMessage(_mActivity, getString(R.string.register_success), () -> {
            goStep6();
        });
    }

    @Override
    public void successOfChildLocation(List<Area> areas, Response<BaseBean> response, int level, int recyclerIndex) {
        ArrayList<BaseBean> data = new ArrayList<>();
        for (int i = 0; i < areas.size(); i++) {
            data.add(new BaseBean(areas.get(i).getId(), areas.get(i).getName()));
        }
        if (level == recyclerIndex || level == RECYCLERLEVEL) {
            response.send(level, null);
            return;
        }

        response.send(level, data);
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
           // dialog.dismiss();
            return;
        }
        if (recyclerIndex != RECYCLERLEVEL) {
            area = baseBeen[baseBeen.length - 1];
            tvBelongArea.setText(area.getName());
            llBelongStress.setVisibility(View.VISIBLE);
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
    public void requestData(BaseBean baseBean, Response<BaseBean> response, int level, int recyclerIndex) {

        if (baseBean == null) {
            if (recyclerIndex == -1) {
                mPresenter.getChildLocation(_mActivity, "", response, level, recyclerIndex);
            } else {
                mPresenter.getChildLocation(_mActivity, area.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex);
            }
        } else {
            mPresenter.getChildLocation(_mActivity, baseBean.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex);
        }
    }

    private void goStep6() {
        ((RegisterActivity) _mActivity).setTitle();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", account);
        fragmentStep6.setArguments(bundle);
        _mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentStep6)
                .setCustomAnimations(R.anim.pop_container_in, R.anim.pop_container_out)
                .addToBackStack("")
                .commit();

    }
}
