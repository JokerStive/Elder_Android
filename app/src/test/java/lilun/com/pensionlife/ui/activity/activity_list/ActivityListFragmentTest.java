package lilun.com.pensionlife.ui.activity.activity_list;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by zp on 2017/7/4.
 */
public class ActivityListFragmentTest {
    ActivityListFragment fragment = new ActivityListFragment();

    @Test
    public void getCategoryIdJson() {
        String str1 = "/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1/考试小区1/#activity-category.其他";
        String ret1 = "[" +
                "\"/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1/考试小区1/#activity-category.其他\"," +
                "\"/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1/#activity-category.其他\"," +
                "\"/地球村/中国/重庆/重庆市/演示区/演示街道/#activity-category.其他\"," +
                "\"/地球村/中国/重庆/重庆市/演示区/#activity-category.其他\"," +
                "\"/地球村/中国/重庆/重庆市/#activity-category.其他\"" + "]";

        String str2 = "/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1/#activity-category.其他";
        String ret2 = "[" +
                "\"/地球村/中国/重庆/重庆市/演示区/演示街道/考试社区1/#activity-category.其他\"," +
                "\"/地球村/中国/重庆/重庆市/演示区/演示街道/#activity-category.其他\"," +
                "\"/地球村/中国/重庆/重庆市/演示区/#activity-category.其他\"," +
                "\"/地球村/中国/重庆/重庆市/#activity-category.其他\"" + "]";


        String str3 = "/地球村/中国/重庆/重庆市/演示区/演示街道/#activity-category.其他";
        String ret3 = "[" +
                "\"/地球村/中国/重庆/重庆市/演示区/演示街道/#activity-category.其他\"," +
                "\"/地球村/中国/重庆/重庆市/演示区/#activity-category.其他\"," +
                "\"/地球村/中国/重庆/重庆市/#activity-category.其他\"" + "]";
        String str4 = "/地球村/中国/重庆/重庆市/演示区/#activity-category.其他";
        String ret4 = "[" +
                "\"/地球村/中国/重庆/重庆市/演示区/#activity-category.其他\"," +
                "\"/地球村/中国/重庆/重庆市/#activity-category.其他\"" + "]";

        String str5 = "/地球村/中国/重庆/重庆市/#activity-category.其他";
        String ret5 = "[" +
                "\"/地球村/中国/重庆/重庆市/#activity-category.其他\"" + "]";
        String str6 = "/地球村/中国/重庆/#activity-category.其他";
        String ret6 = "[" + "]";
        String str7 = "";
        String ret7 = "[" + "]";
        String str8 = null;
        String ret8 = "[" + "]";


        System.out.print(fragment.getCategoryIdJson(str1) + "\n\n");
        assertEquals(ret1, fragment.getCategoryIdJson(str1));

        System.out.print(fragment.getCategoryIdJson(str2) + "\n\n");
        assertEquals(ret2, fragment.getCategoryIdJson(str2));

        System.out.print(fragment.getCategoryIdJson(str3) + "\n\n");
        assertEquals(ret3, fragment.getCategoryIdJson(str3));

        System.out.print(fragment.getCategoryIdJson(str4) + "\n\n");
        assertEquals(ret4, fragment.getCategoryIdJson(str4));

        System.out.print(fragment.getCategoryIdJson(str5) + "\n\n");
        assertEquals(ret5, fragment.getCategoryIdJson(str5));

        System.out.print(fragment.getCategoryIdJson(str6) + "\n\n");
        assertEquals(ret6, fragment.getCategoryIdJson(str6));

        System.out.print(fragment.getCategoryIdJson(str7) + "\n\n");
        assertEquals(ret7, fragment.getCategoryIdJson(str7));
        System.out.print(fragment.getCategoryIdJson(str8) + "\n\n");
        assertEquals(ret8, fragment.getCategoryIdJson(str8));
    }

}