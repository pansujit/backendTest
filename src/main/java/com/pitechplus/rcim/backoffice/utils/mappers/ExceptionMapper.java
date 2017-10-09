package com.pitechplus.rcim.backoffice.utils.mappers;

import com.pitechplus.qautils.restutils.RestTemplateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

/**
 * Created by dgliga on 21.02.2017.
 */
public class ExceptionMapper {
    private static Logger LOGGER = LogManager.getLogger(RestTemplateUtils.class);


    /**
     * This method is used to map exception received from server into a desired exception class
     *
     * @param exception              received from server
     * @param expectedExceptionClass the class in which you want the exception to be mapped
     * @param <E>                    generic for class type of the exception mapped
     * @return desired class with exception mapped
     */
    public static <E> E mapException(HttpStatusCodeException exception, Class<E> expectedExceptionClass) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(exception.getResponseBodyAsByteArray(), expectedExceptionClass);
        } catch (IOException e) {
            LOGGER.debug("Cannot map: " + exception + " into class: " + expectedExceptionClass);
            throw e;
        }
    }

}
