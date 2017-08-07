package lilun.com.pensionlife.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.utils.mqtt.MqttTopic;
import lilun.com.pensionlife.ui.activity.activity_detail.ActivityPartnersListFragment;
import lilun.com.pensionlife.ui.lbs.AnnounceInfoActivity;
import lilun.com.pensionlife.ui.lbs.UrgentAidInfoActivity;

public class HomeActivity extends BaseActivity {

    private int pushAidInfoCunt = 0;
    private int pushInfoCunt = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initView() {
        replaceLoadRootFragment(R.id.fl_root_container, new HomeFragment(), false);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getTopFragment() instanceof ActivityPartnersListFragment) {
            if (((ActivityPartnersListFragment) getTopFragment()).dealOnBack())
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        App.resetMQTT();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPushMessage(Event.BoardMsg data) {
        showBoardMsg(data.topic, data.data);
    }


    /**
     * 显示紧急求助弹窗
     */
    public void showBoardMsg(String topic, String data) {
        Gson gson = new Gson();
        MqttTopic mqttTopic = new MqttTopic();
        JSONObject jsonObject = JSON.parseObject(data);
        if (topic.equals(mqttTopic.urgent_help)) {
            OrganizationAid aid = new OrganizationAid();
            aid.setAddress(jsonObject.getString("address"));
            aid.setMobile(jsonObject.getString("mobile"));
            aid.setCreatedAt(jsonObject.getString("time"));
            aid.setCreatorName(jsonObject.getString("title"));
            aid.setMemo(jsonObject.getString("location"));
            if (pushAidInfoCunt == 0) {
                pushAidInfoCunt++;
                Intent intent = new Intent(this, UrgentAidInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("aid", aid);
                intent.putExtras(bundle);
                startActivityForResult(intent, 123);
            }
            EventBus.getDefault().post(new Event.RefreshUrgentInfo());

        }


        if (topic.contains(mqttTopic.topic_information_suffix)) {
            String infoString = jsonObject.getString("data");
            Information information = JSON.parseObject(infoString, Information.class);
//             Information Information = gson.fromJson(pushMessage.getData(), Information.class);
            if (pushInfoCunt == 0) {
                pushInfoCunt++;
                Intent intent = new Intent(this, AnnounceInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("organizationInfo", information);
                intent.putExtras(bundle);
                startActivityForResult(intent, 123);
            }
            EventBus.getDefault().post(new Event.RefreshUrgentInfo());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == 0) {
            pushAidInfoCunt = 0;
            pushInfoCunt = 0;
        }
    }
}
