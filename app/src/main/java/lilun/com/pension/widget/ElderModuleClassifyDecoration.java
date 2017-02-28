package lilun.com.pension.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import lilun.com.pension.app.App;
import lilun.com.pension.module.utils.UIUtils;

public class ElderModuleClassifyDecoration extends RecyclerView.ItemDecoration {

    private int space = UIUtils.dp2px(App.context, 0.5f);

    public ElderModuleClassifyDecoration(int space) {
        this.space = UIUtils.dp2px(App.context, space);
    }

    public ElderModuleClassifyDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter().getItemCount() <= 1) {
            return;
        }
        outRect.right=space;
        outRect.bottom = space;
    }
}