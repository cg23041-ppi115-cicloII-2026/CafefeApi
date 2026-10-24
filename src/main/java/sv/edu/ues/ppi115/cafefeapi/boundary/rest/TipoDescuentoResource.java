package sv.edu.ues.ppi115.cafefeapi.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.net.URI;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues.ppi115.control.TipoDescuentoService;
import sv.edu.ues.ppi115.entity.TipoDescuento;

@Path("tipodescuento")
public class TipoDescuentoResource implements Serializable {

    @Inject
    TipoDescuentoService tdService;

    /**
     * GET /tipodescuento?first=0&max=50
     * Devuelve un rango de registros y el total en el header Total-registros.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findRange(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {
        if (first >= 0 && max > 0 && max <= 50) {
            try {
                return Response.ok(tdService.findRange(first, max))
                        .type(MediaType.APPLICATION_JSON)
                        .header("Total-registros", tdService.contar())
                        .build();
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .header("App-Error", ex.getMessage())
                        .build();
            }
        }
        return Response.status(422)
                .header("Wrong-Param", "first,max")
                .build();
    }

    /**
     * GET /tipodescuento/{id}
     * Devuelve un registro especifico, o 404 si no existe.
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") UUID id) {
        if (id != null) {
            try {
                TipoDescuento encontrado = tdService.findById(id);
                if (encontrado != null) {
                    return Response.ok(encontrado)
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-Found", "id: " + id)
                        .build();
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .header("App-Error", ex.getMessage())
                        .build();
            }
        }
        return Response.status(422)
                .header("Wrong-Param", "id")
                .build();
    }

    /**
     * POST /tipodescuento
     * Crea un registro. Responde 201 con el header Location apuntando al nuevo recurso.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crear(TipoDescuento nuevo, @Context UriInfo uriInfo) {
        if (nuevo != null) {
            try {
                if (nuevo.getIdTipoDescuento() == null) {
                    nuevo.setIdTipoDescuento(UUID.randomUUID());
                }
                tdService.create(nuevo);
                URI location = uriInfo.getAbsolutePathBuilder()
                        .path(String.valueOf(nuevo.getIdTipoDescuento()))
                        .build();
                return Response.created(location).build();   // 201
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .header("App-Error", ex.getMessage())
                        .build();
            }
        }
        return Response.status(422)
                .header("Wrong-Param", "body")
                .build();
    }
}