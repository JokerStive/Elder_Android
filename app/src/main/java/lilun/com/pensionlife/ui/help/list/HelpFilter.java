package lilun.com.pensionlife.ui.help.list;

import android.text.TextUtils;

/**
 * 互助列表的过滤条件
 *
 * @author yk
 *         create at 2017/4/17 13:12
 *         email : yk_developer@163.com
 */
public class HelpFilter {

    @Override
    public String toString() {
        return "{\"order\":\"createdAt DESC\",\"where\":{\"isDraft\":false,\"title\":{\"like\":\"" + title + "\"}" + organizationId + kind + priority + status + "}}";
    }

    private String title = "";
    private String kind = ",\"kind\":{\"neq\":2}";
    private String priority = "";
    private String status = "";
    private String organizationId = "";

    public String getOrganizationId() {
        return organizationId;
    }

    public HelpFilter setOrganizationId(String id) {
        this.organizationId = TextUtils.isEmpty(id) ? "" : ",\"organizationId\":\"" + id + "/#aid" + "\"";
        return this;
    }

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

    // TODO: 2017/5/19 --zp 修改不显示kind = 2 的情况

    public HelpFilter setKind(String kind) {
        this.kind = TextUtils.isEmpty(kind) ? ",\"kind\":{\"neq\":2}" : ",\"kind\":" + kind + "";
        return this;
    }

    public String getPriority() {
        return priority;
    }

    public HelpFilter setPriority(String priority) {
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
