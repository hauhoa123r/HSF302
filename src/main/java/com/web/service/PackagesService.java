package com.web.service;

import com.web.model.response.PackagesReponse;

public interface PackagesService {
    PackagesReponse getPackage(Long id);
    String deletePackage(Long id);
}
