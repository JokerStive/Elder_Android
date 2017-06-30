package lilun.com.pensionlife.module.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by zp on 2017/6/28.
 */
public class VersionCheckTest {
    @Test
    public void compareVersion() throws Exception {
        String nowVersion = "1.1.0";
        String retVersion = "1.1.1";
        assertEquals(true, VersionCheck.compareVersion(nowVersion, retVersion));
        nowVersion = "1.1.0";
        retVersion = "1.2.1";
        assertEquals(true, VersionCheck.compareVersion(nowVersion, retVersion));
        nowVersion = "1.1.0";
        retVersion = "2.1.1";
        assertEquals(true, VersionCheck.compareVersion(nowVersion, retVersion));
        nowVersion = "1.1.1";
        retVersion = "1.1.1";
        assertEquals(false, VersionCheck.compareVersion(nowVersion, retVersion));
        nowVersion = "";
        retVersion = "";
        assertEquals(false, VersionCheck.compareVersion(nowVersion, retVersion));
        nowVersion = "1.2";
        retVersion = "1.1.1";
        assertEquals(false, VersionCheck.compareVersion(nowVersion, retVersion));
        nowVersion = "1.2.22";
        retVersion = "1.1.11";
        assertEquals(false, VersionCheck.compareVersion(nowVersion, retVersion));
        nowVersion = "21.2.22";
        retVersion = "1.1.11";
        assertEquals(false, VersionCheck.compareVersion(nowVersion, retVersion));
        nowVersion = "21.2.22";
        retVersion = "31.1.11";
        assertEquals(true, VersionCheck.compareVersion(nowVersion, retVersion));
        nowVersion = "21.2.22.1";
        retVersion = "22.1.11.2";
        assertEquals(false, VersionCheck.compareVersion(nowVersion, retVersion));
    }

}