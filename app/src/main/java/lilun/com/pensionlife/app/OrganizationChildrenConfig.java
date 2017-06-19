package lilun.com.pensionlife.app;

/**
*通过organization的children函数获取列表数据时，根据表不同，需要传递的id也不同
*@author yk
*create at 2017/2/15 14:41
*email : yk_developer@163.com
*/

public class OrganizationChildrenConfig {


    public static  String aid(){
        return checkOrganizationId()+"/#aid";
    }

    public static  String activity(){
        return checkOrganizationId()+"/#activity";
    }

    public static  String activityCategory(){
        return checkOrganizationId()+"/#activity-category";
    }

    public static  String product(){
        return checkOrganizationId()+"/#product";
    }

    public static  String product(String id){
        return id+"/#product";
    }

    public static  String information(){
        return checkOrganizationId()+"/#information";
    }

    public static String checkOrganizationId() {
        return User.getCurrentOrganizationId().equals(User.defOrganizationId)?"":User.getCurrentOrganizationId();
    }
}
