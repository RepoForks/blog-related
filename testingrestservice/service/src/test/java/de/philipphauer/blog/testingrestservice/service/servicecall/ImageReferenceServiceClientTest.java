package de.philipphauer.blog.testingrestservice.service.servicecall;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageReferenceServiceClientTest {

    private MockWebServer imageService;
    private ImageServiceClient client;

    @Before
    public void init() throws IOException {
        imageService = new MockWebServer();
        imageService.start();
        HttpUrl baseUrl = imageService.url("/images/");
        client = new ImageServiceClient(baseUrl.host(), baseUrl.port());
    }

    @Test
    public void requestImage_NotFoundImage() throws JsonProcessingException {
        ImageReference expectedImageRef = new ImageReference().setId("123").setHref("http://images.company.org/123");
        String json = new ObjectMapper().writeValueAsString(expectedImageRef);
        imageService.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(json));

        ImageReference retrievedImageRef = client.requestImage("123");

        assertThat(retrievedImageRef.getId()).isEqualTo(expectedImageRef.getId());
        assertThat(retrievedImageRef.getHref()).isEqualTo(expectedImageRef.getHref());
    }
}