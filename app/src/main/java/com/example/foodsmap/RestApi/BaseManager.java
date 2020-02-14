package com.example.foodsmap.RestApi;

public class BaseManager {

    protected RestApi getRestApiClient(){
        RestApiClient  restApiClient =new RestApiClient(BaseUrl.Bilgi_URL);

        return restApiClient.getRestApi();
    }
}
