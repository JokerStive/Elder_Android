package lilun.com.pension.ui.agency.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;

/**
 * 新增预约信息列表V
 *
 * @author yk
 *         create at 2017/3/29 18:47
 *         email : yk_developer@163.com
 */
public class AddServiceInfoFragment extends BaseFragment {

    @Bind(R.id.et_occupant_name)
    EditText etOccupantName;

    @Bind(R.id.tv_sex)
    TextView tvSex;

    @Bind(R.id.tv_birthday)
    TextView tvBirthday;

    @Bind(R.id.tv_health_status)
    TextView tvHealthStatus;

    @Bind(R.id.tv_relation)
    TextView tvRelation;

    @Bind(R.id.et_reservation_name)
    EditText etReservationName;

    @Bind(R.id.et_reservation_phone)
    EditText etReservationPhone;

    @Bind(R.id.tv_check_in_time)
    TextView tvCheckInTime;

    @Bind(R.id.btn_confirm)
    Button btnConfirm;

    public static AddServiceInfoFragment newInstance() {
        AddServiceInfoFragment fragment = new AddServiceInfoFragment();
        return fragment;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_reservation_info;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
