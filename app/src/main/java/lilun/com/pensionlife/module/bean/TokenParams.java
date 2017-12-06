package lilun.com.pensionlife.module.bean;

/**
 * Created by Admin on 2017/12/6.
 */
public class TokenParams {
    private String modelName;
    private String modelId;
    private String tag;

    public String getModelName() {
        return modelName;
    }

    public TokenParams setModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public String getModelId() {
        return modelId;
    }

    public TokenParams setModelId(String modelId) {
        this.modelId = modelId;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public TokenParams setTag(String tag) {
        this.tag = tag;
        return this;
    }
}
