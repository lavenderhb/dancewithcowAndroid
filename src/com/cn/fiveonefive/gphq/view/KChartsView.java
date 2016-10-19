package com.cn.fiveonefive.gphq.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.KBean;
import com.cn.fiveonefive.gphq.dto.KDJEntity;
import com.cn.fiveonefive.gphq.dto.MACDEntity;
import com.cn.fiveonefive.gphq.dto.MALineEntity;
import com.cn.fiveonefive.gphq.glob.GlobMethod;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class KChartsView extends GridChart implements GridChart.OnTabClickListener {

    /** 触摸模式 */
    private static int TOUCH_MODE;
    private final static int NONE = 0;
    private final static int DOWN = 1;
    private final static int MOVE = 2;
    //    private final static int ZOOM = 3;
    private final static int LONG = 4;

    /** 默认Y轴字体颜色 **/
    private static final int DEFAULT_AXIS_Y_TITLE_COLOR = Color.WHITE;

    /** 默认X轴字体颜色 **/
    private static final int DEFAULT_AXIS_X_TITLE_COLOR = Color.WHITE;

    /** 显示的最小Candle数 */
    private final static int MIN_CANDLE_NUM = 10;

    /** 默认显示的Candle数 */
    private final static int DEFAULT_CANDLE_NUM = 50;

    /** 最小可识别的移动距离 */
    private final static int MIN_MOVE_DISTANCE = 15;

    /** Candle宽度 */
    private double mCandleWidth;

    /** 触摸点 */
    private float mStartX;
    private float mStartY;

    /** K数据 */
    private List<KBean> kBeanList;

    /** 显示的K数据起始位置 */
    private int mDataStartIndext;

    /** 显示的K数据个数 */
    private int mShowDataNum;

    /** 是否显示蜡烛详情 */
    private boolean showDetails;

    /** 当前数据的最大最小值 */
    private double mMaxPrice;
    private double mMinPrice;

    /** MA数据 */
    private List<MALineEntity> MALineData;

    private String mTabTitle;

    // 下部表的数据
    MACDEntity mMACDData;
    KDJEntity mKDJData;
    //    RSIEntity mRSIData;
    KChange kChange;
    Timer timer;


    public KChartsView(Context context) {
        super(context);
        init();
    }

    public KChartsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KChartsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public interface KChange{
        void change(KBean kBean);
        void changePage(int i);
        void setNull();
    }
    public void setInter(KChange kChange){
        this.kChange=kChange;
    }

    private void init() {
        super.setOnTabClickListener(this);
        mShowDataNum = DEFAULT_CANDLE_NUM;
        mDataStartIndext = 0;
        showDetails = false;
        mMaxPrice = -1;
        mMinPrice = -1;
        mTabTitle = "成交量";
        timer=new Timer();
        kBeanList = new ArrayList<KBean>();
        mMACDData = new MACDEntity(null);
        mKDJData = new KDJEntity(null);
//        mRSIData = new RSIEntity(null);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (kBeanList == null || kBeanList.size() <= 0) {
            return;
        }
        getVolumnData();
        //上部数据
        drawUpperRegion(canvas);
        //下部数据
        drawLowerRegion(canvas);
        //xy轴详情
        drawTitles(canvas);
        //点击详情
        drawCandleDetails(canvas);
    }
    double low0=0.0;
    double high0 = 0.0;
    double rate0 = 0.0;
    private void getVolumnData(){
        float lowertop = LOWER_CHART_TOP + 1;
        float lowerHight = getHeight() - lowertop - 4;
        low0 = kBeanList.get(mDataStartIndext).getVolume();
        high0 = low0;
        rate0 = 0.0;
        for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < kBeanList.size(); i++) {
            low0 = low0 < kBeanList.get(i).getVolume() ? low0 : kBeanList.get(i).getVolume();
            high0 = high0 > kBeanList.get(i).getVolume() ? high0 : kBeanList.get(i).getVolume();
        }
        rate0 = lowerHight / (high0 - low0);
    }


    private void drawCandleDetails(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        textPaint.setColor(Color.WHITE);
        float width = getWidth();

        float lowertop = LOWER_CHART_TOP + 1;
        float lowerHight = getHeight() - lowertop - 4;
        int selectIndext = (int) ((width - 2.0f -touchX ) / mCandleWidth + mDataStartIndext);
        int chooseIndext=(int) ((width - 2.0f - touchX) / mCandleWidth );
        if (chooseIndext>mShowDataNum)
            return;

        if (mTabTitle.trim().equalsIgnoreCase("成交量")){
            canvas.drawText("成交量",
                    2,
                    lowertop+DEFAULT_AXIS_TITLE_SIZE,
                    textPaint);
            String text="";
            if (showDetails){
                text = "" + GlobMethod.changeCJLToNOShou(Double.toString(kBeanList.get(selectIndext).getVolume()));
            }else {
                text = "" + GlobMethod.changeCJLToNOShou(Double.toString(high0));
            }
            canvas.drawText(text,
                    width - 10 - text.length() * DEFAULT_AXIS_TITLE_SIZE / 2.0f,
                    lowertop+DEFAULT_AXIS_TITLE_SIZE,
                    textPaint);
        }

        kChange.setNull();
        if (showDetails) {
            float startX = (float) (width - 3 - mCandleWidth * chooseIndext - (mCandleWidth - 1) / 2);
//            if (selectIndext>=kBeanList.size()){
//                return ;
//            }
            kChange.change(kBeanList.get(selectIndext));

            // 绘制点击线条及详情区域
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.blue0));
            paint.setAlpha(240);
            paint.setStrokeWidth(2);
            canvas.drawLine(startX, 2.0f + DEFAULT_AXIS_TITLE_SIZE, startX, UPER_CHART_BOTTOM,
                    paint);
            canvas.drawLine(startX, getHeight() - 2.0f, startX, LOWER_CHART_TOP, paint);
            paint.setColor(Color.RED);
            //todo
            double rate = (getUperChartHeight() - 2) / (mMaxPrice - mMinPrice);
            float drawY=(float) ((mMaxPrice - kBeanList.get(selectIndext).getOpen()) * rate+DEFAULT_AXIS_TITLE_SIZE + 4);
            canvas.drawLine(1.0f,drawY,width-1.0f,drawY,paint);

        }

    }

    private void drawTitles(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(DEFAULT_AXIS_Y_TITLE_COLOR);
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        // Y轴Titles
        canvas.drawText(new DecimalFormat("#.##").format(mMinPrice), 1, UPER_CHART_BOTTOM - 1,
                textPaint);
        canvas.drawText(new DecimalFormat("#.##").format(mMinPrice + (mMaxPrice - mMinPrice) / 4),
                1, UPER_CHART_BOTTOM - getLatitudeSpacing() - 1, textPaint);
        canvas.drawText(
                new DecimalFormat("#.##").format(mMinPrice + (mMaxPrice - mMinPrice) / 4 * 2), 1,
                UPER_CHART_BOTTOM - getLatitudeSpacing() * 2 - 1, textPaint);
        canvas.drawText(
                new DecimalFormat("#.##").format(mMinPrice + (mMaxPrice - mMinPrice) / 4 * 3), 1,
                UPER_CHART_BOTTOM - getLatitudeSpacing() * 3 - 1, textPaint);
        canvas.drawText(new DecimalFormat("#.##").format(mMaxPrice), 1,
                DEFAULT_AXIS_TITLE_SIZE * 2, textPaint);

        // X轴Titles
        textPaint.setColor(DEFAULT_AXIS_X_TITLE_COLOR);
        canvas.drawText(GlobMethod.changeDataToData(kBeanList.get(mDataStartIndext).getDate()), getWidth() - 14 - 4.5f
                * DEFAULT_AXIS_TITLE_SIZE, UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE, textPaint);
        try {
            canvas.drawText(
                    String.valueOf(GlobMethod.changeDataToData(kBeanList.get(mDataStartIndext + mShowDataNum / 2).getDate())),
                    getWidth() / 2 - 2.25f * DEFAULT_AXIS_TITLE_SIZE, UPER_CHART_BOTTOM
                            + DEFAULT_AXIS_TITLE_SIZE, textPaint);
            canvas.drawText(
                    String.valueOf(GlobMethod.changeDataToData(kBeanList.get(mDataStartIndext + mShowDataNum - 1).getDate())),
                    2, UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE, textPaint);
        } catch (Exception e) {

        }
    }

    private void drawUpperRegion(Canvas canvas) {
        // 绘制蜡烛图
        Paint redPaint = new Paint();
        redPaint.setColor(getResources().getColor(R.color.red0));
        Paint greenPaint = new Paint();
        greenPaint.setColor(getResources().getColor(R.color.blue0));
        int width = getWidth();
        mCandleWidth = (width - 4) / 10.0 * 10.0 / mShowDataNum;
        double rate = (getUperChartHeight() - 2) / (mMaxPrice - mMinPrice);
        for (int i = 0; i < mShowDataNum && mDataStartIndext + i < kBeanList.size(); i++) {
            KBean entity = kBeanList.get(mDataStartIndext + i);
            float open = (float) ((mMaxPrice - entity.getOpen()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
            float close = (float) ((mMaxPrice - entity.getClose()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
            float high = (float) ((mMaxPrice - entity.getHigh()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
            float low = (float) ((mMaxPrice - entity.getLow()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);

            float left = (float) (width - 2 - mCandleWidth * (i + 1));
            float right = (float) (width - 3 - mCandleWidth * i);
            float startX = (float) (width - 3 - mCandleWidth * i - (mCandleWidth - 1) / 2);
            if (open < close) {
                canvas.drawRect(left, open, right, close, greenPaint);
                canvas.drawLine(startX, high, startX, low, greenPaint);
            } else if (open == close) {
                canvas.drawLine(left, open, right, open, redPaint);
                canvas.drawLine(startX, high, startX, low, redPaint);
            } else {
                canvas.drawRect(left, close, right, open, redPaint);
                canvas.drawLine(startX, high, startX, low, redPaint);
            }
        }

        // 绘制上部曲线图及上部分MA值
        float MATitleWidth = width / 10.0f * 10.0f / MALineData.size();
        for (int j = 0; j < MALineData.size(); j++) {
            MALineEntity lineEntity = MALineData.get(j);

            float startX = 0;
            float startY = 0;
            Paint paint = new Paint();
            paint.setStrokeWidth((float) 2.0);
            paint.setColor(lineEntity.getLineColor());
            paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            if (showDetails){
                int selectIndext = (int) ((width - 2.0f -touchX ) / mCandleWidth + mDataStartIndext);
                canvas.drawText(
                        lineEntity.getTitle()
                                + "="
                                + new DecimalFormat("#.##").format(lineEntity.getLineData().get(
                                selectIndext)), 7 + MATitleWidth * j,
                        DEFAULT_AXIS_TITLE_SIZE, paint);
            }else {
                int index=lineEntity.getLineData().size();
                int indexNow=mDataStartIndext+mShowDataNum;
                indexNow=indexNow>index?index:indexNow;
                canvas.drawText(
                        lineEntity.getTitle()
                                + "="
                                + new DecimalFormat("#.##").format(lineEntity.getLineData().get(
                                indexNow-1)), 7 + MATitleWidth * j,
                        DEFAULT_AXIS_TITLE_SIZE, paint);
            }
            for (int i = 0; i < mShowDataNum
                    && mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
                if (i != 0) {
                    canvas.drawLine(
                            startX,
                            startY + DEFAULT_AXIS_TITLE_SIZE + 4,
                            (float) (width - 2 - mCandleWidth * i - mCandleWidth * 0.5f),
                            (float) ((mMaxPrice - lineEntity.getLineData()
                                    .get(mDataStartIndext + i)) * rate + DEFAULT_AXIS_TITLE_SIZE + 4),
                            paint);
                }
                startX = (float) (width - 2 - mCandleWidth * i - mCandleWidth * 0.5f);
                startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate);
            }
        }
    }

    private void drawLowerRegion(Canvas canvas) {
        float lowertop = LOWER_CHART_TOP + 1;
        float lowerHight = getHeight() - lowertop - 4;
        float viewWidth = getWidth();
        float lowerBootom=lowertop+lowerHight;

        // 下部表的数据
        // MACDData mMACDData;
        // KDJData mKDJData;
        // RSIData mRSIData;
        Paint whitePaint = new Paint();
        whitePaint.setStrokeWidth((float) 2.0);
        whitePaint.setColor(Color.WHITE);
        Paint yellowPaint = new Paint();
        yellowPaint.setStrokeWidth((float) 2.0);
        yellowPaint.setColor(getResources().getColor(R.color.dark_yellow));
        Paint magentaPaint = new Paint();
        magentaPaint.setStrokeWidth((float) 2.0);
        magentaPaint.setColor(Color.MAGENTA);

        Paint textPaint = new Paint();
        textPaint.setColor(DEFAULT_AXIS_Y_TITLE_COLOR);
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        //draw volume
        if (mTabTitle.trim().equalsIgnoreCase("成交量")){

            Paint redPaint = new Paint();
            redPaint.setColor(getResources().getColor(R.color.red0));
            Paint greenPaint = new Paint();
            greenPaint.setColor(getResources().getColor(R.color.blue0));
            float zero0 = (float) (high0 * rate0) + lowertop;
            if (zero0 < lowertop) {
                zero0 = lowertop;
            }

            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i <  kBeanList.size(); i++) {
                float top = (float) ((high0 - kBeanList.get(i).getVolume()) * rate0) + lowertop;

                double open = kBeanList.get(i).getOpen();
                double close = kBeanList.get(i).getClose();

                if (open < close) {
                    canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
                            * (i + 1 - mDataStartIndext), top, viewWidth - 2
                            - (float) mCandleWidth * (i - mDataStartIndext), lowerBootom, redPaint);
                } else if (open == close) {

                    canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
                            * (i + 1 - mDataStartIndext), top, viewWidth - 2
                            - (float) mCandleWidth * (i - mDataStartIndext), lowerBootom, greenPaint);
                } else {

                    canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
                            * (i + 1 - mDataStartIndext), top, viewWidth - 2
                            - (float) mCandleWidth * (i - mDataStartIndext), lowerBootom-1, greenPaint);
                }
            }

        }

        else if (mTabTitle.trim().equalsIgnoreCase("MACD")) {
            List<Double> MACD = mMACDData.getMACD();
            List<Double> DEA = mMACDData.getDEA();
            List<Double> DIF = mMACDData.getDIF();

            double low = DEA.get(mDataStartIndext);
            double high = low;
            double rate = 0.0;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < MACD.size(); i++) {
                low = low < MACD.get(i) ? low : MACD.get(i);
                low = low < DEA.get(i) ? low : DEA.get(i);
                low = low < DIF.get(i) ? low : DIF.get(i);

                high = high > MACD.get(i) ? high : MACD.get(i);
                high = high > DEA.get(i) ? high : DEA.get(i);
                high = high > DIF.get(i) ? high : DIF.get(i);
            }
            rate = lowerHight / (high - low);

            Paint paint = new Paint();
            float zero = (float) (high * rate) + lowertop;
            if (zero < lowertop) {
                zero = lowertop;
            }
            // 绘制双线
            float dea = 0.0f;
            float dif = 0.0f;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < MACD.size(); i++) {
                // 绘制矩形
                if (MACD.get(i) >= 0.0) {
                    paint.setColor(getResources().getColor(R.color.red0));
                    float top = (float) ((high - MACD.get(i)) * rate) + lowertop;
                    if (zero - top < 0.55f) {
                        canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
                                * (i + 1 - mDataStartIndext), zero, viewWidth - 2
                                - (float) mCandleWidth * (i - mDataStartIndext), zero, paint);
                    } else {
                        canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
                                * (i + 1 - mDataStartIndext), top, viewWidth - 2
                                - (float) mCandleWidth * (i - mDataStartIndext), zero, paint);
                    }

                } else {
                    paint.setColor(getResources().getColor(R.color.blue0));
                    float bottom = (float) ((high - MACD.get(i)) * rate) + lowertop;

                    if (bottom - zero < 0.55f) {
                        canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
                                * (i + 1 - mDataStartIndext), zero, viewWidth - 2
                                - (float) mCandleWidth * (i - mDataStartIndext), zero, paint);
                    } else {
                        canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
                                * (i + 1 - mDataStartIndext), zero, viewWidth - 2
                                - (float) mCandleWidth * (i - mDataStartIndext), bottom, paint);
                    }
                }

                if (i != mDataStartIndext) {
                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
                                    * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
                            (float) ((high - DEA.get(i)) * rate) + lowertop, viewWidth - 2
                                    - (float) mCandleWidth * (i - mDataStartIndext)
                                    + (float) mCandleWidth / 2, dea, whitePaint);

                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
                                    * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
                            (float) ((high - DIF.get(i)) * rate) + lowertop, viewWidth - 2
                                    - (float) mCandleWidth * (i - mDataStartIndext)
                                    + (float) mCandleWidth / 2, dif, yellowPaint);
                }
                dea = (float) ((high - DEA.get(i)) * rate) + lowertop;
                dif = (float) ((high - DIF.get(i)) * rate) + lowertop;
            }
