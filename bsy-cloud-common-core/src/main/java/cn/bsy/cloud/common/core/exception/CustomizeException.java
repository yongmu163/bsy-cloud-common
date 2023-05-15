package cn.bsy.cloud.common.core.exception;
import lombok.Data;

/**
 * @author gaoh
 * @desc 自定义异常类
 * @date 2022年01月08日 下午 10:44
 */
@Data
public class CustomizeException extends RuntimeException {
    private String msg;
    private int code = 500;

    public CustomizeException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CustomizeException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public CustomizeException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public CustomizeException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}