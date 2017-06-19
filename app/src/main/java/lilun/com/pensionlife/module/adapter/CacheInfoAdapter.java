package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.CacheInfo;

/**
* 
*@author yk
*create at 2017/4/27 17:56
*email : yk_developer@163.com
*/
public class CacheInfoAdapter extends QuickAdapter<CacheInfo> {
    public CacheInfoAdapter(List<CacheInfo> data) {
        super(R.layout.item_cache_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CacheInfo cacheInfo) {
        helper.setText(R.id.first,cacheInfo.getFirst())
                .setText(R.id.second,cacheInfo.getSecond())
                .setText(R.id.third,cacheInfo.getThird())
                .setText(R.id.fourth,cacheInfo.getFourth());
    }
}
