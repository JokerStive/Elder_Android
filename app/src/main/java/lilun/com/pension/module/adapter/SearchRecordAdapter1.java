package lilun.com.pension.module.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;

/**
 * Created by Admin on 2017/3/29.
 */
public class SearchRecordAdapter1 extends BaseAdapter {

    private  List<String> data;

    public SearchRecordAdapter1(List<String>  records) {
        this.data = records;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(App.context).inflate(R.layout.item_search_record, null);
        TextView tvRecord = (TextView) view.findViewById(R.id.tv_record);
        tvRecord.setText(data.get(position));
        return view;
    }
}
