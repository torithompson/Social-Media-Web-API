package comp74.thompson.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class Model {
    HashMap<Integer, Profile> profiles;
    HashMap<String, Profile> profilesByUsername;
    HashMap<Integer, Posting> postings;

    private static Integer nextProfileId = 100;
    private static Integer nextPostingId = 1000;

    public Model() {
        super();
        profiles = new HashMap<Integer, Profile>();
        postings = new HashMap<Integer, Posting>();
        profilesByUsername = new HashMap<String, Profile>();
    }

    //Create a new profile. Ensure that the username is unique.
    //Return the profile and a status code of 201 (CREATED) if the username is unique.
    //Return a status code of 409 (CONFLICT) if the username is not unique.
    public ResponseEntity<Profile> addProfile(Profile profile) {
        // Create an empty profile
        Profile newProfile = null;
        // Check to see if the username is unique
        if (profilesByUsername.get(profile.getUserName()) == null) {
            // If the username is unique, create a new profile and add it to the HashMaps
            newProfile = profile;
            newProfile.setProfileId(nextProfileId++);
            profiles.put(newProfile.getProfileId(), newProfile);
            profilesByUsername.put(newProfile.getUserName(), newProfile);
            return new ResponseEntity<>(profile, HttpStatus.CREATED);
        } else {
            // If the username is not unique, return a status code of 409 (CONFLICT)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    //Get a profile by ID
    public Profile getProfile(Integer profileId) {
        return profiles.get(profileId);
    }

    //Get a posting by postingID
    public Posting getPosting(Integer postingId) {
        return postings.get(postingId);
    }

    //Get all profiles
    public List<Profile> getProfiles() {
        return new ArrayList<>(profiles.values());
    }

    //Get all postings
    public List<Posting> getPostings() {
        return new ArrayList<>(postings.values());
    }

    //Return a list of postings for a profile. Get the profile by username.
    //If the profile does not exist, return null.
    //If the profile exists, return a list of postings for that profile.
    public List<Profile> getProfileByUsername(String username) {
        // Check to see if the profile exists
        if (profilesByUsername.get(username) == null) {
            // Return null if the profile does not exist
            return null;
        } else {
            // Return a list of postings for the profile
            Profile profile = profilesByUsername.get(username);
            List<Profile> profileList = new ArrayList<>();
            profileList.add(profile);
            return profileList;
        }
    }

    // Add a posting for a profile. Get the profile by ID.
    // If the profile does not exist, return a status code of 404 NOT_FOUND.
    // If the profile exists, add the posting to the profile and return the posting and a status code of 201 (CREATED).
    public ResponseEntity<Posting> addPost(Profile profile, Posting posting) {
        // Check to see if the profile exists
        if (profile == null) {
            // Return a status code of 404 (NOT_FOUND) if the profile does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // Add the posting to the profile and return the posting and a status code of 201 (CREATED)
            posting.setPostingId(nextPostingId++);
            posting.setDateAndTime(LocalDateTime.now());
            posting.setUserName(profile.getUserName());
            postings.put(posting.getPostingId(), posting);
            profile.addPosting(posting);
            return new ResponseEntity<>(posting, HttpStatus.CREATED);
        }
    }

    // Delete a posting for a profile. Get the profile by ID.
    // If the posting exists, delete it and return the posting and a status code of 204 (NO_CONTENT).
    // If the posting does not exist, return a status code of 404 NOT_FOUND.
    public ResponseEntity<Void> deletePost(Profile profile, Integer postingId) {
        // Get the posting by ID
        Posting posting = postings.get(postingId);
        // Check to see if the posting exists
        if (posting != null) {
            // Remove the posting from the postings HashMap and the profile's list of postings
            // and return a status code of 204 (NO_CONTENT)
            postings.remove(postingId);
            profile.removePosting(posting);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Return a status code of 404 (NOT_FOUND) if the posting does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete all postings for a profile. Get the profile by ID.
    // If the profile exists, delete all postings for that profile and return a status code of 204 (NO_CONTENT).
    // If the profile does not exist, return a status code of 404 NOT_FOUND.
    public ResponseEntity<Void> deletePosts(Profile profile) {
        // Check to see if the profile exists
        if (profile == null) {
            // Return a status code of 404 (NOT_FOUND) if the profile does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // Get a list of postings for the profile
            List<Posting> postingsList = profile.getPostings();
            // For each posting in the list, remove it from the postings HashMap
            for (Posting posting : postingsList) {
                postings.remove(posting.getPostingId());
            }
            // Remove all postings from the profile and return a status code of 204 (NO_CONTENT)
            profile.setPostings(new ArrayList<Posting>());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    // Delete a profile by ID. Model checks to see if the profile has any postings.
    // If the profile exists and has no postings, delete it and return the profile 
    // and a status code of 204 (NO_CONTENT).
    // If the profile exists and has postings, return the profile and a status code of 409 (CONFLICT).
    // If the profile does not exist, return a status code of 404 NOT_FOUND.
    public ResponseEntity<Void> deleteProfile(Profile profile) {
        // Check to see if the profile exists
        if (profile != null) {
            // Check to see if the profile has postings
            if (profile.getPostings().isEmpty()) {
                // Delete the profile, remove it from the HashMaps, and return a status code of 204 (NO_CONTENT)
                profiles.remove(profile.getProfileId());
                profilesByUsername.remove(profile.getUserName());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // Return a status code of 409 (CONFLICT) if the profile has postings
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } else {
            // Return a status code of 404 (NOT_FOUND) if the profile does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update a posting for a profile. Get the profile by ID.
    // If the posting exists, update it and return a status code of 204 (NO_CONTENT).
    // If the posting does not exist, return a status code of 404 NOT_FOUND.
    public ResponseEntity<Void> updatePost(Profile profile, Integer postingId, String postingText) {
        // Get the posting by ID
        Posting oldPosting = postings.get(postingId);
        // Check to see if the posting exists
        if (oldPosting != null) {
            // Update the posting text and return a status code of 204 (NO_CONTENT)
            oldPosting.setPostingText(postingText);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Return a status code of 404 (NOT_FOUND) if the posting does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

public ResponseEntity<Profile> updateProfile(Profile profileToUpdate, String newUsername) {
    // Check if the new username already exists
    if (profilesByUsername.get(newUsername) != null && !newUsername.equals(profileToUpdate.getUserName())) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    // Remove the old username from the HashMap
    profilesByUsername.remove(profileToUpdate.getUserName());

    // Set the new username
    profileToUpdate.setUserName(newUsername);

    // Add the updated profile with the new username to the HashMap
    profilesByUsername.put(profileToUpdate.getUserName(), profileToUpdate);

    // Update the username for each posting
    for (Posting posting : profileToUpdate.getPostings()) {
        posting.setUserName(newUsername);
    }

    // Return a 204 (NO_CONTENT) response
    return new ResponseEntity<>(profileToUpdate, HttpStatus.NO_CONTENT);
}


}

