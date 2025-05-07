package com.pose.server.core.mentor.infrastructure;

import com.pose.server.core.mentor.domain.MentorApplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorApplyRepository extends JpaRepository<MentorApplyEntity, Long> {
    Optional<MentorApplyEntity> findByMember(MemberEntity member);
}
