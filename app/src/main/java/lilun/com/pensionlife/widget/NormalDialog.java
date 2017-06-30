package lilun.com.pensionlife.widget;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;

import lilun.com.pensionlife.R;

/**
 * @author yk
 *         create at 2017/3/1 9:10
 *         email : yk_developer@163.com
 */
public class NormalDialog {
    public void createNormal(Activity activity, String msg, OnPositiveListener listener) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .content(msg)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((dialog1, which) -> listener.onPositiveClick())
                .onNegative((dialog1, which) -> dialog1.dismiss())
                .show();
    }

    public void createNormal(Activity activity, int msg, OnPositiveListener listener) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .content(msg)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((dialog1, which) -> listener.onPositiveClick())
                .onNegative((dialog1, which) -> dialog1.dismiss())
                .show();
    }

    public void createShowMessage(Activity activity, String msg, OnPositiveListener listener) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .content(msg)
                .positiveText(R.string.confirm)
                .onPositive((dialog1, which) -> listener.onPositiveClick())
                .show();
    }

    public void createShowMessage(Activity activity, String msg, OnPositiveListener listener, boolean cancelOnOutSide) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .canceledOnTouchOutside(cancelOnOutSide)
                .content(msg)
                .positiveText(R.string.confirm)
                .onPositive((dialog1, which) -> listener.onPositiveClick())
                .show();
    }

    public void createEditMessage(Activity activity, String title, boolean cancelOnOutSide, MaterialDialog.InputCallback inputCallBack) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .canceledOnTouchOutside(cancelOnOutSide)
                .title(title)
                .input("", "", false, inputCallBack)
                .positiveText(R.string.confirm)
                .show();
    }

    public void createCheckDialog(Activity activity, String title, boolean cancelOnOutSide, MaterialDialog.SingleButtonCallback posback) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .canceledOnTouchOutside(cancelOnOutSide)
                .title(title)
                .checkBoxPrompt(activity.getString(R.string.activity_not_allow_join), false, null)
                .positiveText(R.string.confirm)
                .onPositive(posback)
                .show();
    }

    /**
     * 版本更新的提示Dialog
     *
     * @param activity
     * @param desp
     * @param listener
     * @param forced
     */
    public void createVersionDialog(Activity activity, String desp, boolean forced, OnPositiveListener listener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity);
        builder.canceledOnTouchOutside(!forced)
                .title(R.string.version_new)
                .content(desp)
                .positiveText(R.string.version_update_imm)
                .onPositive((dialog1, which) -> listener.onPositiveClick());
        if (!forced) {
            builder.negativeText(R.string.version_update_ignore)
                    .onNegative((dialog1, which) -> dialog1.dismiss());
        }
        builder.show();
    }


    public interface OnPositiveListener {
        void onPositiveClick();
    }
}
