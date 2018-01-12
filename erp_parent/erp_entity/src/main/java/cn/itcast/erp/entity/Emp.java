package cn.itcast.erp.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * 员工实体类
 *
 * @author Administrator *
 */
public class Emp {
    private Long uuid;//编号
    private String username;//登陆名


    @JSONField(serialize = false)
    private String pwd;//登陆密码
    private String name;//真实姓名
    private Long gender;//性别
    private String email;//邮件地址
    private String tele;//联系电话
    private String address;//联系地址
    private Date birthday;//出生年月日

    private Dep dep;//部门编号

    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public java.util.Date getBirthday() {
        return birthday;
    }

    public void setBirthday(java.util.Date birthday) {
        this.birthday = birthday;
    }

    public Dep getDep() {
        return dep;
    }

    public void setDep(Dep dep) {
        this.dep = dep;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", tele='" + tele + '\'' +
                ", address='" + address + '\'' +
                ", birthday=" + birthday +
                ", dep=" + dep +
                '}';
    }
}
