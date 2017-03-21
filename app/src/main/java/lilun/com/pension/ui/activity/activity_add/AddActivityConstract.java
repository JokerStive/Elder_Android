package lilun.com.pension.ui.activity.activity_add;

import java.util.ArrayList;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.OrganizationActivity;

/**
 * 新建活动C
 *
 * @author yk
 *         create at 2017/3/13 11:15
 *         email : yk_developer@163.com
 */
public interface AddActivityConstract {

    interface View extends IView<Presenter> {
        void addActivitySuccess();

    }


    interface Presenter extends IPresenter<View> {
        void addActivity(OrganizationActivity  activity,ArrayList<String> photoData);
        String getRepeatType(String[] repeatedTypeArray,String str);
    }

}
