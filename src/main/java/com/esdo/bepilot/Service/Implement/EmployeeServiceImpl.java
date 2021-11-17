package com.esdo.bepilot.Service.Implement;

import com.esdo.bepilot.Config.EmployeeSpecification;
import com.esdo.bepilot.Exception.InvalidException;
import com.esdo.bepilot.Model.Entity.Account;
import com.esdo.bepilot.Model.Entity.Employee;
import com.esdo.bepilot.Model.Request.EmployeeRequest;
import com.esdo.bepilot.Model.Response.EmployeeResponse;
import com.esdo.bepilot.Model.Response.ListEmployeeResponse;
import com.esdo.bepilot.Repository.AccountRepository;
import com.esdo.bepilot.Repository.EmployeeRepository;
import com.esdo.bepilot.Service.EmployeeService;
import com.esdo.bepilot.Service.Mapper.ConvertObject;
import com.esdo.bepilot.Service.Validate.CheckValid;
import com.esdo.bepilot.Util.UploadAvatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public EmployeeResponse createNewEmployee(EmployeeRequest employeeRequest) {
        CheckValid.checkEmployeeRequest(employeeRequest, accountRepository, employeeRepository, false);
        Account account = new Account();
        account.setEmail(employeeRequest.getEmail());
        account.setPassword(bcryptEncoder.encode(employeeRequest.getPassword()));
        account.setRole(employeeRequest.getRole());
        accountRepository.save(account);

        Employee employee = ConvertObject.fromEmployeeRequestToEmployee(employeeRequest);
        employee.setAccount(account);

        // Save avatar and set avatar
        UploadAvatar.upload(employeeRequest.getAvatar(), employee);

        Employee employeeSave = employeeRepository.save(employee);

        return ConvertObject.fromEmployeeToEmployeeResponse(employeeSave);
    }

    @Override
    public ListEmployeeResponse findAllEmployee(Integer page, Integer size, String key) {
        ListEmployeeResponse response = new ListEmployeeResponse();
        Page<Employee> employeePage = null;
        if (key == null) {
            employeePage = employeeRepository.findAll(PageRequest.of(page-1, size));
        } else {
            employeePage = employeeRepository.findAll(EmployeeSpecification.filter(key), PageRequest.of(page-1, size));
        }

        List<Employee> employeeList = employeePage.getContent();

        List<EmployeeResponse> employeeResponseList = new ArrayList<>();
        for (Employee e : employeeList) {
            employeeResponseList.add(ConvertObject.fromEmployeeToEmployeeResponse(e));
        }

        response.setEmployeeList(employeeResponseList);
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(employeePage.getTotalPages());
        response.setTotalItems((int) employeePage.getTotalElements());

        return response;
    }

    @Override
    public EmployeeResponse findEmployeeById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isEmpty()) {
            throw new InvalidException("Invalid employee has id = " + id);
        }
        Employee employee = employeeOptional.get();

        return ConvertObject.fromEmployeeToEmployeeResponse(employee);
    }

    @Override
    public EmployeeResponse editEmployeeById(Long id, EmployeeRequest request) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new InvalidException("Invalid employee has id = " + id);
        }
        CheckValid.checkEmployeeRequest(request, accountRepository, employeeRepository, true);
        Employee employee = optionalEmployee.get();
        employee.setAvatar(request.getAvatar());
        employee.setName(request.getName());
        employee.setPhone(request.getPhone());
        Optional<Account> optionalAccount = accountRepository.findById(employee.getAccount().getId());
        if (optionalAccount.isEmpty()) {
            throw new InvalidException("Invalid account has id = " + id);
        }
        Account account = optionalAccount.get();
        account.setEmail(request.getEmail());
        account.setPassword(bcryptEncoder.encode(request.getPassword()));
        account.setRole(request.getRole());

        // Save avatar and set avatar
        if (!request.getAvatar().equals(employee.getAvatar())) {
            UploadAvatar.upload(request.getAvatar(), employee);
            File file = new File(employee.getAvatar());
            file.delete();
        }

        Employee employeeEdit = employeeRepository.save(employee);

        return ConvertObject.fromEmployeeToEmployeeResponse(employeeEdit);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isEmpty()) {
            throw new InvalidException("Invalid employee has id = " + id);
        }
        Employee employee = employeeOptional.get();

        employeeRepository.delete(employee);
    }
}
