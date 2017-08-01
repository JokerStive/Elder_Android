package lilun.com.pensionlife.ui.home.info_setting;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.SwitchButton;

/**
 * 个人信息设置
 *
 * @author yk
 *         create at 2017/7/31 15:01
 *         email : yk_developer@163.com
 */
public class InfoSettingFragment extends BaseFragment {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.sb_announce)
    SwitchButton sbAnnounce;
    @Bind(R.id.sb_help)
    SwitchButton sbHelp;
    @Bind(R.id.sb_activity)
    SwitchButton sbActivity;
    @Bind(R.id.sb_activity_cards)
    SwitchButton sbActivityCards;
    @Bind(R.id.sb_activity_dance)
    SwitchButton sbActivityDance;
    @Bind(R.id.sb_activity_walk)
    SwitchButton sbActivityWalk;
    @Bind(R.id.sb_activity_ball)
    SwitchButton sbActivityBall;
    @Bind(R.id.sb_activity_tourism)
    SwitchButton sbActivityTourism;
    @Bind(R.id.sb_activity_other)
    SwitchButton sbActivityOther;
    private ArrayList<SwitchButton> switchButtons = new ArrayList<>();

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_info_setting;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        initSwitchButtons();


        titleBar.setOnBackClickListener(this::pop);
        sbAnnounce.setOnCheckedChangeListener((view, isChecked) -> {
            addOrRemoveTopicToSqlite(isChecked, sbAnnounce);
        });

        sbHelp.setOnCheckedChangeListener((view, isChecked) -> {
            addOrRemoveTopicToSqlite(isChecked, sbHelp);
        });


        sbActivity.setOnCheckedChangeListener((view, isChecked) -> {
            if ((isChecked && isAllChildClassifyToggle(!isChecked)) || !isChecked) {
                toggleTogether(isChecked);
            }
            addOrRemoveTopicToSqlite(isChecked, sbActivity);
        });


        initChildClassifySb();

    }

    private void initChildClassifySb() {
        for (SwitchButton switchButton : switchButtons) {
            switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    if (isAllChildClassifyToggle(isChecked)) {
                        sbActivity.setChecked(isChecked);
                    }

                    if (isChecked && !sbActivity.isChecked()) {
                        sbActivity.setChecked(true);
                    }

                    addOrRemoveTopicToSqlite(isChecked, switchButton);
                }
            });
        }
    }

    private void addOrRemoveTopicToSqlite(boolean isChecked, SwitchButton switchButton) {
        String topic = (String) switchButton.getTag();
        if (isChecked) {
            ToastHelper.get().showNormal("添加---" + topic + "---到本地", Toast.LENGTH_SHORT);
        } else {
            ToastHelper.get().showNormal("移除---" + topic + "---从本地", Toast.LENGTH_SHORT);
        }
    }

    private void initSwitchButtons() {
        sbAnnounce.setTag(InfoSettingFilter.announce);
        sbHelp.setTag(InfoSettingFilter.help);
        sbActivityBall.setTag(InfoSettingFilter.ball);
        sbActivityCards.setTag(InfoSettingFilter.cards);
        sbActivityTourism.setTag(InfoSettingFilter.tourism);
        sbActivityWalk.setTag(InfoSettingFilter.walk);
        sbActivityDance.setTag(InfoSettingFilter.dance);
        sbActivityOther.setTag(InfoSettingFilter.other);


        switchButtons.add(sbActivityCards);
        switchButtons.add(sbActivityBall);
        switchButtons.add(sbActivityTourism);
        switchButtons.add(sbActivityWalk);
        switchButtons.add(sbActivityDance);
        switchButtons.add(sbActivityOther);


        initIsCheck();
    }

    /**
     * 从文件读取开关是否开启
     */
    private void initIsCheck() {
        for (SwitchButton switchButton : switchButtons) {
            String tag = (String) switchButton.getTag();
            if (!TextUtils.isEmpty(tag)) {
                boolean infoFilter = InfoSettingFilter.isInfoFilter(tag);
                switchButton.setChecked(infoFilter);
            }
        }

    }


    /**
     * 是否所有的子类别都关了或者开了
     */
    private boolean isAllChildClassifyToggle(boolean isChecked) {
        boolean result = true;
        for (SwitchButton switchButton : switchButtons) {
            boolean checked = switchButton.isChecked();
            if (checked != isChecked) {
                result = false;
                break;
            }
        }
        return result;
    }


    /**
     * 同时开或者关
     */
    private void toggleTogether(boolean isChecked) {
        for (int i = 0; i < switchButtons.size(); i++) {
            SwitchButton switchButton = switchButtons.get(i);
            switchButton.setChecked(isChecked);
        }
    }


}
