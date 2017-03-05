package ttyy.com.common.camera;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Author: hujinqi
 * Date  : 2016-0radius-23
 * Description: 相册选择对话框
 */
public class PhotoSelectDialog extends Dialog {

    GradientDrawable drawable_back;
    LayerDrawable list_bottom_line_head;
    LayerDrawable list_bottom_line_middle;
    LayerDrawable list_bottom_line_foot;

    public PhotoSelectDialog(Context context) {
        super(context);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.BOTTOM);
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        resourcesInit(context);
        init(context);
    }

    void resourcesInit(Context context){
        
        int radius = 8;
        
        Drawable[] layers_middle = new Drawable[2];
        layers_middle[0] = new ColorDrawable(Color.parseColor("#f4f4f4"));
        layers_middle[1] = new ColorDrawable(Color.WHITE);
        list_bottom_line_middle = new LayerDrawable(layers_middle);
        list_bottom_line_middle.setLayerInset(1,0,0,0,2);

        Drawable[] layers_head = new Drawable[2];
        GradientDrawable head_line_layer_0 = new GradientDrawable();
        // top-left, top-right, bottom-right, bottom-left
        head_line_layer_0.setCornerRadii(new float[]{radius,radius,radius,radius,0,0,0,0});
        head_line_layer_0.setColor(Color.parseColor("#f4f4f4"));

        GradientDrawable head_line_layer_1 = new GradientDrawable();
        // top-left, top-right, bottom-right, bottom-left
        head_line_layer_1.setCornerRadii(new float[]{radius,radius,radius,radius,0,0,0,0});
        head_line_layer_1.setColor(Color.WHITE);
        layers_head[0] = head_line_layer_0;
        layers_head[1] = head_line_layer_1;
        list_bottom_line_head = new LayerDrawable(layers_head);
        list_bottom_line_head.setLayerInset(1,0,0,0,2);

        Drawable[] layers_foot = new Drawable[2];
        GradientDrawable foot_line_layer_0 = new GradientDrawable();
        // top-left, top-right, bottom-right, bottom-left
        foot_line_layer_0.setCornerRadii(new float[]{0,0,0,0,radius,radius,radius,radius,});
        foot_line_layer_0.setColor(Color.parseColor("#f4f4f4"));

        GradientDrawable foot_line_layer_1 = new GradientDrawable();
        // top-left, top-right, bottom-right, bottom-left
        foot_line_layer_1.setCornerRadii(new float[]{0,0,0,0,radius,radius,radius,radius});
        foot_line_layer_1.setColor(Color.WHITE);
        layers_foot[0] = foot_line_layer_0;
        layers_foot[1] = foot_line_layer_1;
        list_bottom_line_foot = new LayerDrawable(layers_foot);
        list_bottom_line_foot.setLayerInset(1,0,0,0,2);

        drawable_back = new GradientDrawable();
        drawable_back.setCornerRadii(new float[]{radius,radius,radius,radius,radius,radius,radius,radius,});
        drawable_back.setColor(Color.WHITE);
    }

    void init(final Context context){
        float density = context.getResources().getDisplayMetrics().density;
        int padding = (int) (density * 15);

        LinearLayout rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setBackgroundDrawable(drawable_back);

        TextView tv_title = new TextView(context);
        tv_title.setText("照片来源");
        tv_title.setTextSize(18);
        tv_title.setGravity(Gravity.CENTER);
        tv_title.setTextColor(Color.parseColor("#333333"));
        tv_title.setBackgroundDrawable(list_bottom_line_head);
        rootView.addView(tv_title);
        tv_title.setPadding(0, padding, 0, padding);

        TextView tv_from_album = new TextView(context);
        tv_from_album.setText("从相册获取");
        tv_from_album.setTextSize(15);
        tv_from_album.setGravity(Gravity.CENTER);
        tv_from_album.setTextColor(Color.parseColor("#333333"));
        tv_from_album.setBackgroundDrawable(list_bottom_line_middle);
        rootView.addView(tv_from_album);
        tv_from_album.setPadding(0, padding, 0, padding);
        tv_from_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                PhotoGetTools.pictureFromAlbum((Activity) context);
            }
        });

        TextView tv_from_camera = new TextView(context);
        tv_from_camera.setText("从相机获取");
        tv_from_camera.setTextSize(15);
        tv_from_camera.setGravity(Gravity.CENTER);
        tv_from_camera.setTextColor(Color.parseColor("#333333"));
        tv_from_camera.setBackgroundDrawable(list_bottom_line_foot);
        rootView.addView(tv_from_camera);
        tv_from_camera.setPadding(0, padding, 0, padding);
        tv_from_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                PhotoGetTools.pictureFromCamera((Activity) context);
            }
        });

        setContentView(rootView);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels);
        getWindow().setAttributes(lp);
    }
}
