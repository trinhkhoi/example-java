package org.example.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.common.dto.kafka.CustomerMessage;
import org.common.dto.response.ResponseDTO;
import org.common.dto.response.pist.ThemeDetailsData;
import org.common.exception.BusinessException;
import org.common.security.Secure;
import org.example.dto.request.CreateUserRequest;
import org.example.dto.response.CustomerInfo;
import org.example.service.CustomerService;
import org.example.service.ThemeService;
import org.example.service.UserService;
import org.example.service.WtsMessageService;
import org.example.util.ModelConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/system", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Secure(Secure.ROLE_SYSTEM)
public class SystemController {
    @Autowired
    private UserService userService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WtsMessageService wtsMessageService;

    @GetMapping("/theme/all")
    @ResponseStatus(OK)
    public ResponseDTO<List<ThemeDetailsData>> getAllThemeDetails() throws BusinessException {
        return ResponseDTO.<List<ThemeDetailsData>>builder()
                .data(themeService.getAllThemes())
                .build();
    }

    @PostMapping("/user/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody CreateUserRequest request) throws BusinessException {
        userService.createUser(request);
    }

    @GetMapping("/import/theme")
    @ResponseStatus(HttpStatus.OK)
    public void importTheme() throws BusinessException {
        themeService.importThemeFromCore();
    }

    @GetMapping("/customer/get-customer-info")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<List<CustomerInfo>> getCustomerInfo(@RequestParam String customerIds) throws BusinessException {
        return ResponseDTO.<List<CustomerInfo>>builder()
                .data(customerService.getCustomerInfoByIds(Arrays.asList(customerIds.split(","))))
                .build();
    }

    @GetMapping("/customer/list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<List<CustomerMessage>> getCustomers(@RequestParam String customerIds) throws BusinessException {
        return ResponseDTO.<List<CustomerMessage>>builder()
                .data(customerService.getAllByIdIsIn(Arrays.asList(customerIds.split(","))).stream()
                        .map(ModelConverter::buildKafkaCustomerMessage)
                        .collect(Collectors.toList()))
                .build();
    }

    @ApiOperation(value = "API to refresh list WTS message")
    @GetMapping("/wts/message/refresh")
    @ResponseStatus(value = OK)
    public void getListMessage() throws BusinessException {
        wtsMessageService.refreshListErrorMessage();
    }
}
