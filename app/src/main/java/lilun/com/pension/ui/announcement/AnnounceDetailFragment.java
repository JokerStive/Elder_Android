package lilun.com.pension.ui.announcement;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.TextView;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.RxSubscriber;
import rx.Observable;
import rx.Subscriber;

/**
 * 公告详情
 *
 * @author yk
 *         create at 2017/5/9 9:59
 *         email : yk_developer@163.com
 */
public class AnnounceDetailFragment extends BaseFragment {


    private Information information;
    private TextView tvH5;

    public static AnnouncementItemFragment newInstance(Information information) {
        AnnouncementItemFragment fragment = new AnnouncementItemFragment();
        Bundle args = new Bundle();
        args.putSerializable("information", information);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        information = (Information) arguments.getSerializable("information");
        Preconditions.checkNull(information);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_announce_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        tvH5 = (TextView) mRootView.findViewById(R.id.tv_h5);
        showH5();
    }

    private void showH5() {
        String context = information.getContext();
        if (!TextUtils.isEmpty(context)) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    Spanned spanned = Html.fromHtml(context, Html.FROM_HTML_MODE_COMPACT);
                    subscriber.onNext(spanned.toString());
                }
            }).compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<String>(_mActivity) {
                        @Override
                        public void _next(String h5) {
                            tvH5.setText(h5);
                        }
                    });
        }
    }
}
