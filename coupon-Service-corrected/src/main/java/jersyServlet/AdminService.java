/**
 * 
 */
package jersyServlet;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.couponSystem.beans.Company;
import com.couponSystem.beans.Customer;
import com.couponSystem.classes.ClientType;
import com.couponSystem.classes.CouponSystemException;
import com.couponSystem.facade.AdminFacade;
import com.couponSystem.pool.LoginManager;

/**
 * @author scary
 *
 */
@Path("/adminService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminService {

	@Context
	HttpSession session;
	AdminFacade adminFacade;

	public AdminService() {
		System.out.println("from CTOR");
	}

	@PostConstruct
	private AdminFacade getFacadeSession(HttpServletRequest request) {
		session = request.getSession();
		adminFacade = (AdminFacade) session.getAttribute("adminService");
		return adminFacade;
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	@Path("/createCompany")
	public Response createCompany(Company company, @Context HttpServletRequest request) {
		adminFacade = getFacadeSession(request);
		try {
			adminFacade.addCompany(company);
			return Response.status(Status.OK).entity(company).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	@Path("removeCompany/{id}")
	public Response removeCompany(@PathParam("id") Company company, @Context HttpServletRequest request) {
		adminFacade = getFacadeSession(request);
		try {
			adminFacade.deleteCompany(company.getId());
			return Response.status(Status.OK).entity(company).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PUT
	@Path("updateCompany")
	public Response updateCompany(@PathParam("id") Company company, @Context HttpServletRequest request) {
		adminFacade = getFacadeSession(request);
		try {
			adminFacade.updateCompany(company);
			return Response.status(Status.OK).entity(company).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}
	}

	@Path("getCompany/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Company getCompany(@PathParam("id") int id, @Context HttpServletRequest request)
			throws CouponSystemException {
		adminFacade = getFacadeSession(request);
		Company company;
		company = adminFacade.getOneCompany(id);
		return company;

	}

	@Path("getAllCompanies")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Company> getAllCompanies(@Context HttpServletRequest request) throws CouponSystemException {
		adminFacade = getFacadeSession(request);
		return adminFacade.getAllCompanies();

	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	@Path("createCustomer")
	public Response createCustomer(Customer customer, @Context HttpServletRequest request) {
		adminFacade = getFacadeSession(request);
		try {
			adminFacade.addCustomer(customer);
			return Response.status(Status.OK).entity(customer).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	@Path("removeCustomer/{id}")
	public Response removeCustomer(@PathParam("id") Customer customer, HttpServletRequest request) {
		adminFacade = getFacadeSession(request);
		try {
			adminFacade.deleteCustomer(customer.getId());
			return Response.status(Status.OK).entity(customer).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PUT
	@Path("updateCustomer")
	public Response updateCustomer(Customer customer, @Context HttpServletRequest request) {
		adminFacade = getFacadeSession(request);
		try {
			adminFacade.updateCustomer(customer);
			return Response.status(Status.OK).entity(customer).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@Path("getCustomer")
	public Customer getCustomer(@PathParam("id") int id, @Context HttpServletRequest request)
			throws CouponSystemException {
		adminFacade = getFacadeSession(request);
		return adminFacade.getOneCustomer(id);

	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@Path("getAllCustomers")
	public List<Customer> getAllCustomers(@Context HttpServletRequest request) throws CouponSystemException {
		System.out.println("from get all customers");
		adminFacade = getFacadeSession(request);
		return adminFacade.getAllCustomers();
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	@Path("login/{user}/{pass}")
	public Response login(@PathParam("user") String userName, @PathParam("pass") String password,
			@Context HttpServletRequest request) {
		try {
			adminFacade = (AdminFacade) LoginManager.getInstance().login(userName, password, ClientType.ADMINISTRATOR);
			if (adminFacade != null) {
				HttpSession session = request.getSession(true);
				session.setAttribute("adminService", adminFacade);
				return Response.status(Status.ACCEPTED).entity(adminFacade).build();
			}
			return Response.status(Status.UNAUTHORIZED).entity(userName).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Path("logout")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response Logout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		session.invalidate();
		return Response.status(Status.ACCEPTED).entity(adminFacade).build();
	}
}
