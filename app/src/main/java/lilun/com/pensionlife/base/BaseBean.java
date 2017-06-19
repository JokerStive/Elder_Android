package lilun.com.pensionlife.base;

import java.io.Serializable;

/**
*基类模型
*@author yk
*create at 2017/2/13 10:50
*email : yk_developer@163.com
*/
public class BaseBean  implements Serializable{
    private static final long serialVersionUID = 1L;
    protected int visible;

    public boolean isSelected() {
        return isSelected;
    }

    public BaseBean setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }

    protected boolean isSelected;

    public int getVisible() {
        return visible;
    }

    public BaseBean setVisible(int visible) {
        this.visible = visible;
        return this;
    }
}
