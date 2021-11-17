package com.esdo.bepilot.Service.Validate;

import com.esdo.bepilot.Exception.ListValidationException;
import com.esdo.bepilot.Model.Request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.List;

@Component
public class UserValidate {

    @Autowired
    private ListValidationException listException;

    /**
     * Hàm validate DurationType
     *
     * @param UserRequest
     */
    public void validate(UserRequest UserRequest) {
        List<ValidationException> listError = listException.getValidationExceptions();
        listError.clear();
        // Check DurationType có hợp lệ không
//        if (!UserRequest.getDurationType().equals(DurationStatus.WEEK.toString())
//                && !UserRequest.getDurationType().equals(DurationStatus.MONTH.toString())) {
//            listError.add(new ValidationException("Invalid DurationType"));
//        }

        if (listError.size() > 0) {
            throw listException;
        }
    }
}
