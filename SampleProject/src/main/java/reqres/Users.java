package reqres;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import java.util.HashMap;

public class Users {

	public static void main(String[] args) {
		RestAssured.baseURI="https://reqres.in";
		//add user
		Object id = given().contentType("application/json")
		.body("{\r\n"
				+ "    \"name\": \"morpheus\",\r\n"
				+ "    \"job\": \"leader\"\r\n"
				+ "}")
		.when().post("/api/users").jsonPath().get("id");
		System.out.println(id);

		//update user
		given().log().all().contentType("application/json")
		.body("{\r\n"
				+ "    \"name\": \"morpheus\",\r\n"
				+ "    \"job\": \"zion resident\"\r\n"
				+ "}")
		.when().put("/api/users/"+id)
		.then().assertThat().statusCode(200).log().all();
		
		//delete user
		given()
		.when().delete("/api/users/"+id)
		.then().assertThat().statusCode(204).log().all();
		
	}

}








