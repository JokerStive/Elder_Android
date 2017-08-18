package lilun.com.pensionlife.ui.help;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Rank;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 评价
 *
 * @author yk
 *         create at 2017/2/28 17:14
 *         email : yk_developer@163.com
 */
public class RankFragment extends BaseFragment {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.tv_new_rank)
    TextView tvNewRank;

    @Bind(R.id.rb_ranking)
    RatingBar rbRanking;

    @Bind(R.id.et_rank_content)
    EditText etRankContent;
    private String whatId;
    private String whatModule;

    public static RankFragment newInstance(String whatModule,String whatId) {
        RankFragment fragment = new RankFragment();
        Bundle args = new Bundle();
        args.putString("whatModule", whatModule);
        args.putString("whatId", whatId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        whatId = arguments.getString("whatId");
        whatModule = arguments.getString("whatModule");
        Preconditions.checkNull(whatId);
        Preconditions.checkNull(whatModule);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        tvNewRank.setOnClickListener(v -> {
            newRank();
        });
    }

    private void newRank() {
        if (!TextUtils.isEmpty(etRankContent.getText().toString())){
            String content = etRankContent.getText().toString();
            Rank rank = new Rank();
            rank.setWhatModel(whatModule);
            rank.setWhatId(whatId);
            rank.setDescription(content);
            rank.setRanking((int) rbRanking.getRating());

            NetHelper.getApi().newRank(rank)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<Object>(_mActivity) {
                        @Override
                        public void _next(Object o) {
                            EventBus.getDefault().post(new Event.AfterRank());
//                            if (TextUtils.equals(whatModule, Constants.organizationAid)){
//                            }
                            pop();
                        }
                    });

        }else {
            ToastHelper.get().showShort("说点什么吧");
        }
    }


}
