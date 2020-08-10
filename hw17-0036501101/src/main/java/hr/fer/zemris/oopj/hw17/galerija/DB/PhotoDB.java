package hr.fer.zemris.oopj.hw17.galerija.DB;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Database of photo information.
 * 
 * @author Disho
 *
 */
public final class PhotoDB {
	/**
	 * list of information for each photo
	 */
	private List<PhotoInfo> photos = new ArrayList<>();
	/**
	 * map of a tags and corresponding photos
	 */
	private Map<String, List<PhotoInfo>> photosOfTag = new HashMap<>();
	/**
	 * set of tags
	 */
	private Set<String> tags = new HashSet<>();
	
	/**
	 * Initializes the database with the given initializer file.
	 * 
	 * @param descriptorPath path to a descriptor
	 */
	public PhotoDB(String descriptorPath) throws IOException {
		Path path = Paths.get(descriptorPath);
		
		List<String> lines = Files.readAllLines(path);
		for(int i = 0; i < lines.size(); i += 3) {
			String name = lines.get(i);
			String description = lines.get(i+1);
			String[] tags = lines.get(i+2).split(",");
			
			for(int j = 0; j < tags.length; ++j) {
				tags[j] = tags[j].trim();
				if(!photosOfTag.containsKey(tags[j])) {
					photosOfTag.put(tags[j], new ArrayList<>());
				}
			}
			
			PhotoInfo photo = new PhotoInfo(name, description, tags, i/3);
			for(String tag : tags) {
				photosOfTag.get(tag).add(photo);
				this.tags.add(tag);
			}
			
			photos.add(photo);
		}
	}
	
	/**
	 * Get all photo tags.
	 * 
	 * @return tags
	 */
	public Set<String> getTags() {
		return tags;
	}
	
	/**
	 * Get a list of information of photos of a given tag.
	 * 
	 * @param tag photos of this tag
	 * @return photo information
	 */
	public List<PhotoInfo> getPhotosOfTag(String tag) {
		return photosOfTag.get(tag);
	}
	
	/**
	 * get photo information of the given photo id
	 * 
	 * @param id photo id
	 * @return photo information
	 */
	public PhotoInfo getPhotoOfId(int id) {
		return photos.get(id);
	}
}
