package comp74.thompson;

import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import comp74.thompson.model.Model;
import comp74.thompson.model.Posting;
import comp74.thompson.model.Profile;

@Component
public class Initializer implements CommandLineRunner {
        Lorem lorem = LoremIpsum.getInstance();
        Model model = new Model();
        final Integer MIN_PROFILES = 2;
        final Integer MAX_PROFILES = 10;
        final Integer MIN_POSTINGS = 2;
        final Integer MAX_POSTINGS = 8;

        public Initializer(Model model) {
            this.model = model;
        }
    
        // Create a random number of profiles with a random number of postings
        @Override
        public void run(String... args) throws Exception {
            Random random = new Random();
            Integer numProfiles = MIN_PROFILES + random.nextInt(MAX_PROFILES - MIN_PROFILES);

            for (int i = 0; i < numProfiles; i++) {
                Profile profile = new Profile(lorem.getFirstName());
                Integer numPostings = MIN_POSTINGS + random.nextInt(MAX_POSTINGS - MIN_POSTINGS);
                for (int j = 0; j < numPostings; j++) {
                    Posting posting = new Posting();
                    posting.setUserName(profile.getUserName());
                    posting.setPostingText(lorem.getParagraphs(1, 3));
                    model.addPost(profile, posting);
                }
                model.addProfile(profile);
            }         
        }
    }
