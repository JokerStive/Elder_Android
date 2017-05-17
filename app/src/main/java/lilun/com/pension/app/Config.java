package lilun.com.pension.app;

/**
 * Created by youke on 2017/1/3.
 * 一些配置的常量标识符
 */
public class Config {
    public static final String TAG_LOGGER = "yk";
    //    public static final String BASE_URL = "http://192.168.3.105:3000/api/";
//    public static final String BASE_URL = "http://192.168.3.14:3001/api/";
    public static final String BASE_URL = "http://elder.lishenghuo.com.cn/api/";
//    public static final String BASE_URL = "http://192.168.3.145:3000/api/";

    public static final String MQTT_URL = "tcp://mqtt.lishenghuo.com.cn:1883";
//    public static final String BASE_URL = "http://112.74.97.254:3000/api/";

    public static final String help = "help";

    public static final int time_out = 15;

    public static final int defLoadDatCount = 20;

    public static final int uploadPhotoCount = 3;

    public static final int uploadPhotoMaxHeight = 1080;

    public static final int uploadPhotoMaxWidth = 1920;

    public static final int uploadPhotoMaxSize = 2048000;

    public static final int list_decoration = 5;

    public static final String agency_product_categoryId = "服务/养老机构";

    public static final String residential_product_categoryId = "服务/居家服务";

    public static final String tourism_product_categoryId = "服务/养老机构/养老旅游";


    public static String announce_root = "/公告";

    public static boolean is_debug = true;
}
