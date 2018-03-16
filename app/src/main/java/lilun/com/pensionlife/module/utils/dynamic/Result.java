package lilun.com.pensionlife.module.utils.dynamic;

import android.view.View;

/**
 * 结果接口，填充extend的每一项
 */
public interface Result {
    String resultKey();
    String resultValue();
    View resultView();
}
