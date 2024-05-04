// package com.example.elearningplatform.cloudinary;

// import java.io.BufferedInputStream;
// import java.io.BufferedOutputStream;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.net.HttpURLConnection;
// import java.net.URI;
// import java.net.URL;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;

// import com.example.elearningplatform.security.TokenUtil;
// import com.example.elearningplatform.user.user.User;
// import com.example.elearningplatform.user.user.UserRepository;

// @Service
// public class ImageService {
//     @Autowired
//     private UserRepository userRepository;
//     @Autowired
//     private TokenUtil tokenUtil;
//     @Value("${bunnycdn.storageZone}")
//     private String storageZone;

//     // String storageZoneRegion = ""; // Change to the desired region (leave empty
//     // if not applicable)
//     String fileName = "image2.jpg";
//     @Value("${bunnycdn.storageZoneapiKey}")
//     private String apiKey;

//     public void uploadFile(String imagePath) throws Exception {

//         try {
//             User user = userRepository.findById(tokenUtil.getUserId())
//                     .orElseThrow(() -> new Exception("User not found"));

//             String fileLocation = "C:\\Users\\MohamedReda\\OneDrive\\Desktop\\112642921.jpeg";
//             String urlStr = "https://storage.bunnycdn.com/" + storageZone + "/user/" + user.getId() + ".jpg";

//             URI uri = new URI(urlStr);
//             URL url = uri.toURL();
//             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//             connection.setRequestMethod("PUT");
//             connection.setRequestProperty("AccessKey", apiKey);
//             connection.setRequestProperty("Content-Type", "application/octet-stream");
//             connection.setDoOutput(true);

//             File file = new File(fileLocation);
//             // long fileSize = file.length();

//             try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
//                     BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream())) {

//                 byte[] buffer = new byte[4096];
//                 int bytesRead;
//                 while ((bytesRead = inputStream.read(buffer)) != -1) {
//                     outputStream.write(buffer, 0, bytesRead);
//                 }
//             }

//             int responseCode = connection.getResponseCode();
//             String responseMsg = connection.getResponseMessage();
//             System.out.println("Response: " + responseCode + " " + responseMsg);

//             // Handle the response as needed
//         } catch (IOException e) {
//             System.out.println("Error: " + e.getMessage());
//         }
//     }

//     /*************************************************************************************************** */

//     public ResponseEntity<?> display() throws IOException, InterruptedException {
//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create("https://storage.bunnycdn.com/e-learning-platform-images/user/image.jpg"))
//                 .header("accept", "*/*")
//                 .header("AccessKey", apiKey)
//                 .method("GET", HttpRequest.BodyPublishers.noBody())
//                 .build();
//         HttpResponse<byte[]> response = HttpClient.newHttpClient().send(request,
//                 HttpResponse.BodyHandlers.ofByteArray());
//         System.out.println(response.body());
//         return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(response.body());
//     }
//     /*************************************************************************************************** */
// }
