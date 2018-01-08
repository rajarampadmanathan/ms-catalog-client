package com.example.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@SpringBootApplication
@EnableEurekaClient
@RestController
//@EnableFeignClients
public class CatalogClientApplication {

	@Autowired
	EurekaClient eurekaClient;
	
	//@Autowired
	//UserClient userClient; 
	
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
	
/*	@Autowired
	HttpServletRequest request;
*/

	@GetMapping(value="/user/{userId}")
public String getCatalogService(@PathVariable("userId") String userId) {
	//System.out.println(loadBalancerClient+"__"+request);
	  ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
	    HttpServletRequest req = sra.getRequest();  
	Application a=eurekaClient.getApplication("catalog-service");
	String url=loadBalancerClient.choose("catalog-service").getUri()+"/user/"+userId;
	System.out.println(url);
	String s =restTemplate.getForObject(url, String.class);
	return s;	
}


//@FeignClient("catalog-service")
interface UserClient{
	String sayHello(int userId);
}
}

/*@Component
class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(CatalogClientApplication.class);
		}
}*/