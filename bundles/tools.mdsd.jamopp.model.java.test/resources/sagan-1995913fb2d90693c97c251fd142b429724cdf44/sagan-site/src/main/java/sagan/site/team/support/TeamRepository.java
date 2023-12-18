package sagan.site.team.support;

import sagan.site.team.MemberProfile;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<MemberProfile, Long> {

    Optional<MemberProfile> findByGithubId(Long githubId);

    Optional<MemberProfile> findByUsername(String username);

    List<MemberProfile> findByHiddenOrderByNameAsc(boolean hidden);

    @Modifying(clearAutomatically = true)
    @Query("update MemberProfile p set p.hidden = true where (p.githubId not in :ids or p.githubId = null)")
    int hideTeamMembersNotInIds(@Param("ids") List<Long> ids);

}