//9-29
//            canvas.drawText(new DecimalFormat("#.##").format(high), 2, lowertop
//                    , textPaint);
////            canvas.drawText(new DecimalFormat("#.##").format((high + low) / 2), 2, lowertop
////                    + lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
////            canvas.drawText(new DecimalFormat("#.##").format(low), 2, lowertop + lowerHight,
////                    textPaint);
//
//            canvas.drawText(new DecimalFormat("#.##").format((low) ), 2, lowertop
//                    + lowerHight / 2 -5, textPaint);

            canvas.drawText(new DecimalFormat("#.##").format(high), 2, lowertop
                    + DEFAULT_AXIS_TITLE_SIZE - 2, textPaint);
            canvas.drawText(new DecimalFormat("#.##").format((high + low) / 2), 2, lowertop
                    + lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
            canvas.drawText(new DecimalFormat("#.##").format(low), 2, lowertop + lowerHight,
                    textPaint);


        } else if (mTabTitle.trim().equalsIgnoreCase("KDJ")) {
            List<Double> Ks = mKDJData.getK();
            List<Double> Ds = mKDJData.getD();
            List<Double> Js = mKDJData.getJ();

            double low = Ks.get(mDataStartIndext);
            double high = low;
            double rate = 0.0;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < Ks.size(); i++) {
                low = low < Ks.get(i) ? low : Ks.get(i);
                low = low < Ds.get(i) ? low : Ds.get(i);
                low = low < Js.get(i) ? low : Js.get(i);

                high = high > Ks.get(i) ? high : Ks.get(i);
                high = high > Ds.get(i) ? high : Ds.get(i);
                high = high > Js.get(i) ? high : Js.get(i);
            }
            rate = lowerHight / (high - low);

            // 绘制白、黄、紫线
            float k = 0.0f;
            float d = 0.0f;
            float j = 0.0f;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < Ks.size(); i++) {

                if (i != mDataStartIndext) {
                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
                                    * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
                            (float) ((high - Ks.get(i)) * rate) + lowertop, viewWidth - 2
                                    - (float) mCandleWidth * (i - mDataStartIndext)
                                    + (float) mCandleWidth / 2, k, whitePaint);

                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
                                    * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
                            (float) ((high - Ds.get(i)) * rate) + lowertop, viewWidth - 2
                                    - (float) mCandleWidth * (i - mDataStartIndext)
                                    + (float) mCandleWidth / 2, d, yellowPaint);

                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
                                    * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
                            (float) ((high - Js.get(i)) * rate) + lowertop, viewWidth - 2
                                    - (float) mCandleWidth * (i - mDataStartIndext)
                                    + (float) mCandleWidth / 2, j, magentaPaint);
                }
                k = (float) ((high - Ks.get(i)) * rate) + lowertop;
                d = (float) ((high - Ds.get(i)) * rate) + lowertop;
                j = (float) ((high - Js.get(i)) * rate) + lowertop;
            }
