package lilun.com.pensionlife.ui.home.personal_setting;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.QINiuToken;

/**
 * Created by zp on 2017/11/28.
 */

public class PersonalSettingContract {
    interface View extends IView {

        /**
         * 执行上传图片动作
         * @param qiNiuToken 七牛的上传/更新图片的token
         */
        void uploadImages(QINiuToken qiNiuToken,String fileName);

        /**
         * 更新显示头像
         *
         * @param account  账号信息
         */
        void showNewAvator(Account account);
    }

    interface Persenter extends IPresenter<View> {
        /**
         * 获取个人信息
         */
        void getMe();

        /**
         * 获取七牛上传token
         * uri eg:"{modelName}/{modelId}/upload/{tag}")
         * @param modelName
         * @param modelId
         * @param tag
         */
        void getUploadToken(String modelName, String modelId, String tag);

        /**
         * 获取七牛更新token
         */
        void getUpdateToken(String modelName, String modelId, String tag,String fileName);
    }
}
