package lilun.com.pensionlife.net;

import java.util.List;

import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.ActivityCategory;
import lilun.com.pensionlife.module.bean.ActivityDetail;
import lilun.com.pensionlife.module.bean.ActivityEvaluate;
import lilun.com.pensionlife.module.bean.AidDetail;
import lilun.com.pensionlife.module.bean.AppVersion;
import lilun.com.pensionlife.module.bean.Area;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.EdusColleageCourse;
import lilun.com.pensionlife.module.bean.ElderEdus;
import lilun.com.pensionlife.module.bean.ElderEdusColleage;
import lilun.com.pensionlife.module.bean.ElderModule;
import lilun.com.pensionlife.module.bean.IconModule;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.NestedReply;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.OrganizationReply;
import lilun.com.pensionlife.module.bean.ProductCategory;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.bean.Rank;
import lilun.com.pensionlife.module.bean.Register;
import lilun.com.pensionlife.module.bean.TokenInfo;
import lilun.com.pensionlife.module.bean.Tourism;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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


//    ====================用户账户相关

    @GET("APPs/{appName}/version/{versionName}")
    Observable<Response<AppVersion>> getVersionInfo(@Path("appName") String appName, @Path("versionName") String versionName);

    /**
     * token检查
     */

    @GET("Accounts/me")
    Observable<Response<Object>> getMe();


    /**
     * 获取位置
     */

//    @GET("Accounts/getChildLocation")
//    Observable<Response<List<String>>> getChildLocation(@Query("locationName") String locationName);

    /**
     * 获取所属组织列表
     */

    @GET("Accounts/{id}/organizations")
    Observable<Response<List<OrganizationAccount>>> getOrganizationAccounts(@Path("id") String organizationId);


    /**
     * 获取当前账户信息
     */

    @GET("Accounts/{id}")
    Observable<Response<Account>> getAccountInfo(@Path("id") String accountId);

    /**
     * 更新一个account
     */
    @PUT("Accounts/{id}")
    Observable<Response<Account>> putAccount(@Path("id") String accountId, @Body Account account);

    /**
     * 更新注册地区 （个人中心修改使用）
     */
    @FormUrlEncoded
    @PUT("Accounts/location")
    Observable<Response<Account>> putAccountLocation(@Field("Location") String location);

    /**
     * 更新注册地区 (注册时使用)
     */
    @FormUrlEncoded
    @PUT("Accounts/location")
    Observable<Response<Account>> putAccountLocation(@Field("Location") String location, @Field("address") String address);


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

    @GET("OrganizationAids/Aids")
    Observable<Response<List<OrganizationAid>>> getOrganizationAids(@Query("filter") String filter);


    /**
     * 获取product
     */

    @GET("OrganizationProducts")
    Observable<Response<List<OrganizationProduct>>> getProducts(@Query("filter") String filter);


    /**
     * 获取某一个product
     */

    @GET("OrganizationProducts/{id}")
    Observable<Response<OrganizationProduct>> getProduct(@Path("id") String productId,@Query("filter") String filter);


    /**
     * 获取旅游列表
     */

    @GET("OrganizationProducts")
    Observable<Response<List<Tourism>>> getTourisms(@Query("filter") String filter);


    /**
     * 获取旅游
     */

    @GET("OrganizationProducts/{id}")
    Observable<Response<Tourism>> getTourism(@Path("id") String tourismId);


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
    Observable<Response<AidDetail>> getAidDetail(@Path("id") String aidId, @Query("replyFilter") String filter);


    /**
     * 根据id获取一个organization
     */

    @GET("Organizations/{id}")
    Observable<Response<Organization>> getOrganizationById(@Path("id") String organizationId, @Query("filter") String filter);


    /**
     * 获取reply集合数据
     */

    @GET("OrganizationReplies")
    Observable<Response<List<OrganizationReply>>> getOrganizationReplies(@Query("filter") String filter);


    /**
     * 获取评价集合数据
     */

    @GET("OrganizationRanks")
    Observable<Response<List<Rank>>> getRanks(@Query("filter") String filter);


    /**
     * 获取某个订单
     */

    @GET("ProductOrders/{id}")
    Observable<Response<ProductOrder>> getOrder(@Path("id") String orderId, @Query("filter") String filter);

    /**
     * 获取订单列表数据
     */

    @GET("ProductOrders")
    Observable<Response<List<ProductOrder>>> getOrders(@Query("filter") String filter);


    /**
     * 获取某一个product的订单列表数据
     */

    @GET("OrganizationProducts/{id}/orders")
    Observable<Response<List<ProductOrder>>> getOrdersOfProduct(@Path("id") String productId, @Query("filter") String filter);


    /**
     * //     * 获取information集合数据
     * //
     */

    @GET("OrganizationInformations")
    Observable<Response<List<Information>>> getInformations(@Query("filter") String filter);

