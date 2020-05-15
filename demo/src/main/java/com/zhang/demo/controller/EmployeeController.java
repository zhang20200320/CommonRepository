package com.zhang.demo.controller;

import com.google.gson.Gson;
import com.zhang.demo.common.CommonResult;
import com.zhang.demo.dao.EmployeeRepository;
import com.zhang.demo.entity.Employee;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;

/**
 * 业务接口【测试】 elasticsearch框架
 */
//@ApiIgnore() // 表示不想接口在页面上显示
@RestController
@RequestMapping("es")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;



    /**
     * 添加
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public CommonResult add() {
        Employee employee = new Employee();
        employee.setAbout("测试elasticsearch");
        for (int i = 1; i < 10; i++) {
            employee.setId(i);
            employee.setFirstName("zhangFirstName" + i);
            employee.setLastName("zhangLastName" + i);
            employee.setAge((1 + 10));
            employeeRepository.save(employee);
        }
        System.err.println("add success");
        return CommonResult.success("add success");
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public CommonResult delete(@PathVariable String id) {
        Employee employee = employeeRepository.queryEmployeeById(id);
        System.err.println(employee.toString());
        employeeRepository.delete(employee);
        return CommonResult.success("delete success");
    }

    /**
     * 局部更新
     * @return
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    public CommonResult<Object> update(@PathVariable String id) {
        Employee employee = employeeRepository.queryEmployeeById(id);
        employee.setFirstName("哈哈");
        employeeRepository.save(employee);
        System.err.println("update success");
        return CommonResult.success("update success");
    }
    /**
     * 查询指定信息
     * @return
     */
    @RequestMapping(value = "query/{id}", method = RequestMethod.GET)
    public CommonResult<Optional<Employee>> query(@PathVariable String id) {
        Optional<Employee> accountInfo = employeeRepository.findById(id);
        System.err.println(new Gson().toJson(accountInfo));
        return CommonResult.success(accountInfo);
    }
    /**
     * 查询所有数据
     * @return
     */
    @RequestMapping(value = "queryAll", method = RequestMethod.GET)
    public CommonResult<Iterable<Employee>> queryAll() {
        Iterable<Employee> accountInfo = employeeRepository.findAll();
        System.err.println(new Gson().toJson(accountInfo));
        return CommonResult.success(accountInfo);
    }
}
