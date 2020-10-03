package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.vo.admin.file.FileEntity;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用章申请表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("request")
public class RequestEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //element图片上传用这个
    @TableField(exist=false)
    private List<FileEntity> fileList;
    //文件类型
    @TableField(exist=false)
    private FileTypeEnum fileTypeEnum;
    //审批列表
    @TableField(exist=false)
    private List<RequestMemberEntity> verifyMemberEntityList;
    //抄送列表
    @TableField(exist=false)
    private List<RequestMemberEntity> copyToMemberEntityList;
    /**
     * 用章审批状态
     */
    @TableField(exist = false)
    private Integer memberStatus;
    /**
     * 用章审批状态
     */
    @TableField(exist = false)
    private String memberStatusCn;
    /**
     * 用章发起用户真实姓名
     */
    @TableField(exist = false)
    private String realname;
    /**
     * 用章发起用户头像
     */
    @TableField(exist = false)
    private String avatarUrl;
    /**
     * 小区名称
     */
    @TableField(exist = false)
    private String zoneName;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 小区id
     */
    private Long zoneId;
    /**
     * 申请人id
     */
    private Long uid;
    /**
     * 已审批过人数
     */
    private Integer step;
    /**
     * 需要审批人数
     */
    private Integer stepTotal;
    /**
     * 类型 1用章
     */
    private Integer type;
    /**
     * 经办人
     */
    private String uname;
    /**
     * 使用日期
     */
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date useDate;
    /**
     * 文件名
     */
    private String documentName;
    /**
     * 文件份数
     */
    private Integer num;
    /**
     * 类别 1合同 2公告 3发函 4其他
     */
    private Integer fileType;
    /**
     * 印章 1业主大会 2业委会 3财务专用章 4其他
     */
    private Integer seal;
    /**
     * 备注
     */
    private String notice;
    /**
     * 状态 0审核中 1审核通过 2撤销 3未通过
     */
    private Integer status;

    /**
     * 审批类型 1 全体业委会审批 2自定义审批人
     */
    @ApiModelProperty("审批类型 1全体业委会审批 2自定义审批人")
    private Integer verifyType;


    //对应中文信息
    /**
     * 状态 0审核中 1审核通过 2撤销 3未通过
     */
    @TableField(exist = false)
    private String statusCn;
    /**
     * 类别 1合同 2公告 3发函 4其他
     */
    @TableField(exist = false)
    private String fileTypeCn;
    /**
     * 印章 1业主大会 2业委会 3财务专用章 4其他
     */
    @TableField(exist = false)
    private String sealCn;
    /**
     * 打印日期 今天
     */
    @TableField(exist = false)
    private Date printDate;

    /**
     * 记录状态 0删除 1业主不显示 2通过
     */
    private Integer recordStatus;

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 设置：创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 获取：创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置：更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 获取：更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
     * 设置：小区id
     */
    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }
    /**
     * 获取：小区id
     */
    public Long getZoneId() {
        return zoneId;
    }
    /**
     * 设置：申请人id
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }
    /**
     * 获取：申请人id
     */
    public Long getUid() {
        return uid;
    }
    /**
     * 设置：已审批过人数
     */
    public void setStep(Integer step) {
        this.step = step;
    }
    /**
     * 获取：已审批过人数
     */
    public Integer getStep() {
        return step;
    }
    /**
     * 设置：需要审批人数
     */
    public void setStepTotal(Integer stepTotal) {
        this.stepTotal = stepTotal;
    }
    /**
     * 获取：需要审批人数
     */
    public Integer getStepTotal() {
        return stepTotal;
    }
    /**
     * 设置：类型 1用章
     */
    public void setType(Integer type) {
        this.type = type;
    }
    /**
     * 获取：类型 1用章
     */
    public Integer getType() {
        return type;
    }
    /**
     * 设置：经办人
     */
    public void setUname(String uname) {
        this.uname = uname;
    }
    /**
     * 获取：经办人
     */
    public String getUname() {
        return uname;
    }
    /**
     * 设置：使用日期
     */
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }
    /**
     * 获取：使用日期
     */
    public Date getUseDate() {
        return useDate;
    }
    /**
     * 设置：文件名
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
    /**
     * 获取：文件名
     */
    public String getDocumentName() {
        return documentName;
    }
    /**
     * 设置：文件份数
     */
    public void setNum(Integer num) {
        this.num = num;
    }
    /**
     * 获取：文件份数
     */
    public Integer getNum() {
        return num;
    }
    /**
     * 设置：类别 1合同 2公告 3发函 4其他
     */
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }
    /**
     * 获取：类别 1合同 2公告 3发函 4其他
     */
    public Integer getFileType() {
        return fileType;
    }
    /**
     * 设置：印章 1业主大会 2业委会 3财务专用章 4其他
     */
    public void setSeal(Integer seal) {
        this.seal = seal;
    }
    /**
     * 获取：印章 1业主大会 2业委会 3财务专用章 4其他
     */
    public Integer getSeal() {
        return seal;
    }
    /**
     * 设置：备注
     */
    public void setNotice(String notice) {
        this.notice = notice;
    }
    /**
     * 获取：备注
     */
    public String getNotice() {
        return notice;
    }
    /**
     * 设置：状态 0审核中 1审核通过 2撤销 3未通过
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 获取：状态 0审核中 1审核通过 2撤销 3未通过
     */
    public Integer getStatus() {
        return status;
    }

    public List<FileEntity> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileEntity> fileList) {
        this.fileList = fileList;
    }

    public FileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }

    public void setFileTypeEnum(FileTypeEnum fileTypeEnum) {
        this.fileTypeEnum = fileTypeEnum;
    }

    public List<RequestMemberEntity> getVerifyMemberEntityList() {
        return verifyMemberEntityList;
    }

    public void setVerifyMemberEntityList(List<RequestMemberEntity> verifyMemberEntityList) {
        this.verifyMemberEntityList = verifyMemberEntityList;
    }

    public List<RequestMemberEntity> getCopyToMemberEntityList() {
        return copyToMemberEntityList;
    }

    public void setCopyToMemberEntityList(List<RequestMemberEntity> copyToMemberEntityList) {
        this.copyToMemberEntityList = copyToMemberEntityList;
    }

    public String getStatusCn() {
        return statusCn;
    }

    public void setStatusCn(String statusCn) {
        this.statusCn = statusCn;
    }

    public String getFileTypeCn() {
        return fileTypeCn;
    }

    public void setFileTypeCn(String fileTypeCn) {
        this.fileTypeCn = fileTypeCn;
    }

    public String getSealCn() {
        return sealCn;
    }

    public void setSealCn(String sealCn) {
        this.sealCn = sealCn;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public Integer getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Integer memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getMemberStatusCn() {
        return memberStatusCn;
    }

    public void setMemberStatusCn(String memberStatusCn) {
        this.memberStatusCn = memberStatusCn;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Integer getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }
}