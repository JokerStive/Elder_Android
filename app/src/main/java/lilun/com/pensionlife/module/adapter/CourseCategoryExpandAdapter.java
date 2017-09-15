package lilun.com.pensionlife.module.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.utils.UIUtils;

public class CourseCategoryExpandAdapter extends BaseMultiItemQuickAdapter<OrganizationProductCategory, BaseViewHolder> {

    private int margin = UIUtils.dp2px(App.context, 16);

    public CourseCategoryExpandAdapter(List<OrganizationProductCategory> data) {
        super(data);
        addItemType(0, R.layout.item_course_category);
        addItemType(1, R.layout.item_course_category);
        addItemType(2, R.layout.item_course_category);
        addItemType(3, R.layout.item_course_category);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrganizationProductCategory category) {

        int level = category.getLevel();
        View view = helper.getView(R.id.tv_category_title);
        view.setPadding(0, 0, 0, 0);
        view.setPadding(margin * (level + 1), 0, 0, 0);

        helper.setText(R.id.tv_category_title, category.getTitle());

    }
}
