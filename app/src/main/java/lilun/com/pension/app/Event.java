package lilun.com.pension.app;

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
}
