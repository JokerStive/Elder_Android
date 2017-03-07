package lilun.com.pension.ui.activity.activity_detail;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.OrganizationActivity;

/**
 * Created by zp on 2017/3/6.
 */

public interface ActivityDetailContact {
    interface View extends IView<Presenter>{
        void showActivityDetail(OrganizationActivity activity);
        void completeRefresh();

    };
    interface Presenter extends IPresenter<View> {
        void getActivityDetail(String id);
    }
}
