package com.itheima.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;

/**  
 * ClassName:CustomerRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月21日 下午8:45:37 <br/>       
 */
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedAreaId);
    // 把关联到指定定区的客户进行解绑操作
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    @Modifying
    void unbindCustomerByFixedArea(String fixedAreaId);
    // 把客户绑定到指定的定区
    @Query("update Customer set fixedAreaId = ?2 where id = ?1")
    @Modifying
    void bindCustomer2FixedArea(Long customerId, String fixedAreaId);


}
  
