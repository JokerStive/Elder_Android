package lilun.com.pension.app;

import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.OrganizationReply;

/**
 * eventBus发送事件统一管理
 *
 * @author yk
 *         create at 2017/2/23 16:55
 *         email : yk_developer@163.com
 */

public class Event {
    //权限拒绝
    public static class PermissionDenied {
    }

    //token失效，跳转登录界面
    public static class TokenFailure {
    }

    //刷新邻居互助分类、列表页面数据
    public static class RefreshHelpData {
    }

    //刷新互助详情页面
    public static class RefreshHelpDetail {
    }


    //刷新邻居互助回答列表
    public static class RefreshHelpReply {
        public OrganizationReply reply;

        public RefreshHelpReply(OrganizationReply reply) {
            this.reply = reply;
        }
    }

    //刷新我的订单列表数据
    public static class RefreshMyOrderData {
    }


    //刷新我的活动。活动列表数据
    public static class RefreshActivityData {
    }

    //刷新活动回复列表
    public static class RefreshActivityReply {
    }

    //刷新活动详情
    public static class RefreshActivityDetail {
    }


    //刷新消息推送栏
    public static class RefreshPushMessage {

    }


    //切换的当前组织
    public static class ChangedOrganization {
    }


    //刷新商家订单列表
    public static class RefreshMerchantOrder {
    }


    //刷新个人资料
    public static class RefreshContract {
    }



    //刷新紧急消息条数
    public static class RefreshUrgentInfo {
        public RefreshUrgentInfo(OrganizationAid aid) {
            this.aid = aid;
        }

        public OrganizationAid aid;
    }
}
