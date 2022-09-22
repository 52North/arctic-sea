/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.faroeREST.springrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ImportResource({"classpath:/faroe.xml",
                 "classpath:/settings-service.xml",
                 "classpath:/settings-service-identification.xml",
                 "classpath:/settings-service-provider.xml"})

@SpringBootApplication
public class SpringrestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringrestApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
        	
        	
            @Override
            public void addCorsMappings(CorsRegistry registry) {
            	 registry.addMapping("/**")
            	 .allowedOrigins("http://localhost:3000")
            	  .allowedMethods("GET","PUT", "POST", "DELETE");
            }
        };
    }

}
