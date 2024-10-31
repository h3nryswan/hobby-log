import com.example.demoplswork.model.Contact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ContactTest
{
    private Contact contact;

    @BeforeEach
    public void setUp()
    {
        contact = new Contact("John","Doe",
                "Hi my name is John","image.png");
    }

    @Test
    public void testGetId()
    {
        contact.setId(1);
        assertEquals(1,contact.getId());
    }

    @Test
    public void testGetFirstName()
    {
        assertEquals("John", contact.getFirstName());
    }

    @Test
    public void testSetFirstName()
    {
        contact.setFirstName("Jane");
        assertEquals("Jane", contact.getFirstName());
    }

    @Test
    public void testGetLastName()
    {
        assertEquals("Doe", contact.getLastName());
    }

    @Test
    public void testSetLastName()
    {
        contact.setLastName("Smith");
        assertEquals("Smith", contact.getLastName());
    }

    @Test
    public void testGetBio()
    {
        assertEquals("Hi my name is John", contact.getBio());
    }

    @Test
    public void testSetEmail()
    {
        contact.setBio("Hi my name is Jane");
        assertEquals("Hi my name is Jane", contact.getBio());
    }

    @Test
    public void testGetPhoto()
    {
        assertEquals("image.png", contact.getPhoto());
    }

    @Test
    public void testSetPhoto()
    {
        contact.setPhoto("random_image.jpg");
        assertEquals("random_image.jpg", contact.getPhoto());
    }
    @Test
    public void testGetFullName()
    {
        assertEquals("John Doe", contact.getFullName());
    }
}
