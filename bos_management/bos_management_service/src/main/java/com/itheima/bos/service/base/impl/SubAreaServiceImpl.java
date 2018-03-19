package com.itheima.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;

/**  
 * ClassName:SubAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午3:27:59 <br/>       
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService{
    //注入DAO
    @Autowired
    private SubAreaRepository subAreaRepository;

    @Override
    public void save(SubArea model) {
          
        subAreaRepository.save(model);
    }
}
  
