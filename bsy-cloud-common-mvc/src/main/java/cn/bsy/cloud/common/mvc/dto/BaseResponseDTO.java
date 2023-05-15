package cn.bsy.cloud.common.mvc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 抽象返回结果对象
 * 
 * @ClassName:BaseResponseDTO
 * @Date:2022年6月23日 下午1:33:34
 * @Author:gaoheng
 */
@NoArgsConstructor
@Getter
@Setter
public abstract class BaseResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键UUID
	 */
	protected String id;

		

}
