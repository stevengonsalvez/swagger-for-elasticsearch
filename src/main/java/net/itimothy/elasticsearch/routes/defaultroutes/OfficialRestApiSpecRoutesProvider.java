package net.itimothy.elasticsearch.routes.defaultroutes;

import net.itimothy.elasticsearch.routes.ModelsCatalog;
import net.itimothy.elasticsearch.routes.RoutesProvider;
import net.itimothy.elasticsearch.restapispec.OfficialRestApiSpecDataProvider;
import net.itimothy.elasticsearch.restapispec.model.Api;
import net.itimothy.elasticsearch.restapispec.model.Param;
import net.itimothy.elasticsearch.routes.model.*;
import net.itimothy.util.CollectionUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.base.Function;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides the routes information derived from the official Elasticsearch REST API specs
 */
public class OfficialRestApiSpecRoutesProvider extends RoutesProvider {
    private final OfficialRestApiSpecDataProvider officialRestApiSpecDataProvider;

    @Inject
    public OfficialRestApiSpecRoutesProvider(Client client, ModelsCatalog modelsCatalog, OfficialRestApiSpecDataProvider officialRestApiSpecDataProvider) {
        super(client, modelsCatalog);

        this.officialRestApiSpecDataProvider = officialRestApiSpecDataProvider;
    }

    @Override
    protected List<Route> getRoutesInternal() {
        Map<String, Api> officialRestApiSpecData = officialRestApiSpecDataProvider.getData(getElasticsearchVersion());

        //
        // Convert to description model
        //
        List<Route> routes = new ArrayList<>();
        for (Map.Entry<String, Api> entry : officialRestApiSpecData.entrySet()) {
            String name = entry.getKey();
            Api api = entry.getValue();
            for (String method : api.methods) {
                for (String path : api.url.paths) {
                    routes.add(
                        Route.builder()
                            .name(name)
                            .group(getRouteGroup(name))
                            .description("<a target=\"_new\" href=\"" + api.documentation + "\">Show documentation for <strong>" + name + "</strong></a>")
                            .method(toMethod(method))
                            .apiPath(path.substring(1))
                            .parameters(
                                toParameters(api)
                            )
                            .build()
                    );
                }
            }
        }

        CollectionUtil.sort(routes, new Function<Route, Comparable>() {
            @Override
            public Comparable apply(Route route) {
                return route.getGroup();
            }
        });

        return routes;
    }

    private String getRouteGroup(String name) {
        name = name.replace('_', '.')
            .replace(".get.", ".")
            .replace(".put.", ".")
            .replace(".post.", ".")
            .replace(".delete.", ".")
            .replace(".", " ");

        String[] parts = name.split(" ");

        return parts.length > 1
            ? parts[0] + ": " + StringUtils.join(parts, " ", 1, parts.length)
            : name;
    }

    private List<Parameter> toParameters(Api api) {
        List<Parameter> parameters = new ArrayList<>();

        if (api.url.parts != null) {
            for (Map.Entry<String, Param> entry : api.url.parts.entrySet()) {
                String name = entry.getKey();
                Param param = entry.getValue();
                parameters.add(
                    Parameter.builder()
                        .name(name)
                        .description(param.description)
                        .defaultValue(param.defaultValue)
                        .model(toModel(name, param))
                        .paramType(ParamType.PATH)
                        .build()
                );
            }
        }

        if (api.url.params != null) {
            for (Map.Entry<String, Param> entry : api.url.params.entrySet()) {
                String name = entry.getKey();
                Param param = entry.getValue();
                parameters.add(
                    Parameter.builder()
                        .name(name)
                        .description(param.description)
                        .defaultValue(param.defaultValue)
                        .model(toModel(name, param))
                        .paramType(ParamType.QUERY)
                        .build()
                );
            }
        }

        if (api.body != null) {
            parameters.add(
                Parameter.builder()
                    .name("body")
                    .description(api.body.description)
                    .defaultValue(api.body.defaultValue)
                    .model(toModel("body", api.body))
                    .paramType(ParamType.BODY)
                    .build()
            );
        }

        return parameters;
    }

    private Model toModel(String name, Param param) {
        return Primitive.STRING;
    }

    private HttpMethod toMethod(String method) {
        switch (method) {
            case "GET":
                return HttpMethod.GET;
            case "DELETE":
                return HttpMethod.DELETE;
            case "PUT":
                return HttpMethod.PUT;
            case "POST":
                return HttpMethod.POST;
            case "HEAD":
                return HttpMethod.HEAD;
            case "OPTIONS":
                return HttpMethod.OPTIONS;
            case "PATCH":
                return HttpMethod.PATCH;
        }
        return HttpMethod.UNKNOWN;
    }
}
