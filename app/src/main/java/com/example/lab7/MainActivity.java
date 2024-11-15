package com.example.lab7;

import static android.os.Looper.myLooper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //建立兩個數值，用於計算兔子跟烏龜的進度
    private int progressRabbit = 0;
    private int progressTurtle = 0;

    private Button btn_start;
    private SeekBar sb_rabbit, sb_turtle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = findViewById(R.id.btn_start);
        sb_rabbit = findViewById(R.id.sb_rabbit);
        sb_turtle = findViewById(R.id.sb_turtle);

        //開始按鈕監聽器
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.setEnabled(false); //進行賽跑後始按鈕不可被操作
                progressRabbit = 0; //初始化兔子的進度
                progressTurtle = 0; //初始化烏龜的進度
                sb_rabbit.setProgress(0);
                sb_turtle.setProgress(0);
                runRabbit(); //執行runRabbit方法
                runTurtle(); //執行runTurtle方法

            }
        });
    }

    //建立Handle物件接收訊息
    private final Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            //判斷編號，並更新SeeBar的進度
            if (msg.what == 1)
                sb_rabbit.setProgress(progressRabbit);
            else if (msg.what == 2)
                sb_turtle.setProgress(progressTurtle);

            //判斷誰抵達終點
            if (progressRabbit >= 100 && progressTurtle < 100) {
                Toast.makeText(MainActivity.this, "兔子勝利", Toast.LENGTH_SHORT).show(); //顯示兔子勝利
                btn_start.setEnabled(true); //按鈕可操作
            } else if (progressTurtle >= 100 && progressRabbit < 100) {
                Toast.makeText(MainActivity.this, "烏龜勝利", Toast.LENGTH_SHORT).show(); //顯示兔子勝利
                btn_start.setEnabled(true); //按鈕可操作

            }
            return false;
        }
    });

    //模擬兔子移動
    private void runRabbit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //兔子有三分之二的機率會偷懶
                boolean[] sleepProbability = {true, true, false};

                while (progressRabbit <= 100 && progressTurtle < 100) {
                    try {
                        Thread.sleep(100); //延遲0.1秒更新實況
                        //隨機產生0~2，0和1為兔子休息，2為移動
                        if (sleepProbability[(int)(Math.random()*3)])
                            Thread.sleep(300); //兔子休息0.3秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressRabbit += 3; //每次跑三步

                    Message msg = new Message(); //建立Message物件
                    msg.what = 1; //加入編號
                    handler.sendMessage(msg); //傳送訊息
                }
            }
        }).start();
    }


    //模擬烏龜移動
    private void runTurtle() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressTurtle <= 100 && progressRabbit < 100) {
                    try {
                        Thread.sleep(100); //延遲0.1秒更新實況
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressTurtle += 1; //每次跑一步

                    Message msg = new Message(); //建立Message物件
                    msg.what = 2; //加入編號
                    handler.sendMessage(msg); //傳送訊息
                }
            }
        }).start();
    }

}