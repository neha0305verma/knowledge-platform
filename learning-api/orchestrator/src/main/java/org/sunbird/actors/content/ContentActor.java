package org.sunbird.actors.content;

import akka.dispatch.Futures;
import org.sunbird.actor.core.BaseActor;
import org.sunbird.common.Platform;
import org.sunbird.common.dto.Request;
import org.sunbird.common.dto.Response;
import org.sunbird.common.dto.ResponseHandler;
import org.sunbird.graph.nodes.DataNode;
import scala.concurrent.Future;


public class ContentActor extends BaseActor {

    public static String objectType = "Content";
    public static String version = "1.0";

    private static final String CONTENT_KEYSPACE_NAME = Platform.config.hasPath("content.keyspace.name") ? Platform.config.getString("content.keyspace.name") : "content_store";
    private static final String CONTENT_TABLE_NAME = Platform.config.hasPath("content.keyspace.table") ? Platform.config.getString("content.keyspace.table") : "content_data";


    public Future<Response> onReceive(Request request) {
        String operation = request.getOperation();
        try {
            if ("createContent".equals(operation)) {
                return create(request);
            } else {
                return ERROR(operation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Futures.successful(new Response());
        }

    }

    private Future<Response> create(Request request) throws Exception {
        Request createRequest = new Request(request, objectType);
        createRequest.setRequest(request.getRequest());
        createRequest.getContext().put("keyspace",CONTENT_KEYSPACE_NAME);
        createRequest.getContext().put("table",CONTENT_TABLE_NAME);
        createRequest.getContext().put("graph_id", "domain");
        createRequest.getContext().put("version", "1.0");
        Future<Response> result = DataNode.create(request, getContext().dispatcher()).map(node -> {
                Response response = ResponseHandler.OK();
                response.put("node_id", node.getIdentifier());
                response.put("versionKey", node.getMetadata().get("versionKey"));
                return response;
        }, getContext().dispatcher());
        return result;
    }

    private void update(Request request) {
        Request updateRequest = new Request(request, objectType);
    }

}
