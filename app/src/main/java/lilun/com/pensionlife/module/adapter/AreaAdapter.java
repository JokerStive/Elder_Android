package lilun.com.pensionlife.module.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import lilun.com.pensionlife.R;

/**
 * Created by Admin on 2017/4/17.
 */
public class AreaAdapter extends BaseAdapter {

    private List<String> data;

    public AreaAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        if (convertView==null){
            holder=new MyHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_condition_option,null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_position);
            convertView.setTag(holder);
        }else {
            holder = (MyHolder) convertView.getTag();
        }

        holder.tv.setText(data.get(position));

        return convertView;
    }


    public class MyHolder {
        public TextView tv;

    }
}
