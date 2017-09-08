package lilun.com.pensionlife.ui.activity.activity_add;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import cn.qqtheme.framework.picker.DateTimePicker;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.OrganizationChildrenConfig;
import lilun.com.pensionlife.base.BaseTakePhotoFragment;
import lilun.com.pensionlife.module.bean.ActivityCategory;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.bean.TakePhotoResult;
import lilun.com.pensionlife.module.utils.BitmapUtils;
import lilun.com.pensionlife.module.utils.GsonUtils;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.mqtt.MQTTManager;
import lilun.com.pensionlife.module.utils.mqtt.MQTTTopicUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.CommonButton;
import lilun.com.pensionlife.widget.InputView;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.TakePhotoLayout;
import okhttp3.MultipartBody;
import rx.Observable;

/**
 * 新建活动V
 *
 * @author yk
 *         create at 2017/3/13 11:18
 *         email : yk_developer@163.com
 */

public class AddActivityFragment extends BaseTakePhotoFragment implements View.OnClickListener {

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

//    @Bind(R.id.rv_activity_classify)
//    RecyclerView rvActivityClassify;

    @Bind(R.id.rl_choice_type)
    RelativeLayout rlChoiceType;
    @Bind(R.id.tv_priority_value)
    TextView tvChoiceType;

    @Bind(R.id.input_title)
    InputView inputTitle;

    @Bind(R.id.input_address)
    InputView inputAddress;


    @Bind(R.id.input_cyclial_gap)
    InputView inputCyclialGap;

    @Bind(R.id.input_require)
    InputView inputRequire;

    @Bind(R.id.input_maxPartner)
    InputView inputMaxPartner;

//    @Bind(R.id.input_location)
//    InputView inputLocation;

    @Bind(R.id.btn_add_activity)
    CommonButton btnAddActivity;

    @Bind(R.id.take_photo)
    TakePhotoLayout takePhotoLayout;

    @Bind(R.id.rg_repeat_type)
    RadioGroup rgRepeatType;

    @Bind(R.id.rl_start_time)
    RelativeLayout rlStartTime;
    @Bind(R.id.rl_end_time)
    RelativeLayout rlEndTime;

    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;

    private ArrayList<ActivityCategory> activityCategories;
    private String mCategoryId;
    private String[] repeatedTypeArray;
    private int chooseTime = 0;
    MaterialDialog typeDalog;
    int typeIndex = 0;
    boolean isrepeat = true;

    public static AddActivityFragment newInstance(ArrayList<ActivityCategory> activityCategories) {
        AddActivityFragment fragment = new AddActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable("activityCategories", activityCategories);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        activityCategories = (ArrayList<ActivityCategory>) arguments.getSerializable("activityCategories");
        Preconditions.checkNull(activityCategories);

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_add;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        //标题栏
        titleBar.setOnBackClickListener(this::pop);


        //图片上传栏
        takePhotoLayout.setFragmentManager(_mActivity.getFragmentManager());
        takePhotoLayout.setOnResultListener(this);
        setTakePhotoLayout(takePhotoLayout);

        //输入类型
        inputMaxPartner.setInputType(InputType.TYPE_CLASS_NUMBER);


        //周期活动控制
        rgRepeatType.check(R.id.rbtn_repeat);
        inputCyclialGap.setVisibility(View.VISIBLE);
        rlStartTime.setVisibility(View.GONE);
        rlEndTime.setVisibility(View.GONE);
        rgRepeatType.setOnCheckedChangeListener((group, checkedId) -> {
            //选择周期活动
            inputCyclialGap.setVisibility(checkedId == R.id.rbtn_once ? View.GONE : View.VISIBLE);
            rlStartTime.setVisibility(checkedId == R.id.rbtn_once ? View.VISIBLE : View.GONE);
            rlEndTime.setVisibility(checkedId == R.id.rbtn_once ? View.VISIBLE : View.GONE);

            if (checkedId == R.id.rbtn_repeat) {
                isrepeat = true;
            } else {
                isrepeat = false;
            }

        });

        tvStartTime.setOnClickListener(this);
        btnAddActivity.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        rlChoiceType.setOnClickListener(this);
    }


