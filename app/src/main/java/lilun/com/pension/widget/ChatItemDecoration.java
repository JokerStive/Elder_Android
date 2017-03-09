package lilun.com.pension.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import lilun.com.pension.app.App;
import lilun.com.pension.module.utils.UIUtils;

public class ChatItemDecoration extends RecyclerView.ItemDecoration {

    private int itemCount;
    private int space;

    public ChatItemDecoration(int space, int itemCount) {
        this.space = UIUtils.dp2px(App.context, space);
        this.itemCount = itemCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (parent.getAdapter().getItemCount() <= 1) {
            return;
        }

        outRect.top = space;

        if (position == itemCount - 1) {
            outRect.bottom = space * 2;
        }
    }
}