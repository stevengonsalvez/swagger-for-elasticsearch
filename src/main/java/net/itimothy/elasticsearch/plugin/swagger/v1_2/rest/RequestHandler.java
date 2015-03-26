package net.itimothy.elasticsearch.plugin.swagger.v1_2.rest;

import net.itimothy.elasticsearch.routes.defaultroutes.DefaultRoutesProvider;
import net.itimothy.elasticsearch.routes.ModelsCatalog;
import net.itimothy.elasticsearch.routes.indexroutes.IndexRoutesProvider;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.common.settings.Settings;
import net.itimothy.elasticsearch.plugin.swagger.v1_2.model.SwaggerModel;
import org.elasticsearch.rest.*;
import net.itimothy.elasticsearch.routes.RoutesProvider;

import static org.elasticsearch.rest.RestStatus.OK;

public abstract class RequestHandler extends BaseRestHandler {

    private final DefaultRoutesProvider defaultRoutesProvider;
    private final ModelsCatalog modelsCatalog;

    protected RequestHandler(Settings settings, RestController controller, Client client, DefaultRoutesProvider defaultRoutesProvider, ModelsCatalog modelsCatalog) {
        super(settings, controller, client);
        this.defaultRoutesProvider = defaultRoutesProvider;
        this.modelsCatalog = modelsCatalog;
    }

    protected abstract SwaggerModel handleRequest(RestRequest request, Client client) throws Exception;

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        try {
            SwaggerModel model = handleRequest(request, client);
            RestResponse response = new BytesRestResponse(OK, "application/json", model.toJson());

            response.addHeader("Access-Control-Allow-Methods", "GET");

            channel.sendResponse(response);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    protected RoutesProvider getMetadataProvider(RestRequest request, Client client) {
        String indexOrAlias = request.param("indexOrAlias");

        return StringUtils.isBlank(indexOrAlias)
            ? defaultRoutesProvider
            : new IndexRoutesProvider(client, modelsCatalog, indexOrAlias);
    }
}
