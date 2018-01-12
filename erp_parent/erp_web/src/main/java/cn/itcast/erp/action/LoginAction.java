package cn.itcast.erp.action;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.utils.ActionUtil;
import com.alibaba.fastjson.JSON;
import org.apache.struts2.ServletActionContext;

import java.util.HashMap;

public class LoginAction {

    private IEmpBiz empBiz;

    private String username;
    private String pwd;

    public void setEmpBiz(IEmpBiz empBiz) {
        this.empBiz = empBiz;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void userLogin(){
        try {
            Emp emp = empBiz.findByUsernameAndPwd(username, pwd);
            if (emp != null) {
                ServletActionContext.getRequest().getSession().setAttribute("user",emp);
                ActionUtil.ajaxReturn(true,"登录成功!");
            }else{
                ActionUtil.ajaxReturn(false,"用户名或密码错误!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ActionUtil.ajaxReturn(false,"登录错误!");
        }
    }

    public void showName(){
        Emp user = (Emp) ServletActionContext.getRequest().getSession().getAttribute("user");

        if (user != null) {
            ActionUtil.ajaxReturn(true,user.getName());
        }else {
            ActionUtil.ajaxReturn(false,"请登录!");
        }
    }

    public void logOut(){
        ServletActionContext.getRequest().getSession().removeAttribute("user");
    }
}
