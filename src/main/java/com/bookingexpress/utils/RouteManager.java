package com.bookingexpress.utils;

import java.util.HashMap;
import java.util.Map;

public class RouteManager {
    // Static map to store routes
    private static final Map<String, String[]> ROUTE_MAP = new HashMap<>();

    // Static initializer block to populate routes
    static {
        // Format: Route Number, [Source City, Destination City]
        ROUTE_MAP.put("R001", new String[]{"Mumbai", "Delhi"});
        ROUTE_MAP.put("R002", new String[]{"Bangalore", "Chennai"});
        ROUTE_MAP.put("R003", new String[]{"Kolkata", "Pune"});
        ROUTE_MAP.put("R004", new String[]{"Delhi", "Jaipur"});
        ROUTE_MAP.put("R005", new String[]{"Chennai", "Hyderabad"});
        ROUTE_MAP.put("R006", new String[]{"Indore", "Bhopal"});
    }

    /**
     * Get source and destination cities for a given route
     * @param routeNumber Route identifier
     * @return String array with source (index 0) and destination (index 1)
     */
    public static String[] getRouteCities(String routeNumber) {
        return ROUTE_MAP.get(routeNumber);
    }

    /**
     * Check if a route exists
     * @param routeNumber Route identifier
     * @return boolean indicating route existence
     */
    public static boolean routeExists(String routeNumber) {
        return ROUTE_MAP.containsKey(routeNumber);
    }

    /**
     * Get the source city for a given route
     * @param routeNumber Route identifier
     * @return Source city name
     */
    public static String getSourceCity(String routeNumber) {
        String[] cities = getRouteCities(routeNumber);
        return cities != null ? cities[0] : null;
    }

    /**
     * Get the destination city for a given route
     * @param routeNumber Route identifier
     * @return Destination city name
     */
    public static String getDestinationCity(String routeNumber) {
        String[] cities = getRouteCities(routeNumber);
        return cities != null ? cities[1] : null;
    }

    /**
     * Add a new route
     * @param routeNumber Route identifier
     * @param sourceCity Source city name
     * @param destinationCity Destination city name
     */
    public static void addRoute(String routeNumber, String sourceCity, String destinationCity) {
        if (!routeExists(routeNumber)) {
            ROUTE_MAP.put(routeNumber, new String[]{sourceCity, destinationCity});
        } else {
            throw new IllegalArgumentException("Route already exists");
        }
    }

    /**
     * Get all available routes
     * @return Map of routes
     */
    public static Map<String, String[]> getAllRoutes() {
        return new HashMap<>(ROUTE_MAP);
    }
}