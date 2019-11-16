package com.example.runtimepermissiontest;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity  {
    protected TextView tv_add;
    protected Button btn_detele;
    protected Button btn_select_all;
    protected ListView listview;
    private List<MediaInfo> mediaInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_second);
        initView();


    }
    private List<MediaInfo> getDatas() {
        List<MediaInfo> list = new ArrayList<>();

        // 使用内容解析者访问系统提供的数据库
        Cursor cursor = getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);// 默认排序顺序
        // 如果游标读取时还有下一个数据，读取

        int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);//获取列名对应的索引
        int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);// 标题
        int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);// 艺术家
        int uriIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);// 文件路径

        while (cursor.moveToNext()) {
            // 根据索引值获取对应列名中的数值
            long _id = cursor.getLong(idIndex);
            String title = cursor.getString(titleIndex);
            String artist = cursor.getString(artistIndex);
            String uri = cursor.getString(uriIndex);

            MediaInfo mediaInfo = new MediaInfo(_id, uri, title, artist);

            list.add(mediaInfo);
        }

        for (MediaInfo mediaInfo : list) {
            Log.d("1507", "" + mediaInfo.toString());
        }
        return list;
    }
    private void initView() {
        tv_add = (TextView) findViewById(R.id.tv_add);

        btn_select_all = (Button) findViewById(R.id.btn_select_all);

        listview = (ListView) findViewById(R.id.listview);
        mediaInfoList = getDatas();
        final ListAdapter adapter = new ListAdapter(this, mediaInfoList);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("状态", adapter.isCheck.toString());

            }

        });
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("状态", adapter.isCheck.toString());
            }
        });
    }

}

