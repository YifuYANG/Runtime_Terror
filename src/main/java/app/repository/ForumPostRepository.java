package app.repository;

import app.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {

    @Query("select fp from ForumPost fp where fp.userId = :userId order by fp.dateCreated desc")
    List<ForumPost> findAllByUserId(@Param("userId") Long userId);

}
