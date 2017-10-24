package lilun.com.pensionlife.module.bean;

/**
*查询用户限制
*@author yk
*create at 2017/10/24 8:56
*email : yk_developer@163.com
*/

public class OrderLimit  {

    /**
     * isLimit : false
     * ordered : false
     */

    private boolean isLimit;
    private boolean ordered;

    public boolean isIsLimit() {
        return isLimit;
    }

    public void setIsLimit(boolean isLimit) {
        this.isLimit = isLimit;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }
}
