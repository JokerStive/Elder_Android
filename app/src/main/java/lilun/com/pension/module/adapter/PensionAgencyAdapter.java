package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationProduct;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class PensionAgencyAdapter extends QuickAdapter<OrganizationProduct> {
    private  BaseFragment fragment;

    public PensionAgencyAdapter(BaseFragment fragment, List<OrganizationProduct> data) {
        super(R.layout.item_agency,data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OrganizationProduct product) {

    }
}
