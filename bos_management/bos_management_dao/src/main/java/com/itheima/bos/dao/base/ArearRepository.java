package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Area;

/**  
 * ClassName:ArearRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午7:10:42 <br/>       
 */
public interface ArearRepository extends JpaRepository<Area, Long> {
    @Query("from Area where province like ?1 or city like ?1 or district like ?1 or postcode like ?1 or citycode like ?1 or shortcode like ?1 ")
    List<Area> findQ(String q);
}
  
