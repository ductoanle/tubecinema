package com.ethan.globalcinema.network;

import com.foxykeep.datadroid.exception.RestClientException;

public interface ErrorHandler {
    public void handleRestClientException(RestClientException e);
    public void handleOtherException(Exception e);
}
