package com.example.runtimepermissiontest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.runtimepermissiontest.MainActivity.dbHelper;
import static com.example.runtimepermissiontest.MainActivity.sqlDB;


public class SecondActivity extends AppCompatActivity  {
    protected TextView tv_add;
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

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int version= sqlDB.getVersion()+1;
                Log.d("version",version+"");
                Log.d("dbHelper2",dbHelper.getDatabaseName());

                dbHelper.onUpgrade( dbHelper.getWritableDatabase(), dbHelper.getWritableDatabase().getVersion(),version);



                for(int i=0;i<adapter.isCheck.size();i++){
                    if(adapter.isCheck.get(i))
                    {
                        MediaInfo mediaInfo= mediaInfoList.get(i);
                        long id = mediaInfo.get_id();
                        String uri=mediaInfo.getUri();
                        String title=mediaInfo.getTitle();
                        String artist = mediaInfo.getArtist();
                        dbHelper.getWritableDatabase().execSQL("INSERT INTO list VALUES(?,?,?,?)", new Object[] {
                                id, uri, title,artist});
                        Log.d("插入成功的歌曲",title);
                    }
                }

            }
        });
    }

}

