package com.cn.fiveonefive.gphq.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.TimeDataBeanChild;
import com.cn.fiveonefive.gphq.dto.TimeViewDto;
import com.cn.fiveonefive.gphq.glob.GlobMethod;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TimesView extends GridChart {
    private final int DATA_MAX_COUNT = 4 * 60;

    private float uperBottom;
    private float uperHeight;
    private float lowerBottom;
    private float lowerHeight;
    private float dataSpacing;

    private float uperHalfHigh;
    private long lowerHigh;
    private float uperRate;
    private float lowerRate;

    private boolean showDetails;
    private float touchX;
//    private float touchY;

    private TimeDataBeanChild timeDataBeanChild;
    private List<TimeViewDto> listTimeViewDto;
    ITimeChange iTimeChange;
    public interface ITimeChange{
        void change(TimeViewDto timeViewDto,TimeDataBeanChild timeDataBeanChild);
        void changePage(int i);
    }
    public void setInter(ITimeChange iTimeChange){
        this.iTimeChange=iTimeChange;
    }

    public TimesView(Context context) {
        super(context);
        init();
    }

    public TimesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    private void init() {
        super.setShowLowerChartTabs(false);
        super.setShowTopTitles(false);
        listTimeViewDto=new ArrayList<TimeViewDto>();


        uperBottom = 0;
        uperHeight = 0;
        lowerBottom = 0;
        lowerHeight = 0;
        dataSpacing = 0;

        uperHalfHigh = 0;
        lowerHigh = 0;
        uperRate = 0;
        lowerRate = 0;
        showDetails = false;
        touchX = 0;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (listTimeViewDto == null || listTimeViewDto.size() <= 0) {
            return;
        }
        uperBottom = UPER_CHART_BOTTOM - 2;
        uperHeight = getUperChartHeight() - 4;
        lowerBottom = getHeight() - 3;
        lowerHeight = getLowerChartHeight() - 2;
        dataSpacing = (getWidth() - 4) * 10.0f / 10.0f / DATA_MAX_COUNT;

        if (uperHalfHigh > 0) {
            uperRate = uperHeight / (uperHalfHigh *2.0f);
        }
        if (lowerHigh > 0) {
            lowerRate = lowerHeight / lowerHigh;
        }

        // 绘制上部曲线及下部线条
        drawLines(canvas);

        // 绘制坐标标题
        drawTitles(canvas);

        // 绘制点击时的详细信息
        drawDetails(canvas);
    }

    private void drawDetails(Canvas canvas) {
        float width = getWidth();
        Paint textPaint = new Paint();
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        textPaint.setColor(Color.WHITE);
        String lowHigh=GlobMethod.changeCJLToNOShou(String.valueOf(lowerHigh));

        int selectIndex=(int) ((touchX - 2) / dataSpacing);
        if (selectIndex>=listTimeViewDto.size()-1) {
            selectIndex = listTimeViewDto.size() - 1;
            touchX=selectIndex*dataSpacing+2;
        }else if (selectIndex<0){
            selectIndex = 0;
        }
        TimeViewDto timeViewDto=listTimeViewDto.get(selectIndex);
        if (!showDetails){
            String text=""+lowHigh;
            canvas.drawText(text,
                    width - 10 - text.length() * DEFAULT_AXIS_TITLE_SIZE / 2.0f,
                    lowerBottom-lowerHeight+DEFAULT_AXIS_TITLE_SIZE,
                    textPaint);
            canvas.drawText("成交量",
                    2,
                    lowerBottom-lowerHeight+DEFAULT_AXIS_TITLE_SIZE,
                    textPaint);
        }
        if (showDetails){
            iTimeChange.change(timeViewDto,timeDataBeanChild);
            String text0=lowHigh+"/"+GlobMethod.changeCJLToNOShou(String.valueOf(timeViewDto.getDoneNum()));
            canvas.drawText(text0,
                    width - 10 - text0.length() * DEFAULT_AXIS_TITLE_SIZE / 2.0f,
                    lowerBottom-lowerHeight+DEFAULT_AXIS_TITLE_SIZE,
                    textPaint);
            canvas.drawText("成交量",
                    2,
                    lowerBottom-lowerHeight+DEFAULT_AXIS_TITLE_SIZE,
                    textPaint);


//			float left = 5.0f;
//			float top = 4.0f;
//			float right = 3.0f + 10 * DEFAULT_AXIS_TITLE_SIZE;
//			float bottom = 7.0f + 4 * DEFAULT_AXIS_TITLE_SIZE;
//			if (touchX < width / 2.0f) {
//				right = width - 4.0f-30.0f*3;
//				left = width - 4.0f - 10 * DEFAULT_AXIS_TITLE_SIZE-30.0f*3;
//			}
            //todo
//			if (Integer.parseInt(listTimeViewDto.get(listTimeViewDto.size()-1).getTime())<1500){
//				if(touchX>3+(listTimeViewDto.size()-1)*dataSpacing){
//					touchX=3+(listTimeViewDto.size()-1)*dataSpacing;
//				}
//			}

            // 绘制点击线条及详情区域
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.blue0));
            paint.setAlpha(240);
            paint.setStrokeWidth(2);
            canvas.drawLine(touchX, 2.0f, touchX, UPER_CHART_BOTTOM, paint);
            canvas.drawLine(touchX, lowerBottom - lowerHeight, touchX, lowerBottom, paint);

            paint.setColor(Color.RED);

            canvas.drawLine(1.0f,
                    (float)(uperBottom/2.0- (Float.parseFloat(timeViewDto.getPrice())- Float.parseFloat(timeDataBeanChild.getYestclose()))*uperRate),
                    width-1.0f,
                    (float)(uperBottom/2.0- (Float.parseFloat(timeViewDto.getPrice())- Float.parseFloat(timeDataBeanChild.getYestclose()))*uperRate),
                    paint);

//            paint.setColor(getResources().getColor(R.color.gray2));
//			canvas.drawRect(left,top,right,bottom,paint);
//
//			Paint borderPaint = new Paint();
//			borderPaint.setColor(Color.WHITE);
//			borderPaint.setStrokeWidth(2);
//			canvas.drawLine(left, top, left, bottom, borderPaint);
//			canvas.drawLine(left, top, right, top, borderPaint);
//			canvas.drawLine(right, bottom, right, top, borderPaint);
//			canvas.drawLine(right, bottom, left, bottom, borderPaint);
//
//			// 绘制详情文字
//
//
//			try {
//
//				iTimeChange.change(timeViewDto,timeDataBeanChild);
//
//				canvas.drawText("时间: " + timeViewDto.getTime(), left + 1, top
//						+ DEFAULT_AXIS_TITLE_SIZE, textPaint);
//
//				canvas.drawText("价格:", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 2.0f, textPaint);
//				double price = Float.parseFloat(timeViewDto.getPrice());
//				if (price >= Float.parseFloat(timeDataBeanChild.getYestclose())) {
//					textPaint.setColor(Color.RED);
//				} else {
//					textPaint.setColor(Color.GREEN);
//				}
//				canvas.drawText(new DecimalFormat("#.##").format(price), left + 1
//								+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + DEFAULT_AXIS_TITLE_SIZE * 2.0f,
//						textPaint);
//
//				textPaint.setColor(Color.WHITE);
//				canvas.drawText("涨跌:", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 3.0f, textPaint);
//				double change = ( Float.parseFloat(timeViewDto.getPrice()) - Float.parseFloat(timeDataBeanChild.getYestclose()))
//						/ Float.parseFloat(timeDataBeanChild.getYestclose());
//				if (change >= 0) {
//					textPaint.setColor(Color.RED);
//				} else {
//					textPaint.setColor(Color.GREEN);
//				}
//				canvas.drawText(new DecimalFormat("#.##%").format(change), left + 1
//								+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + DEFAULT_AXIS_TITLE_SIZE * 3.0f,
//						textPaint);
//
//				textPaint.setColor(Color.WHITE);
//				canvas.drawText("成交:", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 4.0f, textPaint);
//				textPaint.setColor(Color.YELLOW);
//				String cje=timeViewDto.getDoneNum()+"";
//
//				canvas.drawText(GlobMethod.changeCJEToStr(cje), left + 1
//								+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + DEFAULT_AXIS_TITLE_SIZE * 4.0f,
//						textPaint);
//
//			} catch (Exception e) {
//				canvas.drawText("时间: --", left + 1, top + DEFAULT_AXIS_TITLE_SIZE, textPaint);
//				canvas.drawText("价格: --", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 2.0f, textPaint);
//				canvas.drawText("涨跌: --", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 3.0f, textPaint);
//				canvas.drawText("成交: --", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 4.0f, textPaint);
//			}
        }

    }

    private void drawTitles(Canvas canvas) {
        // 绘制Y轴titles
        float viewWidth = getWidth();
        Paint paint = new Paint();
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        paint.setColor(Color.GREEN);
        canvas.drawText(new DecimalFormat("#.##").format(Float.parseFloat(timeDataBeanChild.getYestclose())-uperHalfHigh), 2,
                uperBottom, paint);
        String text = new DecimalFormat("#.##%").format(-uperHalfHigh / Float.parseFloat(timeDataBeanChild.getYestclose())	);
        canvas.drawText(text, viewWidth - 5 - text.length() * DEFAULT_AXIS_TITLE_SIZE / 2.0f,
                uperBottom, paint);

        canvas.drawText(
                new DecimalFormat("#.##").format(Float.parseFloat(timeDataBeanChild.getYestclose()) - uperHalfHigh/2.0 ), 2,
                uperBottom - getLatitudeSpacing(), paint);
        text = new DecimalFormat("#.##%").format(-uperHalfHigh/2.0 / Float.parseFloat(timeDataBeanChild.getYestclose()));
        canvas.drawText(text, viewWidth - 5 - text.length() * DEFAULT_AXIS_TITLE_SIZE / 2.0f,
                uperBottom - getLatitudeSpacing(), paint);

        paint.setColor(Color.WHITE);
        canvas.drawText(new DecimalFormat("#.##").format(Float.parseFloat(timeDataBeanChild.getYestclose())), 2, uperBottom
                - getLatitudeSpacing() * 2, paint);
        text = "0.00%";
        canvas.drawText(text, viewWidth - 6 - text.length() * DEFAULT_AXIS_TITLE_SIZE / 2.0f,
                uperBottom - getLatitudeSpacing() * 2, paint);

        paint.setColor(Color.RED);
        canvas.drawText(
                new DecimalFormat("#.##").format(Float.parseFloat(timeDataBeanChild.getYestclose())+uperHalfHigh/2.0), 2,
                uperBottom - getLatitudeSpacing() * 3, paint);
        text = new DecimalFormat("#.##%").format(uperHalfHigh * 0.5f / Float.parseFloat(timeDataBeanChild.getYestclose()));
        canvas.drawText(text, viewWidth - 6 - text.length() * DEFAULT_AXIS_TITLE_SIZE / 2.0f,
                uperBottom - getLatitudeSpacing() * 3, paint);

        canvas.drawText(new DecimalFormat("#.##").format(uperHalfHigh + Float.parseFloat(timeDataBeanChild.getYestclose())), 2,
                DEFAULT_AXIS_TITLE_SIZE, paint);
        text = new DecimalFormat("#.##%").format(uperHalfHigh / Float.parseFloat(timeDataBeanChild.getYestclose()));
        canvas.drawText(text, viewWidth - 6 - text.length() * DEFAULT_AXIS_TITLE_SIZE / 2.0f,
                DEFAULT_AXIS_TITLE_SIZE, paint);

        // 绘制X轴Titles
        canvas.drawText("09:30", 2, uperBottom + DEFAULT_AXIS_TITLE_SIZE, paint);
        canvas.drawText("11:30/13:00", viewWidth / 2.0f - DEFAULT_AXIS_TITLE_SIZE * 2.5f,
                uperBottom + DEFAULT_AXIS_TITLE_SIZE, paint);
        canvas.drawText("15:00", viewWidth - 2 - DEFAULT_AXIS_TITLE_SIZE * 2.5f, uperBottom
                + DEFAULT_AXIS_TITLE_SIZE, paint);
    }

    private void drawLines(Canvas canvas) {
        float x = 0;
        float uperWhiteY = 0;
        float uperYellowY = 0;
        float oldPrice= Float.parseFloat(timeDataBeanChild.getYestclose());
        Paint paint = new Paint();
        paint.setStrokeWidth((float) 2.0);
        for (int i = 0; i < listTimeViewDto.size() && i < DATA_MAX_COUNT; i++) {
            TimeViewDto timeViewDto=listTimeViewDto.get(i);

            // 绘制上部表中曲线

            float endPrice =  (float)  (uperBottom/2.0- (Float.parseFloat(timeViewDto.getPrice())- Float.parseFloat(timeDataBeanChild.getYestclose()))*uperRate);
            float endAPrice =(float) (uperBottom/2.0-(Float.parseFloat(timeViewDto.getaPrice())- Float.parseFloat(timeDataBeanChild.getYestclose()))*uperRate);
//			float endPrice = (float) (uperBottom - (Float.parseFloat(timeViewDto.getPrice())
//					+ uperHalfHigh )
//					* uperRate);
//			float endAPrice = (float) (uperBottom - (Float.parseFloat(timeViewDto.getaPrice()) + uperHalfHigh)
//					* uperRate);

            if (i != 0) {

                paint.setColor(getResources().getColor(R.color.dark_yellow));
                canvas.drawLine(x, uperYellowY, 3 + dataSpacing * i, endAPrice, paint);
                paint.setColor(Color.WHITE);
                canvas.drawLine(x, uperWhiteY, 3 + dataSpacing * i, endPrice, paint);
            }

            x = 3 + dataSpacing * i;
            uperWhiteY = endPrice;
            uperYellowY = endAPrice;

            // 绘制下部表内数据线
            float price= Float.parseFloat(timeViewDto.getPrice());

            float buy = timeViewDto.getDoneNum();
            if (i==0){
                if (price- Float.parseFloat(timeDataBeanChild.getYestclose())>0){
                    paint.setColor(Color.RED);
                }else
                    paint.setColor(Color.GREEN);
                oldPrice=price;
            }else {
                if (price>oldPrice){
                    paint.setColor(Color.RED);
                }else{
                    paint.setColor(Color.GREEN);
                }
                oldPrice=price;
            }
            canvas.drawLine(x, lowerBottom, x, lowerBottom - buy * lowerRate, paint);
        }

    }

    /** 触摸模式 */
    private static int TOUCH_MODE=0;
    private final static int NONE = 0;
    private final static int DOWN = 1;
    private final static int MOVE = 2;
    private final static int LONG = 3;
    private float mStartX;
    private float mStartY;
    private float mStartTime;
    private final static int MIN_MOVE_DISTANCE = 15;

    //    Runnable runnable=new Runnable() {
