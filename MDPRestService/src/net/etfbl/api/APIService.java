package net.etfbl.api;

import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.etfbl.model.Employee;

@Path("/employees")
public class APIService {

	EmployeeService service;

	public APIService() {
		service = new EmployeeService();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Employee> getAll() {
		return service.getEmployees();
	}

	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByUsername(@PathParam("username") String username) {
		Employee employee = service.getByUsername(username);
		if (employee != null) {
			return Response.status(200).entity(employee).build();
		} else {
			return Response.status(404).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginLogout(Employee employee) {
		if (service.logging(employee, true)) {
			return Response.status(200).entity(employee).build();
		}
		return Response.status(404).build();
	}

	@PUT
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePass(Employee employee, @PathParam("username") String username) {
		if (service.updatePassword(employee)) {
			return Response.status(200).entity(employee).build();
		} else {
			return Response.status(500).entity("Greska pri dodavanju").build();
		}
	}

	@DELETE
	@Path("/{username}")
	public Response remove(@PathParam("username") String username) {
		if (service.logging(new Employee(username,"logout"),false)) {
			return Response.status(200).build();
		}
		return Response.status(404).build();
	}
}
