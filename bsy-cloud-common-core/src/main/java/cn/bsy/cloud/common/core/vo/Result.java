package cn.bsy.cloud.common.core.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.http.HttpStatus;

import java.io.Serializable;

/**
 * @author gaoh
 * @desc 统一返回接口格式
 * @date 2022年01月08日 下午 10:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {
    private static final String SUCCESS = "success";
    /**
     * 状态码
     */
    @JsonProperty(index = 0)
    private Integer code;
    /**
     * 响应消息
     */
    @JsonProperty(index = 1)
    private String message;
    /**
     * 泛型响应消息
     */
    @JsonProperty(index = 2)
    private T data;
    /**
     * 刷新后的tokne
     */
    @JsonProperty(index = 3)
    private String refreshedJwtToken;

    public static <T> Result<T> ok() {
        return restResult(HttpStatus.SC_OK, SUCCESS, null);
    }

    public static <T> Result<T> ok(T data) {
        return restResult(HttpStatus.SC_OK, SUCCESS, data);
    }

    public static <T> Result<T> ok(String message, T data) {
        return restResult(HttpStatus.SC_OK, message, data);
    }

    public static <T> Result<T> error(String message) {
        return restResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, message, null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return restResult(code, message, null);
    }

    public static <T> Result<T> error(String message, T data) {
        return restResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, message, data);
    }

    public static <T> Result<T> error(Integer code, String message, T data) {
        return restResult(code, message, data);
    }

    private static <T> Result<T> restResult(Integer code, String message, T data) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }
}
