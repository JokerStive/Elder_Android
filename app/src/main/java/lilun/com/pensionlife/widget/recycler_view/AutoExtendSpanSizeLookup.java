package lilun.com.pensionlife.widget.recycler_view;

import android.support.v7.widget.GridLayoutManager;

/**
 * recyclerView的layoutManager自动填充
 *
 * @author yk
 *         create at 2017/4/1 13:10
 *         email : yk_developer@163.com
 */
public class AutoExtendSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private int spanCount;
    private int dataSize;
    private int remainder;

    public AutoExtendSpanSizeLookup(int dataSize, int spanCount) {
        this.dataSize = dataSize;
        this.spanCount = spanCount;
        remainder = dataSize%spanCount;
        if (remainder==dataSize){
            remainder =  dataSize%(spanCount/2);
        }
    }



    @Override
    public int getSpanSize(int position) {
//        Logger.d("余数 = "+remainder+"---"+"当前position = "+position);
        int result = 2;
        if (remainder!=0 && position>=dataSize-remainder){
            result =  spanCount/remainder;
        }
        return result;
    }
}
