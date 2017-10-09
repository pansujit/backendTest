package com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders;

import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.service.BackUserService;
import com.pitechplus.rcim.backoffice.service.ConfigsService;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.pitechplus.rcim.backoffice.utils.DataExtractors.extractXAuthTokenFromResponse;

/**
 * Created by dgliga  on 16.08.2017.
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component("files-Ids")
public class UtilFiles {

    @Autowired
    BackUserService backUserService;

    @Autowired
    ConfigsService configsService;

    @Value("${bo.super.admin.username}")
    String boSuperAdminUsername;

    @Value("${bo.super.admin.password}")
    String boSuperAdminPassword;


    public UUID[] createArrayOfValidFiles(int nrOfValidFiles) {

        UUID[] fileId = new UUID[nrOfValidFiles];

        for (int i = 0; i < nrOfValidFiles; i++) {
            fileId[i] = configsService.createFile(extractXAuthTokenFromResponse(backUserService.authUser
                    (new Login(boSuperAdminUsername, boSuperAdminPassword))), DtoBuilders.buildFile()).getBody().getId();
        }
        return fileId;
    }

}
