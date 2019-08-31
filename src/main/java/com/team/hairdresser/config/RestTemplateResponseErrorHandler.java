package com.team.hairdresser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.hairdresser.dto.ExceptionHandlerDto;
import com.team.hairdresser.utils.util.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;


@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {

        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            // handle SERVER_ERROR
        } else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            if (httpResponse.getStatusCode() == HttpStatus.EXPECTATION_FAILED) {

                ObjectMapper mapper = new ObjectMapper();
                String responseString = StreamUtils.copyToString(httpResponse.getBody(), Charset.defaultCharset());
                ExceptionHandlerDto exceptionHandlerDto = mapper.readValue(responseString, ExceptionHandlerDto.class);

                throw new ServiceException(exceptionHandlerDto.getMessage());

            } else if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ServiceException("Response's HttpStatus is NOT_FOUND.");
            }
        }
    }
}


