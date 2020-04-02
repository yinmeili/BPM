package com.h3bpm.web.config;

import OThinker.interceptor.LogicUnitCacheInterceptor;
import com.h3bpm.base.interceptors.AuthInterceptor;
import com.h3bpm.base.interceptors.LocaleInterceptor;
import com.h3bpm.base.interceptors.UserInfoInterceptor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.jws.WebService;


@Configuration
@ComponentScan(
        basePackages = {"${h3.scanBasePackages:OThinker.H3.Controller.*,com.h3bpm.mobile.controller,com.h3bpm.base.controller.handler}"}
        )
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/Portal/index.html");
        registry.addViewController("/Portal/Mobile/").setViewName("redirect:/Portal/Mobile/index.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleInterceptor());
        registry.addInterceptor(new UserInfoInterceptor());
        registry.addInterceptor(new LogicUnitCacheInterceptor());
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/Portal/Mobile/**", "/Portal/Platform/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*registry.addResourceHandler("/Portal/**").addResourceLocations("/Portal/");
        registry.addResourceHandler("/Cluster/**").addResourceLocations("/Cluster/");
        registry.addResourceHandler("/attached/**").addResourceLocations("/attached/");*/
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(3600);
    }

    @Bean
    public CommonsMultipartResolver commonsMultipartResolver(){
        return new CommonsMultipartResolver();
    }

    /*@Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(){
        final Resource resource = new ClassPathResource("config/h3bpm_portal_app.properties");
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(resource);
        propertyPlaceholderConfigurer.setFileEncoding("UTF-8");
        return propertyPlaceholderConfigurer;
    }*/

}
