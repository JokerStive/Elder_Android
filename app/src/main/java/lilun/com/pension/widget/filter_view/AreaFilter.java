package lilun.com.pension.widget.filter_view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.module.adapter.NormalStringFilterAdapter;

/**
 * 区域选择器
 *
 * @author yk
 *         create at 2017/3/31 14:42
 *         email : yk_developer@163.com
 */
public class AreaFilter extends LinearLayout {
    private String[] firstData = new String[]{"重庆", "成都"};
    private List<List<String>> secondData = new ArrayList<>();
    private NormalStringFilterAdapter adapterFirst;
    private NormalStringFilterAdapter adapterSecond;
    private RecyclerView secondLevelArea;
    private String lastClickFirst;

    public AreaFilter(Context context) {
        super(context);
        init(context);

    }

    private void init(Context context) {

        ArrayList<String> options1 = new ArrayList();
        ArrayList<String> options2 = new ArrayList();
        options1.add("重庆1");
        options1.add("重庆2");
        options2.add("成都1");
        options2.add("成都2");
        secondData.add(options1);
        secondData.add(options2);

        View view = LayoutInflater.from(context).inflate(R.layout.area_selector, this);
        RecyclerView first = (RecyclerView) view.findViewById(R.id.wheel_first);
        secondLevelArea = (RecyclerView) view.findViewById(R.id.wheel_second);
        first.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        secondLevelArea.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        adapterFirst = new NormalStringFilterAdapter(firstData);
        adapterFirst.setOnItemClickListener(new NormalStringFilterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int adapterPosition, String option) {
                if (!TextUtils.isEmpty(lastClickFirst) && TextUtils.equals(lastClickFirst, option)) {
                    secondLevelArea.setVisibility(secondLevelArea.getVisibility() == GONE ? VISIBLE : GONE);
                } else {
                    lastClickFirst = option;
                    initSecond(adapterPosition);
                }
            }
        });
        first.setAdapter(adapterFirst);
    }

    private void initSecond(int position) {
        List<String> strings = secondData.get(position);
        secondLevelArea.setVisibility(VISIBLE);
//        if (adapterSecond == null) {
            adapterSecond = new NormalStringFilterAdapter(strings);
            adapterSecond.setOnItemClickListener(new NormalStringFilterAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int adapterPosition, String option) {

                }
            });
            secondLevelArea.setAdapter(adapterSecond);
//        }

//        else {
//            Logger.d("刷新耳机城市");
//            Logger.d(strings.get(0));
//            adapterSecond.clear();
//        adapterSecond.replaceAll();
//            adapterSecond.addAll(strings);
//
//
//        }

    }

}
