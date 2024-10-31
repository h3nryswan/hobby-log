

import com.example.demoplswork.model.Blog;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BlogTest {

    @Test
    void testConstructorWithoutId() {
        int userId = 1;
        String title = "Sample Blog";
        String description = "This is a sample description.";
        String imagePath = "images/sample.jpg";
        String tag = "Tech";

        // Create a blog without an ID
        Blog blog = new Blog(userId, title, description, imagePath, tag);

        // Check that the fields are correctly initialized
        assertEquals(userId, blog.getUserId());
        assertEquals(title, blog.getTitle());
        assertEquals(description, blog.getDescription());
        assertEquals(imagePath, blog.getImagePath());
        assertEquals(tag, blog.getTag());
    }

    @Test
    void testConstructorWithId() {
        int id = 100;
        int userId = 1;
        String title = "Another Blog";
        String description = "This is another blog description.";
        String imagePath = "images/another.jpg";
        String tag = "Travel";

        // Create a blog with an ID
        Blog blog = new Blog(id, userId, title, description, imagePath, tag);

        // Check that the fields are correctly initialized
        assertEquals(id, blog.getId());
        assertEquals(userId, blog.getUserId());
        assertEquals(title, blog.getTitle());
        assertEquals(description, blog.getDescription());
        assertEquals(imagePath, blog.getImagePath());
        assertEquals(tag, blog.getTag());
    }

    @Test
    void testGetId() {
        Blog blog = new Blog(10, 1, "Blog Title", "Blog Description", "images/blog.jpg", "Lifestyle");
        assertEquals(10, blog.getId());
    }

    @Test
    void testGetUserId() {
        Blog blog = new Blog(1, "Blog Title", "Blog Description", "images/blog.jpg", "Health");
        assertEquals(1, blog.getUserId());
    }

    @Test
    void testGetTitle() {
        Blog blog = new Blog(1, "Test Title", "Blog Description", "images/test.jpg", "Sports");
        assertEquals("Test Title", blog.getTitle());
    }

    @Test
    void testGetDescription() {
        Blog blog = new Blog(1, "Blog Title", "Test Description", "images/desc.jpg", "Education");
        assertEquals("Test Description", blog.getDescription());
    }

    @Test
    void testGetImagePath() {
        Blog blog = new Blog(1, "Blog Title", "Blog Description", "images/path.jpg", "Entertainment");
        assertEquals("images/path.jpg", blog.getImagePath());
    }

    @Test
    void testGetTag() {
        Blog blog = new Blog(1, "Blog Title", "Blog Description", "images/path.jpg", "News");
        assertEquals("News", blog.getTag());
    }
}

