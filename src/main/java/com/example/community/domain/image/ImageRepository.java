package com.example.community.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUrl(String url);

    @Query("""
            SELECT i
            FROM Image i
            JOIN FETCH i.post p
            WHERE p.postId IN :postIds
              AND i.imageType = :imageType
              AND i.postOrder = 0
            """)
    List<Image> findPostThumbnails(
            @Param("postIds") Collection<Long> postIds,
            @Param("imageType") ImageType imageType
    );
}
