package hcmute.techshop.Entity.Auth;

import hcmute.techshop.Enum.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "trackings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @Column(nullable = true)
    private String eventData;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
