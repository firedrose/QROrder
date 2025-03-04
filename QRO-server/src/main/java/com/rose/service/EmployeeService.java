package com.rose.service;

import com.rose.dto.EmployeeDTO;
import com.rose.dto.EmployeeLoginDTO;
import com.rose.dto.EmployeePageQueryDTO;
import com.rose.entity.Employee;
import com.rose.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQury(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工
     * @param status
     * @param id
     * @return
     */
    void startOrStop(Integer status, Long id);


    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    void modify(EmployeeDTO employeeDTO);
}