//        @Override
//        public void run() {
//            if (TOUCH_MODE==DOWN){
//                TOUCH_MODE=LONG;
//                showDetails=true;
//                postInvalidate();
//
//            }
//        }
//    };
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    postInvalidate();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                touchX = event.getRawX();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (TOUCH_MODE==DOWN){
                            TOUCH_MODE=LONG;
                            showDetails=true;
                            Message message=new Message();
                            message.what=0;
                            handler.sendMessage(message);
                        }else {
                            showDetails=false;
                        }
                    }
                }, 1000);
//                if (TOUCH_MODE==DOWN){
//                    long nowTime=event.getEventTime();
//                    if (nowTime-mStartTime<500){
//                        break;
//                    }else{
//                        showDetails=true;
//                        TOUCH_MODE=LONG;
//                        postInvalidate();
//                        break;
//                    }
//                }else
                if (TOUCH_MODE==NONE){
                    TOUCH_MODE = DOWN;
                    mStartTime=event.getEventTime();
                    showDetails=false;

                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (TOUCH_MODE==DOWN){
                    float x0=event.getRawX();
                    if (Math.abs(x0-mStartX)<MIN_MOVE_pace)
                        break;
                    TOUCH_MODE=MOVE;
                    showDetails=false;
//                    return false;
                    return super.onTouchEvent(event);
                }else if (TOUCH_MODE==LONG){
                    touchX = event.getRawX();
                    if (touchX < 2 || touchX > getWidth() - 2) {
//                        return false;
                        return super.onTouchEvent(event);
                    }
//                    touchY = event.getY();
                    showDetails=true;
                    postInvalidate();
                    return true;
                }else if (TOUCH_MODE==MOVE){
//                    return false;
                }
//                touchY = event.getY();
//                showDetails = true;
//                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (TOUCH_MODE==MOVE){
                    float nowX=event.getRawX();
                    float nowY=event.getRawY();
                    if ((mStartX-nowX)>MIN_MOVE_DISTANCE){
                        if (event.getEventTime()-mStartTime<MIN_MOVE_timespace){
                            iTimeChange.changePage(1);
                        }
                    }
                }
                TOUCH_MODE=NONE;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                showDetails = false;
                postInvalidate();
                break;

            default:
                break;
        }

        return true;
    }

    public void resetData(){
        timeDataBeanChild=null;
        this.listTimeViewDto.clear();

    }
    public  void setTimeDataBean(TimeDataBeanChild timeDataBeanChild){
        this.timeDataBeanChild=timeDataBeanChild;

        List datas=timeDataBeanChild.getData();
        for(int i=0;i<datas.size();i++){
            String data=datas.get(i).toString();
            int indexBegin=data.indexOf("[");
            int indexEnd=data.indexOf("]");
            data=data.substring(indexBegin+1,indexEnd);
            String[] timeData=data.split(",");
            TimeViewDto timeViewDto=new TimeViewDto();
            timeViewDto.setTime(timeData[0]);
            timeViewDto.setPrice(timeData[1]);
            timeViewDto.setaPrice(timeData[2]);
            String done=GlobMethod.changeXXEXXToNormal(timeData[3].replace(" ",""));
            long x=Long.parseLong(done);
            timeViewDto.setDoneNum(x);

            listTimeViewDto.add(timeViewDto);
            lowerHigh=lowerHigh>timeViewDto.getDoneNum()
                    ?lowerHigh: timeViewDto.getDoneNum();
        }
        float up= Float.parseFloat(timeDataBeanChild.getHigh())- Float.parseFloat(timeDataBeanChild.getYestclose());
        float down= Float.parseFloat(timeDataBeanChild.getYestclose())- Float.parseFloat(timeDataBeanChild.getLow());
        uperHalfHigh=up>down?up:down;

        postInvalidate();

    }


}
