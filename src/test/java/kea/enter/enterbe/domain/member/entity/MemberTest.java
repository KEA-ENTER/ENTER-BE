package kea.enter.enterbe.domain.member.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import kea.enter.enterbe.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
class MemberTest extends IntegrationTestSupport {


    @DisplayName("생년월일의 반환 값의 형식이 yyMMdd 인지 확인합니다. (성공)")
    @Test
    public void getBirthDate(){
        //given
        Member member = createMember();
        String date = "991128";

        //when & then
        assertThat(member.getBirthDate())
            .isEqualTo(date);
    }

    @DisplayName("만 나이를 확인합니다. (성공)")
    @Test
    public void getAge(){
        //given
        Member member = createMember();
        int age = 24;

        //when & then
        assertThat(member.getAge())
            .isEqualTo(age);
    }

    @DisplayName("면허 정보를 업데이트합니다. (성공)")
    @Test
    public void setLicenseInformation() {
        //given
        Member member = createMember();

        //when
        member.setLicenseInformation(
            "11",
            "00",
            true,
            true
        );

        //then
        assertThat(member)
            .extracting("licenseId", "licensePassword", "isLicenseValid", "isAgreeTerms")
            .containsExactly("11", "00", true, true);
    }

    private Member createMember() {
        return Member.of(
            "name",
            "email",
            "password",
            LocalDate.of(1999, 11, 28),
            "licenseId",
            "licensePassword",
            false,
            false,
            10,
            MemberRole.USER,
            MemberState.ACTIVE
        );
    }
}