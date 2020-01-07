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

import com.couponSystem.beans.Category;
import com.couponSystem.beans.Coupon;
import com.couponSystem.classes.ClientType;
import com.couponSystem.classes.CouponSystemException;
import com.couponSystem.facade.CompanyFacade;
import com.couponSystem.pool.LoginManager;

/**
 * @author scary
 *
 */
@Path("/companyService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompanyService {

	@Context
	HttpSession session;
	@Inject
	CompanyFacade companyFacade;

	public CompanyService() {
	}

	@PostConstruct
	private CompanyFacade getFacadeSession(HttpServletRequest request) {
		session = request.getSession();
		companyFacade = (CompanyFacade) session.getAttribute("companyService");
		return companyFacade;
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	@Path("/createCoupon")
	public Response createCoupon(Coupon coupon, @Context HttpServletRequest request) {
		companyFacade = getFacadeSession(request);
		try {
			companyFacade.addCoupon(coupon);
			return Response.status(Status.OK).entity(coupon).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	@Path("/removeCoupon/{id}")
	public Response removeCoupon(@PathParam("id") Coupon coupon, @Context HttpServletRequest request) {
		companyFacade = getFacadeSession(request);
		try {
			companyFacade.deleteCoupon(coupon.getId());
			return Response.status(Status.OK).entity(coupon).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PUT
	@Path("/updateCoupon")
	public Response updateCoupon(Coupon coupon, @Context HttpServletRequest request) {
		companyFacade = getFacadeSession(request);
		try {
			companyFacade.updateCoupon(coupon);
			return Response.status(Status.OK).entity(coupon).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@Path("/getCoupon/{id}")
	public Coupon getCoupon(@PathParam("id") int id, @Context HttpServletRequest request) throws CouponSystemException {
		companyFacade = getFacadeSession(request);
		return companyFacade.getOneCoupon(id);
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@Path("/getAllCoupons")
	public List<Coupon> getAllCoupons(@Context HttpServletRequest request) throws CouponSystemException {
		companyFacade = getFacadeSession(request);
		return companyFacade.getCompanyCoupons();
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@Path("/getCouponByType/{type}")
	public List<Coupon> getCouponByType(@PathParam("type") Category clientType, @Context HttpServletRequest request)
			throws CouponSystemException {
		companyFacade = getFacadeSession(request);
		return companyFacade.getCompanyCoupons(clientType);
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@Path("/getCouponByMaxPrice/{price}")
	public List<Coupon> getCouponByMaxPrice(@PathParam("price") Double MaxPrice, @Context HttpServletRequest request)
			throws CouponSystemException {
		companyFacade = getFacadeSession(request);
		return companyFacade.getCompanyCoupons(MaxPrice);
	}

	@Path("/login/{user}/{pass}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@PathParam("user") String userName, @PathParam("pass") String password,
			@Context HttpServletRequest request) {
		try {
			companyFacade = (CompanyFacade) LoginManager.getInstance().login(userName, password, ClientType.COMPANY);
			if (companyFacade != null) {
				HttpSession session = request.getSession(true);
				session.setAttribute("adminService", companyFacade);
				return Response.status(Status.ACCEPTED).entity(companyFacade).build();
			}
			return Response.status(Status.UNAUTHORIZED).entity(userName).build();
		} catch (CouponSystemException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@Path("/logout")
	public Response logout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		session.invalidate();
		return Response.status(Status.ACCEPTED).entity(companyFacade).build();
	}
}
