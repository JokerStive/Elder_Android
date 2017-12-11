package lilun.com.pensionlife.widget;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

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

    public void createShowMessage(Activity activity, String msg, String posText, OnPositiveListener listener, String negText, boolean cancelOnOutSide) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .content(msg)
                .positiveText(posText)
                .onPositive((dialog1, which) -> listener.onPositiveClick())
                .negativeText(negText)
                .onNegative(((dialog, which) -> dialog.dismiss()))
                .canceledOnTouchOutside(cancelOnOutSide)
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

    public void createEditMessage(Activity activity, String title, String data, boolean cancelOnOutSide, MaterialDialog.InputCallback inputCallBack) {
        MaterialDialog dialog = new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .customView(R.layout.dialog_edit_info, true)
                .canceledOnTouchOutside(cancelOnOutSide)
                .title(title)
                .positiveText(R.string.confirm)
                .onPositive((dialog2, which) -> {
                    String inputdata = ((EditText) dialog2.getView().findViewById(R.id.et_data)).getText().toString();
                    inputCallBack.onInput(dialog2, inputdata);

                })
                .negativeText(R.string.cancel)
                .onNegative((dialog1, which) -> {
                    dialog1.dismiss();
                })
                .build();

        EditText editView = (EditText) dialog.getView().findViewById(R.id.et_data);
        editView.setText(data);
        editView.setSelection(data.length());
        dialog.getView().findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editView.setText("");
            }
        });
        dialog.getWindow().setWindowAnimations(R.style.dialog_animator);
        dialog.show();


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
