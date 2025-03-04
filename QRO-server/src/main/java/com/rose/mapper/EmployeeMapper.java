package com.rose.mapper;

import com.github.pagehelper.Page;
import com.rose.annotation.AutoFill;
import com.rose.dto.EmployeePageQueryDTO;
import com.rose.entity.Employee;
import com.rose.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee(name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user) " +
            "VALUES" +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQury(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据主键动态修改属性
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);


    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Select("select * from employee where id=#{id}")
    Employee getById(Long id);

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
}
