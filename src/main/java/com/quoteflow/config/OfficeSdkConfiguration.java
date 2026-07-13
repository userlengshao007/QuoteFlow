package com.quoteflow.config;

import com.chaoxing.office.app.config.OfficeAppApiConfigTool;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 根据 Spring 配置初始化超星办公 SDK 静态配置。
 *
 * @author zhangyujie
 */
@Configuration
@EnableConfigurationProperties(OfficeSdkProperties.class)
public class OfficeSdkConfiguration {

    /**
     * 超星 SDK 配置。
     */
    private final OfficeSdkProperties officeSdkProperties;

    public OfficeSdkConfiguration(OfficeSdkProperties officeSdkProperties) {
        this.officeSdkProperties = officeSdkProperties;
    }

    /**
     * 将 Spring 配置复制到超星 SDK 静态字段。
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
