package lilun.com.pensionlife.ui.home.personal_setting;

import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.QINiuToken;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/11/28.
 */

public class PersonalSettingPersenter extends RxPresenter<PersonalSettingContract.View>
        implements PersonalSettingContract.Persenter {

    @Override
    public void getMe() {
        NetHelper.getApi()
                .getMe()
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Account>() {
                    @Override
                    public void _next(Account account) {
                       view.showNewAvator(account);
                    }
                });
    }

    /**
     * 获取上传图片token
     *
     * @param modelName
     * @param modelId
     * @param tag
     */
    @Override
    public void getUploadToken(String modelName, String modelId, String tag) {
        NetHelper.getApi()
                .getUploadToken(modelName, modelId, tag)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<QINiuToken>() {
                    @Override
                    public void _next(QINiuToken qiNiuToken) {
                        view.uploadImages(qiNiuToken);
                    }
                });
    }

    /**
     * 获取更新图片token
     *
     * @param modelName
     * @param modelId
     * @param tag
     */
    @Override
    public void getUpdateToken(String modelName, String modelId, String tag) {
        NetHelper.getApi()
                .getUpdateToken(modelName, modelId, tag)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<QINiuToken>() {
                    @Override
                    public void _next(QINiuToken qiNiuToken) {
                        view.uploadImages(qiNiuToken);
                    }
                });
    }
}
