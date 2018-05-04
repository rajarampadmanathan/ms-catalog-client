package com.example.demo;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@SpringBootApplication
@EnableEurekaClient
@RestController
@EnableCaching
@EnableRabbit
@EnableAspectJAutoProxy
// @EnableFeignClients
public class CatalogClientApplication {

	private static final Logger LOG=LoggerFactory.getLogger(CatalogClientApplication.class);
	
	@Autowired
	EurekaClient eurekaClient;

	// @Autowired
	// UserClient userClient;

	public static void main(String[] args) {
		SpringApplication.run(CatalogClientApplication.class, args);
	}

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	LoadBalancerClient loadBalancerClient;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/*
	 * @Autowired HttpServletRequest request;
	 */



	@CacheEvict(key = "#userId", value = "user")
	public void invalidateUser(String userId) {
		System.out.println("Evicting " + userId);
	}

	@Cacheable(key = "#userId", value = "user")
	@GetMapping(value = "/user/{userId}")
	public String getCatalogService(@PathVariable("userId") String userId) {
		// System.out.println(loadBalancerClient+"__"+request);
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest req = sra.getRequest();
		Application a = eurekaClient.getApplication("catalog-service");
		String url = loadBalancerClient.choose("catalog-service").getUri() + "/user/" + userId;
		LOG.debug(url);
		String s = restTemplate.getForObject(url, String.class);
		return s;
	}

	// @FeignClient("catalog-service")
	interface UserClient {
		String sayHello(int userId);
	}
}

/*
 * @Component class JerseyConfig extends ResourceConfig {
 * 
 * public JerseyConfig() { register(CatalogClientApplication.class); } }
 */