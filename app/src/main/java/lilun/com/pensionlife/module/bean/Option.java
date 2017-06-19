package lilun.com.pensionlife.module.bean;

/**
 * Created by Admin on 2017/4/6.
 */
public class Option {
    public String optionKey;

    public String getOptionValue() {
        return optionValue;
    }

    public Option(String optionKey, String optionValue) {
        this.optionKey = optionKey;
        this.optionValue = optionValue;
    }

    public Option setOptionValue(String optionValue) {
        this.optionValue = optionValue;
        return this;
    }

    public String getOptionKey() {
        return optionKey;
    }

    public Option setOptionKey(String optionKey) {
        this.optionKey = optionKey;
        return this;
    }

    public String optionValue;
}

