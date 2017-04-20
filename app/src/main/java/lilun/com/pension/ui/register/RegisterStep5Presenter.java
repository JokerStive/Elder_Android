package lilun.com.pension.ui.register;

import android.util.Log;

import java.util.List;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.Area;
import lilun.com.pension.module.bean.Register;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * Created by zp on 2017/4/14.
 */

public class RegisterStep5Presenter extends RxPresenter<RegisterContract.ViewStep5>
        implements RegisterContract.PresenterStep5 {
    @Override
    public void getChildLocation(String locationName) {
        addSubscribe(NetHelper.getApi()
                .getChildLocation(locationName)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Area>>() {
                    @Override
                    public void _next(List<Area> areas) {
                        view.successOfChildLocation(areas);
                    }
                }));
    }

    @Override
    public void commitRegister(String organizationId, String IDCode, String address, Account account) {
        addSubscribe(NetHelper.getApi()
                .commitRegister(organizationId, IDCode, address,account)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Register>() {
                    @Override
                    public void _next(Register register) {
                        Log.d("zp", "提交返回");
                        view.successOfCommitRegister(register);
                    }
                }));
    }
}
