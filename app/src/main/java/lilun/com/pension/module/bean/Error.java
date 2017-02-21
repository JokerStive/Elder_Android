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
        private String name;
        private int status;
        private String message;
        private int statusCode;
        private String code;
        private String stack;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getStack() {
            return stack;
        }

        public void setStack(String stack) {
            this.stack = stack;
        }
    }
}