//9-29
//            canvas.drawText(new DecimalFormat("#.##").format(high), 2, lowertop
//                    - 2, textPaint);
////            canvas.drawText(new DecimalFormat("#.##").format((high + low) / 2), 2, lowertop
////                    + lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
////            canvas.drawText(new DecimalFormat("#.##").format(low), 2, lowertop + lowerHight,
////                    textPaint);
//
//            canvas.drawText(new DecimalFormat("#.##").format(low), 2, lowertop
//                    + lowerHight / 2 -5, textPaint);


            canvas.drawText(new DecimalFormat("#.##").format(high), 2, lowertop
                    + DEFAULT_AXIS_TITLE_SIZE - 2, textPaint);
            canvas.drawText(new DecimalFormat("#.##").format((high + low) / 2), 2, lowertop
                    + lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
            canvas.drawText(new DecimalFormat("#.##").format(low), 2, lowertop + lowerHight,
                    textPaint);

        } else if (mTabTitle.trim().equalsIgnoreCase("RSI")) {

        }



    }
    private float touchX;
    private float oldX=-1;
    private float mStartTime;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    postInvalidate();
                    break;
                case 1:
                    postInvalidate();
                    break;
                default:
                    break;
            }
        }
    };
    class MyTask extends TimerTask{

        @Override
        public void run() {
            showDetails=false;
            Message message=new Message();
            message.what=1;
            handler.sendMessage(message);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);

        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        float x = event.getRawX();
        float y = event.getRawY();

        if (y <= LOWER_CHART_TOP + rect.top + 2
                && y >= UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE + rect.top) {
            if (mTabWidth <= 0) {
                return true;
            }
            int indext = (int) (x / mTabWidth);

            if (mTabIndex != indext) {
                mTabIndex = indext;
                mOnTabClickListener.onTabClick(mTabIndex);
            }
            return true;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 设置触摸模式
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                touchX = event.getRawX();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (TOUCH_MODE==DOWN){
                            TOUCH_MODE=LONG;
                            int selectIndext = (int) ((getWidth() - 2.0f - touchX) /
                                    mCandleWidth + mDataStartIndext);
                            if (oldX==selectIndext){
                                return;
                            }else {
                                oldX=selectIndext;
                                showDetails=true;
                                Message message=new Message();
                                message.what=0;
                                handler.sendMessage(message);
                            }
                            if (timer!=null){
                                timer.cancel();
                            }
                        }else {
                            showDetails=false;
                        }
                    }
                }, 1000);
                if (TOUCH_MODE==NONE){
                    TOUCH_MODE = DOWN;
                    mStartTime=event.getEventTime();
                    showDetails=false;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (TOUCH_MODE==LONG){
                    timer=new Timer();
                    timer.schedule(new MyTask(),10*1000);
                }
                if (TOUCH_MODE==MOVE){
                    float nowX=event.getRawX();
                    float nowY=event.getRawY();
                    if ((nowX-mStartX)>MIN_MOVE_DISTANCE){
                        if (event.getEventTime()-mStartTime<MIN_MOVE_timespace) {
                            kChange.changePage(0);
                        }
                    }
                }
                TOUCH_MODE=NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (TOUCH_MODE==DOWN){
                    float x0=event.getRawX();
                    if (Math.abs(x0-mStartX)<MIN_MOVE_pace)
                        break;
                    TOUCH_MODE=MOVE;
                    showDetails=false;
                    break;
                }else if (TOUCH_MODE==LONG){
                    touchX = event.getRawX();
                    if (touchX < 2 || touchX > getWidth() - 2) {
                        break;
                    }
                    int selectIndext = (int) ((getWidth() - 2.0f - touchX) /
                            mCandleWidth + mDataStartIndext);
                    if (oldX==selectIndext){
                        break;
                    }else {
                        oldX=selectIndext;
                        showDetails=true;
                        postInvalidate();
                        break;
                    }
                }
            case MotionEvent.ACTION_OUTSIDE:
                showDetails = false;
                postInvalidate();
                break;
        }
        return true;
    }

    private void setCurrentData() {
        if (mShowDataNum > kBeanList.size()) {
            mShowDataNum = kBeanList.size();
        }
        if (MIN_CANDLE_NUM > kBeanList.size()) {
            mShowDataNum = MIN_CANDLE_NUM;
        }

        if (mShowDataNum > kBeanList.size()) {
            mDataStartIndext = 0;
        } else if (mShowDataNum + mDataStartIndext > kBeanList.size()) {
            mDataStartIndext = kBeanList.size() - mShowDataNum;
        }
        mMinPrice = kBeanList.get(mDataStartIndext).getLow();
        mMaxPrice = kBeanList.get(mDataStartIndext).getHigh();
        for (int i = mDataStartIndext + 1; i < kBeanList.size()
                && i < mShowDataNum + mDataStartIndext; i++) {
            KBean entity = kBeanList.get(i);
            mMinPrice = mMinPrice < entity.getLow() ? mMinPrice : entity.getLow();
            mMaxPrice = mMaxPrice > entity.getHigh() ? mMaxPrice : entity.getHigh();
        }

        for (MALineEntity lineEntity : MALineData) {
            for (int i = mDataStartIndext; i < lineEntity.getLineData().size()
                    && i < mShowDataNum + mDataStartIndext; i++) {
                mMinPrice = mMinPrice < lineEntity.getLineData().get(i) ? mMinPrice : lineEntity
                        .getLineData().get(i);
                mMaxPrice = mMaxPrice > lineEntity.getLineData().get(i) ? mMaxPrice : lineEntity
                        .getLineData().get(i);
            }
        }

    }

    public void right(){
        if (kBeanList.size()<=0)
            return;
        mDataStartIndext=mDataStartIndext-mShowDataNum;
        if (mDataStartIndext < 0) {
            mDataStartIndext = 0;
        }
        setCurrentData();
        postInvalidate();
    }

    public void left(){
        if (kBeanList.size()<=0)
            return;
        mDataStartIndext=mDataStartIndext+mShowDataNum;
        while (mDataStartIndext > kBeanList.size()-mShowDataNum) {
            mDataStartIndext--;
        }
        setCurrentData();
        postInvalidate();
    }

    public void up() {
        if (kBeanList.size()<=0)
            return;
        mShowDataNum=mShowDataNum*2;
        if (mShowDataNum > kBeanList.size()) {
            mShowDataNum = MIN_CANDLE_NUM > kBeanList.size() ? MIN_CANDLE_NUM : kBeanList.size();
        }
        setCurrentData();
        postInvalidate();
    }

    public void down() {
        if (kBeanList.size()<=0)
            return;
        mShowDataNum= (int) Math.ceil(mShowDataNum/2);
        if (mShowDataNum < MIN_CANDLE_NUM) {
            mShowDataNum = MIN_CANDLE_NUM;
        }
        setCurrentData();
        postInvalidate();
    }


    private void setTouchMode(MotionEvent event) {
//        float daltX = Math.abs(event.getRawX() - mStartX);
//        float daltY = Math.abs(event.getRawY() - mStartY);
//        if (Math.sqrt(daltX * daltX + daltY * daltY) > MIN_MOVE_DISTANCE) {
//            if (daltX < daltY) {
//                TOUCH_MODE = ZOOM;
//            } else {
//                TOUCH_MODE = MOVE;
//            }
//            mStartX = event.getRawX();
//            mStartY = event.getRawY();
//        }
    }

    /**
     * 初始化MA值，从数组的最后一个数据开始初始化
     *
     * @param entityList
     * @param days
     * @return
     */
    private List<Float> initMA(List<KBean> entityList, int days) {
        if (days < 2 || entityList == null || entityList.size() <= 0) {
            return null;
        }
        List<Float> MAValues = new ArrayList<Float>();

        float sum = 0;
        float avg = 0;
        for (int i = entityList.size() - 1; i >= 0; i--) {
            float close = (float) entityList.get(i).getClose();
            if (i > entityList.size() - days) {
                sum = sum + close;
                avg = sum / (entityList.size() - i);
            } else {
                sum = close + avg * (days - 1);
                avg = sum / days;
            }
            MAValues.add(avg);
        }

        List<Float> result = new ArrayList<Float>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }
    private List<Float> initMAnew(List<KBean> entityList, int days){
        if (days < 2 || entityList == null || entityList.size() <= 0) {
            return null;
        }
        List<Float> MAValues = new ArrayList<Float>();
        float sum = 0;
        float avg = 0;
        for (int i=0;i<entityList.size();i++){
            float close=(float) entityList.get(i).getClose();
            if (i<=days){
                sum=sum+close;
                avg=sum/(i+1);
            }else {
//				for (int j=i-days;j<=i;j++){
//					avg+=(float) entityList.get(j).getClose();
//				}
//				avg=avg/days;
                sum=close+avg*(days-1);
                avg=sum/days;
            }
            MAValues.add(avg);
        }
        return MAValues;
    }

    //    public List<Float> MaValues1(List<KBean> entityList,int days){
