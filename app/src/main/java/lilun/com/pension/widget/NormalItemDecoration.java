package lilun.com.pension.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import lilun.com.pension.app.App;
import lilun.com.pension.module.utils.UIUtils;

public class NormalItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public NormalItemDecoration(int topButtomSpan) {
        this.space = UIUtils.dp2px(App.context,topButtomSpan);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (parent.getAdapter().getItemCount() <= 1) {
            return;
        }

        outRect.bottom = space;
    }
}