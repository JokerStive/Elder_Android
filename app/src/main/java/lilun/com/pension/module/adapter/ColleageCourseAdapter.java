package lilun.com.pension.module.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.EdusColleageCourse;

/**
 * 大学 课程 adapter
 */
public class ColleageCourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private BaseFragment fragment;
    private OnItemClickListener listener;
    List<EdusColleageCourse> data;

    public ColleageCourseAdapter(BaseFragment fragment, List<EdusColleageCourse> data) {
        this.fragment = fragment;
        this.data = data;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.item_colleage_course, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        EdusColleageCourse edusColleageCourse = data.get(position);
        viewHolder.item.setText(edusColleageCourse.getName());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onItemClick(EdusColleageCourse activity);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView item;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.tv_course_name);
        }
    }
}
