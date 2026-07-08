package com.example.community.domain.user.entity;

import com.example.community.domain.image.Image;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "INT UNSIGNED")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true, length = 31)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Builder
    public User(Long userId, String email, String passwordHash, String nickname, Image profileImage) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public String getProfileImageUrl() {
        return profileImage == null ? null : profileImage.getUrl();
    }

    public void updateProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // todo: Hash로 바꿔야함.
    public void updatePassword(String password) {
        this.passwordHash = password;
    }

}
