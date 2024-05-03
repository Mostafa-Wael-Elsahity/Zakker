// package com.example.elearningplatform.cloudinary;

// import java.awt.image.BufferedImage;
// import java.io.IOException;
// import java.util.Map;
// import java.util.Optional;

// import javax.imageio.ImageIO;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.example.elearningplatform.exception.CustomException;
// import com.example.elearningplatform.security.TokenUtil;
// import com.example.elearningplatform.user.user.User;
// import com.example.elearningplatform.user.user.UserRepository;

// @RestController
// @RequestMapping("/cloudinary")
// @CrossOrigin(origins = "http://localhost:4200")
// public class CloudinaryController {
//     @Autowired
//     CloudinaryService cloudinaryService;
//     @Autowired
//     private UserRepository userRepository;
//     @Autowired
//     private TokenUtil tokenUtil;


//     // @GetMapping("/list")
//     // public ResponseEntity<List<Image>> list(){
//     // List<Image> list = imageService.list();
//     // return new ResponseEntity<>(list, HttpStatus.OK);
//     // }

//     @PostMapping("/upload-image")
//     @ResponseBody
//     public ResponseEntity<String> upload(@RequestParam MultipartFile image) throws IOException {
//         try {
//             BufferedImage bi = ImageIO.read(image.getInputStream());
//             if (bi == null) {
//                 throw new CustomException("Invalid image file", HttpStatus.BAD_REQUEST);
//             }
//             User user = userRepository.findById(tokenUtil.getUserId())
//                     .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
//             if (user.getImageId() != null) {
//                 cloudinaryService.delete(user.getImageId());
//             }
//             @SuppressWarnings("rawtypes")
//             Map result = cloudinaryService.upload(image);
//             user.setImageId((String) result.get("public_id"));
//             user.setImageUrl((String) result.get("url"));
//             userRepository.save(user);

//             return new ResponseEntity<>("image uploaded  ! ", HttpStatus.OK);
//         } catch (CustomException e) {
//             return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//         } catch (Exception e) {
//             return new ResponseEntity<>("Failed to upload image to Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }
//     /******************************************************************************************** */

//     @DeleteMapping("/delete-image/{id}")
//     public ResponseEntity<String> delete(@PathVariable("id") int id) {
//         try{

//         User user = userRepository.findById(tokenUtil.getUserId())
//         .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
//         String cloudinaryImageId = user.getImageId();
//         try {
//             cloudinaryService.delete(cloudinaryImageId);
//         } catch (IOException e) {
//             throw new CustomException("Failed to delete image from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//         user.setImageId(null);
//         userRepository.save(user);
//         return new ResponseEntity<>("image deleted !", HttpStatus.OK) ;
//     } catch (CustomException e) {
//         return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//     } catch (Exception e) {
//         return new ResponseEntity<>("Failed to delete image from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
//     }
       
//     }

// }
