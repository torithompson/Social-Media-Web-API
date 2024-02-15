package comp74.thompson.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Posting {
    Integer postingId;
    String postingText;
    LocalDate date;
    LocalTime time;
    String userName;

    public Posting(String postingText) {
        this.postingText = postingText;
    }

    public void setDateAndTime(LocalDateTime datetime) {
        this.date = datetime.toLocalDate();
        this.time = datetime.toLocalTime();
    }
}
