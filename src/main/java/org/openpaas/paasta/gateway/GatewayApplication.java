package org.openpaas.paasta.gateway;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.openpaas.paasta.gateway.GatewayApplication;
import org.openpaas.paasta.gateway.filter.pre.EurekaStaticResourceRouteFilter;
import org.openpaas.paasta.gateway.filter.pre.RequestLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.netflix.zuul.FilterFileManager;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.netflix.zuul.groovy.GroovyFileFilter;

/**
 * The type Application.
 */

@SpringBootApplication
@EnableZuulProxy
@RefreshScope
public class GatewayApplication {
    private static final Logger logger = LoggerFactory.getLogger( GatewayApplication.class );
    
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
    
    @Bean
    public ZuulFilter requestLoggingFilter() {
        final Optional<ZuulFilter> requestLoggingFilter = Optional.of(new RequestLoggingFilter());
        requestLoggingFilter.ifPresent(
                f -> logger.info("Attach filter : {}@{}", f.getClass(), Integer.hashCode(f.hashCode())) );
        
        return requestLoggingFilter.get();
    }
    
    @Bean
    public ZuulFilter eurekaStaticResourceRedirectFilter() {
        final Optional<ZuulFilter> eurekaRedirectFilter = Optional.of(new EurekaStaticResourceRouteFilter());
        eurekaRedirectFilter.ifPresent(
                f -> logger.info("Attach filter : {}@{}", f.getClass(), Integer.hashCode(f.hashCode())) );
        
        return eurekaRedirectFilter.get();
    }

    public void initializeGroovyFilter() {
        final GroovyCompiler compiler = new GroovyCompiler();
        final GroovyFileFilter filter = new GroovyFileFilter();
        FilterLoader.getInstance().setCompiler(compiler);
        FilterFileManager.setFilenameFilter(filter);

        String basePath = "/workspace/PAAS-TA-PORTAL-GATEWAY/custom-filter/";
        String[] filterPathes = {
                basePath + "/pre",
                basePath + "/route",
                basePath + "/post",
                basePath + "/error"
        };
        
        try {
            FilterFileManager.init(1, filterPathes);
        } catch (Exception e) {
            logger.error( "Unexpected exception...", e );
        }
    }
}
