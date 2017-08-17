package lilun.com.pensionlife.widget.numberKeyBoard;

public class KeyModel {

	private Integer code;
	private String label;
	
	public KeyModel(Integer code, String lable){
		this.code = code;
		this.label = lable;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
