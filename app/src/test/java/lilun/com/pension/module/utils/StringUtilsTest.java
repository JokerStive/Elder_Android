package lilun.com.pension.module.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zp on 2017/6/9.
 */
public class StringUtilsTest {
    @Test
    public void isResisterTopCommunity() throws Exception {
        String current = "/地球村/中国/重庆/重庆市/江北区/观音桥街道/富力海洋社区";
        String register = "/地球村/中国/重庆/重庆市/江北区/观音桥街道/富力海洋社区/万科兴城";
        assertEquals(true, StringUtils.isResisterTopCommunity(current, register));

        current = "/地球村/中国/重庆/重庆市/江北区/观音桥街道/富力海洋社区/万科兴城";
        register = "/地球村/中国/重庆/重庆市/江北区/观音桥街道/富力海洋社区/万科兴城";
        assertEquals(true, StringUtils.isResisterTopCommunity(current, register));

        current = "/地球村/中国/重庆/重庆市/江北区/观音桥街道/富力海洋社区/其他";
        register = "/地球村/中国/重庆/重庆市/江北区/观音桥街道/富力海洋社区/万科兴城";
        assertEquals(false, StringUtils.isResisterTopCommunity(current, register));

        current = "/地球村/中国/重庆/重庆市/江北区/观音桥街道/富力海洋社区";
        register = "/地球村/中国/重庆/重庆市/江北区/观音桥街道/富力海洋社区";
        assertEquals(true, StringUtils.isResisterTopCommunity(current, register));

        current = "/地球村/中国/重庆/重庆市/江北区/观音桥街道";
        register = "/地球村/中国/重庆/重庆市/江北区/观音桥街道/富力海洋社区";
        assertEquals(false, StringUtils.isResisterTopCommunity(current, register));
        current = "/地球村/中国/重庆/重庆市/江北区/观音桥街道";
        register = "/地球村/中国/重庆/重庆市/江北区/观音桥街道";
        assertEquals(false, StringUtils.isResisterTopCommunity(current, register));
    }

}