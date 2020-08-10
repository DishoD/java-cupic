package hr.fer.zemris.java.raytracer.model;

/**
 * Represents a sphere as a GraphicalObject.
 * 
 * @author Disho
 *
 */
public class Sphere extends GraphicalObject {
	/**
	 * center of the sphere
	 */
	Point3D center;
	
	/**
	 * radius of the sphere
	 */
	double radius;
	/**
	 * radius squared of the sphere
	 */
	double radius2;
	/**
	 * diffuse coefficient kd for the red component
	 */
	double kdr;
	
	/**
	 * diffuse coefficient kd for the green component
	 */
	double kdg;
	
	/**
	 * diffuse coefficient kd for the blue component
	 */
	double kdb;
	
	/**
	 * reflective coefficient kr for the red component
	 */
	double krr;
	
	/**
	 * reflective coefficient kr for the green component
	 */
	double krg;
	
	/**
	 * reflective coefficient kr for the blue component
	 */
	double krb;
	
	/**
	 * exponent n in reflective part
	 */
	double krn;

	/**
	 * Initializes the sphere with the given parameters.
	 * 
	 * @param center center of the sphere
	 * @param radius radius of the sphere
	 * @param kdr diffuse coefficient kd for the red component
	 * @param kdg diffuse coefficient kd for the green component
	 * @param kdb diffuse coefficient kd for the blue component
	 * @param krr reflective coefficient kr for the red component
	 * @param krg reflective coefficient kr for the green component
	 * @param krb reflective coefficient kr for the blue component
	 * @param krn exponent n in reflective part
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
		this.radius2 = radius*radius;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D L = center.sub(ray.start);
		double dca = L.scalarProduct(ray.direction);
		if(dca < 0) return null;
		
		double d2 = L.scalarProduct(L) - dca*dca;
		if(d2 > radius2) return null;
		
		double dhc = Math.sqrt(radius2 - d2);
		double d0 = dca - dhc;
		double d1 = dca + dhc;
		
		if(d0 > d1) {
			double temp = d0;
			d0 = d1;
			d1 = temp;
		}
		
		if(d0 < 0) {
			d0 = d1;
			if(d0 < 0) return null;
		}
		
		Point3D intersectionPoint = ray.start.add(ray.direction.scalarMultiply(d0));
		Point3D normal = intersectionPoint.sub(center).normalize();
		double distance = intersectionPoint.sub(ray.start).norm();
		
		return new RayIntersection(intersectionPoint, distance, true) {
			
			@Override
			public Point3D getNormal() {
				return normal;
			}
			
			@Override
			public double getKrr() {
				return krr;
			}
			
			@Override
			public double getKrn() {
				return krn;
			}
			
			@Override
			public double getKrg() {
				return krg;
			}
			
			@Override
			public double getKrb() {
				return krb;
			}
			
			@Override
			public double getKdr() {
				return kdr;
			}
			
			@Override
			public double getKdg() {
				return kdg;
			}
			
			@Override
			public double getKdb() {
				return kdb;
			}
		};
	}

}
