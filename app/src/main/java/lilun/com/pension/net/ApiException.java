package lilun.com.pension.net;

import lilun.com.pension.module.bean.Error;

/**
 * Created by youke on 2016/12/29.
 * api异常，统一处理
 */
public class ApiException extends RuntimeException {
    private int errorCode;
    private String errorMessage;
    private Error.ErrorBean  error;

    public Error.ErrorBean  getError() {
        return error;
    }

    public ApiException setError(Error.ErrorBean  error) {
        this.error = error;
        return this;
    }

    public ApiException(int code, String message,Error.ErrorBean  error){
        this.errorCode=code;
        this.errorMessage=message;
        this.error=error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ApiException setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ApiException setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }
}