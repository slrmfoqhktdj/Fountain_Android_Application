package com.example.swipeviewex01;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SecondIMGClickPage extends Activity {

    private ImageView imageView;

//    private ScaleGestureDetector mScaleGestureDetector;
//    private float mScaleFactor = 1.0f;

    enum TOUCH_MODE {
        NONE,   // 터치 안했을 때
        SINGLE, // 한손가락 터치
        MULTI   //두손가락 터치
    }

    private Matrix matrix;      //기존 매트릭스
    private Matrix savedMatrix; //작업 후 이미지에 매핑할 매트릭스
    private PointF startPoint;  //한손가락 터치 이동 포인트
    private TOUCH_MODE touchMode;
    private PointF midPoint;    //두손가락 터치 시 중신 포인트
    private float oldDistance;  //터치 시 두손가락 사이의 거리

//    private float midX;
//    private float midY;
//
//    private float fixX;
//    private float fixY;
//
//    private float minstanceX;
//    private float minstanceY;
//
//    private float remidX;
//    private float remidY;
//
//    private int count = 0;

    
    
    
    
    
    
    
    //최근
//    FrameLayout root;
//    int x;
//    int y;
//    View view;
//    Bitmap bitmap ;
//    int color;
//    int r;
//    int g;
//    int b;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondimgviewclick);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Intent intent = getIntent();
        String URL = intent.getStringExtra("url");

        matrix = new Matrix();
        savedMatrix = new Matrix();

//        root = findViewById(R.id.root);
        imageView = findViewById(R.id.second_imgView_click);

// 에러 이미지 어떻게 나올줄 몰라 일단 주석처리
//        Glide.with(this).load(URL).error(R.drawable.loading_image).into(imageView);
        Glide.with(this).load(URL).error(R.drawable.pictureerror).into(imageView);

        imageView.setOnTouchListener(onTouch);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);

//        mScaleGestureDetector = new ScaleGestureDetector(this,new ScaleListener());

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private final View.OnTouchListener onTouch = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            int parentWidth = ((ViewGroup)v.getParent()).getWidth();    // 부모 View 의 Width
//            int parentHeight = ((ViewGroup)v.getParent()).getHeight();    // 부모 View 의 Height
            if (v.equals(imageView)) {
                int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        touchMode = TOUCH_MODE.SINGLE;
                        donwSingleEvent(event);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (event.getPointerCount() == 2) { // 두손가락 터치를 했을 때
                            touchMode = TOUCH_MODE.MULTI;
                            downMultiEvent(event);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (touchMode == TOUCH_MODE.SINGLE) {
                            moveSingleEvent(event);
                        } else if (touchMode == TOUCH_MODE.MULTI) {
                            moveMultiEvent(event);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
//                        if(touchMode == TOUCH_MODE.SINGLE){
//                            Log.e("view","viewX:"+v.getX()+" viewY:"+v.getY());

//                            upfixSingleEvent(event,v);
//                            fix();

//                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        touchMode = TOUCH_MODE.NONE;
                        break;
                }
            }


            return true;
        }
    };

//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
//        @Override
//        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
//            mScaleFactor = scaleGestureDetector.getScaleFactor();
//            mScaleFactor = Math.max(0.1f,Math.min(mScaleFactor,10.0f));
//
//            imageView.setScaleX(mScaleFactor);
//            imageView.setScaleY(mScaleFactor);
//
//
//
//            return true;
//        }
//    }

//    public boolean onTouchEvent(MotionEvent event){
//        mScaleGestureDetector.onTouchEvent(event);
//        return true;
//    }
//


    private PointF getMidPoint(MotionEvent e) {

        float x = (e.getX(0) + e.getX(1)) / 2;
        float y = (e.getY(0) + e.getY(1)) / 2;

        return new PointF(x, y);
    }

    private float getDistance(MotionEvent e) {
        float x = e.getX(0) - e.getX(1);
        float y = e.getY(0) - e.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    private void downMultiEvent(MotionEvent event) {
        oldDistance = getDistance(event);
        if (oldDistance > 5f) {
            savedMatrix.set(matrix);
            midPoint = getMidPoint(event);
//            double radian = Math.atan2(event.getY() - midPoint.y, event.getX() - midPoint.x);
//            oldDegree = (radian * 180) / Math.PI;
        }
    }

    private void moveMultiEvent(MotionEvent event) {
        float newDistance = getDistance(event);
        if (newDistance > 5f) {
            matrix.set(savedMatrix);
            float scale = newDistance / oldDistance;
            matrix.postScale(scale, scale, midPoint.x, midPoint.y);


//            double nowRadian = Math.atan2(event.getY() - midPoint.y, event.getX() - midPoint.x);
//            double nowDegress = (nowRadian * 180) / Math.PI;
//            float degree = (float) (nowDegress - oldDegree);
            matrix.postRotate(0, midPoint.x, midPoint.y);


            imageView.setImageMatrix(matrix);

        }
    }

    private void donwSingleEvent(MotionEvent event) {
        savedMatrix.set(matrix);
        startPoint = new PointF(event.getX(), event.getY());



//        remidX = midX;
//        remidY = midY;
    }

    private void moveSingleEvent(MotionEvent event) {
        matrix.set(savedMatrix);
        matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
        imageView.setImageMatrix(matrix);



//        minstanceX = event.getX()-startPoint.x;
//        minstanceY = event.getY()-startPoint.y;
    }

    
    //최근
//    @SuppressLint("SetTextI18n")
//    public int selectRGB(){
//        x = root.getWidth()/2;
//        y = root.getHeight()/2;
//
//        //레이아웃에서 뷰를 얻어오기
//        view = findViewById(R.id.root);
//
//        //뷰로부터 비트맵만들기
//        bitmap = viewToBitmap(view);
//
//        //비트맵에서 특정좌표의 rgb값 가져오기
//        color = bitmap.getPixel(x,y);
//
//        r = Color.red(color);
//        g = Color.green(color);
//        b = Color.blue(color);
//        Log.e("rgb","R "+r+" G "+g+" B "+b);
//        return r+g+b;
//    }
//    public Bitmap viewToBitmap(View view) {
//        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//        return bitmap;
//    }
//
//    private void fix(){
//        if(selectRGB()==0){
//            matrix.set(savedMatrix);
//            imageView.setImageMatrix(matrix);
//        }
//    }
    
    
    
    
    
    
    
    
    
    
    
    

//    private void upfixSingleEvent(MotionEvent event,View v){

//        midX = midX + minstanceX;
//        midY = midY + minstanceY;
//
//        Log.e("upfixSingleEvent","midX:"+midX+" midY:"+midY);
//        if(midX<0||midX>fixX){
//            matrix.set(savedMatrix);
//            imageView.setImageMatrix(matrix);
//            midX = remidX;
//            midY = remidY;
//            Log.e("upfixSingleEvent","event.getX()<0");
//        }
//        if(midY<0||midY>fixY){
//            matrix.set(savedMatrix);
//            imageView.setImageMatrix(matrix);
//            Log.e("upfixSingleEvent","event.getY()<0");
//            midX = remidX;
//            midY = remidY;
//        }
//    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus){
//        super.onWindowFocusChanged(hasFocus);
//        if(count == 0){
//            fixX = imageView.getRight();
//            fixY = imageView.getBottom();
//            midX = fixX/2;
//            midY = fixY/2;
//            Log.e("WindowFocusChanged"," midX:"+midX+" midY:"+midY);
//            count = 1;
//        }

//    }
}
