package com.perfectmatch.unicampspringboot.resource;

import com.perfectmatch.unicampspringboot.UnicampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component
@Path("/v1")
@Produces("application/json")
@Consumes("application/json")
public class UnicampResource implements BaseResource{
    @Autowired
    private UnicampService unicampService;

    @GET
    @Path("/user/count")
    public Response getUserCount(){
        try {
            return successResponse(unicampService.userCount());
        } catch (Exception e){
            return serverErrorResponse(e.toString());
        }
    }
}