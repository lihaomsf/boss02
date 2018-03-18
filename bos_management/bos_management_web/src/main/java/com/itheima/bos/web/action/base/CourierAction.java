package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
@SuppressWarnings(value = {"all"})
public class CourierAction extends ActionSupport
        implements ModelDriven<Courier> {

    private Courier model = new Courier();

    @Override
    public Courier getModel() {
        return model;
    }

    @Autowired
    private CourierService courierService;
   
    @Action(value = "courierAction_save", results = {@Result(name="success",location = "/pages/base/courier.html",
            type = "redirect")})
    public String save() {

        courierService.save(model);
        return SUCCESS;
    }
    
    private int page;
    public void setPage(int page) {
        this.page = page;
    }
    private int rows;
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    @Action(value = "courierAction_pageQuery")
    public String pageQuery() throws IOException {
        // 构造查询条件
        Specification<Courier> specification = new Specification<Courier>() {
            /**
             * 构建一个where条件语句
             * 
             * @param root
             *            : 根对象,简单的理解为泛型的对象,
             * @param cb
             *            : 构建查询条件的对象
             * @return a {@link Predicate}, must not be {@literal null}.
             */
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                String courierNum = model.getCourierNum();
                String company = model.getCompany();
                String type = model.getType();
                Standard standard = model.getStandard();

                ArrayList<Predicate> list = new ArrayList<>();

                // 快递员工号不为空,构建一个等值查询
                // where courierNum = ?
                if (StringUtils.isNotEmpty(courierNum)) {
                    // 参数1 :
                    // 参数2 : 代替?的具体值
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courierNum);
                    list.add(p1);
                }

                // 公司不为空,构建一个模糊查询
                if (StringUtils.isNotEmpty(company)) {
                    // 参数1 :
                    // 参数2 : 代替?的具体值
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + company + "%");
                    list.add(p2);
                }
                // 类型不为空,构建一个等值查询
                if (StringUtils.isNotEmpty(type)) {
                    // 参数1 :
                    // 参数2 : 代替?的具体值
                    Predicate p3 = cb.equal(root.get("type").as(String.class), type);
                    list.add(p3);
                }

                if (standard != null) {
                    String name = standard.getName();
                    if (StringUtils.isNotEmpty(name)) {
                        // 参数1 :
                        // 参数2 : 代替?的具体值
                        // 连表查询
                        Join<Object, Object> join = root.join("standard");
                        Predicate p4 = cb.equal(join.get("name").as(String.class), type);
                        list.add(p4);
                    }
                }
                // 用户没有输入查询条件
                if (list.size() == 0) {
                    return null;
                }
                // 构造数组
                Predicate[] arr = new Predicate[list.size()];
                list.toArray(arr);

                return cb.and(arr);
            }
        };

        Pageable pageable = new PageRequest(page - 1, rows);

        Page<Courier> page = courierService.findAll(specification, pageable);

        long total = page.getTotalElements();
        List<Courier> rows = page.getContent();

        Map<String, Object> map = new HashMap<>();

        map.put("total", total);
        map.put("rows", rows);

        JsonConfig jsonConfig = new JsonConfig();
        // 指定在生成json数据的时候要忽略的字段
        jsonConfig.setExcludes(new String[] { "fixedAreas", "takeTime" });

        // 提高服务器的性能,所有页面不需要的数据一律要忽略
        String json = JSONObject.fromObject(map, jsonConfig).toString();

        HttpServletResponse response = ServletActionContext.getResponse();
        // 设置编码
        response.setContentType("application/json;charset=UTF-8");
        // 写出数据
        response.getWriter().write(json);

        return NONE;
    }
    //删除快递员
    //设置属性驱动id
    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }
    @Action(value = "courierAction_batchDel",
            results = {@Result(name = "success",
                    location = "/pages/base/courier.html", type = "redirect")})
    public String batchDel(){
       System.out.println(ids);
        courierService.batchDel(ids);
        return SUCCESS;
    }
    
}