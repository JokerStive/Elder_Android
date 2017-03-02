package lilun.com.pension.ui.home;

import java.util.List;

import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 首页P
 *
 * @author yk
 *         create at 2017/2/6 16:23
 *         email : yk_developer@163.com
 */
public class HomePresenter extends RxPresenter<HomeContract.View> implements HomeContract.Presenter {

    private String[] adUrls =
            {"http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png"
            };


    @Override
    public void getInformation() {
        String filter = "{\"where\":{\"organizationId\":\""+ OrganizationChildrenConfig.information()+"\",\"isCat\":\"false\",\"parentId\":{\"like\":\"/#information/公告\"}}}";
        addSubscribe(NetHelper.getApi()
                .getInformations(StringUtils.addFilterWithDef(filter,0))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Information>>() {
                    @Override
                    public void _next(List<Information> information) {
                        view.showInformation(information);
                    }
                }));
    }
}
