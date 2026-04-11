package restaurant.project.order_table.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        Path uploadPath = Paths.get(uploadDir);
        String absolutePath = uploadPath.toFile().getAbsolutePath();

        System.out.println("Serving static files from: " + absolutePath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + absolutePath + "/");
    }
}