package com.cn.fiveonefive.gphq.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class AndroidTest extends Activity {
    private static final String TAG = "AndroidTest";

    private static LinearLayout ll;
    private Button mBtn;
    private MyView mMyView;
    private boolean mbClickFlg = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);

        // 设置画面布局
        ll = new LinearLayout(this.getBaseContext());
        ll.setOrientation(LinearLayout.VERTICAL);

        // button
        mBtn = new Button(this.getBaseContext());
        mBtn.setText("Draw");
        //
        mBtn.setTextColor(0xff000000);
        mBtn.setTextSize(16);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        lp.topMargin = 5;
        lp.leftMargin = 5;
        ll.addView(mBtn, lp);

        // 设置画图view
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        lp1.weight = 1;
        Rect rect = new Rect(10, 40, 90, 66);
        mMyView = new MyView(this.getBaseContext(), rect);
        mMyView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d(TAG, "mMyView clicked.");
                if (mbClickFlg) {
                    mMyView.setStrText("Click Me");
                } else {
                    mMyView.setStrText("Again...");
                }
                mbClickFlg = !mbClickFlg;
                mMyView.postInvalidate();
            }

        });

        ll.addView(mMyView, lp1);
        setContentView(ll);

        // 设置监听按钮点击事件
        mBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                mMyView.drawLine();
                mMyView.postInvalidate();
            }

        });

    }

    // 自定义画图类，继承自View类
    public class MyView extends View {
        private Paint paint;
        private boolean bDrawLineFlg = false;
        private Rect mRect;
        private String mStrText = "having";

        MyView(Context context) {
            super(context);
            // 生成paint
            paint = new Paint();
        }

        MyView(Context context, Rect rect) {
            super(context);
            // 生成paint
            paint = new Paint();
            mRect = rect;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);

            // 填充整个画布
            canvas.drawColor(Color.GRAY);
//            canvas.drawRGB(255, 255, 255); // 也可以



            // 画图
            if (bDrawLineFlg) {
                Log.d(TAG, "drawing");

                // Draw top 6 lines
                paint.setColor(0xFFFFC0CB);
                canvas.drawLine(mRect.left + 2, mRect.top, mRect.right - 2, mRect.top, paint);
                canvas.drawLine(mRect.left + 1, mRect.top + 1, mRect.right -1, mRect.top + 1, paint);
                for (int i = 2; i < 6; i++) {
                    canvas.drawLine(mRect.left, mRect.top + i, mRect.right, mRect.top + i, paint);
                }

                // Draw middle 14 lines
                paint.setColor(0xFFFFB6C1);
                for (int i = 6; i < 20; i++) {
                    canvas.drawLine(mRect.left, mRect.top + i, mRect.right, mRect.top + i, paint);
                }

                // Draw bottom 6 lines
                paint.setColor(0xFFFFC0CB);
                for (int i = 20; i < 24; i++) {
                    canvas.drawLine(mRect.left, mRect.top + i, mRect.right, mRect.top + i, paint);
                }
                canvas.drawLine(mRect.left + 1, mRect.top + 24, mRect.right - 1, mRect.top + 24, paint);
                canvas.drawLine(mRect.left + 2, mRect.top + 25, mRect.right - 2, mRect.top + 25, paint);

                // draw text
                paint.setColor(0xFF0000FF);
                paint.setTextSize(16);
                paint.setAntiAlias(true); // 消除锯齿
                paint.setFlags(Paint.ANTI_ALIAS_FLAG); // 消除锯齿
                try{
                    float widths[] = new float[mStrText.length()];
                    paint.getTextWidths(mStrText, widths);
                    float textWidth = 0.0f;
                    for(int i = 0; i < widths.length; i++) {
                        textWidth += widths[i];
                        Log.d(TAG, "widths[0] = " + widths[i]);
                    }
                    Log.d(TAG, "textWidth = " + textWidth);
                    float textSize = paint.getTextSize();
                    Log.d(TAG, "textSize = " + textSize);
                    canvas.drawText(mStrText,
                            mRect.left + (mRect.width() - textWidth) / 2,
                            mRect.bottom - (mRect.height() - textSize) / 2 - 3, // add offset to y position
                            paint);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

        }

        public void drawLine() {
            // 设置画线标志
            bDrawLineFlg = !bDrawLineFlg;
            if (bDrawLineFlg) {
                mBtn.setText("Hide");
            } else {
                mBtn.setText("Draw");
            }
            Log.d(TAG, "to be draw");
        }

        public void setStrText(String text) {
            mStrText = text;
        }
    }
}