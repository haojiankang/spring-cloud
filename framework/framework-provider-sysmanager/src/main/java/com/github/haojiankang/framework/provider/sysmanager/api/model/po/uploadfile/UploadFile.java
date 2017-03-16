package com.github.haojiankang.framework.provider.sysmanager.api.model.po.uploadfile;

import java.io.InputStream;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.vo.AbstractVO;
import com.github.haojiankang.framework.provider.utils.hibernate.AbstractPojo;

/**
 * 上传文件记录
 */
@Entity
@Table(name = "HJK_SYS_UPLOADFILE")
public class UploadFile   implements Cloneable,AbstractVO<String>,AbstractPojo<String> {
    private static final long serialVersionUID = -7195643870983197505L;
    @Transient
    @JsonIgnore
	public final String FLAG_COMPLETE = "0";
    @Transient
    @JsonIgnore
	public final String FLAG_INCOMPLETE = "1";

    @Id
    @GeneratedValue(generator="assigned") 
    @GenericGenerator(name="assigned", strategy="assigned")
    private String id;
    @Column(name = "file_name", length = 50)
    private String fileName;
    @Column(name = "file_url", length = 1100)
    private String fileUrl;
    @Column(name = "suffix", length = 5)
    private String suffix;
    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "md5", length = 30)
    private String md5;
    @Column(name = "flag", length = 2)
    private String flag = FLAG_COMPLETE;
    @Column(name = "path", length = 1000)
    private String path;
    @Transient
    @JsonIgnore
    private Long range;
    @Transient
    @JsonIgnore
    private InputStream stream;
    @Transient
    @JsonIgnore
    private byte[] bytes;
    @Transient
    @JsonIgnore
    private Boolean first;

    @Column(name = "create_user")
    private String createUser;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_user")
    private String updateUser;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "remark")
    private String remark;
    @Column(name = "CREATEUSER_TYPE")
    private String createUserType;
    @Column(name = "UPDATEUSER_TYPE")
    private String updateUserType;

    public UploadFile() {}
    public UploadFile(String id) {
    	this.setId(id);
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
       this.id = id;
    }
	public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileUrl() {
        return fileUrl;
    }
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public Long getFileSize() {
        return fileSize;
    }
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    public String getMd5() {
        return md5;
    }
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public Long getRange() {
        return range;
    }
    public void setRange(Long range) {
        this.range = range;
    }
    public InputStream getStream() {
        return stream;
    }
    public void setStream(InputStream stream) {
        this.stream = stream;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getUpdateUser() {
        return updateUser;
    }
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCreateUserType() {
        return createUserType;
    }
    public void setCreateUserType(String createUserType) {
        this.createUserType = createUserType;
    }
    public String getUpdateUserType() {
        return updateUserType;
    }
    public void setUpdateUserType(String updateUserType) {
        this.updateUserType = updateUserType;
    }
	public Boolean getFirst() {
		return first;
	}
	public void setFirst(Boolean first) {
		this.first = first;
	}
}