//========================POST===============================================

    /**
     * 用户登录
     */
    @POST("Accounts/login")
    Observable<Response<TokenInfo>> login(@Body Account account);


    /**
     * 新增一个aid
     */
    @POST("OrganizationAids")
    Observable<Response<OrganizationAid>> newOrganizationAid(@Body OrganizationAid organizationAid);


    /**
     * 新增一个reply
     */
    @POST("OrganizationReplies")
    Observable<Response<OrganizationReply>> newOrganizationReply(@Body OrganizationReply reply);


    /**
     * 新增aid一个reply
     */
    @POST("OrganizationAids/{id}/answer")
    Observable<Response<Object>> newAidReply(@Path("id") String aidId, @Body OrganizationReply reply);


    /**
     * 新增一个个人资料
     */
    @POST("Contacts")
    Observable<Response<Contact>> newContact(@Body Contact contacts);


    /**
     * 新增一个rank
     */
    @POST("OrganizationRanks")
    Observable<Response<Object>> newRank(@Body Rank rank);


    /**
     * 预约订单
     */
    @FormUrlEncoded
    @POST("OrganizationProducts/{id}/createOrder")
    Observable<Response<ProductOrder>> createOrder(@Path("id") String productId, @Field("userInforId") String infoId, @Field("registerDate") String registerDate);


    /**
     * 上传AID多张图片
     */
//    @Multipart
//    @POST("OrganizationAids/upload/image")
//    Observable<Response<List<IconModule>>> newAidAndIcons(@PartMap Map<String, RequestBody> params);
//    @Multipart
    @POST("OrganizationAids/upload/image")
    Observable<Response<Object>> newAidAndIcons(@Body MultipartBody body);


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
    Observable<Response<EdusColleageCourse>> getEduCourses(@Path("id") String courseId, @Query("filter") String filter);

    /**
     * 我参加的课程 --
     */
    @GET("Accounts/getMyCourse")
    Observable<Response<List<ElderEdus>>> getMyCourse(@Query("filter") String filter);

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


//========================================社区活动

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
     * 获取OrganizationActivity详情
     */

    @GET("OrganizationActivities/{id}/details")
    Observable<Response<ActivityDetail>> getOrganizationActivitiesDetail(@Path("id") String organizationId, @Query("filter") String filter);

    /**
     * 根据类型 获取OrganizationActivity列表
     */
    @GET("OrganizationActivities")
    Observable<Response<List<OrganizationActivity>>> getOrganizationActivities(@Query("filter") String filter);

    @GET("OrganizationActivities/myActivities")
    Observable<Response<List<OrganizationActivity>>> getMyActivities(@Query("filter") String filter);

    /**
     * 新建一个活动
     * 包含图片
     */
    @POST("OrganizationActivities")
    Observable<Response<OrganizationActivity>> newActivity(@Body MultipartBody body);


    /**
     * 加入一个活动
     */
    @POST("OrganizationActivities/{id}/join")
    Observable<Response<Object>> joinActivity(@Path("id") String activityId);


    /**
     * 退出一个活动
     */
    @DELETE("OrganizationActivities/{id}/quit")
    Observable<Response<Object>> quitActivity(@Path("id") String activityId);
//
//    /**
//     * 删除一个活动
//     */
//    @DELETE("OrganizationActivities/{id}")
//    Observable<Response<Object>> cancelActivity(@Path("id") String activityId);


    /**
     * 上传活动及其多张图片
     */
    @POST("OrganizationActivities/upload/icon")
    Observable<Response<OrganizationActivity>> newActivityIcons(@Body MultipartBody params);


    /**
     * 添加一个活动提问
     *
     * @param activityId
     * @param question
     * @return
     */
    @POST("OrganizationActivities/{id}/questions")
    Observable<Response<OrganizationReply>> addQuestion(@Path("id") String activityId, @Body OrganizationReply question);

    @GET("OrganizationActivities/{id}/replyList")
    Observable<Response<List<NestedReply>>> replyList(@Path("id") String activityId, @Query("ReplyFilter") String filter);

    /**
     * 添加一个活动提问回答
     *
     * @param activityId
     * @param questionId
     * @param question
     * @return
     */
    @POST("OrganizationActivities/{id}/questions/{questionId}/answer")
    Observable<Response<OrganizationReply>> addAnswer(@Path("id") String activityId, @Path("questionId") String questionId, @Body OrganizationReply question);

    /**
     * 参与活动的列表
     *
     * @param activityId
     * @return
     */
    @GET("OrganizationActivities/{id}/partners")
    Observable<Response<List<Account>>> queryPartners(@Path("id") String activityId, @Query("filter") String filter);


    /**
     * 踢出某一个参与者
     *
     * @param activityId
     * @param userId
     * @return
     */
    @DELETE("OrganizationActivities/{id}/rel/partner")
    Observable<Response<Account>> deleteOfPartners(@Path("id") String activityId, @Header("usersId") String userId);

    /**
     * 踢出参与者并加入黑名单
     *
     * @param activityId
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @PUT("OrganizationActivities/{id}/rel/blockUser")
    Observable<Response<Object>> addBlockUser(@Path("id") String activityId, @Field("usersId") String userId);

    /**
     * 关闭（解散）活动
     *
     * @param activityId
     * @return
     */
    @PUT("OrganizationActivities/{id}/cancelActivity")
    Observable<Response<Object>> cancelActivity(@Path("id") String activityId);

    /**
     * 获取活动评价星级
     *
     * @param activityId
     * @return
     */
    @GET("OrganizationActivities/{id}/Rank")
    Observable<Response<ActivityEvaluate>> getActivityRank(@Path("id") String activityId);

    /**
     * 提交我的评价星级
     *
     * @param activityId
     * @return
     */

    @POST("OrganizationActivities/{id}/Rank")
    Observable<Response<ActivityEvaluate>> postActivityRank(@Path("id") String activityId, @Header("rank") String rank);

    /**
     * 平均评价
     *
     * @param activityId
     * @return
     */
    @GET("OrganizationActivities/{id}/AVGRank")
    Observable<Response<Object>> getAvgRank(@Path("id") String activityId);
