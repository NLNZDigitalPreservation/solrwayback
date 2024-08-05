package dk.kb.netarchivesuite.solrwayback.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dk.kb.netarchivesuite.solrwayback.listeners.InitializationContextListener;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

import javax.servlet.ServletConfig;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import dk.kb.netarchivesuite.solrwayback.service.exception.ServiceExceptionMapper;
import io.swagger.v3.oas.annotations.tags.Tags;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;


@ApplicationPath("/solrwayback/services")
public class SolrWaybackApplication extends Application {

    /**
     * Constructer is declared and edited to create INFO block of OpenApi specification.
     * Defined as specified in the OpenAPI documentation
     * <a href="https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#jax-rs-application-1">here</a>.
     */
    public SolrWaybackApplication(@Context ServletConfig servletConfig) {
        super();
        OpenAPI oas = new OpenAPI();
        Info info = new Info()
                .title("SolrWayback")
                .version(InitializationContextListener.version)
                .summary("Description of the SolrWayback API.")
                .description("SolrWayback is a web application for browsing historical harvested ARC/WARC files similar to the Internet Archive Wayback Machine. " +
                        "SolrWayback runs on a Solr server containing ARC/WARC files indexed using the warc-indexer")
                .contact(new Contact()
                        .name("Placeholder")
                        .email("placeholder@placeholder.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

        oas.info(info);
        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true)
                .resourcePackages(Stream.of("io.swagger.sample.resource").collect(Collectors.toSet()));

        try {
            new JaxrsOpenApiContextBuilder()
                    .servletConfig(servletConfig)
                    .application(this)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

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