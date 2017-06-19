package lilun.com.pensionlife.module.bean;

import java.util.List;

/**
 * 筛选条件
 *
 * @author yk
 *         create at 2017/3/28 9:48
 *         email : yk_developer@163.com
 */

public class ConditionOption {
    private String type;
    private List<Option> val;
    private String condition;
    private String layoutCategory;

    public String getLayoutCategory() {
        return layoutCategory;
    }

    public ConditionOption setLayoutCategory(String layoutCategory) {
        this.layoutCategory = layoutCategory;
        return this;
    }

    public ConditionOption(String key, String conditionKey, List<Option> conditionValue) {
        this.type = key;
        this.condition = conditionKey;
        this.val = conditionValue;
    }

//    public ConditionOption(String key, String conditionKey) {
//        this.type = key;
//        this.condition = conditionKey;
//        this.val = new ArrayList<>();
//        val.add(conditionKey);
//    }

    public String getType() {
        return type;
    }

    public ConditionOption setType(String type) {
        this.type = type;
        return this;
    }

    public String getCondition() {
        return condition;
    }

    public ConditionOption setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    public List<Option> getVal() {
        return val;
    }

    public ConditionOption setVal(List<Option> val) {
        this.val = val;
        return this;
    }


}
