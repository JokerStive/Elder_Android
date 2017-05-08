package lilun.com.pension.ui.help.list;

import android.text.TextUtils;

/**
 * 互助列表的过滤条件
 *
 * @author yk
 *         create at 2017/4/17 13:12
 *         email : yk_developer@163.com
 */
public class HelpFilter {
//    private String priorityKey = "";
//    private String statusKey = "";

    @Override
    public String toString() {
        return "{\"order\":\"createdAt DESC\",\"where\":{\"kind\":" + kind + priority + status + "," + "\"title\":{\"like\":\"" + title + "\"}}}";
    }

    private String title = "";
    private String kind = "{\"neq\":\"2\"}";
    private String priority = "";
    private String status = "";

    public String getTitle() {
        return title;
    }

    public HelpFilter setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getKind() {

        return kind;
    }

    public HelpFilter setKind(String kind) {
        this.kind = TextUtils.isEmpty(kind) ? "{\"neq\":2}" : kind;
        return this;
    }

    public String getPriority() {
        return priority;
    }

    public HelpFilter setPriority(String priority) {
//        priorityKey = TextUtils.isEmpty(priority) ? "" : "priority";
        this.priority = TextUtils.isEmpty(priority) ? "" : ",\"priority\":\"" + priority + "\"";
        return this;
    }

    public String getStatus() {
        return status;
    }

    public HelpFilter setStatus(String status) {
        this.status = TextUtils.isEmpty(status) ? "" : ",\"status\":\"" + status + "\"";
        return this;
    }
}
