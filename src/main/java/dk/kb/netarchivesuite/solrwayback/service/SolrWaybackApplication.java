package dk.kb.netarchivesuite.solrwayback.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import dk.kb.netarchivesuite.solrwayback.service.exception.ServiceExceptionMapper;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;


public class SolrWaybackApplication extends Application {

    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(
            JacksonJsonProvider.class,
            SolrWaybackResource.class,
            SolrWaybackResourceWeb.class,
            ServiceExceptionMapper.class,
            OpenApiResource.class
            ));
    }


}