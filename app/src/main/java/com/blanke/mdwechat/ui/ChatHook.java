package com.blanke.mdwechat.ui;

import android.graphics.Bitmap;
import android.view.View;

import com.blanke.mdwechat.Common;
import com.blanke.mdwechat.util.DrawableUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.xMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

/**
 * Created by blanke on 2017/9/2.
 */

public class ChatHook extends BaseHookUi {
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        final Bitmap bubbleLeft = DrawableUtils.getExternalStorageAppBitmap(Common.CHAT_BUBBLE_LEFT_FILENAME);
        final Bitmap bubbleRight = DrawableUtils.getExternalStorageAppBitmap(Common.CHAT_BUBBLE_RIGHT_FILENAME);
        if (bubbleLeft != null || bubbleRight != null) {
            xMethod("com.tencent.mm.ui.chatting.ek",
                    "p", View.class, boolean.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Object viewHolder = param.getResult();
                            boolean isOther = (boolean) param.args[1];
                            Object xoi = getObjectField(viewHolder, "xoi");
                            View iq = (View) xoi;
                            if (isOther && bubbleLeft != null) {
                                iq.setBackground(DrawableUtils.getNineDrawable(iq.getResources(), bubbleLeft));
//                                Drawable drawable = ContextCompat.getDrawable(iq.getContext(), R.drawable.bubble_left);
//                                iq.setBackground(drawable);
                            }
                            if (!isOther && bubbleRight != null) {
                                iq.setBackground(DrawableUtils.getNineDrawable(iq.getResources(), bubbleRight));
//                                Drawable drawable = ContextCompat.getDrawable(iq.getContext(), R.drawable.bubble_right);
//                                iq.setBackground(drawable);
                            }
                        }
                    });
        }
    }
}
