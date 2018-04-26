package lilun.com.pensionlife.pay;

/**
 * 订单状态和支付状态
 */
public class Order {
    public static class Status {
        /**
         * 0: 新订单(已预约);
         */
        public static final int reserved = 0;
        /**
         * 10: 已付款/客户已确认;
         */
        public static final int payed = 10;
        /**
         * 20: 已受理;
         */
        public static final int accepted = 20;
        /**
         * 21: 拒绝退款;
         */
        public static final int refused = 21;
        /**
         * 30: 已发货/已派遣;
         */
        public static final int shiped = 30;
        /**
         * 33: 已到目的地；
         */
        public static final int arrived = 33;
        /**
         * 36: 已签收;
         */
        public static final int signed = 36;
        /**
         * 100: 已完成;
         */
        public static final int completed = 100;
        /**
         * 106: 已经评价
         */
        public static final int assessed = 106;
        /**
         * 200:已取消;
         */
        public static final int canceled = 200;
        /**
         * 201:已延期;
         */
        public static final int delayed = 201;
        /**
         * 206：已退款 ,
         */
        public static final int refunded = 206;
    }

    public static class Paid {

        public static final  long invalid_time=3600;
        /**
         * null/0: 未付款;
         */
        public static final int unpaid = 0;
        /**
         * 1: 已付款;
         */
        public static final int paid = 1;
        /**
         * 2: 正在发起退款;
         */
        public static final int refunding = 2;
        /**
         * -1: 已经退款 ;
         */
        public static final int refunded = -1;
    }

    public static class paymentMethods {
        /**
         * 微信支付
         */
        public static final String weixin = "weixin";
        /**
         * 支付宝支付
         */
        public static final String alipay = "alipay";
        /**
         * 线下支付
         */
        public static final String offline = "offline";
    }


    public static class Type {
        /**
         * phone: 电话预约，没有线上预约
         */
        public static final String phone = "phone";

        /**
         * booking: 线上预约
         */
        public static final String booking = "booking";

        /**
         * payment: 电子支付
         */
        public static final String payment = "payment";
    }
}
