package ca.saultcollege.server.repositories;

import ca.saultcollege.server.data.Account;
import ca.saultcollege.server.data.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySender(String sender);

}
