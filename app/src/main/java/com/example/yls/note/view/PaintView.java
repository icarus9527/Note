package com.example.yls.note.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by icarus9527 on 2017/4/22.
 */

public class PaintView extends View {

    private Context context;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;// 画布的画笔
    private Paint mPaint;// 真实的画笔
    private float mX, mY;// 临时点坐标
    private float sX, sY;// 起始坐标
    private static final float TOUCH_TOLERANCE = 4;
    // 保存Path路径的集合
    private static List<DrawPath> savePath;
    // 保存已删除Path路径的集合
    private static List<DrawPath> deletePath;
    // 记录Path路径的对象
    private DrawPath dp;
    //绘画区域的宽高
    private int screenWidth, screenHeight;
    //画笔颜色
    private int currentColor = Color.RED;
    //画笔的宽度
    private int currentSize = 5;

    public int getCurrentStyle() {
        return currentStyle;
    }

    public void setCurrentStyle(int currentStyle) {
        this.currentStyle = currentStyle;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }

    //用来决定是画笔还是橡皮擦
    private int currentStyle = 1;
    private class DrawPath {
        public Path path;// 路径
        public Paint paint;// 画笔
        public float pointX, pointY;//点
    }

    public PaintView(Context context, int w, int h) {
        super(context);
        this.context = context;
        screenHeight = h;
        screenWidth = w;
        //设置默认样式，去除dis-in的黑色方框以及clear模式的黑线效果
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        initCanvas();
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
    }


    private void initCanvas() {
        setPaintStyle();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        //画布大小
        mBitmap = Bitmap.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(0,0,0,0));
        //所有mCanvas画的东西都被保存在了mBitmap中
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT);

    }

    private void setPaintStyle() {
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);//设置为画线
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置形状
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setDither(true);//设置防抖动

        if(currentStyle == 1){
            mPaint.setStrokeWidth(currentSize);
            mPaint.setColor(currentColor);
        }else{
            mPaint.setAlpha(0);
            //设置控制图像合成的模式，一共有十六中模式，DST_IN表示取两层绘制交集，显示下层绘制的图片
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setStrokeWidth(50);
        }
    }

    /**
     * 这个方法会在初始化后背调用一次,invaildate()的时候会被调用
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //将前面已经画过的显示出来
        canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
        for (DrawPath db:savePath){
            if(db.path != null){
                canvas.drawPath(db.path,db.paint);
            }
            if(db.pointX != 0){
                canvas.drawPoint(db.pointX,db.pointY,db.paint);
            }
        }
        if (mPath !=null){
            //实时显示
            canvas.drawPath(mPath,mPaint);
        }

    }

    private void touch_start(float x,float y){
        mPath.moveTo(x,y);
        sY = y;
        sX = x;
        mX = x;
        mY = y;
    }

    private void touch_move(float x,float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(mY-y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也可以)
            mPath.quadTo(mX,mY,(x + mX)/2, (y + mY)/2);
            //mPath.lineTo(mX,mY);
            mX = x;
            mY = y;
        }
    }

    private void touch_up(){
        mPath.lineTo(mX,mY);
        if(Math.abs(mY-sY)<1 && Math.abs(mX-sX)<2){
            mCanvas.drawPoint(sX,sY,mPaint);
            DrawPath drawPath = new DrawPath();
            drawPath.paint = mPaint;
            drawPath.pointX = sX;
            drawPath.pointY = sY;
            savePath.add(drawPath);
        }else{
            //将一条完整的路径保存下来
            savePath.add(dp);
        }
        mCanvas.drawPath(mPath,mPaint);
        mPath=null;
    }

    /**
     * 撤销
     * 撤销的核心思想就是将画布清空，
     * 将保存下来的Path路径最后一个移除掉，
     * 重新将路径画在画布上面。
     */
    public void undo(){
        if (savePath != null && savePath.size() > 0){
            DrawPath drawPath = savePath.get(savePath.size()-1);
            deletePath.add(drawPath);
            savePath.remove(savePath.size()-1);
            redrawOnBitmap();
        }
    }

    /*
    * 清空
    * */
    public void redo(){
        if (savePath != null &&savePath.size()>0){
            savePath.clear();
            redrawOnBitmap();
        }
    }
    private void redrawOnBitmap() {
        initCanvas();
        invalidate();
    }

    /**
     * 恢复，恢复的核心就是将删除的那条路径重新添加到savapath中重新绘画即可
     */
    public void recover(){
        if (deletePath != null && deletePath.size()>0){
            //取出删除列表的最后一个，将取出的路径重新绘制，并保存到路径保存列表中，最后将这条路径从删除列表移除
            DrawPath drawPath = deletePath.get(deletePath.size() - 1);
            savePath.add(drawPath);
            mCanvas.drawPath(drawPath.path,drawPath.paint);
            deletePath.remove(deletePath.size() - 1);
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                dp = new DrawPath();
                dp.paint = mPaint;
                dp.path = mPath;
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    //保存到SD卡
    public void saveToSDCard(){
        //获得系统当前时间，并以该时间作为文件名
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate) + "paint.png";
        File file = new File("/mnt/sdcard/pic/" + str);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //发送Sd卡的就绪广播,要不然在手机图库中不存在
        Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
        intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
        context.sendBroadcast(intent);
        Toast.makeText(context,"图片已保存",Toast.LENGTH_SHORT).show();
    }

    //修改样式
    //设置画笔样式
    public void selectPaintStyle(int which){
        if (which == 0){
            currentStyle = 1;
            setPaintStyle();
        }
        //橡皮
        if (which == 1){
            currentStyle = 2;
            setPaintStyle();
        }
    }

    //设置画笔大小
    public void selectPaintSize(int which){
        currentSize = which;
        setPaintStyle();
    }

    //设置画笔颜色
    public void selectPaintColor(int currentColor){
        this.currentColor = currentColor;
        if (currentStyle == 1){
            setPaintStyle();
        }
    }
}
