package ua.knu.knudev.employeemanagerapi.api;

import ua.knu.knudev.employeemanagerapi.request.AccountCredentialsUpdateRequest;
import ua.knu.knudev.employeemanagerapi.request.AccountReceivingRequest;
import ua.knu.knudev.employeemanagerapi.response.AccountReceivingResponse;

public sealed interface AccountManagementApi permits EmployeeApi {

    AccountReceivingResponse register(AccountReceivingRequest  request);

    AccountReceivingResponse updateCredentials(AccountCredentialsUpdateRequest request);

}