    private void addActivity() {
        if (TextUtils.isEmpty(mCategoryId)) {
            ToastHelper.get().showWareShort("请选择活动类别");
            return;
        }

        int intDuration = 0;
        String title = inputTitle.getInput();

        String address = inputAddress.getInput();

        String startTime = tvStartTime.getText().toString();
        String endTime = tvEndTime.getText().toString();


        String maxPartner = inputMaxPartner.getInput();

        String require = inputRequire.getInput();

//        String location = inputLocation.getInput();
//
//        String repeatedType = inputRepeatType.getInput();


//        TODO 输入检查
        if (TextUtils.isEmpty(title)) {
            showNotEmpty(R.string.activity_title);
            return;
        }

        if (TextUtils.isEmpty(address)) {
            showNotEmpty(R.string.activity_address);
            return;
        }
        if (TextUtils.isEmpty(require)) {
            showNotEmpty(R.string.activity_desp);
            return;
        }


        String max = StringUtils.get_StringNum(maxPartner);
        Integer maxPart = null;
        if (!TextUtils.isEmpty(max)) {
            try {
                maxPart = Integer.parseInt(maxPartner);
            } catch (Exception e) {
                maxPart = 10000;
            }
        } else maxPart = 0;

        if (maxPart > 9999) {
            ToastHelper.get().showWareShort(getString(R.string.act_max_partner));
            inputMaxPartner.setInput(9999 + "");
            return;
        }
        OrganizationActivity activity = new OrganizationActivity();
        activity.setCategoryId(mCategoryId);
        activity.setTitle(title);
        activity.setAddress(address);
        if (isrepeat) {
            if (TextUtils.isEmpty(inputCyclialGap.getInput())) {
                showNotEmpty(R.string.activity_repeat);
                return;
            }
            activity.setRepeatedDesc(inputCyclialGap.getInput().trim());
        } else {

            if (TextUtils.isEmpty(startTime)) {
                showNotEmpty(R.string.activity_time);
                return;
            }


            if (TextUtils.isEmpty(endTime)) {
                showNotEmpty(R.string.activity_end_time);
                return;
            }
            String startTimeISO = startTime + ":00";
            activity.setStartTime(startTimeISO);
            String endTimeISO = endTime + ":00";
            activity.setEndTime(endTimeISO);
            if (StringUtils.string2Date(startTimeISO).before(new Date())) {
                ToastHelper.get(getContext()).showWareShort("开始时间应大于现在");
                return;
            }
            if (StringUtils.string2Date(startTimeISO).getTime() - StringUtils.string2Date(endTimeISO).getTime() >= 0) {
                ToastHelper.get(getContext()).showWareShort("结束时间应大于开始时间");
                return;
            }
        }
        activity.setDruation(intDuration);
        activity.setOrganizationId(OrganizationChildrenConfig.activity());
        activity.setDescription(require);
        activity.setMaxPartner(maxPart);


        try {
            JSONObject aidJsonObject = GsonUtils.objectToJSONObject(activity);
            MultipartBody multipartBody = BitmapUtils.filesToMultipartBody(aidJsonObject, getPhotoData());
            NetHelper.getApi()
                    .newActivityIcons(multipartBody)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<OrganizationActivity>(_mActivity) {
                        @Override
                        public void _next(OrganizationActivity activity) {
                            String topic = MQTTTopicUtils.getActivityTopic(activity.getOrganizationId(), activity.getId());
                            MQTTManager.getInstance().subscribe(topic, 2);
                            EventBus.getDefault().post(new Event.RefreshActivityData());
                            pop();
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNotEmpty(int string) {
        ToastHelper.get().showWareShort("请输入" + getString(string));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_activity:
                addActivity();
                break;

//            case R.id.input_repeat_type:
//                //定期类型
//                chooseRepeatedType();
//                break;

            case R.id.tv_start_time:
                // 选择活动开始时间
                chooseTime = 0;
                chooseTime();
                break;
            case R.id.tv_end_time:
                // 选择活动预期结束时间
                chooseTime = 1;
                chooseTime();
                break;
            case R.id.rl_choice_type:
                chooseActivityType();
                break;

        }
    }

    private void chooseTime() {
        DateTimePicker picker = new DateTimePicker(_mActivity, DateTimePicker.YEAR_MONTH_DAY, DateTimePicker.HOUR_24);
        if (App.widthDP > 820) {
            picker.setTextSize(12 * 2);
            picker.setCancelTextSize(12 * 2);
            picker.setSubmitTextSize(12 * 2);
            picker.setTopPadding(15 * 3);
            picker.setTopHeight(40 * 2);
        }
        picker.setRange(2010, 2099);


        picker.setOnDateTimePickListener((DateTimePicker.OnYearMonthDayTimePickListener) (year, month, day, hour, minute) -> {
            //   int year = Calendar.getInstance().get(Calendar.YEAR);
            String time = year + "-" + month + "-" + day + " " + hour + ":" + minute;
            if (chooseTime == 0) {
//                if (etStartTime.getVisibility() == ViewStep2.VISIBLE) {
//                    etStartTime.setText(time);
//                } else {
//
//                }
                tvStartTime.setText(time);
            } else {
                tvEndTime.setText(time);
            }
            Logger.d(time);
        });
        picker.show();
    }


    /**
     * 选择活动持续时长
     */
//    private void chooseDuration() {
//        if (durationArray == null) {
//            durationArray = getResources().getStringArray(R.array.activity_duration);
//        }
//        MaterialDialog d = new MaterialDialog.Builder(_mActivity)
//                .items(durationArray)
//                .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
//                    inputDuration.setInput(durationArray[which]);
//                    return true;
//                })
//                .positiveText(R.string.choose)
//                .show();
//    }


//    /**
//     * 选择求助信息的优先级
//     */
//    private void chooseRepeatedType() {
//        if (repeatedTypeArray == null) {
//            repeatedTypeArray = getResources().getStringArray(R.array.activity_repeatedType);
//        }
//        MaterialDialog d = new MaterialDialog.Builder(_mActivity)
//                .items(repeatedTypeArray)
//
//                .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
//                    inputRepeatType.setInput(repeatedTypeArray[which]);
//                    return true;
//                })
//                .positiveText(R.string.choose)
//                .show();
//
//    }
    private void chooseActivityType() {
        ArrayList<String> list = new ArrayList<>();
        for (ActivityCategory tmp : activityCategories)
            list.add(tmp.getName());
        new MaterialDialog.Builder(_mActivity)
                .title("-选择类型-")
                .items(list)
                .itemsCallbackSingleChoice(typeIndex, (dialog, view, which, text) -> {
                    tvChoiceType.setText(list.get(which));
                    mCategoryId = activityCategories.get(which).getId();
                    typeIndex = which;
                    return true;
                }).show();


    }


    @Override
    protected void onTakePhotoSuccess(TResult tResult) {
        List<TakePhotoResult> results = new ArrayList<>();
        for (TImage tImage : tResult.getImages()) {
            TakePhotoResult result1 = TakePhotoResult.of(tImage.getOriginalPath(), tImage.getCompressPath(), tImage.getFromType(), TakePhotoResult.TYPE_PHOTO);
            results.add(result1);
        }
        Observable.just("")
                .compose(RxUtils.applySchedule())
                .subscribe(s -> {
                    takePhotoLayout.showPhotos(results);
                });
    }


}
