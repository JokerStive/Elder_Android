package lilun.com.pensionlife.ui.register;

import com.vanzh.library.BaseBean;
import com.vanzh.library.DataInterface;

import java.util.List;

import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.Area;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/7/18.
 */

public class RegisterInfoPresenter extends RxPresenter<RegisterContract.ViewInfo>
        implements RegisterContract.PresenterInfo {
    @Override
    public void getChildLocation(String locationName, DataInterface.Response<BaseBean> response, int level, int recyclerIndex, int skip, int limitSkip) {
        addSubscribe(NetHelper.getApi()
                .getChildLocation(locationName, skip, limitSkip)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Area>>() {
                    @Override
                    public void _next(List<Area> areas) {
                        view.successOfChildLocation(areas, response, level, recyclerIndex);
                    }
                }));
    }

    @Override
    public void putAccountLocation(String organizationId, String address) {
        addSubscribe(NetHelper.getApi()
                .putAccountLocation(organizationId, address)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Account>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Account account) {
                        view.successOfAccountLocation(account);
                    }
                }));
    }
}
