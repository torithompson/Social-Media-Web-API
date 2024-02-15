package comp74.thompson.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This class represents a profile in the social media application.
 * A profile has a unique profileId, a userName, and a list of postings.
 */
@NoArgsConstructor
@Data
public class Profile {
    Integer profileId;
    String userName;
    List<Posting> postings;

    public Profile(String userName) {
        this.userName = userName;
        postings = new ArrayList<>();
    }

    // Add a posting to the list of postings
    public void addPosting(Posting posting) {
        postings.add(posting);
    }

    // Remove a posting from the list of postings
    public void removePosting(Posting posting) {
        postings.remove(posting);
    }

    public List<Posting> getPostings() {
        if (postings == null) {
            postings = new ArrayList<>();
        }
        return postings;
    }
}