//    ================================================================

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


    /**
     * 删除一个aid
     */
    @DELETE("Contacts/{id}")
    Observable<Response<Object>> deleteContact(@Path("id") String contactId);


//    =========================================PUT

    /**
     * 更新一个aid
     */
    @PUT("OrganizationAids/{id}")
    Observable<Response<Object>> putAid(@Path("id") String aidId, @Body OrganizationAid aid);

    /**
     * 删除aid的回答
     */
    @DELETE("OrganizationAids/{aidId}/answer/{replyId}")
    Observable<Response<Object>> deleteAidAnswer(@Path("aidId") String aidId, @Path("replyId") String replyId);

    /**
     * 更新个人资料
     */
    @PUT("Contacts/{id}")
    Observable<Response<Object>> putContact(@Path("id") String contactId, @Body Contact contact);


    /**
     * 修改订单状态
     */
    @FormUrlEncoded
    @PUT("ProductOrders/{id}/changeStatus")
    Observable<Response<Object>> changeOrderStatus(@Path("id") String productId, @Field("status") String status);

    /**
     * 修改订单
     */
    @FormUrlEncoded
    @PUT("ProductOrders/{id}/communicationRemark")
    Observable<Response<Object>> putMerchantMemoOrder(@Path("id") String orderId, @Field("status") String status,
                                                      @Field("callStatus") String callStatus,
                                                      @Field("remark") String remark,
                                                      @Field("delayTime") String delayTime);


    /**
     * 修改默认资料
     */
    @PUT("Contacts/{id}/changeDefault")
    Observable<Response<Object>> putDefContact(@Path("id") String contactId);


//    ====================养老机构

    /**
     * 获取预约登记信息列表
     */
    @GET("Contacts")
    Observable<Response<List<Contact>>> getContacts(@Query("filter") String filter);


    /**
     * 获取预约登记信息
     */
    @GET("Contacts/{id}")
    Observable<Response<Contact>> getContact(@Path("id") String contactId);

    /**
     * 获取获取电话号码验证码
     * type: 2 修改密码   1 注册用户
     */
    @GET("Accounts/IDCode")
    Observable<Response<Object>> getIDCode(@Query("mobile") String phone, @Query("type") String type);

    /**
     * 获取获取身份验证
     */
    @POST("Accounts/identifyUser")
    Observable<Response<TokenInfo>> getIdentifyUser(@Header("mobile") String phone, @Header("Code") String code);

    /**
     * 修改密码
     */
    @PUT("Accounts/changePassword")
    Observable<Response<Object>> changePassword(@Header("newPassword") String password, @Header("Authorization") String takon);

    /**
     * 检测验证码
     */
    @POST("Accounts/IDCode")
    Observable<Response<Boolean>> checkIDCode(@Query("mobile") String phone, @Query("IDCode") String aIDCode);


    @GET("Accounts/getChildLocation")
    Observable<Response<List<Area>>> getChildLocation(@Query("locationName") String locationName, @Query("skipNumber") int skipNumber, @Query("limitNumber") int limitNumber);

    /**
     * 1.1.5以前使用，1.1.6 已弃用
     *
     * @param organizationId 组织id :/地球村/中国/重庆/重庆市/南岸区/铜元局街道
     * @param IDCode         验证码
     * @param address        居住地址： 重庆市南岸区铜元局街道盘龙花园。。。
     * @return
     */
    /**
     * @deprecated
     */
    @POST("Accounts/register/OrgID/{organizationId}/IDCode/{IDCode}/address/{address}")
    Observable<Response<Register>> commitRegister(@Path("organizationId") String organizationId, @Path("IDCode") String IDCode, @Path("address") String address, @Body Account account);

    @POST("Accounts/register/code/{IDCode}")
    Observable<Response<Register>> commitRegister(@Path("IDCode") String IDCode, @Body Account account);


    /**
     * 上传/更新用户头像
     */
    @Multipart
    @PUT("Accounts/{id}/update/image/{imageName}")
    Observable<Response<IconModule>> updateImage(@Path("id") String id, @Path("imageName") String imageName, @Part("file\"; filename=\"avatar.png") RequestBody file);
}
