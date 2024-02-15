package comp74.thompson.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import comp74.thompson.model.Model;
import comp74.thompson.model.Posting;
import comp74.thompson.model.Profile;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api")
@CrossOrigin
public class MainController {
    Model model;
    
    public MainController(Model model) {
        this.model = model;
    }

    //Get all profiles or get a profile by username
    // Username is optional. If username is not provided, return all profiles.
    @GetMapping("/profiles")
    public List<Profile> getProfiles(
            @RequestParam(name = "username", required = false, 
                    defaultValue = "") String username) {
        // If username is empty, return all profiles
        if (username.isEmpty()) {
            return model.getProfiles();
        } else
            // If username is not empty, return the profile with that username
            return model.getProfileByUsername(username);
    }

    // Add a profile. Controller gets the profile information from the request body.
    @PostMapping("/profiles")
    public ResponseEntity<Profile> addProfile(@RequestBody Profile profile) {
        return model.addProfile(profile);
    }

    // Get a profile by ID. Controller gets the profile by ID.
    @GetMapping("/profiles/{profileId}")
    public Profile getProfile(@PathVariable Integer profileId) {
        return model.getProfile(profileId);
    }

    // Delete a profile by ID. Controller gets the profile by ID.
    // Sends profile to model.
    // Returns a status code of 204 (NO_CONTENT) if the profile was deleted.
    // Returns a status code of 404 (NOT_FOUND) if the profile was not found.
    // Returns a status code of 409 (CONFLICT) if the profile was not deleted because it had postings.
    @DeleteMapping("/profiles/{profileId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Integer profileId) {
        // Get the profile by ID
        Profile profile = model.getProfile(profileId);
        // Send the profile to the model to delete the profile and return the status code
        return model.deleteProfile(profile);
    }

    // Get all postings for a profile. Controller gets the profile by ID.
    @GetMapping("/profiles/{profileId}/postings")
    public List<Posting> getPostings(@PathVariable Integer profileId) {
        return model.getProfile(profileId).getPostings();
    }

    // Add a posting for a profile. Controller gets the profile by ID.
    // Controller gets the posting information from the request body.
    // Sends profile and posting to model.
    // Returns a status code of 201 (CREATED) if the posting was added.
    // Returns a status code of 404 (NOT_FOUND) if the profile was not found.
    @PostMapping("/profiles/{profileId}/postings")
    public ResponseEntity<Posting> addPosting(@PathVariable Integer profileId, 
            @RequestBody Posting posting) {
        Profile profile = model.getProfile(profileId);
        ResponseEntity<Posting> newPosting = model.addPost(profile, posting);
        return newPosting;
    }

    // Delete all postings for a profile. Controller gets the profile by ID.
    // Sends profile and posting ID to model.
    //Returns a status code of 204 (NO_CONTENT) if the postings were deleted.
    // Returns a status code of 404 (NOT_FOUND) if the profile was not found.
    @DeleteMapping("profiles/{profileId}/postings")
    public ResponseEntity<Void> deletePostings(@PathVariable Integer profileId) {
        // Get the profile by ID
        Profile profile = model.getProfile(profileId);
        // Send the profile to the model to delete all postings for that profile
        ResponseEntity<Void> deletedPostings = model.deletePosts(profile);
        // Return the status code
        return deletedPostings;
    }

    // Delete a posting for a profile. Get the profile by ID.
    // If the posting exists, delete it and return a status code of 204 (NO_CONTENT).
    @DeleteMapping("/profiles/{profileId}/postings/{postingId}")
    public ResponseEntity<Void> deletePosting(@PathVariable Integer profileId, 
            @PathVariable Integer postingId) {
        // Get the profile by ID
        Profile profile = model.getProfile(profileId);
        // Send the profile and posting ID to the model to delete the posting
        ResponseEntity<Void> deletedPosting = model.deletePost(profile, postingId);
        // Return the status code   
        return deletedPosting;
    }
    
    // Update a posting for a profile. Get the profile by ID.
    // If the posting exists, update it return a status code of 204 (NO_CONTENT).
    // If the posting does not exist, return a status code of 404 NOT_FOUND.
    @PutMapping("/profiles/{profileId}/postings/{postingId}")
    public ResponseEntity<Void> updatePosting(@PathVariable Integer profileId, 
            @PathVariable Integer postingId, @RequestBody Posting posting) {
        // Get the profile by ID
        Profile profile = model.getProfile(profileId);
        // Get the posting text from the request body
        String postingText = posting.getPostingText();
        // Send the profile, posting ID, and posting text to the model to update the posting
        ResponseEntity<Void> updatedPosting = model.updatePost(profile, postingId, postingText);
        // Return the status code
        return updatedPosting;
    }
    
    // Adds a posting for a profile. Controller gets the profile by username.
    // Controller gets the posting information from the request body.
    // Sends profile and posting to model.
    // Returns a status code of 201 (CREATED) if the posting was added.
    // Returns a status code of 404 (NOT_FOUND) if the profile was not found.
    @PostMapping("/profileByUsername")
    public ResponseEntity<Posting> addPostingByUsername(@RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestBody Posting posting) {
        Profile profile = model.getProfileByUsername(username).get(0);
        ResponseEntity<Posting> newPosting = model.addPost(profile, posting);
        return newPosting;
    }

    // Get all postings for a profile. Controller gets the profile by username.
    @GetMapping("/profileByUsername")
    public Profile getProfileByUsername(
            @RequestParam(name = "username", required = false, defaultValue = "") String username) {
        Profile profile = model.getProfileByUsername(username).get(0);
        if (profile == null) {
            return null;
    }
    return profile;
    }

    // Update a profile by username. Controller gets the profile by username.
    // If the profile exists, update it and return a status code of 204 (NO_CONTENT).
    // If the profile does not exist, return a status code of 404 NOT_FOUND.
    @PutMapping("/updateUsername")
    public ResponseEntity<Profile> updateProfileByUsername(
        @RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestBody Map<String, String> request) {
        String newUsername = request.get("newUsername");
        Profile profileToUpdate = model.getProfileByUsername(username).get(0);
        ResponseEntity<Profile> updatedProfile = model.updateProfile(profileToUpdate, newUsername);
        return updatedProfile;
    }
}
