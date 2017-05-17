package lilun.com.pension.module.bean;

/**
 * Created by yk on 2017/1/6.
 * 异常模型
 */
public class Error {

    private ErrorBean error;

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public static class ErrorBean {
        private int status;
        private String message;



        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


    }
}
