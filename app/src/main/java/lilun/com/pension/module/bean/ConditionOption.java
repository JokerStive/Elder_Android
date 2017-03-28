package lilun.com.pension.module.bean;

/**
*筛选条件
*@author yk
*create at 2017/3/28 9:48
*email : yk_developer@163.com
*/

public class ConditionOption {
    private String key;
    private String conditionKey;

    public ConditionOption(String key, String conditionKey, String conditionValue) {
        this.key = key;
        this.conditionKey = conditionKey;
        this.conditionValue = conditionValue;
    }

    private String conditionValue;

    public String getKey() {
        return key;
    }

    public ConditionOption setKey(String key) {
        this.key = key;
        return this;
    }

    public String getConditionKey() {
        return conditionKey;
    }

    public ConditionOption setConditionKey(String conditionKey) {
        this.conditionKey = conditionKey;
        return this;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public ConditionOption setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
        return this;
    }
}
