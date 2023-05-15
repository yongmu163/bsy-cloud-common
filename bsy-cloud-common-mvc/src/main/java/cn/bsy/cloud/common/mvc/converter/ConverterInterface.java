package cn.bsy.cloud.common.mvc.converter;



import cn.bsy.cloud.common.mvc.dto.PageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * Model与DTO互相转换接口
 *
 * @ClassName:AbstractConverter
 * @Date:2022年6月23日 上午12:06:12
 * @Author: WangHui
 */

public interface ConverterInterface<Model, RequestDTO, ResponseDTO> {

    /**
     * 自定义转换：dto to model
     *
     * @param dto
     * @return
     */
    Model requestToModel(final RequestDTO dto);

    /**
     * 自定义转换：model to dto
     *
     * @param model
     * @return
     */
    ResponseDTO modelToResponse(final Model model);

    /**
     * 自定义转换： dtoList to modelList
     *
     * @param dtoList
     * @return
     * @throws Exception
     */
    List<Model> requestToModelList(List<RequestDTO> dtoList) throws Exception;

    /**
     * 自定义转换： modelPage to dtoPage
     *
     * @param modelPage
     * @return
     * @throws Exception
     */
    PageDTO<ResponseDTO> modelToResponsePage(Page<Model> modelPage) throws Exception;
}
