package com.thed.zephyr.cloud.rest.util;

import com.thed.zephyr.cloud.rest.constant.ApplicationConstants;
import com.thed.zephyr.cloud.rest.exception.BadRequestParamException;
import com.thed.zephyr.cloud.rest.model.Cycle;
import com.thed.zephyr.cloud.rest.model.Execution;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by Kavya on 26-04-2016.
 */
public class ValidateUtil {

    private static Logger log = LoggerFactory.getLogger(ValidateUtil.class);

    public static <T> void validate(T... inputs) throws BadRequestParamException {
        try {
            for (T input : inputs) {
                if (input instanceof Cycle)
                    validateCycle((Cycle) input);
                else if (input instanceof Execution)
                    validateExecution((Execution) input);
                else if (input instanceof Collection)
                    validateArrayInput((Collection) input);
                else
                    validateInput(input);
            }
        } catch (BadRequestParamException e) {
            log.error("Validation failed");
            throw e;
        }
    }

    private static <T> void validateInput(T input) throws BadRequestParamException {
        if (null == input)
            throw new BadRequestParamException("Required request parameter is missing", new NullPointerException());
        if (input instanceof String)
            validateBlankInput((String) input);
    }

    private static  void validateBlankInput(String input) throws BadRequestParamException {
        if (StringUtils.isBlank(input)) {
            throw new BadRequestParamException("Required parameter is blank");
        }
    }

    private static void validateInputMaxLength(String param, int maxLength) throws BadRequestParamException {
        if (param.length() > maxLength)
            throw new BadRequestParamException("Request parameter is too long");
    }

    private static void validateCycle(Cycle cycle) throws BadRequestParamException {
        validateBlankInput(cycle.name);
        validateInput(cycle.projectId);
        validateInput(cycle.versionId);
        if (cycle.name != null) validateInputMaxLength(cycle.name, ApplicationConstants.CYCLE_NAME_MAX_LENGTH);
        if (cycle.startDate == null && cycle.endDate != null)
            throw new BadRequestParamException("End date can not be present without start date");
        if (cycle.startDate != null && cycle.endDate != null)
            if (cycle.startDate.after(cycle.endDate))
                throw new BadRequestParamException("End date of the cycle can not be before start date");

    }

    private static void validateExecution(Execution execution) throws BadRequestParamException {
        validateInput(execution.projectId);
        validateInput(execution.issueId);
    }

    private static <T> void validateArrayInput(Collection elements) throws BadRequestParamException {
        if (null == elements) {
            throw new BadRequestParamException("Required request parameter is missing", new NullPointerException());
        }
        if (elements.isEmpty()) {
            throw new BadRequestParamException("Required request parameter is empty");
        }
        if (elements.contains(null)) {
            throw new BadRequestParamException("The required array should not contain null value");
        }
    }

    public static void validateNegativeValue(int input, String inputName) throws BadRequestParamException {
        if (input < 0) {
            throw new BadRequestParamException(inputName + " canont be negative");
        }
    }
}
