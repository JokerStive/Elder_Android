//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package lilun.com.pensionlife.widget;

import android.content.Context;
import android.util.TypedValue;

public class CardConfig {
    public static int MAX_SHOW_COUNT;
    public static float SCALE_GAP;
    public static int TRANS_Y_GAP;

    public CardConfig() {
    }

    public static void initConfig(Context context) {
        MAX_SHOW_COUNT = 4;
        SCALE_GAP = 0.05F;
        TRANS_Y_GAP = (int)TypedValue.applyDimension(1, 15.0F, context.getResources().getDisplayMetrics());
    }
}
