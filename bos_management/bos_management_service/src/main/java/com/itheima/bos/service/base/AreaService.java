package com.itheima.bos.service.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Area;

/**  
 * ClassName:AreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午7:11:35 <br/>       
 */
public interface AreaService {

    void save(List<Area> list);

    Page<Area> findAll(Pageable pageable);
    
}
  
