package lilun.com.pensionlife.ui.register;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.QINiuToken;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/4/14.
 */

public class RegisterAvatorPresenter extends RxPresenter<RegisterContract.ViewAvator>
        implements RegisterContract.PresenterAvator {
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
                .getPostFileToken(modelName, modelId, tag)
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
