package lilun.com.pensionlife.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.utils.UIUtils;

public class ElderModuleItemDecoration extends RecyclerView.ItemDecoration {

    private int headTopSpace = UIUtils.dp2px(App.context, 10f);
    private int normalTopSpace = UIUtils.dp2px(App.context, 27f);

//    public ElderModuleItemDecoration(int space) {
//        this.space = space;
//    }

    public ElderModuleItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (parent.getAdapter().getItemCount() <= 1) {
            return;
        }

       if (position==1){
           outRect.top=headTopSpace;
       }else if(position>1) {
           outRect.top=normalTopSpace;
       }
    }
}