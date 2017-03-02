package lilun.com.pension.ui.help;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Rank;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.widget.NormalTitleBar;

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
                            pop();
                        }
                    });

        }else {
            ToastHelper.get().showShort("说点什么吧");
        }
    }


}
