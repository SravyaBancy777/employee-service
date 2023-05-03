package com.employee.employee.Controller;

import com.employee.employee.Model.EmployeeRequestModel;
import com.employee.employee.Model.EmployeeResponseModel;
import com.employee.employee.Service.EmployeeService;
import com.employee.employee.Shared.EmployeeDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @GetMapping()
    public ResponseEntity<List<EmployeeResponseModel>> getAllEmployees(){
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<EmployeeResponseModel> employeesList = new ArrayList<>();
        employees.forEach((employeeDto -> {
            EmployeeResponseModel employeeResponseModel = modelMapper.map(employeeDto, EmployeeResponseModel.class);
            employeesList.add(employeeResponseModel);
        }));

        if(employeesList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employeesList, HttpStatus.OK);
    }
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseModel> createEmployee(@Valid @RequestBody EmployeeRequestModel employeeDetails){

        String email = employeeDetails.getEmail();
        if(employeeService.getEmployeeByEmail(email) != null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        EmployeeDto employeeDto = modelMapper.map(employeeDetails, EmployeeDto.class);
        EmployeeDto createdValue = employeeService.createEmployee(employeeDto);
        EmployeeResponseModel returnValue = modelMapper.map(createdValue, EmployeeResponseModel.class);

                return new ResponseEntity<>(returnValue, HttpStatus.CREATED);


    }
    

    @GetMapping("/id/{id}")
    public ResponseEntity<EmployeeResponseModel> getEmployeeById(@PathVariable Integer id){
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        if(employeeDto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        EmployeeResponseModel returnValue = modelMapper.map(employeeDto, EmployeeResponseModel.class);

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeResponseModel> getEmployeeByEmail(@PathVariable String email){
        EmployeeDto employeeDto = employeeService.getEmployeeByEmail(email);

        if(employeeDto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        EmployeeResponseModel returnValue = modelMapper.map(employeeDto, EmployeeResponseModel.class);
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseModel>> searchEmployees(
            @RequestParam(value = "first-name", required = false) String firstName,
            @RequestParam(value = "last-name", required = false) String lastName){
        List<EmployeeDto> employees = null;
        if(firstName != null && lastName != null){
            employees = employeeService.getEmployeeByFirstNameAndLastName(firstName, lastName);
        }else if(lastName != null){
            employees = employeeService.getEmployeeByLastName(lastName);
        }else if(firstName != null){
            employees = employeeService.getEmployeeByFirstName(firstName);
        }

        if(employees == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<EmployeeResponseModel> employeesList = new ArrayList<>();

        employees.forEach(employeeDto -> {
            EmployeeResponseModel employeeResponseModel = modelMapper.map(employeeDto, EmployeeResponseModel.class);
            employeesList.add(employeeResponseModel);
        });

        return new ResponseEntity<>(employeesList, HttpStatus.OK);
    }


    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<EmployeeResponseModel> updateEmployee(@Valid @RequestBody EmployeeRequestModel employeeDetails){
        String email = employeeDetails.getEmail();
        if(employeeService.getEmployeeByEmail(email) == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //Getting existing user
        EmployeeDto existingUser = employeeService.getEmployeeByEmail(email);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        EmployeeDto employeeDto = modelMapper.map(employeeDetails, EmployeeDto.class);
        employeeDto.setId(existingUser.getId());

        EmployeeDto createdValue = employeeService.createEmployee(employeeDto);

        EmployeeResponseModel returnValue = modelMapper.map(createdValue, EmployeeResponseModel.class);

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }
    @DeleteMapping("/{email}")
    ResponseEntity<Void> delete (@PathVariable String email){
        if(employeeService.getEmployeeByEmail(email) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        boolean res = employeeService.deleteEmployeeByEmail(email);
        if(res){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }






}
