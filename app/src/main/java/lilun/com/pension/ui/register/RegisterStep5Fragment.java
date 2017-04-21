package lilun.com.pension.ui.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.Area;
import lilun.com.pension.module.bean.Register;
import lilun.com.pension.ui.welcome.LoginModule;

/**
 * Created by zp on 2017/4/13.
 */

public class RegisterStep5Fragment extends BaseFragment<RegisterContract.PresenterStep5>
        implements RegisterContract.ViewStep5 {
    RegisterStep6Fragment fragmentStep6 = new RegisterStep6Fragment();
    Account account;
    String IDCode;
    String belongOrganizationId;
    String detailAddress;
    List<ListPopupWindow> mListWindow = new ArrayList<>();
    Area choiceArea, choiceStress;
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
            mListWindow.clear();
            mPresenter.getChildLocation(_mActivity,"");
        } else if (view.getId() == R.id.tv_belong_stress) {
            mPresenter.getChildLocation(_mActivity, choiceArea.getId().replace(getString(R.string.common_address), ""));
        } else if (view.getId() == R.id.fab_go_next) {
            belongOrganizationId = getBelongOrganizationId();
            detailAddress = belongOrganizationId.replace(getString(R.string.common_address), "") + getDetailAddress();
            detailAddress.replace("/", "");
            account.setDefaultOrganizationId(belongOrganizationId);
            mPresenter.commitRegister(_mActivity,belongOrganizationId, IDCode, detailAddress, account);
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
        account = register.getUser();
        LoginModule loginModule = new LoginModule();
        loginModule.putToken(register.getId());
        loginModule.putAccountInfo(account);
        goStep6();
    }

    @Override
    public void successOfChildLocation(List<Area> areas) {

        ListPopupWindow tmp = new ListPopupWindow(getContext());
        tmp.setAdapter(new ListAdapter(getContext(), android.R.layout.simple_list_item_1, areas));
        // tmp.setWidth(UIUtils.dp2px(getContext(), 200));
        tmp.setModal(true);
        mListWindow.add(tmp);
        if (mListWindow.size() == 1) {
            mListWindow.get(0).setAnchorView(llBelongArea);
            mListWindow.get(0).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Area area = (Area) mListWindow.get(0).getListView().getAdapter().getItem(position);
                    mPresenter.getChildLocation(_mActivity, area.getId().replace(getString(R.string.common_address), ""));
                }
            });
            mListWindow.get(0).show();
        } else if (mListWindow.size() == 2) {
            mListWindow.get(0).dismiss();
            mListWindow.get(1).setAnchorView(llBelongArea);
            mListWindow.get(1).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Area area = (Area) mListWindow.get(1).getListView().getAdapter().getItem(position);
                    mPresenter.getChildLocation(_mActivity, area.getId().replace(getString(R.string.common_address), ""));
                }
            });
            mListWindow.get(1).show();
        } else if (mListWindow.size() == 3) {
            mListWindow.get(1).dismiss();
            mListWindow.get(2).setAnchorView(llBelongArea);
            mListWindow.get(2).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Area area = (Area) mListWindow.get(2).getListView().getAdapter().getItem(position);
                    tvBelongArea.setText(area.getName());
                    llBelongStress.setVisibility(View.VISIBLE);
                    choiceArea = area;
                    mListWindow.get(2).dismiss();
                }
            });
            mListWindow.get(2).show();
        } else if (mListWindow.size() == 4) {
            mListWindow.get(3).setAnchorView(llBelongStress);
            mListWindow.get(3).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Area area = (Area) mListWindow.get(3).getListView().getAdapter().getItem(position);
                    tvBelongSteress.setText(area.getName());
                    choiceStress = area;
                    mListWindow.get(3).dismiss();
                }
            });
            mListWindow.get(3).show();
        }
    }

    public String getBelongOrganizationId() {
        return choiceStress == null ? "" : choiceStress.getId();
    }

    public String getDetailAddress() {
        return etBelongCommunite.getText().toString().trim();
    }

    class ListAdapter extends ArrayAdapter<Area> {
        int resource;
        TextView text;

        public ListAdapter(Context context, int resource, List<Area> objects) {
            super(context, resource, objects);
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
                text = (TextView) view.findViewById(android.R.id.text1);
            } else {
                view = convertView;
            }

            final Area item = getItem(position);

            text.setText(item.getName());


            return view;
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
