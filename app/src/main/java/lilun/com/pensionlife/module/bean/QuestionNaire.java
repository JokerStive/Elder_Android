package lilun.com.pensionlife.module.bean;

import java.io.Serializable;

import lilun.com.pensionlife.base.BaseBean;

/**
 * Created by zp on 2017/12/12.
 */


public class QuestionNaire extends BaseBean {

    private Prizedraw prizedraw;

    public Prizedraw getPrizedraw() {
        return prizedraw;
    }

    public void setPrizedraw(Prizedraw prizedraw) {
        this.prizedraw = prizedraw;
    }

   public static class Prizedraw implements Serializable {
        /**
         * id : f70535c0-de3b-11e7-aa18-3bb15149e4e4
         * title : 十九大问卷抽奖活动
         * startTime : 2017-12-11 11:00:00
         * endTime : 2017-12-30 00:00:00
         */
        private String id;
        private String title;
        private String startTime;
        private String endTime;
       private String image;


       public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

       public String getImage() {
           return image;
       }

       public void setImage(String image) {
           this.image = image;
       }
    }
}
