package com.lendico.plangenerator.service;

import com.lendico.plangenerator.model.RequestObject;
import com.lendico.plangenerator.model.ResponseObject;

import java.util.Collection;

public interface GeneratePayLoudObjectInterface {

    Collection<ResponseObject> generateResponsePayLoad(RequestObject requestObject);

    ResponseObject responseObject(RequestObject requestObject, int monthIndex);
}
