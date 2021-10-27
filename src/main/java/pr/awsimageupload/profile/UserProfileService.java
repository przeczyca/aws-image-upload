package pr.awsimageupload.profile;

import java.io.IOException;
import java.util.*;

import pr.awsimageupload.bucket.BucketName;
import pr.awsimageupload.filestore.FileStore;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserProfileService {
    
    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore){
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles(){
        return userProfileDataAccessService.getUserProfiles();
    }

    //remove "static" at some point
    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file){
        // 1. Check if image is not empty
        isFileEmpty(file);

        // 2. If file is an image
        isImage(file);
        
        // 3. The user exists in database
        UserProfile user = getUserProfiles()
            .stream()
            .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        // 5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try{
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e){
            throw new IllegalStateException(e);
        }
    }

    private void isFileEmpty(MultipartFile file){
        if (file.isEmpty()) throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + " ]");
    }

    private void isImage(MultipartFile file){
        if (!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(),
                           ContentType.IMAGE_PNG.getMimeType(),
                           ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }
}
