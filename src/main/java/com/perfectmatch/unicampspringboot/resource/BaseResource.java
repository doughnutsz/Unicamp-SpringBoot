package com.perfectmatch.unicampspringboot.resource;

import javax.ws.rs.core.Response;

public interface BaseResource {
    default Response successResponse(Object data){
        return helper(Response.Status.OK, data);
    }

    default Response clientErrorResponse(Object data){
        return helper(Response.Status.BAD_REQUEST, data);
    }

    default Response serverErrorResponse(Object data){
        return helper(Response.Status.INTERNAL_SERVER_ERROR, data);
    }

    default Response helper(Response.Status status, Object data){
        return Response.status(status).entity(data).build();
    }
}
