package lilun.com.pension.ui.activity.activity_add;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Map;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import me.yokeyword.fragmentation.SupportActivity;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * 新建活动P
 *
 * @author yk
 *         create at 2017/3/13 11:19
 *         email : yk_developer@163.com
 */
public class AddActivityPresenter extends RxPresenter<AddActivityConstract.View> implements AddActivityConstract.Presenter {
    @Override
    public void addActivity(SupportActivity _mActivity, OrganizationActivity activity, ArrayList<String> photoData) {
        Observable<OrganizationActivity> observable = activityObservable(activity, null);
        if (photoData != null) {
            Map<String, RequestBody> requestBodies = BitmapUtils.createRequestBodies(photoData);
            if (requestBodies != null) {
                observable = iconObservable(requestBodies).flatMap(iconModules -> activityObservable(activity, iconModules));
            }
        }
        addSubscribe(observable
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationActivity>(_mActivity) {
                    @Override
                    public void _next(OrganizationActivity activity1) {
                        view.addActivitySuccess();
                    }
                }));
    }


    private Observable<ArrayList<IconModule>> iconObservable(Map<String, RequestBody> requestBodies) {
        return NetHelper.getApi()
                .newActivityIcons(requestBodies)
                .compose(RxUtils.handleResult());
    }

    private Observable<OrganizationActivity> activityObservable(OrganizationActivity activity, ArrayList<IconModule> iconModules) {
        activity.setIcon(iconModules);
        return NetHelper.getApi()
                .newActivity(activity)
                .compose(RxUtils.handleResult());
    }


    @Override
    public String getRepeatType(String[] repeatedTypeArray, String str) {
        String result = "";
        if (TextUtils.isEmpty(str) || repeatedTypeArray == null || repeatedTypeArray.length < 4) {
            return result;
        }
        if (TextUtils.equals(str, repeatedTypeArray[0])) {
            result = "daily";
        } else if (TextUtils.equals(str, repeatedTypeArray[1])) {
            result = "weekly";
        } else if (TextUtils.equals(str, repeatedTypeArray[2])) {
            result = "monthly";
        } else if (TextUtils.equals(str, repeatedTypeArray[3])) {
            result = "yearly";
        }

        return result;
    }

}
