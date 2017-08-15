package lilun.com.pensionlife.module.utils;

import android.text.TextUtils;

import org.junit.Test;

import java.util.ArrayList;

import lilun.com.pensionlife.app.User;

import static org.junit.Assert.assertEquals;

/**
 * Created by zp on 2017/8/15.
 */
public class ActUitlsTest {

    @Test
    public void isResisterTopActivity() throws Exception {
        String ctgId = "/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1/考试小区1";
        String curCmmty = "/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1/考试小区1";
        assertEquals(true, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1";
        assertEquals(true, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆/重庆市/演示区/演示街道";
        assertEquals(true, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆/重庆市/演示区";
        assertEquals(true, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆/重庆市";
        assertEquals(true, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆";
        assertEquals(false, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1/考试小区1";
        curCmmty = "/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1";
        assertEquals(false, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1";
        assertEquals(true, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆/重庆市/演示区/演示街道";
        assertEquals(true, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆/重庆市/演示区";
        assertEquals(true, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆/重庆市";
        assertEquals(true, ActUitls.isParentTopActivity(curCmmty, ctgId));

        ctgId = "/地球村/中国/重庆";
        assertEquals(false, ActUitls.isParentTopActivity(curCmmty, ctgId));
        ctgId = "/地球村/中国/重庆/#acti";
        assertEquals(false, ActUitls.isParentTopActivity(curCmmty, ctgId));
    }

}