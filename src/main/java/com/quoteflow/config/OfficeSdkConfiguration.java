package com.quoteflow.config;

import com.chaoxing.office.app.config.OfficeAppApiConfigTool;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Initializes Chaoxing office SDK static configuration from Spring properties.
 *
 * @author QuoteFlow
 */
@Configuration
@EnableConfigurationProperties(OfficeSdkProperties.class)
public class OfficeSdkConfiguration {

    /**
     * Chaoxing SDK properties.
     */
    private final OfficeSdkProperties officeSdkProperties;

    public OfficeSdkConfiguration(OfficeSdkProperties officeSdkProperties) {
        this.officeSdkProperties = officeSdkProperties;
    }

    /**
     * Copies Spring configuration into Chaoxing SDK static fields.
     */
    @PostConstruct
    public void init() {
        OfficeAppApiConfigTool.officeApiServerDomain = officeSdkProperties.getServerDomain();
        OfficeAppApiConfigTool.officeApiServerFrontDomain = officeSdkProperties.getServerFrontDomain();
        OfficeAppApiConfigTool.officeApiFormSign = officeSdkProperties.getFormsSign();
        OfficeAppApiConfigTool.officeApiFormKey = officeSdkProperties.getFormsKey();
        OfficeAppApiConfigTool.officeApiApproveSign = officeSdkProperties.getApproveSign();
        OfficeAppApiConfigTool.officeApiApproveKey = officeSdkProperties.getApproveKey();
    }
}
