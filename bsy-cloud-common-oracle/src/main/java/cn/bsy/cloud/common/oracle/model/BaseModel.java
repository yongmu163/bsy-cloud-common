package cn.bsy.cloud.common.oracle.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @Description:属性自动配置
 * @ClassName:BaseModel
 * @Date:2022年6月8日 上午11:44:17
 * @Author:gaoheng
 */

public class BaseModel<T extends BaseModel<?>> extends Model<T> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "ID", type = IdType.UUID)
    protected String id;
    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    protected LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(value = "UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    protected LocalDateTime updateTime;

    /**
     * 删除标识 0有效1已删除
     */
    @TableLogic
    @TableField("IS_DELETED")
    protected boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    public void preInsert() {
        init();
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    public void preUpdate() {
        init();
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 初始化默认值
     */
    public void init() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

