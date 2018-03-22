package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport 
    implements ModelDriven<Standard>{
    
    private Standard model = new Standard();
    
    //注入service
    @Autowired
    private StandardService standardService;
    
    @Override
    public Standard getModel() {
          
        return model;
    }
    @Action(value="standardAction_save" , results={@Result(name="success",location="/pages/base/standard.html",type="redirect")})
    public String save(){
       standardService.save(model);
        return SUCCESS;
    }
    
    // 使用属性驱动获取数据
    private int page;// 第几页
    private int rows;// 每一页显示多少条数据

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    // AJAX请求不需要跳转页面
    @Action(value = "standardAction_pageQuery")
    public String pageQuery() throws IOException {

        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1

        Pageable pageable = new PageRequest(page - 1, rows);

        Page<Standard> page = standardService.findAll(pageable);

        // 总数据条数
        long total = page.getTotalElements();
        // 当前页要实现的内容
        List<Standard> list = page.getContent();
        // 封装数据
        Map<String, Object> map = new HashMap<>();

        map.put("total", total);
        map.put("rows", list);

        // JSONObject : 封装对象或map集合
        // JSONArray : 数组,list集合
        // 把对象转化为json字符串
        String json = JSONObject.fromObject(map).toString();
        
        
//        ServletContext servletContext = ServletActionContext.getServletContext();
//        servletContext.getRealPath("");
//        servletContext.getMimeType("");

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

        return NONE;
    }
    
    // 查询所有的派送标准
    @Action(value = "standard_findAll")
    public String findAll() throws IOException {
        // 查询数据
        Page<Standard> page = standardService.findAll(null);
        // 获取页面的数据
        List<Standard> list = page.getContent();
        // 转换数据为json并传回页面
        String json = JSONArray.fromObject(list).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        return NONE;

    }
    
}