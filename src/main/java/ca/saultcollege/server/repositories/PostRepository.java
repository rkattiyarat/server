package ca.saultcollege.server.repositories;

import ca.saultcollege.server.data.Post;
import ca.saultcollege.server.data.Registry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
    Post findByAuthorId(int authorId);
}
