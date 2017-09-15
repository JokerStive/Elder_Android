package lilun.com.pensionlife.module.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Semester;
import lilun.com.pensionlife.module.utils.UIUtils;

public class SemesterAdapter extends QuickAdapter<Semester> {

    private int margin = UIUtils.dp2px(App.context, 16);

    public SemesterAdapter(List<Semester> data) {
        super(R.layout.item_course_category, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Semester semester) {
        View view = helper.getView(R.id.tv_category_title);
        view.setPadding(0, 0, 0, 0);
        helper.setText(R.id.tv_category_title, semester.getName());

    }
}
