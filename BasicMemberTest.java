import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicMemberTest {

    @Test
    void test7_MemberBasicPointsAndID() {
        Member m1 = new Member("Charlie"); // M1001
        Member m2 = new Member("Dave");    // M1002

        m1.addPoints(100);
        assertEquals(100, m1.getPoints());
        assertTrue(m2.getMemberId().contains("M100")); // Check format
    }

    @Test
    void test8_MemberBasicUsePoints() {
        Member m = new Member("Eve");
        m.addPoints(50); // 50 points

        // Total 300, 允许 3 blocks (30 points), 实际只有 50 points (5 blocks)
        // 受限于 Total: 3 blocks (30 points) -> 30 dollars deduction
        int deduction = m.usePoints(300);

        assertEquals(30, deduction);
        assertEquals(20, m.getPoints()); // 50 - 30 = 20
    }


    @Test
    void test9_GoldMemberOverriddenLogic() {
        GoldMember gm = new GoldMember("Frank");
        gm.addPoints(60); // 60 points

        // Total 300. 允许 3 blocks (60 points/ $60 deduction)
        // 积分足够: 60 points used -> 60 dollars deduction
        int deduction = gm.usePoints(300);

        assertEquals(60, deduction);
        assertEquals(0, gm.getPoints());
    }
}