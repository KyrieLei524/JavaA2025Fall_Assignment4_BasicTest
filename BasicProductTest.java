import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicProductTest {

    @Test
    void test1_ProductBasicCreationAndToString() {
        Product book = new Book("Java Basics", 60, 10);
        assertEquals("Java Basics", book.getName());
        assertEquals(60, book.getPrice());
        assertEquals("BOOK - Java Basics: $60, Stock: 10", book.toString());
    }

    @Test
    void test2_SubclassTypes() {
        Book book = new Book("Algorithms", 100, 5);
        Food food = new Food("Bread", 3, 50);

        assertEquals(ProductType.BOOK, book.getType());
        assertEquals(ProductType.FOOD, food.getType());
    }

    @Test
    void test3_StockManagementBasics() {
        Product p = new Product("Test Item", 1, 10, ProductType.FOOD);

        p.addStock(5);
        assertEquals(15, p.getStockQuantity());

        p.reduceStock(5);
        assertEquals(10, p.getStockQuantity());

        p.reduceStock(20);
        assertEquals(0, p.getStockQuantity());
    }
}