import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

// 假设所有的类 (Member, GoldMember, Supermarket, Product, CartItem, ShoppingCart, etc.)
// 都已实现，且 Supermarket 提供了 addProduct 等方法。

public class BasicShoppingCartTest {

    private Supermarket s;
    private Member m;
    private GoldMember gm; 
    private Product p1;
    private Product p2;
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /** Helper method to normalize output line endings for robust comparison. */
    private String normalize(String output) {
        return output.replace("\r\n", "\n").replace('\r', '\n').trim();
    }

    @BeforeEach
    void setupCartEnv() {
        s = new Supermarket();
        m = new Member("Grace");
        gm = new GoldMember("Henry");
        
        p1 = new Book("Test Book", 50, 10); // $50/ea, Stock 10
        p2 = new Food("Test Food", 5, 20);  // $5/ea, Stock 20
        s.addProduct(p1);
        s.addProduct(p2);
        
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void tearDown() {
        System.setOut(originalOut);
    }

    // --- 基础逻辑和无积分小票验证 ---

    @Test
    void test10_CartAddAndCheckoutNoPoints() {
        ShoppingCart cart = new ShoppingCart(m, s);

        cart.add("Test Book", 2); // $100
        cart.add("Test Food", 5); // $25
        // Raw Total: $125

        int initialPoints = m.getPoints(); // 0
        int initialStock1 = s.getStock("Test Book"); // 10

        cart.checkout(); // checkout(false)

        // 1. 验证逻辑
        assertEquals(initialPoints + 125, m.getPoints(), "Points should accumulate 125.");
        assertEquals(0, cart.getItems().size(), "Cart must be cleared.");
        assertEquals(initialStock1 - 2, s.getStock("Test Book"), "Stock must be reduced.");

        // 2. 验证小票格式
        String expectedReceipt =
                """
                === Checkout Receipt ===
                Test Book x2 - $50/ea = $100
                Test Food x5 - $5/ea = $25
                Total: $125
                Points used: $0 (0 points)
                Final Total: $125
                Points earned: 125
                ===
                """;

        assertEquals(normalize(expectedReceipt), normalize(outContent.toString()), "Receipt format for no points is incorrect.");
    }

    // --- 积分和小票验证 ---

    @Test
    void test11_CheckoutWithPoints_StandardMember() {
        ShoppingCart cart = new ShoppingCart(m, s);
        m.addPoints(30); // 初始积分 30

        // 购买金额超过 $100
        cart.add("Test Book", 3); // $150
        cart.add("Test Food", 5);  // $25
        // Raw Total: $175

        // Standard Member Rule: $10 deduction per 10 points per $100 spent.
        // Total amount limit: floor(175/100) = 1 block. Max deduction $10 (10 points).
        // Points limit: 30 points available, allows 3 blocks, but amount limits to 1 block.
        // Deduction: $10 (10 points used)
        // Final Total: 175 - 10 = $165. Points earned: 165. Final points: 30 - 10 + 165 = 185.

        cart.checkout(true);

        // 1. 验证逻辑
        assertEquals(185, m.getPoints(), "Final points should be 185.");

        // 2. 验证小票格式
        String expectedReceipt =
                """
                === Checkout Receipt ===
                Test Book x3 - $50/ea = $150
                Test Food x5 - $5/ea = $25
                Total: $175
                Points used: $10 (10 points)
                Final Total: $165
                Points earned: 165
                ===
                """;

        assertEquals(normalize(expectedReceipt), normalize(outContent.toString()), "Receipt format and point logic for standard member is incorrect.");
    }

    @Test
    void test12_CheckoutWithPoints_GoldMemberOverridden() {
        ShoppingCart cart = new ShoppingCart(gm, s);
        gm.addPoints(50); // 初始积分 50

        // 购买金额超过 $200
        cart.add("Test Book", 4); // $200

        // Raw Total: $200

        // Gold Member Rule: $20 deduction per 20 points per $100 spent.
        // Total amount limit: floor(200/100) = 2 blocks. Max deduction $40 (40 points).
        // Points limit: 50 points available, allows 2 blocks (40 points).
        // Deduction: $40 (40 points used)
        // Final Total: 200 - 40 = $160. Points earned: 160. Final points: 50 - 40 + 160 = 170.

        cart.checkout(true);

        // 1. 验证逻辑
        assertEquals(170, gm.getPoints(), "Final points for GoldMember should be 170.");

        // 2. 验证小票格式
        String expectedReceipt =
                """
                === Checkout Receipt ===
                Test Book x4 - $50/ea = $200
                Total: $200
                Points used: $40 (40 points)
                Final Total: $160
                Points earned: 160
                ===
                """;

        assertEquals(normalize(expectedReceipt), normalize(outContent.toString()), "Receipt format and point logic for GoldMember is incorrect.");
    }
}