//        List<Float> MAValues = new ArrayList<Float>();
//        for (int i=0;i<entityList.size();i++){
//            MAValues.add(calcMA(entityList.get(i),days,i));
//        }
//        return MAValues;
//    }
    private  List<Float> calcMA(List<KBean> entityList, int days) {
        List<Float> MAValues = new ArrayList<Float>();
        float sum = 0;
        float avg = 0;
        for (int i=0;i<entityList.size();i++){
            float close=(float) entityList.get(i).getClose();
            if (i <days) {
                sum = sum + close;
                avg = sum / (i + 1f);
            } else {
                sum = close+avg*(days-1);
                avg = sum / days;
            }
            MAValues.add(avg);
        }


        return MAValues;
//        if (days == 5) {
//            kChartDayBean.setMaValue5(avg);
//        } else if (days == 10) {
//            kChartDayBean.setMaValue10(avg);
//        } else if (days == 20) {
//            kChartDayBean.setMaValue20(avg);
//        }
    }

    public List<KBean> getKData() {
        return kBeanList;
    }

    public void setKData(List<KBean> kBeans) {
        if (kBeans == null || kBeans.size() <= 0) {
            return;
        }
        this.kBeanList = kBeans;
        initMALineData();
        mMACDData = new MACDEntity(kBeanList);
        mKDJData = new KDJEntity(kBeanList);
//        mRSIData = new RSIEntity(kBeanList);

        setCurrentData();
        postInvalidate();
    }

    private void initMALineData() {
        MALineEntity MA5 = new MALineEntity();
        MA5.setTitle("MA5");
        MA5.setLineColor(Color.WHITE);
        MA5.setLineData(initMA(kBeanList, 5));
//        List<Float> ss=initMAnew(kBeanList,5);
//        List<Float> dd=initMA(kBeanList,5);
//        for (int i=0;i<ss.size();i++){
//            Log.i("AAAAAAAAAA", ss.get(i)+"");
//            Log.i("BBBBBBBBBB", dd.get(i)+"");
//            Log.i("sssss", kBeanList.get(i).getDate());
//        }


        MALineEntity MA10 = new MALineEntity();
        MA10.setTitle("MA10");
        MA10.setLineColor(getResources().getColor(R.color.dark_yellow));
        MA10.setLineData(initMA(kBeanList, 10));

        MALineEntity MA20 = new MALineEntity();
        MA20.setTitle("MA20");
        MA20.setLineColor(getResources().getColor(R.color.holo_blue_light));
        MA20.setLineData(initMA(kBeanList, 20));

        MALineEntity MA30 = new MALineEntity();
        MA30.setTitle("MA30");
        MA30.setLineColor(getResources().getColor(R.color.pupl));
        MA30.setLineData(initMA(kBeanList, 30));

        MALineData = new ArrayList<MALineEntity>();
        MALineData.add(MA5);
        MALineData.add(MA10);
        MALineData.add(MA20);
        MALineData.add(MA30);

    }
    public void setTitle(String str){
        mTabTitle = str;
        postInvalidate();
    }
    public void onTabClick(int indext) {
        String[] titles = getLowerChartTabTitles();
        mTabTitle = titles[indext];
        postInvalidate();
    }
}
