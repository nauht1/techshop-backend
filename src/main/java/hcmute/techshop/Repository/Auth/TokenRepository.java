package hcmute.techshop.Repository.Auth;

import hcmute.techshop.Entity.Auth.TokenEntity;
import hcmute.techshop.Enum.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {
    @Query(value = """
      select t from TokenEntity t inner join UserEntity u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false)\s
      """)
    List<TokenEntity> findAllValidTokenByUser(Integer id);

    Optional<TokenEntity> findByToken(String token);

    @Query("SELECT t FROM TokenEntity t WHERE t.user.id = :userId AND t.tokenType = :tokenType AND t.expired = false AND t.revoked = false ORDER BY t.createdAt DESC")
    Optional<TokenEntity> findValidTokenByUser(@Param("userId") Integer userId, @Param("tokenType") TokenType tokenType);

    @Modifying
    @Query("DELETE FROM TokenEntity t WHERE t.user.id = :userId AND t.tokenType = :tokenType")
    void deleteOldTokens(@Param("userId") Integer userId, @Param("tokenType") TokenType tokenType);
    @Query("SELECT t FROM TokenEntity t WHERE t.token = :token AND t.tokenType = :tokenType AND t.expired = false AND t.revoked = false")
    Optional<TokenEntity> findValidTokenByTokenAndType(@Param("token") String token, @Param("tokenType") TokenType tokenType);


}
