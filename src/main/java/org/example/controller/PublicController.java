package org.example.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.common.dto.response.ResponseDTO;
import org.common.exception.BusinessException;
import org.common.security.Secure;
import org.example.dto.request.RegisterCustomerPistRequest;
import org.example.dto.request.RegisterCustomerTempRequest;
import org.example.service.CustomerRegisterTempService;
import org.example.service.CustomerService;
import org.example.service.ParameterService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping(value = "/public", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Secure(Secure.ROLE_PUBLIC)
@Validated
public class PublicController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private CustomerRegisterTempService customerRegisterTempService;

    @PostMapping("/customer/pist/register")
    @ResponseStatus(HttpStatus.OK)
    public void createPistAccount(@RequestBody @Valid RegisterCustomerPistRequest request) throws BusinessException {
        customerService.createNewPistCustomer(request);
    }

    @ApiOperation(value = "API for get stock codes which is suggested for customer in the first login")
    @GetMapping("/suggested/stockcodes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<List<String>> findSuggestedStockCodes() throws BusinessException {
        return ResponseDTO.<List<String>>builder()
                .data(parameterService.getSuggestedStockCodes())
                .build();
    }

    @ApiOperation(value = "API for get stock codes which are allowed for buying")
    @GetMapping("/permission/stockcodes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<List<String>> findPermisBuyingStockCodes() throws BusinessException {
        return ResponseDTO.<List<String>>builder()
                .data(parameterService.getSuggestedStockCodes())
                .build();
    }

    @ApiOperation(value = "API for creating register customer website")
    @PostMapping("/customer/register/website")
    @ResponseStatus(value = CREATED)
    public void registerCustomerWebsite(@RequestBody @Valid RegisterCustomerTempRequest request) throws BusinessException {
        customerRegisterTempService.createNewPistCustomer(request);
    }

    @ApiOperation(value = "API for creating register customer website")
    @GetMapping("/email/verify")
    @ResponseStatus(value = OK)
    public void verifyEmailOtp(@RequestParam String otp) throws BusinessException {
        customerRegisterTempService.verifyEmailOtp(otp);
    }
}
