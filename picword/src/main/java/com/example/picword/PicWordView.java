package com.example.picword;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: document your custom view class.
 */
public class PicWordView extends View {
    private SpannableStringBuilder mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;
    private int mLineHeight;
    private Layout layout = null;
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private int picPosX;
    private int picPosY;
    private CharSequence mData = null;
    private String TAG_POS="";
    private LinearLayout mContentView = null;
    ArrayList<Bitmap> bitmapList = new ArrayList<>();
    ArrayList<Pos>posList=new ArrayList<>();



    public PicWordView(Context context) {
        super(context);
        init(null, 0);
    }

    public PicWordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PicWordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PicWordView, defStyle, 0);

        mExampleString = SpannableStringBuilder.valueOf(a.getString(
                R.styleable.PicWordView_exampleString));
        mExampleString.clear();
        mExampleColor = a.getColor(
                R.styleable.PicWordView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.PicWordView_exampleDimension,
                mExampleDimension);
        TAG_POS=a.getString(R.styleable.PicWordView_mode);
        Log.i("TAG",TAG_POS);
        if (a.hasValue(R.styleable.PicWordView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.PicWordView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString.toString());
        cacuLineHeight();
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        Log.i("contentHeight width",contentHeight+" "+contentWidth);
        // Draw the text.
        Log.i("String",mExampleString.toString());
        int currentHeight=paddingTop;

        String s[]=mExampleString.toString().split("\n");
        int count=0;
        float bitmapHeight=0;
        for(String ss:s) {
            Log.i("ss",ss);
            if (ss.endsWith("img")) {//如果带有img标签
                if(TAG_POS.equals("right")||TAG_POS.equals("")) {//带有right标签或者为空
                    Bitmap bitmap=bitmapList.get(count);

                }
                if(TAG_POS.equals("center")){
                    Bitmap bitmap = bitmapList.get(count);
                    Log.i("bitmapHeight",""+bitmap.getHeight());
                    Log.i("center",bitmap.toString());
                    mTextWidth = mTextPaint.measureText(ss.substring(0, ss.length() - 3));
                    StaticLayout sl = new StaticLayout(ss.substring(0, ss.length() - 3), mTextPaint,contentWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//从0,0开始绘制

                     Log.i("currentHeight",currentHeight+"");
                    canvas.translate(paddingLeft, currentHeight);
                    currentHeight=0;
                    sl.draw(canvas);
                    //canvas.drawText(ss, paddingLeft,currentHeight, mTextPaint);
                    currentHeight += sl.getHeight();
                    //canvas.translate(paddingLeft, 0);
                    if (bitmap.getWidth() > contentWidth) {
                        bitmap = resizeBitmap(bitmap,  (contentWidth),  (int)(bitmap.getHeight() *((contentWidth)*1.0 / bitmap.getWidth())));
                    }
                    bitmapHeight = bitmap.getHeight();
                    Log.i("bitmapHeight",""+bitmapHeight);
                    canvas.drawBitmap(bitmap, paddingLeft,currentHeight , new Paint());
                    currentHeight+=bitmapHeight ;
                    Log.i("currentHeight",currentHeight+"");
                    count++;
                }

            }else {
                Log.i("pureString",currentHeight+"");
                if (currentHeight < bitmapHeight) {
                    StaticLayout sl = new StaticLayout(ss, mTextPaint,getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                    canvas.translate(paddingLeft,currentHeight);
                    currentHeight=0;
                    sl.draw(canvas);
                    //canvas.drawText(ss, paddingLeft,currentHeight, mTextPaint);

                    currentHeight += sl.getHeight();
                    Log.i("currentHeight",currentHeight+"");

                }else{
                    StaticLayout sl = new StaticLayout(ss, mTextPaint,getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                    canvas.translate(paddingLeft,currentHeight);
                    currentHeight=0;
                    sl.draw(canvas);
                    //canvas.drawText(ss, paddingLeft,currentHeight, mTextPaint);

                    currentHeight += sl.getHeight();
                    Log.i("currentHeight",currentHeight+"");
                    Log.i("LineHeight",mLineHeight+"");
            }
                Log.i("pureString",currentHeight+"");
            }

        }
        //canvas.drawBitmap(bitmapList.get(0),paddingLeft,paddingTop+mLineHeight,mTextPaint);
        // Draw the example drawable on top of the text.

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        //设置该View大小为 80 80
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec)*4);
    }



    private void cacuLineHeight()
    {
        layout = new StaticLayout("爱我中华", mTextPaint, 0, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);
        mLineHeight = layout.getLineBottom(0) - layout.getLineTop(0);
    }
    public void setText(CharSequence sequence) {
        ArrayList<URI> urlImage = new ArrayList<>();
        SpannableStringBuilder spanBuilder = null;

        try {
            if (TextUtils.isEmpty(sequence)) return;
            else if (sequence.equals(mData)) return;
            mData = sequence;
            Pattern pattern = Pattern.compile("<img(\\d+)/>");
            spanBuilder = new SpannableStringBuilder(mData);
            Matcher matcher = pattern.matcher(mData);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();

                //mData=mData.toString().replaceAll("<img(.+)/>","");
                Log.i(">>mData", matcher.group(1));
                if (matcher.group(1).matches("\\d+")) {
                    Drawable drawable = getResources().getDrawable(Integer.parseInt(matcher.group(1)));
                    ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                    spanBuilder.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    spanBuilder.replace(start,end,"img\n");

                } else {
                    urlImage.add(URI.create(matcher.group()));
                }
            }

/*
            for(URI img :urlImage){

                //这里做自己的异步加载操作；
                //注意要保证线程程同步
            }
            */

        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            mExampleString.clear();
            mExampleString=spanBuilder;
            invalidateTextPaintAndMeasurements();
            imageLoad();
        }

    }

    private void imageLoad() {
        ImageSpan[] s = mExampleString.getSpans(0,mExampleString.length(), ImageSpan.class);
        for (int i = 0; i < s.length; i++) {
            Drawable drawables = s[i].getDrawable();
            bitmapList.add(drawable2Bitmap(drawables));
        }

    }

    public static Bitmap drawable2Bitmap(Drawable drawable){
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap() ;
        }else if(drawable instanceof NinePatchDrawable){
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }else{
            return null ;
        }
    }
    private Bitmap resizeBitmap(Bitmap bitmap,int w,int h)
    {
        if(bitmap!=null)
        {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = w;
            int newHeight = h;
            float scaleWight = ((float)newWidth)/width;
            float scaleHeight = ((float)newHeight)/height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWight, scaleHeight);
            Bitmap res = Bitmap.createBitmap(bitmap, 0,0,width, height, matrix, true);
            return res;

        }
        else{
            return null;
        }
    }
    public void setPos(int x,int y){
        Pos p=new Pos();
        p.posX=x;
        p.posY=y;
        posList.add(p);
    }
    private class Pos {
        int posX;
        int posY;
    }
    public void addText(CharSequence s){

        mExampleString.append(s);
        setText(mExampleString);

    }

    private void getPicSize(Bitmap bitmap){
        int height = bitmap.getHeight();
        int width= bitmap.getWidth();
    }
    /*
    * params:center/left/right/default
    *center:放到当前行下面的中间
    *left：放在当前行左边
    *right：放在当前行右边
    *default("")：放在当前行右边
    * */

    public void setPicMode(String mode){
        TAG_POS=mode;

    }


}
