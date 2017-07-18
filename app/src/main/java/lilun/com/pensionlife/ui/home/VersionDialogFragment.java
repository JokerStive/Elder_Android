package lilun.com.pensionlife.ui.home;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.module.bean.AppVersion;
import lilun.com.pensionlife.module.utils.SystemUtils;
import lilun.com.pensionlife.ui.home.upload.DownLoadObserver;
import lilun.com.pensionlife.ui.home.upload.DownLoadService;
import lilun.com.pensionlife.ui.home.upload.DownNotification;
import lilun.com.pensionlife.ui.home.upload.DownloadInfo;
import lilun.com.pensionlife.ui.home.upload.DownloadManager;

/**
 * Created by zp on 2017/6/29.
 */

public class VersionDialogFragment extends DialogFragment {
    static String VERSION = "version";
    TextView tvVersionDesp;
    TextView tvVersionUpdate;
    ImageView igClose;
    private ProgressBar progressBar;
    private LinearLayout llVersion;
    private DownNotification downNotification;
    private TextView tvProgress;

    public static VersionDialogFragment newInstance(AppVersion version) {

        Bundle args = new Bundle();
        args.putSerializable(VERSION, version);
        VersionDialogFragment fragment = new VersionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.window_version_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        AppVersion version = (AppVersion) getArguments().getSerializable(VERSION);
        if (version == null) return;
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvVersionDesp = (TextView) view.findViewById(R.id.tv_version_desp);
        tvVersionUpdate = (TextView) view.findViewById(R.id.tv_version_update);
        igClose = (ImageView) view.findViewById(R.id.ig_version_close);
        llVersion = (LinearLayout) view.findViewById(R.id.ll_version);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tvProgress = (TextView) view.findViewById(R.id.tv_progress);


        String forcedUpdate = "";
        if (version.getForced()) {
            igClose.setVisibility(View.GONE);
            forcedUpdate = "您需要升级后才能使用<br/>";
        }
        setCancelable(!version.getForced());
        Spanned spanned = Html.fromHtml(getString(R.string.version_new) + "<font color = '#F7631A'>" + version.getVersion() + "</font><br/>"
                + forcedUpdate + version.getDescription());
        tvVersionDesp.setText(spanned);
        tvVersionUpdate.setOnClickListener(v -> {
            startLoad(version);
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setData(Uri.parse(version.getUrl()));
//            try {
//                startActivity(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        });
        igClose.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void startLoad(AppVersion version) {
        if (version.getForced()) {
            startLoadApk(version);
        } else {
            startLoadApkBackground(version);
        }


    }

    /**
     * 后台下载apk
     */
    private void startLoadApkBackground(AppVersion version) {
        Intent intent = new Intent(getActivity(), DownLoadService.class);
        intent.putExtra("url", version.getUrl());
        getActivity().startService(intent);
        dismiss();
    }


    /**
     * 强制更新下载apk
     */
    private void startLoadApk(AppVersion version) {
        tvVersionUpdate.setVisibility(View.GONE);
        tvProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        DownloadManager.getInstance().download(version.getUrl(), new DownLoadObserver() {
            @Override
            public void onNext(DownloadInfo downloadInfo) {
                super.onNext(downloadInfo);
//                if (downloadInfo.getProgress() == 0) {
//                    Logger.d("总进度 ---" + downloadInfo.getTotal());
//                    Logger.d("保存路径 ---" + downloadInfo.getFileName());
//                    Logger.d("url ---" + version.getUrl());
//                }
//                Logger.d("当前进度 ---" + downloadInfo.getProgress());


                int progress = (int) downloadInfo.getProgress();
                int total = (int) downloadInfo.getTotal();
                progressBar.setMax(total);
                progressBar.setProgress(progress);


                float result = (float) progress / total * 100;
                tvProgress.setText((int) result + "%");
                if (downloadInfo.getProgress() == downloadInfo.getTotal()) {
                    SystemUtils.installApk(getActivity(), downloadInfo.getFilePath());
                    dismiss();
                }
            }

        });
    }


}
