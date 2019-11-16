
package com.example.runtimepermissiontest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapter extends BaseAdapter {
    private List<MediaInfo> list = new ArrayList<MediaInfo>();
    private Context mContext;
    public Map<Integer, Boolean> isCheck = new HashMap<Integer, Boolean>();
    // 构造方法
    public ListAdapter(Context mContext,List list) {
        super();
        this.mContext = mContext;
        this.list=list;
        // 默觉得不选中
        initCheck(false);
    }

    // 初始化map集合
    public void initCheck(boolean flag) {
        // map集合的数量和list的数量是一致的
        for (int i = 0; i < list.size(); i++) {
            // 设置默认的显示
            isCheck.put(i, flag);
        }
    }

    // 设置数据
    public void setData(List<MediaInfo> data) {
        this.list = data;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        // 假设为null就返回一个0
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View view = null;
        // 推断是不是第一次进来
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.tvTitle);
            viewHolder.cbCheckBox = (CheckBox) view.findViewById(R.id.cbCheckBox);
            // 标记，能够复用
            view.setTag(viewHolder);
        } else {
            view = convertView;
            // 直接拿过来用
            viewHolder = (ViewHolder) view.getTag();
        }
        // 拿到对象
        MediaInfo MediaInfo = list.get(position);
        // 填充数据
        viewHolder.title.setText(MediaInfo.getTitle().toString());
        // 勾选框的点击事件
        viewHolder.cbCheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        isCheck.put(position, isChecked);
                    }
                });
        // 设置状态
        if (isCheck.get(position) == null) {
            isCheck.put(position, false);
        }
        viewHolder.cbCheckBox.setChecked(isCheck.get(position));
        return view;
    }

    // 优化
    public static class ViewHolder {
        public TextView title;
        public CheckBox cbCheckBox;
    }

    // 全选button获取状态
    public Map<Integer, Boolean> getMap() {
        // 返回状态
        return isCheck;
    }


}

