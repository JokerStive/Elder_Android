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
    protected int visible;  //公开程度(可见度), 0=公开;10=子组织可见;20=特殊子组织可见;30=本组织可见;40=本特殊组织可见,50=内部组织可见，100=秘密

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
