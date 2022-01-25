package org.example.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.common.dto.response.ResponseDTO;
import org.common.exception.BusinessException;
import org.common.security.Secure;
import org.example.dto.request.CustomerProfileUpdateRequest;
import org.example.dto.response.CustomerProfileDto;
import org.example.dto.response.CustomerProfileOtherDto;
import org.example.service.CustomerService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/private/customer/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Secure({Secure.ROLE_CUSTOMER})
@Validated
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @ApiOperation(value = "API get customer profile from other")
    @GetMapping("{customerId}/profile")
    @ResponseStatus(OK)
    public ResponseDTO<CustomerProfileOtherDto> getCustomer(@PathVariable Long customerId) throws BusinessException {
        return ResponseDTO.<CustomerProfileOtherDto>builder()
                .data(customerService.getCustomerProfileById(customerId))
                .build();
    }

    @ApiOperation(value = "API get customer profile themselves")
    @GetMapping("/profile")
    @ResponseStatus(OK)
    public ResponseDTO<CustomerProfileDto> getCustomer() throws BusinessException {
        return ResponseDTO.<CustomerProfileDto>builder()
                .data(customerService.getCustomerProfile())
                .build();
    }

    @ApiOperation(value = "API update customer profile")
    @PutMapping("/update")
    @ResponseStatus(OK)
    public ResponseDTO<Boolean> updateCustomer(@RequestBody CustomerProfileUpdateRequest request) throws BusinessException {
        return ResponseDTO.<Boolean>builder()
                .data(customerService.updateCustomerProfile(request))
                .build();
    }
}
