package restaurant.project.order_table.service;

import restaurant.project.order_table.entity.CustomerEntity;

public interface CustomerService {

    /**
     * Register a new customer account
     *
     * @param fullName customer full name
     * @param phone customer phone number
     * @param rawPassword customer raw password (will be encoded)
     * @return created customer entity
     */
    CustomerEntity register(String fullName, String phone, String rawPassword);

    /**
     * Authenticate a customer and generate a JWT token
     *
     * @param phone customer phone number
     * @param rawPassword customer raw password
     * @return JWT token string
     */
    String login(String phone, String rawPassword);
}
