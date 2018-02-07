package org.openpaas.paasta.gateway.filter.pre;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.openpaas.paasta.gateway.filter.FilterOrder.*;
import org.openpaas.paasta.gateway.filter.FilterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

// Eureka redirect filter (bean)
/**
 *
 * @since 2018-02-06
 * @author Hyungu Cho
 *
 */
public class EurekaStaticResourceRouteFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger( EurekaStaticResourceRouteFilter.class ); 
    
    @Value("${zuul.EurekaStaticResourceRouteFilter.pre.redirect-starts-with.filters:/eureka/css/,/eureka/js/,/eureka/fonts/,/eureka/images/}")
    private String[] filters;
    
    @Value("${zuul.EurekaStaticResourceRouteFilter.pre.redirect-starts-with.route-path:/eureka}")
    private String routePath;
    
    @Value("${zuul.EurekaStaticResourceRouteFilter.pre.redirect-starts-with.additional-prefix:/eureka}")
    private String toAdditionalPrefix;
    
    @Override
    public boolean shouldFilter() {
        return true;
    }

    private String getURLRoot(String fullURL, String fullURI) {
        return fullURL.substring(0, fullURL.indexOf(fullURI)) + routePath;
    }
    
    @Override
    public Object run() {
        Optional<RequestContext> ctxOp = Optional.of(RequestContext.getCurrentContext());
        final Stream<String> filterStream = Arrays.stream( Optional.of(filters).get() );
        
        ctxOp.ifPresent(c -> {
            final String uri = c.getRequest().getRequestURI();
            filterStream.filter(fStr -> uri.startsWith(fStr)).findFirst()
                .ifPresent(fStr -> {
                    try {
                        // Destination URL = http://localhost:19999/eureka/eureka/css/wro.css
                        // ex) Root URL = http://localhost:19999/eureka/
                        final String beforeURL = getURLRoot(c.getRequest().getRequestURL().toString(), uri);
                        final String afterURL = beforeURL + toAdditionalPrefix;
                        c.setRouteHost(new URL(afterURL));
                        logger.info("Changing Route URL (for Eureka static resource) : {} -> {}", beforeURL, afterURL);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        });
        
        return null;
    }

    @Override
    public String filterType() {
        return FilterType.ROUTE;
    }

    @Override
    public int filterOrder() {
        return RouteOrder.EurekaStaticResourceRouteFilter;
    }
}

