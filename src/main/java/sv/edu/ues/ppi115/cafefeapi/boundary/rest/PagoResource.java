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
import sv.edu.ues.ppi115.control.PagoService;
import sv.edu.ues.ppi115.entity.Pago;
 
@Path("pago")
public class PagoResource implements Serializable {
 
    @Inject
    PagoService service;
 
    /**
     * GET /pago?first=0&max=50
     * Devuelve un rango de registros y el total en el header Total-registros.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findRange(
            @QueryParam("factura") UUID factura,
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {
        if (first >= 0 && max > 0 && max <= 50) {
            try {
                if (factura != null) {
                    // GET /pago?factura={uuid}
                    return Response.ok(service.findByFactura(factura, first, max))
                            .type(MediaType.APPLICATION_JSON)
                            .header("Total-registros", service.contarPorFactura(factura))
                            .build();
                }
                return Response.ok(service.findRange(first, max))
                        .type(MediaType.APPLICATION_JSON)
                        .header("Total-registros", service.contar())
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
     * GET /pago/{id}
     * Devuelve un registro especifico, o 404 si no existe.
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") UUID id) {
        if (id != null) {
            try {
                Pago encontrado = service.findById(id);
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
     * POST /pago
     * Crea un registro. Responde 201 con el header Location del nuevo recurso.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crear(Pago nuevo, @Context UriInfo uriInfo) {
        if (nuevo != null) {
            try {
                if (nuevo.getIdPago() == null) {
                    nuevo.setIdPago(UUID.randomUUID());
                }
                service.create(nuevo);
                URI location = uriInfo.getAbsolutePathBuilder()
                        .path(String.valueOf(nuevo.getIdPago()))
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
 
