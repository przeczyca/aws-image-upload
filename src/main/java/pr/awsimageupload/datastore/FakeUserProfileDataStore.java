package pr.awsimageupload.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import pr.awsimageupload.profile.UserProfile;

@Repository
public class FakeUserProfileDataStore {
    
    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static{
        USER_PROFILES.add(new UserProfile(UUID.fromString("88eb5a52-c278-460e-a062-2bdca86cf9e6"), "janetjones", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("fc7182b4-d845-4eda-840d-5047e8500015"), "antoniojunior", null));
    }

    public List<UserProfile>  getUserProfiles(){
        return USER_PROFILES;
    }
}
