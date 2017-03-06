package lilun.com.pension.app;

import lilun.com.pension.module.bean.OrganizationReply;

/**
*eventBus发送事件统一管理
*@author yk
*create at 2017/2/23 16:55
*email : yk_developer@163.com
*/

public class Event {
    //权限拒绝
    public static  class PermissionDenied{}

    //token失效，跳转登录界面
    public static  class TokenFailure{}

    //刷新邻居互助分类、列表页面数据
    public static  class RefreshHelpData{}


    //刷新邻居互助回答列表
    public static  class RefreshHelpReply{
        public   OrganizationReply reply;
        public RefreshHelpReply(OrganizationReply reply) {
            this.reply = reply;
        }
    }

    //刷新我的订单列表数据
    public static  class RefreshMyOrderData{}
}
