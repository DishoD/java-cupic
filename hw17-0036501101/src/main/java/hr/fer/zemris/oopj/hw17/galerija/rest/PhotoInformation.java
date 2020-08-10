package hr.fer.zemris.oopj.hw17.galerija.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import hr.fer.zemris.oopj.hw17.galerija.DB.PhotoDBProvider;

/**
 * Retrieves the information of the photo in the JSON format.
 * 
 * @author Disho
 *
 */
@Path("/photo")
public class PhotoInformation {
	/**
	 * Retrieves an photo information for the photo of the given id.
	 * 
	 * @param id photo id
	 * @return  photo information in JSON format
	 */
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPhotoInformation(@PathParam("id") int id) {
		Gson gson =  new Gson();
		
		return Response.ok(gson.toJson(PhotoDBProvider.getPhotoDB().getPhotoOfId(id)).toString()).build();
	}
}
