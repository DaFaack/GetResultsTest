package com.fujigames.buttonmakerview.views;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.fujigames.buttonmakerview.R;

import java.util.ArrayList;


public class ButtonMakerView extends RelativeLayout {

    private ImageView mImage;
    private RelativeLayout mTextContainer;
    public Bitmap shadow,base,shadow2,base2;
    ArrayList<Bitmap> shadowArray;
    ImageView iv_button;
    View view;
    Bitmap exportedPrev;

    public ButtonMakerView(Context c) {
        super(c);
        init();
    }

    public ButtonMakerView(Context c, AttributeSet attr)
    {
        super(c, attr);
        init();
    }

    public ButtonMakerView(Context c, AttributeSet attr, int style)
    {
        super(c, attr, style);
        init();
    }

    public void init()
    {
        removeAllViews();

        mTextContainer = new RelativeLayout(getContext());
        mTextContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mImage = new ImageView(getContext());
        mImage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        addView(mImage);
        addView(mTextContainer);


        LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);;


        iv_button = new ImageView(getContext());
        iv_button.setImageResource(R.drawable.btn_template);
        iv_button.setLayoutParams(params2);
        addView(iv_button);


    }

    public void addImage(Bitmap image) {
        view = new SandboxView(getContext(), image);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        
        mTextContainer.addView(view);

    }
    public void setButtonColor(int color){
        shadowArray = new ArrayList<>();


        Bitmap exported = Bitmap.createBitmap(512,512, Bitmap.Config.ARGB_8888);
        exported.eraseColor(color);

        shadow = BitmapFactory.decodeResource(getResources(), R.drawable.shadow_stencil);
        shadow = Bitmap.createScaledBitmap(shadow, 512 , 512, false);
        exported = getCroppedBitmap(exported,shadow);

        Bitmap shadowOverlay = BitmapFactory.decodeResource(getResources(), R.drawable.shadow);
        shadowOverlay = Bitmap.createScaledBitmap(shadowOverlay, 512 , 512, false);


        base = Bitmap.createBitmap(512,512, Bitmap.Config.ARGB_8888);
        base.eraseColor(Color.TRANSPARENT);

        Canvas canvas = new Canvas(base);
        canvas.drawBitmap(exported,0,0,null);
        canvas.drawBitmap(shadowOverlay,0,0,null);


        shadowArray.add(base);
        base = null;
        shadow = null;



        Bitmap exported2 = Bitmap.createBitmap(512,512, Bitmap.Config.ARGB_8888);
        exported2.eraseColor(color);

        shadow2 = BitmapFactory.decodeResource(getResources(), R.drawable.stencil_pressed);
        shadow2 = Bitmap.createScaledBitmap(shadow2, 512 , 512, false);
        exported2 = getCroppedBitmap(exported2,shadow2);

        Bitmap shadowOverlay2 = BitmapFactory.decodeResource(getResources(), R.drawable.shadow_pressed);
        shadowOverlay2 = Bitmap.createScaledBitmap(shadowOverlay2, 512 , 512, false);


        base2 = Bitmap.createBitmap(512,512, Bitmap.Config.ARGB_8888);
        base2.eraseColor(Color.TRANSPARENT);

        Canvas canvas2 = new Canvas(base2);
        canvas2.drawBitmap(exported2,0,0,null);
        canvas2.drawBitmap(shadowOverlay2,0,0,null);

        shadowArray.add(base2);
        base2 = null;
        shadow2 = null;

        exportedPrev = Bitmap.createBitmap(512,512, Bitmap.Config.ARGB_8888);
        exported.eraseColor(Color.TRANSPARENT);

        Canvas prev = new Canvas(exportedPrev);


        Bitmap shape = BitmapFactory.decodeResource(getResources(), R.drawable.btn_template);
        shape =   Bitmap.createScaledBitmap(shape, 512 , 512, false);

        prev.drawBitmap(shape,0,0,null);
        prev.drawBitmap(shadowArray.get(0),0,0,null);

        iv_button.setImageBitmap(exportedPrev);

    }
    public ArrayList<Bitmap> getResult() {
        ArrayList<Bitmap> buttons = new ArrayList<>();

        Bitmap exported = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(exported);
        draw(canvas);

        Bitmap shape = BitmapFactory.decodeResource(getResources(), R.drawable.stencil);


        exported = Bitmap.createScaledBitmap(exported, 512, 512, false);

        shape = Bitmap.createScaledBitmap(shape, 512 , 512, false);
        exported = getCroppedBitmap(exported,shape);

        if (shadowArray != null) {

            Bitmap exportedShadow = Bitmap.createBitmap(512,512, Bitmap.Config.ARGB_8888);
            exportedShadow.eraseColor(Color.TRANSPARENT);

            Canvas canvas2 = new Canvas(exportedShadow);
            canvas2.drawBitmap(exported,0,0,null);
            canvas2.drawBitmap(shadowArray.get(0),0,0,null);

            buttons.add(exportedShadow);
        } else{
            buttons.add(exported);
        }

        buttons.add(getResultPressed());
        if(shadowArray != null){
            iv_button.setImageBitmap(exportedPrev);
        }else {
            iv_button.setImageResource(R.drawable.btn_template);
        }
        return buttons;
    }
    private Bitmap getResultPressed() {
        Bitmap exported = Bitmap.createBitmap(512,512, Bitmap.Config.ARGB_8888);
        exported.eraseColor(Color.TRANSPARENT);

        Bitmap template = BitmapFactory.decodeResource(getResources(), R.drawable.btn_template_pressed);
        template = Bitmap.createScaledBitmap(template, 512, 512, false);

        Bitmap shape = BitmapFactory.decodeResource(getResources(), R.drawable.stencil_pressed_2);
        shape = Bitmap.createScaledBitmap(shape, 512, 512, false);


        iv_button.setVisibility(INVISIBLE);
        Bitmap image;
        image = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas3 = new Canvas(image);
        draw(canvas3);
        image = Bitmap.createScaledBitmap(image, 512,512, false);

        iv_button.setVisibility(VISIBLE);


        Canvas canvas = new Canvas(exported);

        int x = (canvas.getWidth()  - image.getWidth()) /2;
        int y = (canvas.getHeight() - image.getHeight()) /2;


        canvas.drawBitmap(image,x,y + 45,null);
        canvas.drawBitmap(template,0,0,null);


        if (shadowArray != null) {

            Bitmap exportedShadow = Bitmap.createBitmap(512,512, Bitmap.Config.ARGB_8888);
            exportedShadow.eraseColor(Color.TRANSPARENT);

            Canvas canvas2 = new Canvas(exportedShadow);
            canvas2.drawBitmap(exported,0,0,null);
            canvas2.drawBitmap(shadowArray.get(1),0,0,null);


            return getCroppedBitmap(exportedShadow,shape);
        } else{
           return getCroppedBitmap(exported,shape);
        }
    }

    public static Bitmap getCroppedBitmap(Bitmap src,Bitmap shape) {
        Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0XFF000000);

        canvas.drawBitmap(shape,0,0,paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(src, 0, 0, paint);

        return output;
    }

}
