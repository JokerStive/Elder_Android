package lilun.com.pensionlife.pay;

/**
 * 订单状态和支付状态
 */
public class Order {
    public static class Status {

        public static String getStatusMapping(int status, Integer paid) {
            String result = "";
            switch (status) {
                case reserved:
                    result = "待付款";
                    break;

                case payed:
                    if (paid != null) {
                        result = "已付款";
                    } else {
                        result = "已预约";
                    }
                    break;

                case accepted:
                    result = "已受理";
                    break;

                case refused:
                    result = "拒绝退款";
                    break;


                case shiped:
                    result = "已发货";
                    break;


                case arrived:
                    result = "已到达";
                    break;


                case signed:
                    result = "已签收";
                    break;


                case completed:
                    result = "已完成";
                    break;


                case assessed:
                    result = "已评价";
                    break;


                case canceled:
                    result = "已取消";
                    break;


                case delayed:
                    result = "已延期";
                    break;

                case refunded:
                    result = "已退款";
                    break;

            }

            return result;
        }


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

        public static final long invalid_time = 3600 * 1000;
        /**
         * null/0: 未付款;
         */
        public static final int unpaid = 0;
        /**
         * 1: 已付款;
         */
        public static final int paid = 1;

        /**
         * 2: 已线下付款;
         */
        public static final int paidOffline = 2;
        /**
         * 2: 正在发起退款;
         */
        public static final int refunding = 32;
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
