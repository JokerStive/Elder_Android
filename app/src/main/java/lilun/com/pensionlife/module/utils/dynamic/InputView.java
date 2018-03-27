package lilun.com.pensionlife.module.utils.dynamic;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;

/**
 * 数据类型的布局
 */
public class InputView extends BaseView {


    private EditText et_mate_value;

    public InputView(JSONObject setting) {
        setSetting(setting);
    }


    @Override
    public View createView() {
        return initView();
    }


    private View initView() {
        View inputView = LayoutInflater.from(App.context).inflate(R.layout.dynamic_input_view, null);
        TextView tv_mate_title = (TextView) inputView.findViewById(R.id.mate_title);
        et_mate_value = (EditText) inputView.findViewById(R.id.mate_value);

        tv_mate_title.setText(mate_title);
        et_mate_value.setHint(mate_description);
        if (mate_value != null) {
            et_mate_value.setText(mate_value.toString());
        }


        if (isOnlyShow) {
            et_mate_value.setEnabled(false);
            return inputView;
        }

        et_mate_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    setValue(null);
                } else {
                    setValue(s.toString());
                }
            }
        });

        String type = setting.getString("type");
        int inputType = EditorInfo.TYPE_CLASS_TEXT;
        if (type.equals("integer")) {
            inputType = EditorInfo.TYPE_CLASS_NUMBER;
        }

        et_mate_value.setInputType(inputType);


        return inputView;
    }

}
