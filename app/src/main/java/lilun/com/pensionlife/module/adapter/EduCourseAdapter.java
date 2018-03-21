package lilun.com.pensionlife.module.adapter;

import android.text.Html;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 大学-课程adapter
 */
public class EduCourseAdapter extends QuickAdapter<OrganizationProduct> {

    public EduCourseAdapter(List<OrganizationProduct> data) {
        super(R.layout.item_course, data);
    }


    @Override
    protected void convert(BaseViewHolder help, OrganizationProduct course) {
        int totalCount = course.getSold() + course.getStock();
        Double price = course.getPrice();
        String formatPriceToFree = StringUtils.formatPrice(price);
        String topPriceResult = !TextUtils.isEmpty(formatPriceToFree) ? "¥" + formatPriceToFree : StringUtils.formatPriceToFree(price);

        help.setText(R.id.tv_course_title, course.getName())
                .setText(R.id.tv_course_allow_count, "报名人数:" + totalCount)
                .setText(R.id.tv_course_stock, "报名人数:" + course.getStock())
                .setText(R.id.tv_course_price, Html.fromHtml("合计: <font color='#ff4400'>" + topPriceResult + "</font>"));


        String iconUrl = StringUtils.getFirstIcon(course.getImage());
        ImageLoaderUtil.instance().loadImage(iconUrl, help.getView(R.id.iv_course_icon));

    }



}
