package cn.itcast.erp.utils;

import cn.itcast.erp.entity.Emp;
import com.alibaba.fastjson.JSON;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ActionUtil {
    /**
     * //{"name":"管理员组","tele":"000011","uuid":1}
     * @param jsonString JSON数据字符串
     * @param prefix 要加上的前缀
     * @return  {"t.name":"管理员组","t.tele":"000011","t.uuid":1}
     */
    public static String mapData(String jsonString, String prefix){
        Map<String, Object> map = JSON.parseObject(jsonString);

        //存储key加上前缀后的值
        Map<String, Object> dataMap = new HashMap<String, Object>();
        //给每key值加上前缀
        for(String key : map.keySet()){

            if (map.get(key) instanceof Map) {
                Map<String,Object> innnerMap = JSON.parseObject(JSON.toJSONString(map.get(key)));
                for (String innerKey : innnerMap.keySet()) {
                    dataMap.put(prefix + "." + key + "." + innerKey, innnerMap.get(innerKey));
                }
            }else {
                dataMap.put(prefix + "." + key, map.get(key));
            }


        }
        return JSON.toJSONString(dataMap);
    }

    /**
     * 返回前端操作结果
     * @param success
     * @param message
     */
    public static void ajaxReturn(boolean success, String message){
        //返回前端的JSON数据
        Map<String, Object> rtn = new HashMap<String, Object>();
        rtn.put("success",success);
        rtn.put("message",message);
        write(JSON.toJSONString(rtn));
    }

    /**
     * 输出字符串到前端
     * @param jsonString
     */
    public static void write(String jsonString){
        try {
            //响应对象
            HttpServletResponse response = ServletActionContext.getResponse();
            //设置编码
            response.setContentType("text/html;charset=utf-8");
            //输出给页面
            response.getWriter().write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前的登录用户
     */
    public static Emp getLoginEmp(){
        return (Emp) ServletActionContext.getRequest().getSession().getAttribute("user");
    }
}
