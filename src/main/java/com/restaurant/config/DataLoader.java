package com.restaurant.config;

import com.restaurant.entity.*;
import com.restaurant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si la base está vacía
        if (userRepository.count() == 0) {
            loadSampleData();
        }
    }

    private void loadSampleData() {
        try {
            // Crear usuario de prueba
            User user = new User();
            user.setEmail("test@example.com");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setFullName("Usuario de Prueba");
            user.setPhone("123456789");
            user.setAddress("Calle Principal 123");
            user.setRole(UserRole.CUSTOMER);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);

            // Crear usuario admin
            User admin = new User();
            admin.setEmail("admin@restaurant.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrador");
            admin.setPhone("987654321");
            admin.setAddress("Oficina Central");
            admin.setRole(UserRole.ADMIN);
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);

            // Crear restaurante de ejemplo
            Restaurant restaurant1 = new Restaurant();
            restaurant1.setName("La Pizzería Italiana");
            restaurant1.setDescription("Auténtica pizza italiana con ingredientes frescos");
            restaurant1.setAddress("Av. Lima 456, Miraflores");
            restaurant1.setPhone("987654321");
            restaurant1.setImageUrl("https://images.unsplash.com/photo-1513104890138-7c749659a591?w=300&h=200&fit=crop");
            restaurant1.setCapacity(50);
            restaurant1.setOpenTime("11:00");
            restaurant1.setCloseTime("23:00");
            restaurant1 = restaurantRepository.save(restaurant1);

            Restaurant restaurant2 = new Restaurant();
            restaurant2.setName("Sushi Sakura");
            restaurant2.setDescription("Sushi fresco y delicioso preparado por chefs japoneses");
            restaurant2.setAddress("Jr. Cusco 789, San Isidro");
            restaurant2.setPhone("555444333");
            restaurant2.setImageUrl("https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=300&h=200&fit=crop");
            restaurant2.setCapacity(30);
            restaurant2.setOpenTime("12:00");
            restaurant2.setCloseTime("22:00");
            restaurant2 = restaurantRepository.save(restaurant2);

            Restaurant restaurant3 = new Restaurant();
            restaurant3.setName("Burger House");
            restaurant3.setDescription("Las mejores hamburguesas artesanales de la ciudad");
            restaurant3.setAddress("Av. Arequipa 1234, San Isidro");
            restaurant3.setPhone("111222333");
            restaurant3.setImageUrl("https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=300&h=200&fit=crop");
            restaurant3.setCapacity(40);
            restaurant3.setOpenTime("10:00");
            restaurant3.setCloseTime("24:00");
            restaurant3 = restaurantRepository.save(restaurant3);

            // Crear items del menú para La Pizzería
            createMenuItem("Pizza Margherita", "Tomate, mozzarella y albahaca fresca",
                    new BigDecimal("25.90"), "Pizzas", restaurant1);
            createMenuItem("Pizza Pepperoni", "Tomate, mozzarella y pepperoni",
                    new BigDecimal("29.90"), "Pizzas", restaurant1);
            createMenuItem("Pizza Hawaiana", "Tomate, mozzarella, jamón y piña",
                    new BigDecimal("28.90"), "Pizzas", restaurant1);
            createMenuItem("Lasagna Bolognesa", "Capas de pasta con salsa bolognesa y bechamel",
                    new BigDecimal("32.90"), "Pastas", restaurant1);
            createMenuItem("Tiramisu", "Postre italiano con café y mascarpone",
                    new BigDecimal("12.90"), "Postres", restaurant1);

            // Crear items del menú para Sushi Express
            createMenuItem("Sashimi de Salmón", "Cortes frescos de salmón (8 piezas)",
                    new BigDecimal("32.00"), "Sashimi", restaurant2);
            createMenuItem("California Roll", "Cangrejo, aguacate y pepino (8 piezas)",
                    new BigDecimal("28.50"), "Rolls", restaurant2);
            createMenuItem("Philadelphia Roll", "Salmón, queso crema y aguacate (8 piezas)",
                    new BigDecimal("35.00"), "Rolls", restaurant2);
            createMenuItem("Gyoza", "Empanadillas japonesas rellenas de cerdo (6 piezas)",
                    new BigDecimal("18.90"), "Entradas", restaurant2);
            createMenuItem("Mochi", "Postre japonés de arroz dulce (3 piezas)",
                    new BigDecimal("15.90"), "Postres", restaurant2);

            // Crear items del menú para Burger House
            createMenuItem("Burger Clásica", "Carne de res, lechuga, tomate, cebolla y papas",
                    new BigDecimal("24.90"), "Hamburguesas", restaurant3);
            createMenuItem("Bacon Cheeseburger", "Carne de res, bacon, queso cheddar y papas",
                    new BigDecimal("29.90"), "Hamburguesas", restaurant3);
            createMenuItem("Chicken Burger", "Pollo a la plancha, lechuga, tomate y papas",
                    new BigDecimal("22.90"), "Hamburguesas", restaurant3);
            createMenuItem("Alitas BBQ", "Alitas de pollo con salsa barbacoa (8 piezas)",
                    new BigDecimal("26.90"), "Entradas", restaurant3);
            createMenuItem("Milkshake de Chocolate", "Batido cremoso de chocolate",
                    new BigDecimal("12.90"), "Bebidas", restaurant3);

            System.out.println("✅ Datos de ejemplo cargados exitosamente!");
            System.out.println("👤 Usuario de prueba: test@example.com / password123");
            System.out.println("👨‍💼 Admin: admin@restaurant.com / admin123");
            System.out.println("🏪 Restaurantes creados: " + restaurantRepository.count());
            System.out.println("🍕 Items del menú creados: " + menuItemRepository.count());

        } catch (Exception e) {
            System.err.println("❌ Error al cargar datos de ejemplo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createMenuItem(String name, String description, BigDecimal price,
                                String category, Restaurant restaurant) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setImageUrl("https://via.placeholder.com/200x150?text=" + name.replace(" ", "+"));
        menuItem.setCategory(category);
        menuItem.setAvailable(true);
        menuItem.setRestaurant(restaurant);
        menuItemRepository.save(menuItem);
    }
}