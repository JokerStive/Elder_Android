package lilun.com.pensionlife.ui.residential.rank;

import java.util.List;

import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Rank;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 评价P
 *
 * @author yk
 *         create at 2017/2/28 9:35
 *         email : yk_developer@163.com
 */
public class RankListPresenter extends RxPresenter<RankListContract.View> implements RankListContract.Presenter {
    @Override
    public void getRanks(String whatModule, String aidId, int skip) {
        String filter = "{\"order\":\"createdAt DESC\",\"where\":{\"whatModel\":\"" + whatModule + "\",\"whatId\":\"" + aidId + "\"}}";
        addSubscribe(NetHelper.getApi()
                .getRanks(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Rank>>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(List<Rank> ranks) {
                        view.showRanks(ranks, skip != 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }

}
