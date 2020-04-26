package cn.hll520.lpc.SoftwareTest.uilt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.hll520.lpc.SoftwareTest.R;
import cn.hll520.lpc.SoftwareTest.data.People;

public class PeoAdapter extends BaseAdapter {
    //定义一个context，即活动页面——在那个页面显示，当前的活动/环境
    private Context context;
    private List<People> data;

    public PeoAdapter(Context context, List<People> data) {
        this.context = context;
        this.data = data;
    }

    public void setListData(List<People> data) {
        this.data = data;
    }

    public List<People> getStringList() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return getCount() == 0 ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //定义一个支架
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.peocard, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTv = convertView.findViewById(R.id.name);
            viewHolder.callTv = convertView.findViewById(R.id.call);
            viewHolder.EmailTv = convertView.findViewById(R.id.email);
            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
            viewHolder.other = convertView.findViewById(R.id.peoother);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.other.setVisibility(View.GONE);
        viewHolder.EmailTv.setVisibility(View.VISIBLE);
        viewHolder.checkBox.setVisibility(View.VISIBLE);
        viewHolder.nameTv.setText(data.get(position).getName());
        viewHolder.callTv.setText(data.get(position).getPhone());
        viewHolder.EmailTv.setText(data.get(position).getQQ());
        if (data.get(position).isFlag())
            viewHolder.checkBox.setChecked(true);
        else
            viewHolder.checkBox.setChecked(false);
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    data.get(position).setFlag(true);
                    Log.i("peoo", "YESSSS______" + data.get(position).toString());
                } else {
                    data.get(position).setFlag(false);
                    Log.i("peoo", "NOPP________" + data.get(position).toString());
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView nameTv, callTv, EmailTv;
        CheckBox checkBox;
        LinearLayout other;
    }
}
