package lilun.com.pension.net;

import java.util.List;
import java.util.Map;

import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.AidDetail;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.bean.ElderEdusColleage;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.bean.ProductCategory;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.bean.Rank;
import lilun.com.pension.module.bean.TokenInfo;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 网络接口
 *
 * @author yk
 *         create at 2017/2/13 13:09
 *         email : yk_developer@163.com
 */
public interface ApiService {


    /**
     * token检查
     */

    @GET("Accounts/me")
    Observable<Response<Object>> getMe();

    /**
     * 获取当前账户信息
     */

    @GET("Accounts/{id}/?filter={\"include\":\"defaultOrganization\"}")
    Observable<Response<Account>> getAccountInfo(@Path("id") String accountId);


    /**
     * 获取elderModule分类数据
     */

    @GET("ElderModules")
    Observable<Response<List<ElderModule>>> getElderModules(@Query("filter") String filter);


    /**
     * 获取ProductCategory数据
     */

    @GET("ProductCategories")
    Observable<Response<List<ProductCategory>>> getProductCategories(@Query("filter") String filter);


    /**
     * 获取求助数据
     */

    @GET("Organizations/{id}/children")
    Observable<Response<List<OrganizationAid>>> getOrganizationAids(@Path("id") String organizationId, @Query("filter") String filter);


    /**
     * 获取product
     */

    @GET("OrganizationProducts")
    Observable<Response<List<OrganizationProduct>>> getProducts(@Query("filter") String filter);


    /**
     * 获取organization
     */

    @GET("Organizations/{id}/children")
    Observable<Response<List<Organization>>> getOrganizations(@Path("id") String organizationId, @Query("filter") String filter);


    /**
     * 获取求助数据
     */

    @GET("OrganizationAids/getMyAid")
    Observable<Response<List<OrganizationAid>>> getMyAids(@Query("filter") String filter);

    /**
     * 获取求助详情数据
     */

    @GET("OrganizationAids/{id}/getDetails")
    Observable<Response<AidDetail>> getAidDetail(@Path("id") String aidId);


    /**
     * 获取ActivityCategory
     */

    @GET("OrganizationActivityCategories")
    Observable<Response<List<ActivityCategory>>> getActivityCategories(@Query("filter") String filter);

    /**
     * 获取OrganizationActivity
     */

    @GET("Organizations/{id}/children")
    Observable<Response<List<OrganizationActivity>>> getOrganizationActivities(@Path("id") String organizationId, @Query("filter") String filter);


    /**
     * 获取productOrder
     */

    @GET("ProductOrders")
    Observable<Response<List<ProductOrder>>> getProductOrders(@Query("filter") String filter);


    /**
     * 根据id获取一个organization
     */

    @GET("Organizations/{id}")
    Observable<Response<Organization>> getOrganizationById(@Path("id") String organizationId);


    /**
     * 获取reply集合数据
     */

    @GET("OrganizationReplies")
    Observable<Response<List<OrganizationReply>>> getOrganizationReplies(@Query("filter") String filter);


    /**
//     * 获取information集合数据
//     */

    @GET("OrganizationInformations")
    Observable<Response<List<Information>>> getInformations(@Query("filter") String filter);

//========================POST===============================================

    /**
     * 用户登录
     */
    @POST("Accounts/login")
    Observable<Response<TokenInfo>> login(@Body Account account);


    /**
     * 用户登录
     */
    @POST("OrganizationAids")
    Observable<Response<OrganizationAid>> newOrganizationAid(@Body OrganizationAid organizationAid);


    /**
     * 新增一个reply
     */
    @POST("OrganizationReplies")
    Observable<Response<OrganizationReply>> newOrganizationReply(@Body OrganizationReply reply);


    /**
     * 新增一个rank
     */
    @POST("OrganizationRanks")
    Observable<Response<Object>> newRank(@Body Rank rank);


    /**
     * 上传AID多张图片
     */
    @Multipart
    @POST("OrganizationAids/uploadpicture")
    Observable<Response<List<IconModule>>> newAidIcons(@PartMap Map<String, RequestBody> params);


    //==============================养老教育

    /**
     * 获取organization -- 大学
     */
    @GET("OrganizationActivities")
    Observable<Response<List<OrganizationActivity>>> getOrganizationsActivities(@Query("filter") String filter);

    /**
     * 获取organization -- 大学
     */
    @GET("OrganizationEdus")
    Observable<Response<List<ElderEdusColleage>>> getOrganizationsEdus(@Query("filter") String filter);

    /**
     * 获取organization -- 大学课程
     */
    @GET("OrganizationEdus/{id}/courses")
    Observable<Response<List<EdusColleageCourse>>> getOrganizationsEdusCourse(@Path("id") String courseId, @Query("filter") String filter);

    /**
     * 获取课程详情
     */
    @GET("EduCourses/{id}")
    Observable<Response<EdusColleageCourse>> getEduCourses(@Path("id") String courseId,@Query("filter") String filter);

    /**
     * 参加课程 -- 大学课程
     */
    @POST("EduCourses/{id}/joinCourse")
    Observable<Response<Object>> joinCourse(@Path("id") String courseId, @Query("filter") String filter);

   /**
     * 参加课程 -- 大学课程
     */
    @DELETE("EduCourses/{id}/quitCourse")
    Observable<Response<Object>> quitCourse(@Path("id") String courseId, @Query("filter") String filter);

//==========================================DELETE

    /**
     * 删除一个reply
     */
    @DELETE("OrganizationReplies/{id}")
    Observable<Response<Object>> deleteReply(@Path("id") String replyId);

    /**
     * 删除一个aid
     */
    @DELETE("OrganizationAids/{id}")
    Observable<Response<Object>> deleteAid(@Path("id") String aidId);



//    =========================================PUT

    /**
     * 更新一个aid
     */
    @PUT("OrganizationAids/{id}")
    Observable<Response<Object>> putAid(@Path("id") String aidId, @Body OrganizationAid aid);

    /**
     * 更新一个reply
     */
    @PUT("OrganizationReplies/{id}")
    Observable<Response<Object>> putReply(@Path("id") String replyId, @Body OrganizationReply reply);

}
