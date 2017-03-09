package lilun.com.pension.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.QuickAdapter;

/**
 * 展示推送消息的adapter
 *
 * @author yk
 *         create at 2017/2/8 16:31
 *         email : yk_developer@163.com
 */
public class PushInfoAdapter extends QuickAdapter<String> {


    private RecyclerView recyclerView;
    private List<String> data;
    private Context context;
    private onPushClickListener listener;

    public PushInfoAdapter(RecyclerView recyclerView, List<String> data, int id) {
        super(id, data);
        this.recyclerView = recyclerView;
    }



    //
//    @Override
//    protected void convert(RecyclerAdapterHelper helper, String item) {
//        helper.setOnClickListener(R.id.delete,v -> {
//            if (listener!=null){
//                //传viewHolder,实现点击删除拖拽
//                listener.onDeleteClick(item);
////                listener.onDeleteClick(helper.);
//            }
//        });
//        helper.setOnClickListener(R.id.expand,v1 -> {
//            if (listener!=null){
//                listener.onExpandClick();
//            }
//        });
//    }


    @Override
    public void setEmptyView(View emptyView) {
        super.setEmptyView(emptyView);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setOnClickListener(R.id.delete, v -> {
            if (listener != null) {
                //传viewHolder,实现点击删除拖拽
                listener.onDeleteClick(item);
//                listener.onDeleteClick(helper.);
            }
        });
        helper.setOnClickListener(R.id.expand, v1 -> {
            if (listener != null) {
                listener.onExpandClick();
            }
        });
    }

    //    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_push_info, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.btnDelete.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                   listener.onDeleteClick(holder);
//                }
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        public Button btnDelete;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            btnDelete = (Button) itemView.findViewById(R.id.delete);
//
//        }
//
//
//    }


    public void setOnPushClickListener(onPushClickListener listener) {
        this.listener = listener;
    }


    public interface onPushClickListener {
        void onDeleteClick(String item);

        void onItemClick();

        void onExpandClick();
    }
}
