package lilun.com.pensionlife.ui.education.course_list;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.EduCourseAdapter;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.bean.Provider;
import lilun.com.pensionlife.module.bean.Semester;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.ui.education.colleage_details.CollegeDetailFragment;
import lilun.com.pensionlife.ui.education.course_details.CourseDetailFragment;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;


/**
 * 老年教育 课程列表 V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class CourseListFragment extends BaseFragment<CourseListContract.Presenter> implements CourseListContract.View {
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private EduCourseAdapter mEduCourseAdapter;
    private String mCollegeId;
    private CourseListFilter mFilter;
    private ImageView ivCollegeIcon;
    private TextView tvCollegeDesc;
    private WebView webView;


    public static CourseListFragment newInstance(String collegeId) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putString("collegeId", collegeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mCollegeId = arguments.getString("collegeId");
        Preconditions.checkNull(mCollegeId);
        mFilter = new CourseListFilter(mCollegeId);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new CourseListPresenter();
        mPresenter.bindView(this);
        mPresenter.getCollege(mCollegeId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_course_list;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getDataList(0);
        }
    }


    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle("报名班级");
        titleBar.setOnBackClickListener(this::pop);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, (int) App.context.getResources().getDimension(R.dimen.dp_1), Color.parseColor("#f5f5f9")));

        mEduCourseAdapter = new EduCourseAdapter(new ArrayList<>());
        mEduCourseAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrganizationProduct course = mEduCourseAdapter.getItem(position);
            assert course != null;
            start(CourseDetailFragment.newInstance(course.getId()));
        });
        mEduCourseAdapter.setOnLoadMoreListener(() -> getDataList(mEduCourseAdapter.getItemCount()), mRecyclerView);
        setHead();

        mRecyclerView.setAdapter(mEduCourseAdapter);

    }

    private void setHead() {
        RelativeLayout head = (RelativeLayout) LayoutInflater.from(App.context).inflate(R.layout.head_course_list, null);
        ivCollegeIcon = (ImageView) head.findViewById(R.id.iv_college_icon);
        tvCollegeDesc = (TextView) head.findViewById(R.id.tv_college_desc);


        head.setOnClickListener(v -> {
            //大学简介
            start(CollegeDetailFragment.newInstance(mCollegeId));
        });
        mEduCourseAdapter.addHeaderView(head);
    }


    /**
     * 获取班级（产品）列表
     */
    private void getDataList(int skip) {
        String gtmTime = StringUtils.currentTime();
        mFilter.setTime(gtmTime);
        Gson gson = new Gson();
        String filter = gson.toJson(mFilter);
        Logger.d("班级 filter ----- " + filter);
        mPresenter.getCourses(filter, skip);
    }


    @Override
    public void showCollageCourseList(List<OrganizationProduct> courses, boolean isLoadMore) {
        if (isLoadMore) {
            mEduCourseAdapter.addAll(courses, true);
        } else {
            mEduCourseAdapter.replaceAll(courses);
        }

    }

    @Override
    public void showCollege(Organization college) {
        ImageLoaderUtil.instance().loadImage(college.getIcon(), ivCollegeIcon);
        Provider provider = college.getProvider();
        if (provider != null) {
            String url = provider.getContext();
            webView = new WebView(App.context);
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setLoadsImagesAutomatically(false);
            settings.setBlockNetworkImage(true);

            webView.addJavascriptInterface(new MyJavaScriptInterface(tvCollegeDesc), "INTERFACE");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
                }
            });
            webView.loadUrl(url);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.loadUrl(null);
        webView.destroy();
        webView = null;
    }


        class MyJavaScriptInterface
        {
            private TextView contentView;

            public MyJavaScriptInterface(TextView aContentView)
            {
                contentView = aContentView;
            }

            @JavascriptInterface
            public void processContent(String aContent)
            {
                final String content = aContent;
                contentView.post(new Runnable()
                {
                    public void run()
                    {
                        contentView.setText(content);
                    }
                });
            }

    }

    @Override
    public void completeRefresh() {
    }

    @Override
    public void getCategorySuccess(List<OrganizationProductCategory> categories) {

    }

    @Override
    public void getSemesterSuccess(List<Semester> semesters) {
    }


}
