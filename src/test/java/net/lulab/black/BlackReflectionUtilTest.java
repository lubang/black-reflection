package net.lulab.black;

import net.lulab.black.fixture.City;
import net.lulab.black.fixture.ExtendedCity;
import net.lulab.black.fixture.User;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class BlackReflectionUtilTest {
    @Test
    public void create_default_values() {
        // Arrange
        final byte expectedByte = 0;
        final short expectedShort = 0;
        final int expectedInt = 0;
        final long expectedLong = 0L;
        final float expectedFloat = 0.f;
        final double expectedDouble = 0.d;
        final char expectedChar = '\u0000';
        final boolean expectedBoolean = false;

        // Act
        final byte actualByte = BlackReflectionUtil.createDefaultValue(byte.class);
        final short actualShort = BlackReflectionUtil.createDefaultValue(short.class);
        final int actualInt = BlackReflectionUtil.createDefaultValue(int.class);
        final long actualLong = BlackReflectionUtil.createDefaultValue(long.class);
        final float actualFloat = BlackReflectionUtil.createDefaultValue(float.class);
        final double actualDouble = BlackReflectionUtil.createDefaultValue(double.class);
        final char actualChar = BlackReflectionUtil.createDefaultValue(char.class);
        final boolean actualBoolean = BlackReflectionUtil.createDefaultValue(boolean.class);

        // Assert
        Assert.assertEquals(expectedByte, actualByte);
        Assert.assertEquals(expectedShort, actualShort);
        Assert.assertEquals(expectedInt, actualInt);
        Assert.assertEquals(expectedLong, actualLong);
        Assert.assertEquals(expectedFloat, actualFloat, 0.f);
        Assert.assertEquals(expectedDouble, actualDouble, 0.f);
        Assert.assertEquals(expectedChar, actualChar);
        Assert.assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void get_method_getId() throws NoSuchMethodException {
        // Arrange
        final Method expected = User.class.getDeclaredMethod("getId");

        // Act
        final Method actual = BlackReflectionUtil.getMethod(User.class, User::getId);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void get_method_getAge() throws NoSuchMethodException {
        // Arrange
        final Method expected = User.class.getDeclaredMethod("getAge");

        // Act
        final Method actual = BlackReflectionUtil.getMethod(User.class, User::getAge);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void get_method_getRegisteredAt() throws NoSuchMethodException {
        // Arrange
        final Method expected = User.class.getDeclaredMethod("getRegisteredAt");

        // Act
        final Method actual = BlackReflectionUtil.getMethod(User.class, User::getRegisteredAt);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void get_method_getName_from_POJO() throws NoSuchMethodException {
        // Arrange
        final Method expected = City.class.getDeclaredMethod("getName");

        // Act
        final Method actual = BlackReflectionUtil.getMethod(City.class, City::getName);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void get_method_getName_from_Extended_Class() throws NoSuchMethodException {
        // Arrange
        final Method expectedName = ExtendedCity.class.getMethod("getName");
        final Method expectedCities = ExtendedCity.class.getMethod("getCities");

        // Act
        final Method actualName = BlackReflectionUtil.getMethod(ExtendedCity.class, ExtendedCity::getName);
        final Method actualCities = BlackReflectionUtil.getMethod(ExtendedCity.class, ExtendedCity::getCities);

        // Assert
        Assert.assertEquals(expectedName, actualName);
        Assert.assertEquals(expectedCities, actualCities);
    }

    @Test
    public void get_method_getName_from_Object() throws NoSuchMethodException {
        // Arrange
        final Method expected = Object.class.getMethod("toString");

        // Act
        final Method actual = BlackReflectionUtil.getMethod(Object.class, Object::toString);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void get_bean_name() {
        // Arrange
        final String expected = "name";

        // Act
        final String actual = BlackReflectionUtil.getBeanName(User.class, User::getName);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = BlackReflectionException.class)
    public void get_bean_name_throws_by_invalid_method() {
        // Arrange
        final String expected = "string";

        // Act
        final String actual = BlackReflectionUtil.getBeanName(User.class, User::toString);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void get_all_bean_names_from_user() {
        // Arrange
        final List<String> expected = Arrays.asList("id", "name", "age", "registeredAt");
        expected.sort(String.CASE_INSENSITIVE_ORDER);

        // Act
        final List<String> actual = BlackReflectionUtil.getBeanNames(User.class);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void get_all_bean_names_from_extended_class() {
        // Arrange
        final List<String> expected = Arrays.asList("cities", "lat", "lon", "name");
        expected.sort(String.CASE_INSENSITIVE_ORDER);

        // Act
        final List<String> actual = BlackReflectionUtil.getBeanNames(ExtendedCity.class);

        // Assert
        Assert.assertEquals(expected, actual);
    }
}
