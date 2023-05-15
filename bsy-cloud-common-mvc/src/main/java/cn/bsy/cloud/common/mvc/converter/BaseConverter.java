package cn.bsy.cloud.common.mvc.converter;
import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.bsy.cloud.common.core.utils.ObjectUtils;
import cn.bsy.cloud.common.mvc.dto.PageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * Model与DTO互相转换基础抽象类
 *
 * @ClassName:AbstractConverter
 * @Date:2022年6月23日 上午12:06:12
 * @Author:gaoheng
 */
public abstract class BaseConverter<Model, RequestDTO, ResponseDTO>
        implements ConverterInterface<Model, RequestDTO, ResponseDTO> {

    /**
     * 自动复制 dto to model
     *
     * @param dto
     * @param model
     * @param <T>
     * @return
     */
    protected final <T> Model copyToModel(T dto, Class<Model> model) {
        try {
            return ObjectUtils.cloneObj(dto, model);
        } catch (Exception e) {
            throw new CustomizeException(e.getMessage(), e);
        }
    }

    /**
     * 自动复制 dtoList to modelList
     *
     * @param dtoList
     * @param model
     * @param <T>
     * @return
     */
    protected final <T> List<Model> copyToModelList(List<T> dtoList, final Class<Model> model) {
        try {
            return ObjectUtils.cloneObjList(dtoList, model);
        } catch (Exception e) {
            throw new CustomizeException(e.getMessage(), e);
        }
    }

    /**
     * 自动复制 modelList to dtoList @param: modelList @param:
     * dto @return:List<T> @throws
     */
    protected final <T> List<T> copyToDtoList(List<Model> modelList, final Class<T> dto) {
        try {
            return ObjectUtils.cloneObjList(modelList, dto);
        } catch (Exception e) {
            throw new CustomizeException(e.getMessage(), e);
        }
    }

    /**
     * 自动复制 model to dto
     *
     * @param model
     * @param dto
     * @param <T>
     * @return
     */
    protected final <T> T copyToDTO(Model model, Class<T> dto) {
        try {
            return ObjectUtils.cloneObj(model, dto);
        } catch (Exception e) {
            throw new CustomizeException(e.getMessage(), e);
        }
    }

    /**
     * 自动复制分页 modelPage to dtoPage
     *
     * @param modelPage
     * @param dto
     * @param <T>
     * @return
     */
    protected final <T> PageDTO<T> copyToDtoPage(Page<Model> modelPage, final Class<T> dto) {
        Page<T> page = new Page<>();
        page.setCurrent(modelPage.getCurrent());
        page.setSize(modelPage.getSize());
        page.setTotal(modelPage.getTotal());
        try {
            page.setRecords(ObjectUtils.cloneObjList(modelPage.getRecords(), dto));
        } catch (Exception e) {
            throw new CustomizeException(e.getMessage(), e);
        }
        return toDtoPage(page);
    }

    /**
     * 自动复制分页
     *
     * @param page
     * @param <T>
     * @return
     */
    private <T> PageDTO<T> toDtoPage(Page<T> page) {
        final PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setCurrent(page.getCurrent());
        pageDTO.setSize(page.getSize());
        pageDTO.setTotal(page.getTotal());
        pageDTO.setRecords(page.getRecords());
        return pageDTO;
    }
}
