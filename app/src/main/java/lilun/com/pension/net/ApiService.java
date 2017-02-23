package lilun.com.pension.net;

import java.util.List;

import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.bean.ProductCategory;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.bean.TokenInfo;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
*网络接口
*@author yk
*create at 2017/2/13 13:09
*email : yk_developer@163.com
*/
public interface ApiService {


    /**
     *获取当前账户信息
     */

    @GET("Accounts/{id}/?filter={\"include\":\"defaultOrganization\"}")
    Observable<Response<Account>> getAccountInfo(@Path("id") String accountId);



    /**
     *获取elderModule分类数据
     */

    @GET("ElderModules")
    Observable<Response<List<ElderModule>>> getElderModules(@Query("filter") String filter);


    /**
     *获取ProductCategory数据
     */

    @GET("ProductCategories")
    Observable<Response<List<ProductCategory>>> getProductCategories(@Query("filter") String filter);



    /**
     *获取求助数据
     */

    @GET("Organizations/{id}/children")
    Observable<Response<List<OrganizationAid>>> getOrganizationAids(@Path("id") String organizationId,@Query("filter") String filter);


    /**
     *获取product
     */

    @GET("OrganizationProducts")
    Observable<Response<List<OrganizationProduct>>> getProducts(@Query("filter") String filter);


    /**
     *获取organization
     */

    @GET("Organizations/{id}/children")
    Observable<Response<List<Organization>>> getOrganizations(@Path("id") String organizationId, @Query("filter") String filter);



    /**
     *获取求助数据
     */

    @GET("OrganizationAids/getMyAid")
    Observable<Response<List<OrganizationAid>>> getMyAids(@Query("filter") String filter);

    /**
     *获取求助详情数据
     */

    @GET("OrganizationAids/{id}/getDetails")
    Observable<Response<OrganizationAid>> getAidDetail(@Path("id") String aidId);


    /**
     *获取ActivityCategory
     */

    @GET("OrganizationActivityCategories")
    Observable<Response<List<ActivityCategory>>> getActivityCategories(@Query("filter") String filter);

    /**
     *获取OrganizationActivity
     */

    @GET("Organizations/{id}/children")
    Observable<Response<List<OrganizationActivity>>> getOrganizationActivities(@Path("id") String organizationId, @Query("filter") String filter);


    /**
     *获取productOrder
     */

    @GET("ProductOrders")
    Observable<Response<List<ProductOrder>>> getProductOrders( @Query("filter") String filter);


    /**
     *根据id获取一个organization
     */

    @GET("Organizations/{id}")
    Observable<Response<Organization>> getOrganizationById(@Path("id") String organizationId);

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


}
