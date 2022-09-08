package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired LogRepository logRepository;

    /**
     * memberService        @Transactional:OFF
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON
     */
    @Test
    void outerTxOff_success() {
        //given
        String username = "outerTxOff_success";

        //when
        memberService.joinV1(username);

        //then : 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.findByUsername(username).isPresent());
        assertTrue(logRepository.findByMessage(username).isPresent());
    }

    /**
     * memberService        @Transactional:OFF
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON Exception
     */
    @Test
    void outerTxOff_fail() {
        //given
        String username = "로그예외_outerTxOff_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        //then : member는 정상 commit, log는 rollback
        assertTrue(memberRepository.findByUsername(username).isPresent());
        assertTrue(logRepository.findByMessage(username).isEmpty());
    }


    /**
     * memberService        @Transactional:ON
     * memberRepository     @Transactional:OFF
     * logRepository        @Transactional:OFF
     */
    @Test
    void singleTx() {
        //given
        String username = "outerTxOff_success";

        //when
        memberService.joinV1(username);

        //then : 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.findByUsername(username).isPresent());
        assertTrue(logRepository.findByMessage(username).isPresent());
    }

    /**
     * memberService        @Transactional:ON -- 신규 트랜잭션
     * memberRepository     @Transactional:ON -- 신규 트랜잭션 X == 실제 commit X
     * logRepository        @Transactional:ON -- 신규 트랜잭션 X == 실제 commit X
     */
    @Test
    void outerTxOn_success() {
        //given
        String username = "outerTxOn_success";

        //when
        memberService.joinV1(username);

        //then : 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.findByUsername(username).isPresent());
        assertTrue(logRepository.findByMessage(username).isPresent());
    }

    /**
     * memberService        @Transactional:ON == transaction manager -> rollback
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON Exception == rollbackOnly mark
     */
    @Test
    void outerTxOn_fail() {
        //given
        String username = "로그예외_outerTxOn_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        //then : 모든 데이터가 rollback 된다.
        assertTrue(memberRepository.findByUsername(username).isEmpty());
        assertTrue(logRepository.findByMessage(username).isEmpty());
    }

    /**
     * memberService        @Transactional:ON
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON Exception
     */
    @Test
    void recoverException_fail() {
        //given
        String username = "로그예외_recoverException_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV2(username))
                .isInstanceOf(UnexpectedRollbackException.class);

        //then : 모든 데이터가 rollback 된다.
        assertTrue(memberRepository.findByUsername(username).isEmpty());
        assertTrue(logRepository.findByMessage(username).isEmpty());
    }

    /**
     * memberService        @Transactional:ON
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON(REQUIRES_NEW) Exception
     */
    @Test
    void recoverException_success() {
        //given
        String username = "로그예외_recoverException_fail";

        //when
        memberService.joinV2(username);

        //then : member 저장, log 롤백
        assertTrue(memberRepository.findByUsername(username).isPresent());
        assertTrue(logRepository.findByMessage(username).isEmpty());
    }



}