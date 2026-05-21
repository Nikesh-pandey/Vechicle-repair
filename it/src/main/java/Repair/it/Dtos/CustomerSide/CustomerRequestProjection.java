package Repair.it.Dtos.CustomerSide;

import Repair.it.Entity.OperatorSide.RegisterStatus;

public interface CustomerRequestProjection {
    String getName();
    String getPhoneNumber();
    String getMessage();
    RegisterStatus getStatus();
}