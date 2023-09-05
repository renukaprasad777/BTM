package shoppersStack;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class ShoppersStackShopper {
	String baseUrl = "https://www.shoppersstack.com/shopping";
	int shopperId;
	String token;
	int productId;
	@Test(priority = 1)
	void shopperLogin() {
		RestAssured.baseURI="https://www.shoppersstack.com/shopping";
//		HashMap lb=new HashMap();
//		lb.put("email", "api.rp21@gmail.com");
//		lb.put("password", "Reset@123");
//		lb.put("role", "SHOPPER");
		
		String loginResponse=given().log().all().contentType(ContentType.JSON).body("{\r\n"
				+ "  \"email\": \"api.rp21@gmail.com\",\r\n"
				+ "  \"password\": \"Reset@123\",\r\n"
				+ "  \"role\": \"SHOPPER\"\r\n"
				+ "}")
		.when().post("/users/login")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		System.out.println(loginResponse);
		
		JsonPath js=new JsonPath(loginResponse);
		shopperId=js.get("data.userId");
		token = js.get("data.jwtToken");
		System.out.println(shopperId);
		System.out.println(token);
	}
	@Test(priority = 2)
	void ViewProducts() {
		
		productId =given().log().all()
		.when().get(baseUrl+"/products/alpha")
		.jsonPath().get("data[0].productId");
		System.out.println(productId);
	}
	@Test(priority = 3)
	void wishlist() {
		given().log().all().contentType(ContentType.JSON)
		.header("Authorization","Bearer "+token)
		.body("{\r\n"
				+ "  \"productId\":"+productId+" ,\r\n"
				+ "  \"quantity\": 0\r\n"
				+ "}")
		.when().post(baseUrl+"/shoppers/"+shopperId+"/wishlist")
		.then().assertThat().statusCode(201).log().all();
		
		//get products
		given().header("Authorization","Bearer "+token)
		.when().get(baseUrl+"/shoppers/"+shopperId+"/wishlist")
		.then().assertThat().statusCode(200);
		
		//delete products
				given().header("Authorization","Bearer "+token)
				.when().delete(baseUrl+"/shoppers/"+shopperId+"/wishlist/"+productId)
				.then().assertThat().statusCode(204);
		
	}


}












