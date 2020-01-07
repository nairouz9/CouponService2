/**
 * 
 */
package jersyServlet;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.couponSystem.beans.Category;
import com.couponSystem.beans.Coupon;
import com.couponSystem.classes.ClientType;
import com.couponSystem.classes.CouponSystemException;
import com.couponSystem.facade.CustomerFacade;
import com.couponSystem.pool.LoginManager;

/**
 * @author scary
 *
 */
@Path("/customerService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerService {

	@Context
	HttpSession session;
	@Inject
	CustomerFacade customerFacade;

	public CustomerService() {
	}

	@PostConstruct
	private CustomerFacade getFacadeSession(HttpServletRequest request) {
		session = request.getSession();
		customerFacade = (CustomerFacade) session.getAttribute("customerService");
		return customerFacade;
	}

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	@Path("/purchaseCoupon")
	public Response purchaseCoupon(Coupon coupon, @Context HttpServletRequest request) {
		customerFacade = getFacadeSession(request);
		try {
			customerFacade.purCoupon(coupon);
			return Response.status(Status.OK).entity(coupon).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@GET
	@Path("/getAllPurchased")
	public List<Coupon> getAllPurchased(@Context HttpServletRequest request) throws CouponSystemException {
		customerFacade = getFacadeSession(request);
		return customerFacade.getCustomerCoupons();
	}

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@GET
	@Path("/getAllPurchasedByType/{type}")
	public List<Coupon> getAllPurchasedByType(@PathParam("type") Category type, @Context HttpServletRequest request)
			throws CouponSystemException {
		customerFacade = getFacadeSession(request);
		return customerFacade.getCustomerCoupons(type);
	}

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@GET
	@Path("/getAllPurchasedByPrice/{price}")
	public List<Coupon> getAllPurchasedByPrice(@PathParam("price") double price, @Context HttpServletRequest request)
			throws CouponSystemException {
		customerFacade = getFacadeSession(request);
		return customerFacade.getCustomerCoupons(price);
	}

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@GET
	@Path("/login/{user}/{pass}")
	public Response login(@PathParam("user") String user, @PathParam("pass") String password,
			@Context HttpServletRequest request) throws CouponSystemException {
		customerFacade = (CustomerFacade) LoginManager.getInstance().login(user, password, ClientType.CUSTOMER);
		if (customerFacade != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("customerService", customerFacade);
			return Response.status(Status.ACCEPTED).entity(customerFacade).build();
		}
		return Response.status(Status.UNAUTHORIZED).entity(user).build();
	}

	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@Path("/logout")
	public Response logout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		session.invalidate();
		return Response.status(Status.ACCEPTED).entity(customerFacade).build();
	}
}
