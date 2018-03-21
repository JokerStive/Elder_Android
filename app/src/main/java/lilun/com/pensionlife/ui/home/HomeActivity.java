package lilun.com.pensionlife.ui.home;

import android.content.ContentValues;
import android.view.KeyEvent;

import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.bean.ds_bean.PushMessage;
import lilun.com.pensionlife.ui.activity.activity_detail.ActivityPartnersListFragment;

public class HomeActivity extends BaseActivity {

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
        dbUpgrade();
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

    //版本更新数据库 PushMessage 表新增聊天类别 chatType ，原数据此值由null 设置为 "0"
    public void dbUpgrade(){
        ContentValues values = new ContentValues();
        values.put("chatType", "0");
        DataSupport.updateAllAsync(PushMessage.class, values, "chatType is null")
                .listen(rowsAffected -> {
                    Logger.d("聊天类别null数据更改 生效个数: " + rowsAffected);
                });
    }


}
