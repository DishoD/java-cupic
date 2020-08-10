package hr.fer.zemris.oopj.hw17.galerija.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import hr.fer.zemris.oopj.hw17.galerija.DB.PhotoDBProvider;
import hr.fer.zemris.oopj.hw17.galerija.DB.PhotoInfo;

/**
 * Retrieves JSON information of photo tags.
 * 
 * @author Disho
 *
 */
@Path("/tags")
public class Tags {
	/**
	 * Retrieves a JSON array of the available photo tags.
	 * 
	 * @return JSON array of tags
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTags() {
		Gson gson = new Gson();
		return Response.ok(gson.toJson(PhotoDBProvider.getPhotoDB().getTags()).toString()).build();
	}
	
	/**
	 * Retrieves a JSON array of the photo ID-s that have the given tag.
	 * 
	 * @param tag tag of the searched photos
	 * @return JSON array of the photo ID-s
	 */ 
	@Path("{tag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPhotosOfTag(@PathParam("tag") String tag) {
		Gson gson = new Gson();
		List<Integer> photoIds = PhotoDBProvider.getPhotoDB().getPhotosOfTag(tag).stream().map(PhotoInfo::getId).collect(Collectors.toList());
		return Response.ok(gson.toJson(photoIds).toString()).build();
	}
}
