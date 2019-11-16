package com.example.runtimepermissiontest;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;

import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;




public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    ArrayList<Map<String, Object>> result;
    protected String xunhuanmoshi="顺序播放";
    public static MyDataBaseHelper dbHelper;
    public static SQLiteDatabase sqlDB ;
    protected ListView mListView;
    protected Button mPreviousBtn;
    protected Button mPlayBtn;
    protected Button mNextBtn;
    protected TextView mCurrentTimeTv;
    protected TextView title;
    protected TextView mTotalTimeTv;
    protected SeekBar mSeekBar;
    private List<MediaInfo> mMediaInfoList;

    private MediaPlayer mMediaPlayer;

    // 记录当前播放歌曲的位置
    private int mCurrentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
        Button all=(Button)findViewById(R.id.all);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });
        setDatas();



        mListView.setOnItemClickListener(this);

        final Button button=(Button)findViewById(R.id.xunhuananniu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button.getText().toString().equals("顺序播放"))
                {
                    xunhuanmoshi="单曲循环";
                    button.setText("单曲循环");
                    Toast.makeText(MainActivity.this,"单曲循环"+xunhuanmoshi,Toast.LENGTH_SHORT).show();
                }
                else if(button.getText().toString().equals("单曲循环"))
                {
                    xunhuanmoshi="随机播放";
                    button.setText("随机播放");
                    Toast.makeText(MainActivity.this,"随机播放"+xunhuanmoshi,Toast.LENGTH_SHORT).show();
                }
                else if(button.getText().toString().equals("随机播放"))
                {
                    xunhuanmoshi="顺序播放";
                    button.setText("顺序播放");
                    Toast.makeText(MainActivity.this,"顺序播放"+xunhuanmoshi,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setDatas();

    }

    private void initView() {


        dbHelper = MyDataBaseHelper.getInstance(MainActivity.this);

        sqlDB=dbHelper.getWritableDatabase();
        title = (TextView)findViewById(R.id.title);
        mListView = (ListView) findViewById(R.id.list_view);
        mPreviousBtn = (Button) findViewById(R.id.previous_btn);
        mPreviousBtn.setOnClickListener(MainActivity.this);
        mPlayBtn = (Button) findViewById(R.id.play_btn);
        mPlayBtn.setOnClickListener(MainActivity.this);
        mNextBtn = (Button) findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(MainActivity.this);
        mCurrentTimeTv = (TextView) findViewById(R.id.current_time_tv);
        mTotalTimeTv = (TextView) findViewById(R.id.total_time_tv);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

        // SeekBar绑定监听器，监听拖动到指定位置
        mSeekBar.setOnSeekBarChangeListener(this);

    }

    private void setDatas() {

        String key ="";
        Cursor cursor = sqlDB.rawQuery(
                "SELECT * FROM list" ,
                new String[]{});
        result = new ArrayList<Map<String, Object>>();
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id",cursor.getInt(0));
            map.put("uri", cursor.getString(1));
            map.put("title", cursor.getString(2));
            map.put("artist", cursor.getString(3));
            result.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getApplicationContext(),
                result,
                R.layout.line,
                new String[]{"title"},
                new int[]{R.id.textView1});
        mListView.setAdapter(simpleAdapter);


        for (int i=0;i<result.size();i++) {
            Log.d("1507", "" + result.get(i));
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentPosition = position;
        String s=result.get(position).get("title").toString();
   
        title.setText(s);
        changeMusic(mCurrentPosition);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.previous_btn) {// 上一首
            changeMusic(--mCurrentPosition);
        } else if (view.getId() == R.id.play_btn) {// 播放/暂停

            // 首次点击播放按钮，默认播放第0首
            if (mMediaPlayer == null) {
                changeMusic(0);
            } else {
                playOrPause();
            }

        } else if (view.getId() == R.id.next_btn) {// 下一首
            if(xunhuanmoshi.equals("顺序播放"))
                changeMusic(++mCurrentPosition);
            if(xunhuanmoshi.equals("单曲循环"))
                changeMusic(mCurrentPosition);
            if(xunhuanmoshi.equals("随机播放")) {
                Random r=new Random();
                int i=r.nextInt(result.size());
                changeMusic(i);
            }
        }

    }

    // 播放或暂停
    private void playOrPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
    }

    // 基本数据类型和String在方法调用中传递的是值
    // 其他数据类型在方法调用中传递的是引用

    // 切歌
    private void changeMusic(int position) {
        if (position < 0) {
            mCurrentPosition = position = result.size() - 1;
        } else if (position > result.size() - 1) {
            mCurrentPosition = position = 0;
        }

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            // 绑定播放完毕监听器
            mMediaPlayer.setOnCompletionListener(this);
        }

        try {
            // 切歌之前先重置，释放掉之前的资源
            mMediaPlayer.reset();
            // 设置播放源

            mMediaPlayer.setDataSource(result.get(position).get("uri").toString());
            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mMediaPlayer.prepare();
            // 开始播放
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "播放错误", Toast.LENGTH_SHORT).show();
        }

        // 切歌时重置进度条并展示歌曲时长
        mSeekBar.setProgress(0);
        mSeekBar.setMax(mMediaPlayer.getDuration());
        mTotalTimeTv.setText(parseTime(mMediaPlayer.getDuration()));

        updateProgress();
    }

    // 每间隔1s通知更新进度
    private void updateProgress() {
        // 使用Handler每间隔1s发送一次空消息，通知进度条更新
        Message msg = Message.obtain();// 获取一个现成的消息
        mHandler.sendMessageDelayed(msg, INTERNAL_TIME);
    }

    // 解析时间
    private String parseTime(int oldTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");// 时间格式
        String newTime = sdf.format(new Date(oldTime));
        return newTime;
    }

    private static final int INTERNAL_TIME = 1000;// 音乐进度间隔时间

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    // 当手停止拖拽进度条时执行该方法
    // 获取拖拽进度
    // 将进度对应设置给MediaPlayer
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        mMediaPlayer.seekTo(progress);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(xunhuanmoshi.equals("顺序播放"))
            changeMusic(++mCurrentPosition);
        if(xunhuanmoshi.equals("单曲循环"))
            changeMusic(mCurrentPosition);
        if(xunhuanmoshi.equals("随机播放")) {
            Random r=new Random();
            int i=r.nextInt(result.size());
            changeMusic(i);
        }
    }
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            // 展示给进度条和当前时间
            int progress = mMediaPlayer.getCurrentPosition();
            mSeekBar.setProgress(progress);
            mCurrentTimeTv.setText(parseTime(progress));

            // 继续定时发送数据
            updateProgress();

            return true;
        }
    });

}
