package uk.ac.shef.oak.com4510.ui.history;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import uk.ac.shef.oak.com4510.R;

public class MyImageDialog extends Dialog {

    private Window window = null;
    private ImageView iv;
    private Bitmap bms;
    public MyImageDialog(Context context, boolean cancelable,
                         DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public MyImageDialog(Context context, int cancelable,int x,int y,Bitmap bm) {
        super(context, cancelable);
        windowDeploy(x, y);
        bms = bm;

    }
    public MyImageDialog(Context context) {
        super(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        //Initialise layout
        View loadingview= LayoutInflater.from(getContext()).inflate(R.layout.imagedialogview,null);
        iv=(ImageView) loadingview.findViewById(R.id.imageview_head_big);
        iv.setImageBitmap(bms);
        //Set the layout of dialog
        setContentView(loadingview);
        loadingview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //If need to animate when zooming in or out, can do it directly here for loadingview or iv, and below in SHOW or DISMISS
        super.onCreate(savedInstanceState);
    }

    //Set window display
    public void windowDeploy(int x, int y){
        window = getWindow(); //Get the dialog box
        window.setWindowAnimations(R.style.dialogWindowAnim); //Set window pop-up animation
        window.setBackgroundDrawableResource(android.R.color.transparent); //Set the background of the dialog box to transparent
        WindowManager.LayoutParams wl = window.getAttributes();
        //Set the position of the window to be displayed according to the x and y coordinates
        wl.x = x; //Shift left if x is less than 0, shift right if greater than 0
        wl.y = y; //y less than 0 shifts up, greater than 0 shifts down
//            wl.alpha = 0.6f; //Set transparency
//            wl.gravity = Gravity.BOTTOM; //Set gravity
        window.setAttributes(wl);
    }

    public void show() {
        //Set the touch dialog box to cancel the dialog box in unexpected places
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        super.show();
    }
    public void dismiss() {
        super.dismiss();
    }
}