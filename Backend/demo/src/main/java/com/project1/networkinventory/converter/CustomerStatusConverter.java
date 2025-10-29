package com.project1.networkinventory.converter;

import com.project1.networkinventory.enums.CustomerStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerStatusConverter extends CaseInsensitiveEnumConverter<CustomerStatus> {
    public CustomerStatusConverter() {
        super(CustomerStatus.class);
    }
}
