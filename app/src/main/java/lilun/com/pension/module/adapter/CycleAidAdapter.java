package lilun.com.pension.module.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.User;
import lilun.com.pension.module.bean.OrganizationAid;

/**
 * Created by Admin on 2017/4/19.
 */
public class CycleAidAdapter extends RecyclerView.Adapter<CycleAidAdapter.MyHolder> {

    private List<OrganizationAid> data;
    public static  int data_limit=5;

    public CycleAidAdapter(List<OrganizationAid> data) {
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aid_cycle, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        OrganizationAid aid;
        if (data.size() <= data_limit) {
            aid = data.get(position);
        } else {
            aid = data.get(position % data.size());
        }

        String creatorId = aid.getCreatorId();
        String creatorName = aid.getCreatorName();
        if (User.getUserId().equals(creatorId)) {
            creatorName = "我";
        }
        holder.tvCreatorName.setText(creatorName);
        holder.tvContent.setText(aid.getTitle());

        holder.tvStatus.setText("发起");

//        aid.get

    }

    @Override
    public int getItemCount() {
        if (data.size() <= data_limit) {
            return data.size();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView tvCreatorName;
        public TextView tvContent;
        public TextView tvStatus;

        public MyHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvCreatorName = (TextView) itemView.findViewById(R.id.tv_creator_name);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
        }
    }
}
