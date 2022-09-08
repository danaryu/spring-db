package hello.springtx.propagation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDataRepository extends JpaRepository<Member, Long> {
}
