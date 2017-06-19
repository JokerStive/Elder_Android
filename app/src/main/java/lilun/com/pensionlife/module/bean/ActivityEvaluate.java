package lilun.com.pensionlife.module.bean;

import lilun.com.pensionlife.base.BaseBean;

/**
 * Created by zp on 2017/4/21.
 */

public class ActivityEvaluate extends BaseBean {
    private int myRanking;
    private int avgRanking;

    public int getRank() {
        return myRanking;
    }

    public void setRank(int rank) {
        this.myRanking = rank;
    }

    public int getAvgRank() {
        return avgRanking;
    }

    public void setAvgRank(int avgRank) {
        this.avgRanking = avgRank;
    }
}
