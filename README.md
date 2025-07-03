# Salenty - Spring E-commerce Application

## Description

Salenty is a full-featured e-commerce platform built with the Spring Framework. It provides a robust backend for managing products, users, orders, and carts. The application is designed with a classic Model-View-Controller (MVC) architecture and utilizes Spring Boot for ease of development and deployment. It also integrates with a MySQL database for data persistence and uses Thymeleaf for server-side rendering of web pages.

## Features

* **User Management:**
    * User registration and login with password encryption.
    * Role-based access control (USER, SELLER, ADMIN).
    * Admin panel for managing users.
* **Product Management:**
    * Sellers can add, edit, and delete their products.
    * Admins can view and manage all products.
    * Image uploading for product visuals.
* **Shopping Cart:**
    * Add, remove, and update product quantities in the cart.
    * Persistent cart for logged-in users.
* **Order Processing:**
    * Secure checkout process with order creation.
    * Order history for users.
    * Order management for sellers and admins.
* **Search and Filtering:**
    * Search for products by name.
    * Filter products by category.

## Technologies Used

* **Backend:**
    * Java 21
    * Spring Boot 3.3.0
    * Spring Security
    * Spring Data JPA
    * Maven
* **Frontend:**
    * Thymeleaf
    * Tailwind CSS
    * JavaScript
* **Database:**
    * MySQL
* **Image Hosting:**
    * ImgBB API

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/alpeerkaraca/salenty-spring.git](https://github.com/alpeerkaraca/salenty-spring.git)
    ```
2.  **Navigate to the project directory:**
    ```bash
    cd salenty-spring
    ```
3.  **Install dependencies:**
    ```bash
    mvn install
    ```
4.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

## Configuration

The application's configuration is located in the `src/main/resources/application.properties` file. Key properties include:

* **Database Connection:**
    * `spring.datasource.url`
    * `spring.datasource.username`
    * `spring.datasource.password`
* **Server Port:**
    * `server.port` (default is 8090)

## API Endpoints

The primary endpoints are managed by the following controllers:

* **`AuthenticationController`:** Handles user registration and login.
* **`ProductController`:** Manages product-related operations like adding, editing, and searching for products.
* **`CartController`:** Handles shopping cart functionality.
* **`OrderController`:** Manages the order processing.
* **`GetOperationsController`:** Handles various GET requests for pages like the homepage, product details, and the user account section.

## Database Schema

The application uses several main entities:

* **User:** Represents a user of the platform. It includes details like username, email, password, address, and role.
* **Product:** Represents a product for sale. It includes information like name, price, description, and images.
* **Order:** Represents a customer's order. It contains details about the buyer, the products ordered, and the order status.
* **Cart:** Represents a user's shopping cart, which contains a list of `CartItem` entities.
* **CartItem:** Represents an item within a shopping cart, linking a product to a cart and storing the quantity.
* **Category:** Represents a product category.

## Future Work

* **Implement a more advanced search functionality:** Add features like filtering by price range, brand, and other attributes.
* **Enhance the user interface:** Improve the design and user experience of the frontend.
* **Add payment gateway integration:** Integrate with a real payment gateway like Stripe or PayPal for secure and reliable payment processing.
* **Implement product reviews and ratings:** Allow users to leave reviews and ratings for products.
* **Add internationalization support:** Make the application available in multiple languages.
* **Create a RESTful API:** Develop a separate RESTful API to allow for a decoupled frontend or mobile app.
