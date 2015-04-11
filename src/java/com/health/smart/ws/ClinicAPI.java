/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.ws;

import com.health.smart.ejb.ClinicEJB;
import static com.health.smart.entity.Patient_.token;
import com.health.smart.entity.User;
import com.health.smart.util.GsonHelper;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/clinic")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class ClinicAPI {

    @Inject
    private ClinicEJB clinicEjb;
    final String SUCCESS_MSG = "SUCCESS";

    @GET
    @Path("/login")
    public Response getToken(@QueryParam("id") String id, @QueryParam("password") String password) {
        try {
            User u = clinicEjb.login(id, password);
            return Response.status(200).entity(GsonHelper.toString(u)).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }
}
